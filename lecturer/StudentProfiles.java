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
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import login.Login;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

public class StudentProfiles extends JFrame {
    private JTable tblData;
    private DefaultTableModel model;
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private Panel contentPane;
    private int lecturerId; // Assuming you have a way to get the lecturer's ID

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    StudentProfiles frame = new StudentProfiles();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public StudentProfiles() {
        setUndecorated(true);
        setTitle("View Attendance!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 450);
        contentPane = new Panel();
        contentPane.setForeground(new Color(153, 51, 153));
        contentPane.setBackground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        tblData = new JTable();
        JScrollPane scroll = new JScrollPane(tblData);
        scroll.setBounds(179, 135, 765, 263);
        contentPane.add(scroll);

        JButton dashboard_return = new JButton("Back");
        dashboard_return.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\logout.png"));
        dashboard_return.setForeground(Color.WHITE);
        dashboard_return.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        dashboard_return.setBackground(new Color(0, 0, 0));
        dashboard_return.setBounds(46, 182, 123, 36);

        dashboard_return.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                LecturerDashboard object = new LecturerDashboard();
                object.setVisible(true);
            }
        });

        contentPane.add(dashboard_return);

        JButton retrieve = new JButton("Retrieve");
        retrieve.setForeground(new Color(255, 255, 255));
        retrieve.setBackground(new Color(75, 0, 130));
        retrieve.addActionListener(new ActionListener() {
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
        retrieve.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        retrieve.setBounds(46, 135, 123, 36);
        contentPane.add(retrieve);

        // Get lecturer's ID from Login.getUserId() assuming it returns the lecturer's ID
        String userId = Login.getUserId();
        if (userId == null || userId.isEmpty()) {
            // Handle case where userId is null or empty
            JOptionPane.showMessageDialog(null, "User ID is null or empty. Cannot proceed.");
            // You might choose to exit the application or take appropriate action
            // Here, we are setting lecturerId to 0 as a default value
            lecturerId = 0;
        } else {
            // Parse lecturer's ID from the user ID string
            try {
                lecturerId = Integer.parseInt(userId);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid lecturer ID format. Cannot proceed.");
                e.printStackTrace();
                // You might choose to exit the application or take appropriate action
                // Here, we are setting lecturerId to 0 as a default value
                lecturerId = 0;
            }
        }

        try {
            // Database connection setup
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");

            // Prepare SQL statement with a parameter for lecturer_id
            String query = "SELECT DISTINCT t.course_code " +
                    "FROM timetable t " +
                    "WHERE t.lecturer_id = ?";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, lecturerId);

            // Execute query and get result set
            ResultSet rs1 = preparedStatement.executeQuery();

            // Initialize the combo box model
            DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();

            // Populate the combo box with course codes
            while (rs1.next()) {
                String courseCode = rs1.getString("course_code");
                comboBoxModel.addElement(courseCode);
            }

            // Create the JComboBox for course codes
            JComboBox<String> course_code = new JComboBox<>(comboBoxModel);
            course_code.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
            course_code.setBounds(219, 93, 137, 31);
            contentPane.add(course_code);
            
            

            // Initialize the table model
            model = new DefaultTableModel(new String[]{"student_no", "FName", "LName", "Gender", "Semester", "Department", "Email", "PhoneNo"}, 0) {
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return String.class; // Ensures all columns are treated as String
                }
            };

            // Set table title font
            tblData.getTableHeader().setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
            tblData.setFont(new Font("Segoe UI Semilight", Font.BOLD, 12));
            

            // Populate the table model with data for the selected course code
            course_code.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedCourseCode = (String) course_code.getSelectedItem();
                    loadData(selectedCourseCode); // Load data for the selected course code
                }
            });

            // Set the table model to the JTable
            tblData.setModel(model);

            // Search label
            JLabel lblNewLabel = new JLabel("Search Student:");
            lblNewLabel.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\Employee\\icons\\search_26px.png"));
            lblNewLabel.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
            lblNewLabel.setForeground(new Color(255, 255, 255));
            lblNewLabel.setBounds(46, 88, 160, 36);
            contentPane.add(lblNewLabel);
            
            Panel panel = new Panel();
            panel.setForeground(new Color(153, 51, 153));
            panel.setBackground(new Color(0, 0, 0));
            panel.setBounds(0, 0, 1000, 58);
            contentPane.add(panel);
            
            JLabel lblWelcomeUserNull = new JLabel("Welcome user "+Login.userId+" !");
            lblWelcomeUserNull.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\male_user_50px.png"));
            lblWelcomeUserNull.setForeground(Color.WHITE);
            lblWelcomeUserNull.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
            panel.add(lblWelcomeUserNull);

        } catch (ClassNotFoundException | SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void loadData(String courseCode) {
        try {
            // Database connection setup
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");

            // Prepare SQL statement with parameters for lecturer_id and course_code
            String query = "SELECT s.student_no, s.FName, s.LName, s.Gender, s.Semester, s.Department, s.Email, s.PhoneNo " +
                    "FROM student s " +
                    "JOIN timetable t ON s.semester = t.semester " +
                    "WHERE t.lecturer_id = ? AND t.course_code = ?";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, lecturerId);
            preparedStatement.setString(2, courseCode);

            // Execute query and get result set
            ResultSet rs = preparedStatement.executeQuery();

            // Clear existing data in the table model
            model.setRowCount(0);

            // Populate the table model with data
            while (rs.next()) {
                String studentNo = rs.getString("student_no");
                String fName = rs.getString("FName");
                String lName = rs.getString("LName");
                String gender = rs.getString("Gender");
                String semester = rs.getString("Semester");
                String department = rs.getString("Department");
                String email = rs.getString("Email");
                String phoneNo = rs.getString("PhoneNo");
                model.addRow(new Object[]{studentNo, fName, lName, gender, semester, department, email, phoneNo});
            }

            // Notify user if no data found for the selected course code
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No student data found for the selected course code.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
