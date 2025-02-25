package lecturer;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.toedter.calendar.JDateChooser;

import login.Login;

import javax.swing.ImageIcon;
import java.awt.SystemColor;

public class explore_attendance extends JFrame {
    private JTable tblData;
    DefaultTableModel model;
    Connection conn = null;
    ResultSet rs = null;
    private JPanel contentPane;
    private JComboBox<String> selectCourse; // Declare selectCourse as an instance variable

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    explore_attendance frame = new explore_attendance();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public explore_attendance() {
        setUndecorated(true);
        setTitle("View Attendance!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 500);
        contentPane = new Panel();
        contentPane.setForeground(new Color(153, 51, 153));
        contentPane.setBackground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        selectCourse = new JComboBox<>(); // Initialize selectCourse here
        selectCourse.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        selectCourse.setBounds(198, 68, 128, 26);
        contentPane.add(selectCourse);
        
        
        String loggedInLecturerId = Login.userId;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
            conn = con;

            // Fetch course codes from timetable based on lecturer ID
            String query = "SELECT course_code FROM timetable WHERE lecturer_id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, loggedInLecturerId); // Assuming getLecturerId() retrieves the lecturer's ID
            ResultSet rs = stmt.executeQuery();
            
            // Populate the JComboBox with course codes
            while (rs.next()) {
                String courseCode = rs.getString("course_code");
                selectCourse.addItem(courseCode);
            }

            tblData = new JTable();
            JScrollPane scroll = new JScrollPane(tblData);
            scroll.setBounds(197, 105, 981, 365);
            contentPane.add(scroll);

            // Set table header font
            JTableHeader header = tblData.getTableHeader();
            header.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));

            JButton btnNewButton = new JButton("Back");
            btnNewButton.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\logout.png"));
            btnNewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    LecturerDashboard object = new LecturerDashboard();
                    object.setVisible(true);
                }
            });
            btnNewButton.setBackground(new Color(0, 0, 0));
            btnNewButton.setForeground(new Color(255, 255, 255));
            btnNewButton.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
            btnNewButton.setBounds(25, 328, 128, 33);
            contentPane.add(btnNewButton);

            JButton on_time = new JButton("On Time");
            on_time.setForeground(new Color(0, 0, 0));
            on_time.setBackground(new Color(0, 128, 0));
            on_time.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
            on_time.setBounds(25, 114, 128, 33);
            on_time.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    displayAttendanceData("On Time");
                }
            });
            contentPane.add(on_time);

            JButton late = new JButton("Late");
            late.setForeground(new Color(0, 0, 0));
            late.setBackground(new Color(255, 165, 0));
            late.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
            late.setBounds(25, 158, 128, 31);
            late.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    displayAttendanceData("Late");
                }
            });
            contentPane.add(late);

            JButton too_late = new JButton("Too Late");
            too_late.setForeground(new Color(0, 0, 0));
            too_late.setBackground(new Color(255, 69, 0));
            too_late.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
            too_late.setBounds(25, 200, 128, 31);
            too_late.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    displayAttendanceData("Too Late");
                }
            });
            contentPane.add(too_late);

            JButton flagged = new JButton("Flagged");
            flagged.setForeground(new Color(0, 0, 0));
            flagged.setBackground(new Color(255, 0, 0));
            flagged.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
            flagged.setBounds(25, 242, 128, 31);
            flagged.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	 displayFlaggedStudents();
                }
            });
            contentPane.add(flagged);

            JLabel lblNewLabel_1 = new JLabel("Select Course:");
            lblNewLabel_1.setForeground(new Color(255, 255, 255));
            lblNewLabel_1.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
            lblNewLabel_1.setBounds(25, 68, 128, 23);
            contentPane.add(lblNewLabel_1);
            
            JButton btnRetrieve = new JButton("Retrieve");
            btnRetrieve.addActionListener(new ActionListener() {
            	 public void actionPerformed(ActionEvent e) {
                     JFileChooser choose = new JFileChooser();
                     int result = choose.showSaveDialog(null);
                     if (result == JFileChooser.APPROVE_OPTION) {
                         File selectedFile = choose.getSelectedFile();
                         try {
                             FileWriter writer = new FileWriter(selectedFile + ".csv");

                             // Write column names (titles)
                             for (int i = 0; i < tblData.getColumnCount(); i++) {
                                 writer.write(tblData.getColumnName(i) + ",");
                             }
                             writer.write("\n");

                             // Write data
                             for (int i = 0; i < tblData.getRowCount(); i++) {
                                 for (int j = 0; j < tblData.getColumnCount(); j++) {
                                     writer.write(tblData.getValueAt(i, j).toString() + ",");
                                 }
                                 writer.write("\n");
                             }
                             writer.close();
                             JOptionPane.showMessageDialog(null, "CSV file saved successfully!");
                         } catch (IOException ex) {
                             ex.printStackTrace();
                         }
                     }
                 }
            });
            btnRetrieve.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\downl.png"));
            btnRetrieve.setForeground(new Color(0, 0, 0));
            btnRetrieve.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
            btnRetrieve.setBackground(new Color(255, 255, 255));
            btnRetrieve.setBounds(25, 284, 128, 33);
            contentPane.add(btnRetrieve);
            
            Panel panel = new Panel();
            panel.setForeground(new Color(153, 51, 153));
            panel.setBackground(new Color(0, 0, 0));
            panel.setBounds(388, 0, 393, 57);
            contentPane.add(panel);
            
            JLabel lblNewLabel_2 = new JLabel("Explore Attendance Data");
            lblNewLabel_2.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\tiles_26px.png"));
            lblNewLabel_2.setForeground(new Color(255, 255, 255));
            lblNewLabel_2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
            lblNewLabel_2.setBackground(Color.BLACK);
            panel.add(lblNewLabel_2);
            
            JPanel panel_1 = new JPanel();
            panel_1.setForeground(new Color(153, 51, 153));
            panel_1.setBackground(Color.BLACK);
            panel_1.setBounds(0, 0, 393, 57);
            contentPane.add(panel_1);
            
            JPanel panel_2 = new JPanel();
            panel_2.setForeground(new Color(153, 51, 153));
            panel_2.setBackground(Color.BLACK);
            panel_2.setBounds(766, 0, 434, 57);
            contentPane.add(panel_2);

        } catch (ClassNotFoundException | SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void displayFlaggedStudents() {
        try {
            String selectedCourse = selectCourse.getSelectedItem().toString();

            // Combined query to identify students with total absences and consecutive absences
            String query = "SELECT student.student_no, student.FName, student.LName, " +
                           "COUNT(a.status = 'Absent' OR NULL) AS num_absences, " +
                           "IFNULL(MAX(flagged_absences.consecutive_absences), 0) AS consecutive_absences " +
                           "FROM attendance a " +
                           "JOIN student ON a.student_no = student.student_no " +
                           "LEFT JOIN ( " +
                           "    SELECT student_no, " +
                           "           COUNT(*) AS consecutive_absences " +
                           "    FROM ( " +
                           "        SELECT student_no, " +
                           "               DATE(date) AS abs_date, " +
                           "               @rn := IF(@prev_student = student_no, @rn + 1, 1) AS rn, " +
                           "               @prev_student := student_no " +
                           "        FROM attendance, (SELECT @rn := 0, @prev_student := '') AS vars " +
                           "        WHERE course_code = ? " +
                           "        AND status = 'Absent' " +
                           "        ORDER BY student_no, date " +
                           "    ) AS absences " +
                           "    GROUP BY student_no, rn " +
                           "    HAVING COUNT(*) >= 3 " +
                           ") AS flagged_absences ON a.student_no = flagged_absences.student_no " +
                           "WHERE a.course_code = ? " +
                           "GROUP BY student.student_no, student.FName, student.LName";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, selectedCourse);
            preparedStatement.setString(2, selectedCourse);
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
            model = new DefaultTableModel();
            int col = rsmd.getColumnCount();

            for (int i = 1; i <= col; i++)
                model.addColumn(rsmd.getColumnName(i));

            while (rs.next()) {
                Object[] rowData = new Object[col];
                for (int i = 1; i <= col; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                model.addRow(rowData);
            }
            tblData.setModel(model);

            tblData.setFont(new Font("Segoe UI Semilight", Font.BOLD, 12));

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            tblData.setDefaultRenderer(Object.class, centerRenderer);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }





	private void displayAttendanceData(String status) {
        try {
            String selectedCourse = selectCourse.getSelectedItem().toString(); // Access selectCourse here
            String query = "SELECT attendance.*, student.FName, student.LName, courses.course_title " +
                           "FROM attendance " +
                           "JOIN student ON attendance.student_no = student.student_no " +
                           "JOIN courses ON attendance.course_code = courses.course_code " +
                           "WHERE attendance.status = ? AND attendance.course_code = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, selectedCourse);
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
            model = new DefaultTableModel();
            int col = rsmd.getColumnCount();

            for (int i = 1; i <= col; i++)
                model.addColumn(rsmd.getColumnName(i));

            while (rs.next()) {
                Object[] rowData = new Object[col];
                for (int i = 1; i <= col; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                model.addRow(rowData);
            }
            tblData.setModel(model);

            // Set table data font
            tblData.setFont(new Font("Segoe UI Semilight", Font.BOLD, 12));

            // Center align content in cells
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            tblData.setDefaultRenderer(Object.class, centerRenderer);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
