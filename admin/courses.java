package admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;

public class courses extends JFrame {
    private JTable tblData;
    private Panel contentPane;
    private JTextField course_code;
    private JFormattedTextField course_title;
    private DefaultTableModel model;
    private JTextField search_code;

    // Day names corresponding to day integers
    private final String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                courses frame = new courses();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public courses() {
        setUndecorated(true);
        setTitle("Set/Update Timetable");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 550, 550);
        contentPane = new Panel();
        contentPane.setForeground(new Color(0, 0, 102));
        contentPane.setBackground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblIcon = new JLabel(new ImageIcon("path_to_your_icon.png"));
        lblIcon.setBounds(154, 26, 35, 38);
        contentPane.add(lblIcon);

        JLabel lblCourseCode = new JLabel("Course_Code:");
        lblCourseCode.setForeground(Color.WHITE);
        lblCourseCode.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblCourseCode.setBounds(56, 136, 116, 23);
        contentPane.add(lblCourseCode);

        course_code = new JTextField();
        course_code.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        course_code.setColumns(10);
        course_code.setBounds(214, 134, 264, 32);
        contentPane.add(course_code);

        JLabel lblStartTime = new JLabel("Course Title:");
        lblStartTime.setForeground(Color.WHITE);
        lblStartTime.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblStartTime.setBounds(56, 182, 116, 14);
        contentPane.add(lblStartTime);

        course_title = new JFormattedTextField(new SimpleDateFormat("HH:mm"));
        course_title.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        course_title.setBounds(214, 181, 264, 32);
        contentPane.add(course_title);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\submiy.png"));
        btnSubmit.setForeground(new Color(0, 0, 0));
        btnSubmit.setBackground(new Color(255, 255, 255));
        btnSubmit.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        btnSubmit.setBounds(183, 241, 116, 39);
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });
        contentPane.add(btnSubmit);

        JButton update = new JButton("Update");
        update.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\refresh.png"));
        update.setForeground(Color.WHITE);
        update.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        update.setBackground(new Color(0, 0, 128));
        update.setBounds(309, 240, 132, 40);
        update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleUpdate();
            }
        });
        contentPane.add(update);

        JButton clear = new JButton("Clear");
        clear.setForeground(Color.WHITE);
        clear.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        clear.setBackground(new Color(255, 0, 0));
        clear.setBounds(451, 240, 89, 40);
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleClear();
            }
        });
        contentPane.add(clear);

        // Initialize JTable for displaying timetable entries
        tblData = new JTable();
        JScrollPane scroll = new JScrollPane(tblData);
        scroll.setBounds(84, 291, 421, 224);
        contentPane.add(scroll);

        search_code = new JTextField();
        search_code.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        search_code.setToolTipText("search course code");
        search_code.setColumns(10);
        search_code.setBounds(246, 75, 133, 32);
        contentPane.add(search_code);

        JButton searchbtn = new JButton("Search");
        searchbtn.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\search_26px.png"));
        searchbtn.setForeground(new Color(255, 255, 255));
        searchbtn.setBackground(new Color(0, 0, 0));
        searchbtn.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        searchbtn.setBounds(389, 75, 116, 32);
        searchbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSearch();
            }
        });
        contentPane.add(searchbtn);

        // Initialize the table model with column names
        model = new DefaultTableModel(new Object[]{"course_code", "course_title"}, 0);
        tblData.getTableHeader().setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        tblData.setFont(new Font("Segoe UI Semilight", Font.BOLD, 12));
        tblData.setModel(model);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                AdminDashboard object = new AdminDashboard();
                object.setVisible(true);
            }
        });
        btnBack.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\logout.png"));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        btnBack.setBackground(new Color(0, 0, 0));
        btnBack.setBounds(56, 241, 115, 38);
        contentPane.add(btnBack);

        JLabel lblSearchByCode = new JLabel("Search by Code:");
        lblSearchByCode.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\loupe.png"));
        lblSearchByCode.setForeground(Color.WHITE);
        lblSearchByCode.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblSearchByCode.setBounds(56, 79, 145, 23);
        contentPane.add(lblSearchByCode);

        Panel panel = new Panel();
        panel.setForeground(new Color(0, 0, 0));
        panel.setBackground(new Color(0, 0, 51));
        panel.setBounds(0, 0, 550, 51);
        contentPane.add(panel);

        JLabel lblNewLabel = new JLabel("Set/Update Courses");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\edit.png"));
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        panel.add(lblNewLabel);

        // Load data to display in the table
        loadData();
    }

    private void loadData() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM courses")) {
            model.setRowCount(0);

            while (rs.next()) {
                String courseCode = rs.getString("course_code");
                String courseTitle = rs.getString("course_title");

                model.addRow(new Object[]{courseCode, courseTitle});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSubmit() {
        String courseCode = course_code.getText();
        String courseTitle = course_title.getText();

        if (courseCode.isEmpty() || courseTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
             PreparedStatement psCheck = con.prepareStatement("SELECT * FROM courses WHERE course_code = ?");
             PreparedStatement psInsert = con.prepareStatement("INSERT INTO courses (course_code, course_title) VALUES (?, ?)")) {

            // Check if the entry already exists
            psCheck.setString(1, courseCode);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Course entry already exists for the given course code!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Insert new entry
                psInsert.setString(1, courseCode);
                psInsert.setString(2, courseTitle);
                psInsert.executeUpdate();

                JOptionPane.showMessageDialog(this, "Course entry added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                handleClear();
                loadData(); // Refresh table
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding course entry: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() {
        String courseCode = course_code.getText();
        String courseTitle = course_title.getText();

        if (courseCode.isEmpty() || courseTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
             PreparedStatement psCheck = con.prepareStatement("SELECT * FROM courses WHERE course_code = ?");
             PreparedStatement psUpdate = con.prepareStatement("UPDATE courses SET course_title = ? WHERE course_code = ?")) {

            // Check if the entry exists
            psCheck.setString(1, courseCode);
            ResultSet rs = psCheck.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "No course entry found for the given course code!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Update the existing entry
                psUpdate.setString(1, courseTitle);
                psUpdate.setString(2, courseCode);
                psUpdate.executeUpdate();

                JOptionPane.showMessageDialog(this, "Course entry updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                handleClear();
                loadData(); // Refresh table
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating course entry: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void handleClear() {
        course_code.setText("");
        course_title.setText("");
    }

    private void handleSearch() {
        String searchCode = search_code.getText();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
             PreparedStatement ps = con.prepareStatement("SELECT * FROM courses WHERE course_code = ?")) {

            ps.setString(1, searchCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                course_code.setText(rs.getString("course_code"));
                course_title.setText(rs.getString("course_title"));
            } else {
                JOptionPane.showMessageDialog(this, "No course entry found!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching for course entry: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
