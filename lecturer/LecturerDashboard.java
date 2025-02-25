package lecturer;

import chart.ModelCard;
import chart.ModelChart;
import login.Login;
import src.view.Mail;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import javax.swing.GroupLayout.Alignment;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import admin.ImageAvatar;
import admin.Panel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;
import chart.Card;
import chart.Card1;
import javax.swing.JComboBox;

public class LecturerDashboard extends javax.swing.JFrame {
    private String currentCourseCode = "";
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedDate = currentDate.format(formatter);
    String currentDater = currentDate.format(formatter);
    LocalDate previousDate1 = currentDate.minusDays(1);
    String formattedDate1 = previousDate1.format(formatter);
    LocalDate previousDate5 = currentDate.minusDays(5);
    String formattedDate5 = previousDate5.format(formatter);

    public LecturerDashboard() {
        initComponents();
        setBackground(new Color(0, 0, 0, 0));
        initData();
    }

    private void initData() {
        int totalStudents = 0;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
            Statement stmt = conn.createStatement();
            String studentQuery = "SELECT COUNT(*) AS total FROM student";
            ResultSet studentRs = stmt.executeQuery(studentQuery);

            if (studentRs.next()) {
                totalStudents = studentRs.getInt("total");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int currentWeek = calculateCurrentWeek();
        List<String> courseCodes = fetchCourseCodesFromDatabase();

        // Update the cards with data
        if (card1 != null) {
            card1.setData(new ModelCard("Date", formattedDate, "Today's Date"));
        }
        if (card3 != null) {
            card3.setData(new ModelCard("Students", String.valueOf(totalStudents), "Total Students"));
        }
        if (card4 != null) {
            card4.setData(new ModelCard("Week", String.valueOf(currentWeek), "Current Week"));
        }
        
        if (chart != null) {
            chart.addLegend("Absentees", new Color(12, 84, 175), new Color(0, 108, 247));
            chart.addLegend("On Time", new Color(54, 4, 143), new Color(104, 49, 200));
            chart.addLegend("Late", new Color(5, 125, 0), new Color(95, 209, 69));
            chart.addLegend("Too Late", new Color(186, 37, 37), new Color(241, 100, 120));
            for (String courseCode : courseCodes) {
                double[] attendanceData = getAttendanceData(courseCode);
                chart.addData(new ModelChart(courseCode, attendanceData));  // Data is now in percentages
            }
        }
    }

    public void setCurrentCourseCode(String courseCode) {
        this.currentCourseCode = courseCode;
        updateAttendanceData();
    }

    private void updateAttendanceData() {
        double[] attendanceData = getAttendanceData(currentCourseCode);
        updateChart(attendanceData);
    }

    private void updateChart(double[] attendanceData) {
        System.out.println("Updating chart with data: " + Arrays.toString(attendanceData)); // Debug line
        
        if (chart != null) {
            chart.clear(); // Clear existing data before adding new data
            chart.addLegend("Absentees", new Color(12, 84, 175), new Color(0, 108, 247));
            chart.addLegend("On Time", new Color(54, 4, 143), new Color(104, 49, 200));
            chart.addLegend("Late", new Color(5, 125, 0), new Color(95, 209, 69));
            chart.addLegend("Too Late", new Color(186, 37, 37), new Color(241, 100, 120));
            chart.addData(new ModelChart(currentCourseCode, attendanceData)); // Pass percentage data
            chart.repaint();
        }
    }



    private double[] getAttendanceData(String courseCode) {
        double[] attendanceData = new double[4];
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
            String attendanceQuery = "SELECT " +
                    "SUM(CASE WHEN status = 'Absent' THEN 1 ELSE 0 END) AS Absentees, " +
                    "SUM(CASE WHEN status = 'On Time' THEN 1 ELSE 0 END) AS OnTime, " +
                    "SUM(CASE WHEN status = 'Late' THEN 1 ELSE 0 END) AS Late, " +
                    "SUM(CASE WHEN status = 'Too Late' THEN 1 ELSE 0 END) AS TooLate, " +
                    "COUNT(*) AS total " +
                    "FROM attendance WHERE course_code = ? AND date >= ? AND date <= ?";
            PreparedStatement pstmt = conn.prepareStatement(attendanceQuery);
            pstmt.setString(1, courseCode);
            pstmt.setString(2, formattedDate5); // Start date of the interval
            pstmt.setString(3, currentDater); // End date of the interval
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                if (total > 0) {
                    attendanceData[0] = (rs.getDouble("Absentees") / total) * 100; // Percentage calculation
                    attendanceData[1] = (rs.getDouble("OnTime") / total) * 100;  // Percentage calculation
                    attendanceData[2] = (rs.getDouble("Late") / total) * 100;    // Percentage calculation
                    attendanceData[3] = (rs.getDouble("TooLate") / total) * 100; // Percentage calculation
                }
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attendanceData;
    }

    private List<String> fetchCourseCodesFromDatabase() {
        List<String> courseCodes = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/attendancedb";
        String username = "root";
        String password = "";
        String sql = "SELECT DISTINCT course_code FROM timetable WHERE lecturer_id = ?"; // Query to fetch course codes
        String loggedInLecturerId = Login.userId;

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loggedInLecturerId); // Set the lecturer ID
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String code = rs.getString("course_code");
                courseCodes.add(code);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courseCodes;
    }

    private int calculateCurrentWeek() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
            Statement stmt = conn.createStatement();
            String query = "SELECT semester_start, semester_end FROM admin";
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                LocalDate semesterStart = rs.getDate("semester_start").toLocalDate();
                long daysBetween = ChronoUnit.DAYS.between(semesterStart, LocalDate.now());
                int currentWeek = (int) (daysBetween / 7) + 1;
                conn.close();
                return currentWeek;
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0; // Default return value if calculation fails
    }
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    

    	
    	

        panel1 = new Panel();
        panel2 = new Panel();
        chart = new chart.Chart();
        card1 = new chart.Card();
        card3 = new chart.Card();
        card4 = new chart.Card();
        chart_interval_lbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        panel1.setBackground(new Color(0, 0, 0));
        panel1.setForeground(new Color(0, 0, 0));

        panel2.setBackground(new Color(0, 0, 0));
        panel2.setForeground(new Color(128, 0, 128));

        card1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/income.png")));

        card3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/profit.png"))); // NOI18N

        card4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/cost.png"))); // NOI18N

        chart_interval_lbl.setFont(new java.awt.Font("sansserif", 0, 20)); // NOI18N
        chart_interval_lbl.setForeground(new java.awt.Color(224, 224, 224));
        chart_interval_lbl.setText("Chart Analysis from " + formattedDate5 + " to " + currentDate);
        
        
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        
        

        javax.swing.GroupLayout panel2Layout = new javax.swing.GroupLayout(panel2);
        panel2Layout.setHorizontalGroup(
        	panel2Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panel2Layout.createSequentialGroup()
        			.addGap(30)
        			.addGroup(panel2Layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(panel2Layout.createSequentialGroup()
        					.addComponent(chart_interval_lbl)
        					.addContainerGap(649, Short.MAX_VALUE))
        				.addGroup(panel2Layout.createSequentialGroup()
        					.addGroup(panel2Layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(panel2Layout.createSequentialGroup()
        							.addGap(10)
        							.addComponent(chart, GroupLayout.DEFAULT_SIZE, 1020, Short.MAX_VALUE))
        						.addGroup(panel2Layout.createSequentialGroup()
        							.addGap(65)
        							.addComponent(card1, GroupLayout.PREFERRED_SIZE, 261, GroupLayout.PREFERRED_SIZE)
        							.addGap(59)
        							.addComponent(card3, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
        							.addComponent(card4, GroupLayout.PREFERRED_SIZE, 269, GroupLayout.PREFERRED_SIZE)
        							.addGap(52)))
        					.addGap(30))))
        );
        panel2Layout.setVerticalGroup(
        	panel2Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panel2Layout.createSequentialGroup()
        			.addGap(30)
        			.addGroup(panel2Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(panel2Layout.createSequentialGroup()
        					.addGroup(panel2Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(card1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(card3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addGap(20)
        					.addComponent(chart_interval_lbl))
        				.addComponent(card4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(chart, GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
        			.addGap(20))
        );
        panel2.setLayout(panel2Layout);
        
        panel = new Panel();
        panel.setForeground(new Color(153, 51, 153));
        panel.setBackground(new Color(0, 0, 0));
        
        JLabel home = new JLabel();
        home.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\home.png"));
        home.setBounds(22, 16, 50, 32);
        panel.add(home);
        
        JLabel lectlabel = new JLabel("Lecturer Dashboard");
        lectlabel.setForeground(Color.WHITE);
        lectlabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 35));
        lectlabel.setBounds(70, 12, 250, 28);
        panel.add(lectlabel);
        
        
        
        panel_1 = new Panel();
        panel_1.setBackground(new Color(0, 0, 0));
        
    	imageAvatar1 = new ImageAvatar();

        // setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        // setUndecorated(true);

       //  imageAvatar1.setBorderColor(new java.awt.Color(0, 101, 180));
         imageAvatar1.setImage(new ImageIcon("C:\\Users\\PARADISE\\Pictures\\orsrc40980.jpg")); 
         panel_1.add(imageAvatar1);    	
        
        panel_2 = new Panel();
        panel_2.setForeground(new Color(0, 0, 0));
        panel_2.setBackground(new Color(0, 0, 0));

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1Layout.setHorizontalGroup(
        	panel1Layout.createParallelGroup(Alignment.TRAILING)
        		.addComponent(panel, GroupLayout.DEFAULT_SIZE, 1314, Short.MAX_VALUE)
        		.addGroup(panel1Layout.createSequentialGroup()
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(panel2, GroupLayout.PREFERRED_SIZE, 1090, GroupLayout.PREFERRED_SIZE)
        			.addGap(18))
        		.addGroup(panel1Layout.createSequentialGroup()
        			.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 1304, Short.MAX_VALUE)
        			.addContainerGap())
        );
        panel1Layout.setVerticalGroup(
        	panel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(panel1Layout.createSequentialGroup()
        			.addComponent(panel, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(panel1Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(panel2, 0, 0, Short.MAX_VALUE)
        				.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 559, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        JButton btnNewButton_3 = new JButton("Log out");
        btnNewButton_3.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        btnNewButton_3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setVisible(false);
                Login object = new Login();
                object.setVisible(true);
        	}
        });
        btnNewButton_3.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\logout.png"));
        btnNewButton_3.setForeground(new Color(255, 255, 255));
        btnNewButton_3.setBackground(new Color(0, 0, 0));
        panel_2.add(btnNewButton_3);
        
        btnNewButton_1 = new JButton("Student Profiles");
        btnNewButton_1.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        btnNewButton_1.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\Perfom.png"));
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 setVisible(false);
        		 StudentProfiles object = new StudentProfiles();
   	            object.setVisible(true);
        	}
        });
        btnNewButton_1.setForeground(new Color(255, 255, 255));
        btnNewButton_1.setBackground(new Color(64, 0, 64));
        panel_1.add(btnNewButton_1);
        
        btnNewButton_2 = new JButton("View Attendance");
        btnNewButton_2.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        btnNewButton_2.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\google_forms_24px.png"));
        btnNewButton_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 setVisible(false);
        		 myViewAttendance object = new myViewAttendance();
    	            object.setVisible(true);
        	}
        });
        btnNewButton_2.setForeground(new Color(255, 255, 255));
        btnNewButton_2.setBackground(new Color(64, 0, 64));
        panel_1.add(btnNewButton_2);
        
        submitReports = new JButton("Submit Reports");
        submitReports.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        submitReports.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\date.png"));
        btnNewButton_2.setIcon(new javax.swing.ImageIcon("C:\\Users\\PARADISE\\Desktop\\Employee\\icons\\google_forms_24px.png"));
        submitReports.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		submitReportsActionPerformed(e);
        	}
        });
        submitReports.setBackground(new Color(64, 0, 64));
        submitReports.setForeground(new Color(255, 255, 255));
        panel_1.add(submitReports);
        
        userIcon = new JButton("");
        userIcon.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        userIcon.setBackground(new Color(0, 0, 0));
       // userIcon.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\Employee\\project Image\\AddStudent.png"));
        
     // Resize the image in userIcon to 128x128 pixels
        ImageIcon icon = new ImageIcon("C:\\Users\\PARADISE\\Desktop\\Employee\\project Image\\bake.jpg");
        Image image = icon.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
        userIcon.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\Employee\\project Image\\AddStudent.png"));

        
        
        panel_1.add(userIcon);
        
        btnTimetable = new JButton("Timetable");
        btnTimetable.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\sort_window_24px.png"));
        btnTimetable.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setVisible(false);
        		myTimetable object = new myTimetable();
	            object.setVisible(true);
        	}
        });
        btnTimetable.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        btnTimetable.setForeground(new Color(255, 255, 255));
        btnTimetable.setBackground(new Color(64, 0, 64));
        panel_1.add(btnTimetable);
        
        btnExploreAttendance = new JButton("Attendance Summary");
        btnExploreAttendance.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\temp.png"));
        btnExploreAttendance.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
       		 setVisible(false);
       		explore_attendance object = new explore_attendance();
                object.setVisible(true);
        	}
        });
        btnExploreAttendance.setForeground(Color.WHITE);
        btnExploreAttendance.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        btnExploreAttendance.setBackground(new Color(64, 0, 64));
        panel_1.add(btnExploreAttendance);
        
        btnTakeAttendance = new JButton("Take Attendance");
        btnTakeAttendance.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setVisible(false);
       		 run object = new run();
   	            object.setVisible(true);
        		
        	}
        });
        
        btnUserManual = new JButton("User Manual");
        btnUserManual.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setVisible(false);
          		 help object = new help();
      	            object.setVisible(true);
           		
        		
        	}
        });
        btnUserManual.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\key.png"));
        btnUserManual.setForeground(Color.WHITE);
        btnUserManual.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        btnUserManual.setBackground(new Color(64, 0, 64));
        panel_1.add(btnUserManual);
        btnTakeAttendance.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\bell_26px.png"));
        btnTakeAttendance.setForeground(Color.WHITE);
        btnTakeAttendance.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        btnTakeAttendance.setBackground(new Color(64, 0, 64));
        panel_1.add(btnTakeAttendance);
        panel1.setLayout(panel1Layout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addContainerGap())
        );
        getContentPane().setLayout(layout);
        
        
        pack();
        setLocationRelativeTo(null);
        
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        chart.start();
    }//GEN-LAST:event_formWindowOpened
    private void submitReportsActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Desktop desktop = Desktop.getDesktop();
            URI uri = new URI("https://mail.google.com");
            desktop.browse(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LecturerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LecturerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LecturerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LecturerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LecturerDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private chart.Card card1;
   // Card1 card2 = new Card1();
    private chart.Card card3;
    private chart.Card card4;
    private chart.Chart chart;
    private javax.swing.JLabel chart_interval_lbl;
    private Panel panel1;
    private Panel panel2;
    private Panel panel;
    private Panel panel_1;
    private JButton submitReports;
    private JButton btnNewButton_1;
    private JButton btnNewButton_2;
    private Panel panel_2;
    private ImageAvatar imageAvatar1;
    private JButton userIcon;
    private JButton btnTimetable;
    private JButton btnExploreAttendance;
    private JButton btnTakeAttendance;
    private JButton btnUserManual;
}