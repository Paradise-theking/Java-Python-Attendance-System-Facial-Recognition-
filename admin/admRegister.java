package admin;

import javax.swing.*;

import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import com.toedter.calendar.JDateChooser;

public class admRegister extends JFrame {
    private Panel contentPane;
    private JTextField workNo;
    private JTextField fname;
    private JTextField lname;
    private JTextField password;
    private JButton btnNewButton;
    private JTextField searchtxt;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    admRegister frame = new admRegister();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public admRegister() {
        setUndecorated(true);
        setTitle("Register Admin Student!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 500);
        contentPane = new Panel();
        contentPane.setForeground(new Color(0, 0, 102));
        contentPane.setBackground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel_1 = new JLabel("WorkNo:");
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_1.setBounds(51, 156, 86, 14);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Firstname:");
        lblNewLabel_2.setForeground(Color.WHITE);
        lblNewLabel_2.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_2.setBounds(51, 199, 86, 14);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("Lastname:");
        lblNewLabel_3.setForeground(Color.WHITE);
        lblNewLabel_3.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_3.setBounds(51, 240, 86, 14);
        contentPane.add(lblNewLabel_3);

        JLabel lblNewLabel_9 = new JLabel("Password:");
        lblNewLabel_9.setForeground(Color.WHITE);
        lblNewLabel_9.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_9.setBounds(54, 281, 74, 14);
        contentPane.add(lblNewLabel_9);

        JLabel lblNewLabel_9_1 = new JLabel("Semester Start:");
        lblNewLabel_9_1.setForeground(Color.WHITE);
        lblNewLabel_9_1.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_9_1.setBounds(29, 335, 120, 14);
        contentPane.add(lblNewLabel_9_1);

        JLabel lblNewLabel_9_2 = new JLabel("Semester End:");
        lblNewLabel_9_2.setForeground(Color.WHITE);
        lblNewLabel_9_2.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_9_2.setBounds(29, 374, 108, 14);
        contentPane.add(lblNewLabel_9_2);

        workNo = new JTextField();
        workNo.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        workNo.setBounds(176, 154, 264, 32);
        contentPane.add(workNo);
        workNo.setColumns(10);

        fname = new JTextField();
        fname.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        fname.setBounds(176, 197, 264, 32);
        contentPane.add(fname);
        fname.setColumns(10);

        lname = new JTextField();
        lname.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lname.setBounds(176, 238, 264, 32);
        contentPane.add(lname);
        lname.setColumns(10);

        password = new JTextField();
        password.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        password.setBounds(176, 280, 183, 32);
        contentPane.add(password);
        password.setColumns(10);

        JDateChooser sdate = new JDateChooser();
        sdate.setBackground(new Color(0, 0, 0));
        sdate.setDateFormatString("yyyy-MM-dd");
        sdate.setBounds(176, 323, 183, 27);
        contentPane.add(sdate);

        JDateChooser edate = new JDateChooser();
        edate.setBackground(new Color(0, 0, 0));
        edate.setDateFormatString("yyyy-MM-dd");
        edate.setBounds(176, 361, 183, 27);
        contentPane.add(edate);

        JButton Submit = new JButton("Submit");
        Submit.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\submiy.png"));
        Submit.setForeground(new Color(0, 0, 0));
        Submit.setBackground(new Color(255, 255, 255));
        Submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
                    String query = "insert into admin (WorkNo, FirstName, LastName, Password, semester_start, semester_end) values (?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setLong(1, Long.parseLong(workNo.getText()));
                    ps.setString(2, fname.getText());
                    ps.setString(3, lname.getText());
                    ps.setString(4, password.getText());

                    // Convert JDateChooser date to java.sql.Date
                    java.sql.Date startDate = new java.sql.Date(sdate.getDate().getTime());
                    java.sql.Date endDate = new java.sql.Date(edate.getDate().getTime());

                    ps.setDate(5, startDate);
                    ps.setDate(6, endDate);

                    int i = ps.executeUpdate();

                    if (i > 0) {
                        JOptionPane.showMessageDialog(Submit, i + " Record successfully added");
                    } else {
                        JOptionPane.showMessageDialog(null, "Password or UserID is incorrect!");
                    }

                    ps.close();
                    conn.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        Submit.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        Submit.setBounds(156, 409, 130, 38);
        contentPane.add(Submit);

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
        btnNewButton.setBounds(29, 409, 108, 38);
        contentPane.add(btnNewButton);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\refresh.png"));
        btnUpdate.setForeground(new Color(255, 255, 255));
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
                    String query = "UPDATE admin SET FirstName=?, LastName=?, Password=?, semester_start=?, semester_end=? WHERE WorkNo=?";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setString(1, fname.getText());
                    ps.setString(2, lname.getText());
                    ps.setString(3, password.getText());

                    // Convert JDateChooser date to java.sql.Date
                    java.sql.Date startDate = new java.sql.Date(sdate.getDate().getTime());
                    java.sql.Date endDate = new java.sql.Date(edate.getDate().getTime());

                    ps.setDate(4, startDate);
                    ps.setDate(5, endDate);

                    String workNoText = workNo.getText().trim();
                    if (!workNoText.isEmpty()) {
                        ps.setInt(6, Integer.parseInt(workNoText));
                    } else {
                        JOptionPane.showMessageDialog(btnUpdate, "WorkNo cannot be empty");
                        return;
                    }

                    int updatedRows = ps.executeUpdate();

                    if (updatedRows > 0) {
                        JOptionPane.showMessageDialog(btnUpdate, "Record successfully updated");
                    } else {
                        JOptionPane.showMessageDialog(btnUpdate, "No records updated");
                    }

                    ps.close();
                    conn.close();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(btnUpdate, "Invalid WorkNo format: " + ex.getMessage());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        btnUpdate.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        btnUpdate.setBackground(new Color(0, 0, 128));
        btnUpdate.setBounds(310, 410, 130, 37);
        contentPane.add(btnUpdate);

        JButton btnRemove = new JButton("Remove");
        btnRemove.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\close.png"));
        btnRemove.setForeground(new Color(255, 255, 255));
        btnRemove.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        btnRemove.setBackground(new Color(128, 0, 0));
        btnRemove.setBounds(460, 409, 120, 38);
        contentPane.add(btnRemove);

        JLabel lblNewLabel_5 = new JLabel("Search:");
        lblNewLabel_5.setForeground(new Color(255, 255, 255));
        lblNewLabel_5.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_5.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\loupe.png"));
        lblNewLabel_5.setBounds(57, 86, 89, 26);
        contentPane.add(lblNewLabel_5);

        searchtxt = new JTextField();
        searchtxt.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        searchtxt.setBounds(156, 91, 130, 27);
        contentPane.add(searchtxt);
        searchtxt.setColumns(10);
        
        Panel panel = new Panel();
        panel.setForeground(new Color(0, 0, 0));
        panel.setBackground(new Color(0, 0, 51));
        panel.setBounds(0, 0, 600, 51);
        contentPane.add(panel);
        
        JLabel lblNewLabel = new JLabel("Admin Registration");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\about.png"));
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        panel.add(lblNewLabel);

        searchtxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchAndUpdate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchAndUpdate();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }

            private void searchAndUpdate() {
                String searchValue = searchtxt.getText().trim();
                if (searchValue.length() == 6) { // Check if WorkNo has 6 digits
                    try {
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
                        String query = "SELECT * FROM admin WHERE WorkNo=?";
                        PreparedStatement ps = conn.prepareStatement(query);
                        ps.setInt(1, Integer.parseInt(searchValue));
                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                            workNo.setText(rs.getString("WorkNo"));
                            fname.setText(rs.getString("FirstName"));
                            lname.setText(rs.getString("LastName"));
                            password.setText(rs.getString("Password"));
                            
                            // Convert java.sql.Date to java.util.Date for JDateChooser
                            java.util.Date startDate = rs.getDate("semester_start");
                            java.util.Date endDate = rs.getDate("semester_end");
                            
                            if (startDate != null) {
                                sdate.setDate(startDate);
                            } else {
                                sdate.setDate(null);
                            }
                            
                            if (endDate != null) {
                                edate.setDate(endDate);
                            } else {
                                edate.setDate(null);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "No record found for the given WorkNo.");
                        }

                        rs.close();
                        ps.close();
                        conn.close();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid WorkNo format: " + ex.getMessage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}

       
