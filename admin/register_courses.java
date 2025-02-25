package admin;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ImageIcon;

public class register_courses extends JFrame {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    private JPanel contentPane;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JComboBox<String> dpt;
    private JSpinner semester;
    private JButton btnNewButton;
    private JLabel lblNewLabel_4;
    private JComboBox<String> stu_id;
    private JTextField course_code;
    private JButton update;
    private JComboBox<String> courses_registered;
    private JLabel lblRegisteredCourses;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    register_courses frame = new register_courses();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public register_courses() {
        setUndecorated(true);
        setTitle("Register New Course!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 430);
        contentPane = new Panel();
        contentPane.setForeground(new Color(0, 0, 0));
        contentPane.setBackground(new Color(0, 0, 128));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\expense.png"));
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        lblNewLabel.setBounds(161, 37, 52, 38);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("StudentId:");
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_1.setBounds(139, 198, 74, 25);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_6 = new JLabel("Semester:");
        lblNewLabel_6.setForeground(Color.WHITE);
        lblNewLabel_6.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_6.setBounds(139, 160, 74, 27);
        contentPane.add(lblNewLabel_6);

        JLabel lblNewLabel_7 = new JLabel("Department:");
        lblNewLabel_7.setForeground(Color.WHITE);
        lblNewLabel_7.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_7.setBounds(129, 123, 86, 25);
        contentPane.add(lblNewLabel_7);

        JButton submit = new JButton("Submit");
        submit.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\submiy.png"));
        submit.setForeground(new Color(0, 0, 0));
        submit.setBackground(new Color(255, 255, 255));
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitForm();
            }
        });
        submit.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        submit.setBounds(187, 337, 115, 38);
        contentPane.add(submit);

        dpt = new JComboBox<>();
        dpt.setBounds(274, 123, 172, 30);
        dpt.addItem("Computer Science");
        dpt.addItem("Nursing");
        dpt.setEditable(true);
        dpt.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        contentPane.add(dpt);

        semester = new JSpinner();
        semester.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        semester.setModel(new SpinnerNumberModel(1, 1, 8, 1));
        semester.setBounds(274, 162, 52, 29);
        contentPane.add(semester);

        btnNewButton = new JButton("Back");
        btnNewButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\PARADISE\\Desktop\\Employee\\INTACT ABODE\\Images\\logout.png"));
        btnNewButton.setForeground(new Color(255, 255, 255));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	 setVisible(false);
      		   AdminDashboard object = new AdminDashboard();
                 object.setVisible(true);

            }
        });
        
        semester.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateStudentIds();
            }
        });
        
        btnNewButton.setBackground(new Color(0, 0, 0));
        btnNewButton.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        btnNewButton.setBounds(54, 337, 109, 38);
        contentPane.add(btnNewButton);

        lblNewLabel_4 = new JLabel("New Course Registration!");
        lblNewLabel_4.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        lblNewLabel_4.setForeground(Color.WHITE);
        lblNewLabel_4.setBounds(218, 42, 205, 23);
        contentPane.add(lblNewLabel_4);
        
        stu_id = new JComboBox<>();
        stu_id.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        stu_id.setEditable(true);
        stu_id.setBounds(274, 203, 172, 30);
        stu_id.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayRegisteredCourses();
            }
        });
        contentPane.add(stu_id);
        
        JLabel courseCodeLbl = new JLabel("Course Code:");
        courseCodeLbl.setForeground(Color.WHITE);
        courseCodeLbl.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        courseCodeLbl.setBounds(122, 290, 91, 14);
        contentPane.add(courseCodeLbl);
        
        course_code = new JTextField();
        course_code.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        course_code.setBounds(274, 285, 109, 29);
        contentPane.add(course_code);
        course_code.setColumns(10);
        
        update = new JButton("Update");
        update.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\refresh.png"));
        update.setForeground(Color.WHITE);
        update.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        update.setBackground(new Color(0, 0, 139));
        update.setBounds(326, 337, 120, 38);
        update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateCourse();
            }
        });
        contentPane.add(update);

        courses_registered = new JComboBox<>();
        courses_registered.setBounds(274, 244, 89, 30);
        courses_registered.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        courses_registered.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedCourseCode = (String) courses_registered.getSelectedItem();
                if (selectedCourseCode != null) {
                    course_code.setText(selectedCourseCode);
                }
            }
        });
        contentPane.add(courses_registered);
        
        lblRegisteredCourses = new JLabel("Registered Courses:");
        lblRegisteredCourses.setForeground(Color.WHITE);
        lblRegisteredCourses.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblRegisteredCourses.setBounds(76, 248, 147, 23);
        contentPane.add(lblRegisteredCourses);
        
        // Add action listeners to update the stu_id combo box when semester or department changes
        ActionListener updateStudentIdListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateStudentIds();
            }
        };
        
        dpt.addActionListener(updateStudentIdListener);
    }

    private void updateStudentIds() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
            String selectedDepartment = (String) dpt.getSelectedItem();
            int selectedSemester = (int) semester.getValue();
            stu_id.removeAllItems();
            String query = "SELECT student_no FROM student WHERE Department = ? AND Semester = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, selectedDepartment);
            pstmt.setInt(2, selectedSemester);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                stu_id.addItem(rs.getString("student_no"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching student IDs: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void displayRegisteredCourses() {
        String studentId = (String) stu_id.getSelectedItem();
        if (studentId == null || studentId.isEmpty()) {
            courses_registered.removeAllItems();
            return;
        }

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
            String query = "SELECT course_code FROM course_reg WHERE student_no = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, studentId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    courses_registered.removeAllItems();
                    while (rs.next()) {
                        courses_registered.addItem(rs.getString("course_code"));
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error fetching registered courses: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void submitForm() {
        String studentId = (String) stu_id.getSelectedItem();
        String courseCode = course_code.getText();
        
        if (studentId == null || studentId.isEmpty() || courseCode.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select a student ID, enter a course code.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "")) {
            // Check if the course registration already exists
            String checkQuery = "SELECT COUNT(*) FROM course_reg WHERE student_no = ? AND course_code = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, studentId);
                checkStmt.setString(2, courseCode);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(null, "This course is already registered for the student.");
                        return;
                    }
                }
            }

            // If not exists, insert the new course registration
            String insertQuery = "INSERT INTO course_reg (student_no, course_code) VALUES ( ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, studentId);
                insertStmt.setString(2, courseCode);
              
                insertStmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Course registered successfully!");
                displayRegisteredCourses();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error registering course: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateCourse() {
        String studentId = (String) stu_id.getSelectedItem();
        String courseCode = course_code.getText();
        if (studentId == null || studentId.isEmpty() || courseCode.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select a student ID, enter a course code.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
             PreparedStatement pstmt = conn.prepareStatement("UPDATE course_reg SET course_code = ? WHERE student_no = ?")) {
            pstmt.setString(1, courseCode);
            pstmt.setString(3, studentId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Course updated successfully!");
            displayRegisteredCourses();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating course: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
