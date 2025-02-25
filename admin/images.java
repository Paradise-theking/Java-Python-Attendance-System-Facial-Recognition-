package admin;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class images extends JFrame {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    private Panel contentPane;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JTable tblData;
    DefaultTableModel model;
    private JComboBox<String> dpt;
    private JSpinner semester;
    private JButton btnNewButton;
    private JTextField img_path;
    private JLabel upload;
    private JComboBox<String> stu_id;
    private JPanel panel;
    private JLabel lblNewLabel;
    private JLabel lblNewLabel_2;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    images frame = new images();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public images() {
        setUndecorated(true);
        setTitle("Register New Student!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 620);
        contentPane = new Panel();
        contentPane.setForeground(new Color(0, 0, 102));
        contentPane.setBackground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel_1 = new JLabel("StudentId:");
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_1.setBounds(134, 174, 74, 28);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_6 = new JLabel("Semester:");
        lblNewLabel_6.setForeground(Color.WHITE);
        lblNewLabel_6.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_6.setBounds(134, 136, 74, 27);
        contentPane.add(lblNewLabel_6);

        JLabel lblNewLabel_7 = new JLabel("Department:");
        lblNewLabel_7.setForeground(Color.WHITE);
        lblNewLabel_7.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_7.setBounds(124, 99, 86, 25);
        contentPane.add(lblNewLabel_7);

        JButton submit_upload = new JButton("Submit");
        submit_upload.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\submiy.png"));
        submit_upload.setForeground(new Color(0, 0, 0));
        submit_upload.setBackground(new Color(255, 255, 255));
        submit_upload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitForm();
            }
        });
        submit_upload.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        submit_upload.setBounds(377, 539, 119, 41);
        contentPane.add(submit_upload);

        dpt = new JComboBox<>();
        dpt.setBounds(249, 94, 172, 30);
        dpt.addItem("Computer Science");
        dpt.addItem("Nursing");
        dpt.setEditable(true);
        dpt.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        contentPane.add(dpt);

        semester = new JSpinner();
        semester.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        semester.setModel(new SpinnerNumberModel(1, 1, 8, 1));
        semester.setBounds(247, 135, 52, 29);
        contentPane.add(semester);

        btnNewButton = new JButton("Back");
        btnNewButton.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\logout.png"));
        btnNewButton.setForeground(new Color(255, 255, 255));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	 setVisible(false);
      		   AdminDashboard object = new AdminDashboard();
                 object.setVisible(true);

            }
        });
        btnNewButton.setBackground(new Color(0, 0, 0));
        btnNewButton.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        btnNewButton.setBounds(249, 539, 109, 40);
        contentPane.add(btnNewButton);

        JLabel lblNewLabel_1_1 = new JLabel("Image:");
        lblNewLabel_1_1.setForeground(Color.WHITE);
        lblNewLabel_1_1.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_1_1.setBounds(148, 225, 60, 27);
        contentPane.add(lblNewLabel_1_1);

        upload = new JLabel("");
        upload.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\iconn.png"));
        upload.setBackground(Color.BLACK);
        upload.setForeground(Color.ORANGE);
        upload.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        upload.setBounds(247, 225, 319, 265);
        contentPane.add(upload);

        JButton search_img = new JButton("Search Image");
        search_img.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\search_26px.png"));
        search_img.setForeground(Color.WHITE);
        search_img.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        search_img.setBackground(new Color(0, 128, 0));
        search_img.setBounds(55, 486, 153, 42);
        search_img.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    img_path.setText(selectedFile.getAbsolutePath());
                    upload.setIcon(new ImageIcon(new ImageIcon(selectedFile.getAbsolutePath()).getImage().getScaledInstance(319, 265, Image.SCALE_SMOOTH)));
                }
            }
        });
        contentPane.add(search_img);

        img_path = new JTextField();
        img_path.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        img_path.setBounds(217, 490, 371, 38);
        contentPane.add(img_path);
        img_path.setColumns(10);
        
        stu_id = new JComboBox<>();
        stu_id.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        stu_id.setEditable(true);
        stu_id.setBounds(245, 176, 172, 30);
        contentPane.add(stu_id);
        
        Panel panel = new Panel();
        panel.setForeground(new Color(0, 0, 0));
        panel.setBackground(new Color(0, 0, 51));
        panel.setBounds(0, 0, 650, 60);
        contentPane.add(panel);
        
        lblNewLabel = new JLabel("");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        panel.add(lblNewLabel);
        
        lblNewLabel_2 = new JLabel("Add Student Image");
        lblNewLabel_2.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\icons8_User_Male_50px.png"));
        lblNewLabel_2.setForeground(Color.WHITE);
        lblNewLabel_2.setFont(new Font("Segoe UI Semilight", Font.BOLD, 22));
        panel.add(lblNewLabel_2);
        
        // Add action listeners to update the stu_id combo box when semester or department changes
        ActionListener updateStudentIdListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateStudentIds();
            }
        };
        semester.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateStudentIds();
            }
        });
        dpt.addActionListener(updateStudentIdListener);
    }

    private void updateStudentIds() {
        try {
            // Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");

            // Get the selected department and semester
            String selectedDepartment = (String) dpt.getSelectedItem();
            int selectedSemester = (int) semester.getValue();

            // Clear the existing items in the stu_id combo box
            stu_id.removeAllItems();

            // Create and execute the SQL query
            String query = "SELECT student_no FROM student WHERE Department = '" + selectedDepartment + "' AND Semester = " + selectedSemester;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            // Add the results to the stu_id combo box
            while (rs.next()) {
                stu_id.addItem(rs.getString("student_no"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching student IDs: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void submitForm() {
        String studentId = (String) stu_id.getSelectedItem();
        String imagePath = img_path.getText();
        
        if (studentId == null || studentId.isEmpty() || imagePath.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select a student ID and an image.");
            return;
        }

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");

            // Create and execute the SQL query to insert the image
            String query = "INSERT INTO images (student_no, image) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, studentId);
            
            // Read the image file and set it as a binary stream
            FileInputStream fis = new FileInputStream(imagePath);
            pstmt.setBinaryStream(2, fis, (int) new File(imagePath).length());

            // Execute the update
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Image uploaded successfully!");

        } catch (SQLException e) {
            if (e instanceof com.mysql.cj.jdbc.exceptions.PacketTooBigException) {
                JOptionPane.showMessageDialog(null, "The image file is too large to upload. Please choose a smaller file.");
            } else {
                JOptionPane.showMessageDialog(null, "Error uploading image: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading image file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
