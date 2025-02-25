package admin;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Users extends JFrame {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    private JPanel contentPane;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    DefaultTableModel model;
    private JLabel lblNewLabel_4;
    private JTable tblData;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Users frame = new Users();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Users() {
    	setUndecorated(true);
        setTitle("Register Admin Student!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(200, 100, 850, 500);
        contentPane = new Panel();
        contentPane.setForeground(new Color(0, 0, 128));
        contentPane.setBackground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\Employee\\project Image\\about.png"));
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        lblNewLabel.setBounds(155, 11, 35, 38);
        contentPane.add(lblNewLabel);

        JButton viewStu = new JButton("Student");
        viewStu.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\Perfom.png"));
        viewStu.setForeground(new Color(255, 255, 255));
        viewStu.setBackground(new Color(0, 0, 128));
        viewStu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
                    String query = "SELECT * FROM Student";
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(query);

                    // Create table model
                    model = new DefaultTableModel();
                    tblData.setModel(model);

                    // Add columns
                    model.addColumn("student_no");
                    model.addColumn("Name");
                    model.addColumn("Surname");
                    model.addColumn("Gender");
                    model.addColumn("Semester");
                    model.addColumn("Department");
                    model.addColumn("Email");
                    model.addColumn("PhoneNo");

                    // Add rows
                    while (rs.next()) {
                        String studentID = rs.getString("student_no");
                        String name = rs.getString("FName");
                        String surname = rs.getString("LName");
                        String gender = rs.getString("Gender");
                        String semester = rs.getString("Semester");
                        String dpt = rs.getString("Department");
                        String email = rs.getString("Email");
                        String phoneNo = rs.getString("PhoneNO");
                        model.addRow(new Object[]{studentID, name, surname, gender, semester, dpt, email, phoneNo});
                    }
                    //tblData.getColumnModel().getColumn(7).setCellRenderer(new PasswordRendering());
                } catch (Exception ex) {
                    ex.printStackTrace();
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
        });
        viewStu.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        viewStu.setBounds(22, 71, 121, 33);
        contentPane.add(viewStu);

        lblNewLabel_4 = new JLabel("New Student Registration!");
        lblNewLabel_4.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        lblNewLabel_4.setForeground(Color.WHITE);
        lblNewLabel_4.setBounds(188, 26, 205, 23);
        contentPane.add(lblNewLabel_4);

        JButton viewLect = new JButton("Lecturer");
        viewLect.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\man.png"));
        viewLect.setForeground(new Color(255, 255, 255));
        viewLect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
                    String query = "SELECT * FROM Lecturer";
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(query);

                    // Create table model
                    model = new DefaultTableModel() {
                        // Override isCellEditable method to make all cells non-editable
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    tblData.setModel(model);

                    // Add columns
                    model.addColumn("lecturer_id");
                    model.addColumn("Name");
                    model.addColumn("Surname");
                    model.addColumn("Department");
                    model.addColumn("Email");
                    model.addColumn("Password");

                    // Add rows
                    while (rs.next()) {
                        String 	lecturer_id  = rs.getString("lecturer_id");
                        String name = rs.getString("Name");
                        String surname = rs.getString("Surname");
                        String dpt = rs.getString("Department");
                        String mail = rs.getString("Email");
                        String password = rs.getString("Password");

                        model.addRow(new Object[]{lecturer_id, name, surname, dpt, mail, password});
                    }

                    // Set custom renderer for password column
                    tblData.getColumnModel().getColumn(5).setCellRenderer(new PasswordRendering());
                } catch (Exception ex) {
                    ex.printStackTrace();
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
        });
        viewLect.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        viewLect.setBackground(new Color(0, 0, 128));
        viewLect.setBounds(22, 129, 121, 33);
        contentPane.add(viewLect);

        JButton viewAdmn = new JButton("Admin");
        viewAdmn.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\account.png"));
        viewAdmn.setForeground(new Color(255, 255, 255));
        viewAdmn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
                    String query = "SELECT * FROM admin";
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(query);

                    // Create table model
                    model = new DefaultTableModel() {
                        // Override isCellEditable method to make all cells non-editable
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    tblData.setModel(model);

                    // Add columns
                    model.addColumn("WorkNo");
                    model.addColumn("FirstName");
                    model.addColumn("LastName");
                    model.addColumn("Password");

                    // Add rows
                    while (rs.next()) {
                        String WorkNo = rs.getString("WorkNo");
                        String name = rs.getString("FirstName");
                        String surname = rs.getString("LastName");
                        String password = rs.getString("Password");

                        model.addRow(new Object[]{WorkNo, name, surname, password});
                    }

                    // Set custom renderer for password column
                    tblData.getColumnModel().getColumn(3).setCellRenderer(new PasswordRendering());
                } catch (Exception ex) {
                    ex.printStackTrace();
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
        });
        viewAdmn.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        viewAdmn.setBackground(new Color(0, 0, 128));
        viewAdmn.setBounds(22, 182, 121, 33);
        contentPane.add(viewAdmn);

        JPanel panel = new JPanel();
        panel.setBounds(165, 60, 626, 390);
        contentPane.add(panel);
        panel.setLayout(null);

        JButton dashboard_return = new JButton("Back");
        dashboard_return.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\Employee\\INTACT ABODE\\Images\\logout.png"));
        dashboard_return.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                AdminDashboard object = new AdminDashboard();
                object.setVisible(true);
            }
        });
        dashboard_return.setForeground(Color.WHITE);
        dashboard_return.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        dashboard_return.setBackground(new Color(0, 0, 0));
        dashboard_return.setBounds(22, 233, 121, 33);
        contentPane.add(dashboard_return);
        
                // Initialize tblData
                tblData = new JTable();
                // Add tblData to a JScrollPane
                tblData.getTableHeader().setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
                tblData.setFont(new Font("Segoe UI Semilight", Font.BOLD, 12));
                JScrollPane scrollPane = new JScrollPane(tblData);
                scrollPane.setBounds(165, 60, 626, 390);
                contentPane.add(scrollPane);
    }
}

class RegisterStudent extends JFrame {
    // Placeholder implementation for setting student data
    public void setStudentData(String studentID, String name, String surname, String gender, String semester, String department, String email, String phoneNo) {
        // Implement this method to set the data in the registration form fields
        // For now, let's just print the data to console
        System.out.println("Student ID: " + studentID);
        System.out.println("Name: " + name);
        System.out.println("Surname: " + surname);
        System.out.println("Gender: " + gender);
        System.out.println("Semester: " + semester);
        System.out.println("Department: " + department);
        System.out.println("Email: " + email);
        System.out.println("Phone No: " + phoneNo);
    }
}

// Password Renderer Class
class PasswordRendering extends DefaultTableCellRenderer {
    public PasswordRendering() {
        super();
    }

    public void setValue(Object value) {
        if (value != null) {
            String password = "";
            for (int i = 0; i < value.toString().length(); i++) {
                password += "*"; // Replace each character with an asterisk
            }
            setText(password);
        } else {
            setText("");
        }
    }
}
