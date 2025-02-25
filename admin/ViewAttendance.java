package admin;
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
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import javax.swing.ImageIcon;

public class ViewAttendance extends JFrame {
    private JTable tblData;
    DefaultTableModel model;
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    private Panel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ViewAttendance frame = new ViewAttendance();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ViewAttendance() {
    	setUndecorated(true);
        setTitle("View Attendance!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 500);
        contentPane = new Panel();
        contentPane.setBackground(new Color(0, 0, 128));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JComboBox<String> selectCourse = new JComboBox<>();
        selectCourse.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        selectCourse.setBounds(25, 68, 128, 28);
        contentPane.add(selectCourse);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
            stmt = con.createStatement();

            // Fetch course codes from the database and populate the JComboBox
            String query = "SELECT course_code FROM timetable";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String courseCode = rs.getString("course_code");
                selectCourse.addItem(courseCode);
            }

            tblData = new JTable();
            tblData.getTableHeader().setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
            tblData.setFont(new Font("Segoe UI Semilight", Font.BOLD, 12));
            JScrollPane scroll = new JScrollPane(tblData);
            scroll.setBounds(163, 75, 988, 402);
            contentPane.add(scroll);
            
            JButton btnNewButton = new JButton("Back");
            btnNewButton.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\logout.png"));
            btnNewButton.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		 setVisible(false);
            		 AdminDashboard object = new AdminDashboard();
                     object.setVisible(true);

            	}
            });
            btnNewButton.setBackground(new Color(0, 0, 0));
            btnNewButton.setForeground(new Color(255, 255, 255));
            btnNewButton.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
            btnNewButton.setBounds(25, 118, 128, 34);
            contentPane.add(btnNewButton);

            // Other UI components and action listeners...

            selectCourse.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String selectedCourse = (String) selectCourse.getSelectedItem();
                        String query = "SELECT * FROM attendance LEFT JOIN student ON attendance.student_no = student.student_no WHERE course_code = ?";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.setString(1, selectedCourse);
                        rs = preparedStatement.executeQuery();
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
