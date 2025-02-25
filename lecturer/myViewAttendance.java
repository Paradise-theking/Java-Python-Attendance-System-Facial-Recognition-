package lecturer;

import javax.swing.*;


import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import login.Login;

import java.awt.*;

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

public class myViewAttendance extends JFrame {
    private JTable tblData;
    DefaultTableModel model;
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    private JPanel contentPane;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JLabel lblNewLabel_4;
   // private JLabel lecturerId;
    private JButton btnNewButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    myViewAttendance frame = new myViewAttendance();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public myViewAttendance() {
    	setUndecorated(true);
        setTitle("View Attendance!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 550);
        contentPane = new Panel();
        contentPane.setForeground(new Color(153, 51, 153));
        contentPane.setBackground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JComboBox<String> selectCourse = new JComboBox<>();
        selectCourse.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        selectCourse.setBounds(25, 68, 128, 26);
        contentPane.add(selectCourse);

        try {
            // Establishing database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
            stmt = con.createStatement();

            // Query to fetch courses associated with the logged-in lecturer
            String query = "SELECT DISTINCT course_code FROM timetable WHERE lecturer_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, Login.getUserId()); // Using getUserId() from Login class
            ResultSet rs1 = preparedStatement.executeQuery();

            selectCourse.removeAllItems();
            while (rs1.next()) {
                String courseCode = rs1.getString("course_code"); // Corrected column name to course_code
                selectCourse.addItem(courseCode);
            }

            // Creating table to display attendance data
            tblData = new JTable();
            tblData.getTableHeader().setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
            tblData.setFont(new Font("Segoe UI Semilight", Font.BOLD, 12));
            JScrollPane scroll = new JScrollPane(tblData);
            scroll.setBounds(165, 75, 974, 425);
            contentPane.add(scroll);

            // Button to return to lecturer dashboard
            JButton dashboard_return = new JButton("Back");
            dashboard_return.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\logout.png"));
            dashboard_return.setForeground(Color.WHITE);
            dashboard_return.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
            dashboard_return.setBackground(new Color(0, 0, 0));
            dashboard_return.setBounds(25, 167, 128, 36);

            dashboard_return.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    LecturerDashboard object = new LecturerDashboard();
                    object.setVisible(true);
                }
            });

            contentPane.add(dashboard_return);

            // Button to retrieve and save attendance as CSV
            JButton retrieve = new JButton("Retrieve");
            retrieve.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\downl.png"));
            retrieve.setForeground(new Color(0, 0, 0));
            retrieve.setBackground(new Color(255, 255, 255));
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
            retrieve.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
            retrieve.setBounds(25, 111, 128, 36);
            contentPane.add(retrieve);
            
            Panel panel = new Panel();
            panel.setForeground(new Color(153, 51, 153));
            panel.setBackground(new Color(0, 0, 0));
            panel.setBounds(0, 0, 1200, 57);
            contentPane.add(panel);
            
            JLabel lblWelcomeUserNull = new JLabel("Welcome user "+Login.userId+" !");
            lblWelcomeUserNull.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\male_user_50px.png"));
            lblWelcomeUserNull.setForeground(Color.WHITE);
            lblWelcomeUserNull.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
            panel.add(lblWelcomeUserNull);

            // Action listener for selectCourse combo box to fetch attendance data
            selectCourse.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String selectedCourse = (String) selectCourse.getSelectedItem();

                        // Query to fetch attendance data for the selected course
                        String query = "SELECT * FROM attendance LEFT JOIN student ON attendance.student_no = student.student_no WHERE course_code = ?";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.setString(1, selectedCourse);
                        rs = preparedStatement.executeQuery();

                        // Populate table with fetched attendance data
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
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        } catch (ClassNotFoundException | SQLException e1) {
            e1.printStackTrace();
        }
    }
}
