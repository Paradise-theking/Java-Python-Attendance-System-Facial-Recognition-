package admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;

public class timetable extends JFrame {
    private JTable tblData;
    private Panel contentPane;
    private JTextField course_code;
    private JFormattedTextField stime;
    private JFormattedTextField etime;
    private JComboBox<String> venueComboBox;
    private JSpinner day_spinner;
    private JTextField lecturer_id;
    private DefaultTableModel model;
    private JTextField search_code;
    private JSpinner search_day;
    private JSpinner semester_spinner;

    // Day names corresponding to day integers
    private final String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                timetable frame = new timetable();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public timetable() {
        setUndecorated(true);
        setTitle("Set/Update Timetable");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1250, 520);
        contentPane = new Panel();
        contentPane.setBackground(new Color(0, 0, 153));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblIcon = new JLabel(new ImageIcon("path_to_your_icon.png"));
        lblIcon.setBounds(154, 26, 35, 38);
        contentPane.add(lblIcon);

        JLabel lblCourseCode = new JLabel("Course_Code:");
        lblCourseCode.setForeground(Color.WHITE);
        lblCourseCode.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblCourseCode.setBounds(56, 176, 86, 23);
        contentPane.add(lblCourseCode);

        course_code = new JTextField();
        course_code.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        course_code.setColumns(10);
        course_code.setBounds(181, 173, 264, 32);
        contentPane.add(course_code);

        JLabel lblStartTime = new JLabel("Start_Time:");
        lblStartTime.setForeground(Color.WHITE);
        lblStartTime.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblStartTime.setBounds(56, 222, 86, 14);
        contentPane.add(lblStartTime);

        stime = new JFormattedTextField(new SimpleDateFormat("HH:mm"));
        stime.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        stime.setBounds(181, 220, 264, 32);
        contentPane.add(stime);

        JLabel lblEndTime = new JLabel("End_Time:");
        lblEndTime.setForeground(Color.WHITE);
        lblEndTime.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblEndTime.setBounds(56, 265, 86, 14);
        contentPane.add(lblEndTime);

        etime = new JFormattedTextField(new SimpleDateFormat("HH:mm"));
        etime.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        etime.setBounds(181, 263, 264, 32);
        contentPane.add(etime);

        JLabel lblDay = new JLabel("Day:");
        lblDay.setForeground(Color.WHITE);
        lblDay.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblDay.setBounds(56, 311, 86, 14);
        contentPane.add(lblDay);

        day_spinner = new JSpinner(new SpinnerNumberModel(0, 0, 4, 1)); // Day spinner from 0 to 4 (Monday to Friday)
        day_spinner.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        day_spinner.setBounds(181, 306, 64, 29);
        contentPane.add(day_spinner);

        JLabel lblVenue = new JLabel("Venue:");
        lblVenue.setForeground(Color.WHITE);
        lblVenue.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblVenue.setBounds(59, 347, 74, 14);
        contentPane.add(lblVenue);

        venueComboBox = new JComboBox<>(new String[]{"Lab1", "Lab2", "C2", "E5", "L2"});
        venueComboBox.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        venueComboBox.setBounds(181, 344, 264, 32);
        contentPane.add(venueComboBox);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\submiy.png"));
        btnSubmit.setForeground(new Color(0, 0, 0));
        btnSubmit.setBackground(new Color(255, 255, 255));
        btnSubmit.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        btnSubmit.setBounds(181, 450, 114, 39);
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });
        contentPane.add(btnSubmit);

        JLabel lblLecturerid = new JLabel("Lecturer_id:");
        lblLecturerid.setForeground(Color.WHITE);
        lblLecturerid.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblLecturerid.setBounds(56, 396, 74, 14);
        contentPane.add(lblLecturerid);

        lecturer_id = new JTextField();
        lecturer_id.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lecturer_id.setColumns(10);
        lecturer_id.setBounds(181, 389, 264, 32);
        contentPane.add(lecturer_id);

        JButton update = new JButton("Update");
        update.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\refresh.png"));
        update.setForeground(Color.WHITE);
        update.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        update.setBackground(new Color(0, 0, 128));
        update.setBounds(320, 450, 125, 39);
        update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleUpdate();
            }
        });
        contentPane.add(update);

        JButton clear = new JButton("Clear");
        clear.setForeground(Color.WHITE);
        clear.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        clear.setBackground(new Color(255, 0, 0));
        clear.setBounds(470, 451, 89, 38);
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleClear();
            }
        });
        contentPane.add(clear);

        // Initialize JTable for displaying timetable entries
        tblData = new JTable();
        JScrollPane scroll = new JScrollPane(tblData);
        scroll.setBounds(554, 70, 660, 343);
        contentPane.add(scroll);

        JLabel lblTitle = new JLabel("Set/Update Timetable");
        lblTitle.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\date.png"));
        lblTitle.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(189, 26, 205, 23);
        contentPane.add(lblTitle);

        search_code = new JTextField();
        search_code.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        search_code.setToolTipText("search course code");
        search_code.setColumns(10);
        search_code.setBounds(56, 93, 133, 32);
        contentPane.add(search_code);

        JLabel lblDay_1 = new JLabel("Day:");
        lblDay_1.setForeground(Color.WHITE);
        lblDay_1.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblDay_1.setBounds(203, 99, 47, 23);
        contentPane.add(lblDay_1);

        search_day = new JSpinner(new SpinnerNumberModel(0, 0, 4, 1)); // Search day spinner from 0 to 4 (Monday to Friday)
        search_day.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        search_day.setBounds(260, 93, 74, 29);
        contentPane.add(search_day);

        JButton searchbtn = new JButton("Search");
        searchbtn.setForeground(new Color(255, 255, 255));
        searchbtn.setBackground(new Color(0, 0, 0));
        searchbtn.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        searchbtn.setBounds(356, 90, 89, 32);
        searchbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSearch();
            }
        });
        contentPane.add(searchbtn);

        JLabel lblSemester = new JLabel("Semester:");
        lblSemester.setForeground(Color.WHITE);
        lblSemester.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblSemester.setBounds(256, 306, 98, 23);
        contentPane.add(lblSemester);

        semester_spinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1)); // Semester spinner from 1 to 8
        semester_spinner.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        semester_spinner.setBounds(381, 306, 64, 29);
        contentPane.add(semester_spinner);

        // Initialize the table model with column names
        model = new DefaultTableModel(new Object[]{"course_code", "start_time", "end_time", "day", "venue", "lecturer_id", "semester"}, 0);
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
        btnBack.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\Employee\\INTACT ABODE\\Images\\logout.png"));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        btnBack.setBackground(new Color(0, 0, 0));
        btnBack.setBounds(56, 451, 105, 38);
        contentPane.add(btnBack);

        // Load data to display in the table
        loadData();
    }

    private void loadData() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM timetable")) {
            model.setRowCount(0);

            while (rs.next()) {
                String courseCode = rs.getString("course_code");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");
                int day = rs.getInt("day");
                String venue = rs.getString("venue");
                String lecturerId = rs.getString("lecturer_id");
                int semester = rs.getInt("semester");

                if (day < 0 || day >= dayNames.length) {
                    System.out.println("Invalid day value: " + day);
                    continue;
                }

                model.addRow(new Object[]{courseCode, startTime, endTime, dayNames[day], venue, lecturerId, semester});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSubmit() {
        String courseCode = course_code.getText();
        String startTime = stime.getText();
        String endTime = etime.getText();
        int day = (Integer) day_spinner.getValue();
        String venue = (String) venueComboBox.getSelectedItem();
        String lecturerId = lecturer_id.getText();
        int semester = (Integer) semester_spinner.getValue();

        if (courseCode.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || lecturerId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
             PreparedStatement psCheck = con.prepareStatement("SELECT * FROM timetable WHERE course_code = ? AND day = ?");
             PreparedStatement psInsert = con.prepareStatement("INSERT INTO timetable (course_code, start_time, end_time, day, venue, lecturer_id, semester) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            // Check if the entry already exists
            psCheck.setString(1, courseCode);
            psCheck.setInt(2, day);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Timetable entry already exists for the given course code and day!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Insert new entry
                psInsert.setString(1, courseCode);
                psInsert.setString(2, startTime);
                psInsert.setString(3, endTime);
                psInsert.setInt(4, day);
                psInsert.setString(5, venue);
                psInsert.setString(6, lecturerId);
                psInsert.setInt(7, semester);

                psInsert.executeUpdate();

                JOptionPane.showMessageDialog(this, "Timetable entry added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                handleClear();
                loadData(); // Refresh table
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding timetable entry: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void handleUpdate() {
        String courseCode = course_code.getText();
        String startTime = stime.getText();
        String endTime = etime.getText();
        int day = (Integer) day_spinner.getValue();
        String venue = (String) venueComboBox.getSelectedItem();
        String lecturerId = lecturer_id.getText();
        int semester = (Integer) semester_spinner.getValue();

        if (courseCode.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || lecturerId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
             PreparedStatement ps = con.prepareStatement("UPDATE timetable SET start_time = ?, end_time = ?, day = ?, venue = ?, lecturer_id = ?, semester = ? WHERE course_code = ? AND day = ?")) {

            ps.setString(1, startTime);
            ps.setString(2, endTime);
            ps.setInt(3, day);
            ps.setString(4, venue);
            ps.setString(5, lecturerId);
            ps.setInt(6, semester);
            ps.setString(7, courseCode);
            ps.setInt(8, day); // Ensure to match both course_code and day for the WHERE clause

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Timetable entry updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No matching timetable entry found for course code and day!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            handleClear();
            loadData(); // Refresh table

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating timetable entry: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void handleClear() {
        course_code.setText("");
        stime.setText("");
        etime.setText("");
        day_spinner.setValue(0);
        venueComboBox.setSelectedIndex(0);
        lecturer_id.setText("");
        semester_spinner.setValue(1); // Reset semester spinner to default value
    }

    private void handleSearch() {
        String searchCode = search_code.getText();
        int searchDay = (Integer) search_day.getValue();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
             PreparedStatement ps = con.prepareStatement("SELECT * FROM timetable WHERE course_code = ? AND day = ?")) {

            ps.setString(1, searchCode);
            ps.setInt(2, searchDay);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                course_code.setText(rs.getString("course_code"));
                stime.setText(rs.getString("start_time"));
                etime.setText(rs.getString("end_time"));
                day_spinner.setValue(rs.getInt("day"));
                venueComboBox.setSelectedItem(rs.getString("venue"));
                lecturer_id.setText(rs.getString("lecturer_id"));
                semester_spinner.setValue(rs.getInt("semester"));
            } else {
                JOptionPane.showMessageDialog(this, "No timetable entry found!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching for timetable entry: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}

