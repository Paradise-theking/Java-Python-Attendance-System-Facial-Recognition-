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
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.ImageIcon;

public class LecRegister extends JFrame {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    private Panel contentPane;
    private JTextField workNo;
    private JTextField fname;
    private JTextField mail;
    private JTextField password;
    private JTextField lname;
    private JComboBox<String> dpt;
    private JButton btnNewButton;
    private JButton btnUpdate;
    private JButton btnRemove;
    private JTable tblData;
    private JLabel lblNewLabel_5;
    private JTextField searchtxt;
    private JPanel panel;
    private JLabel lblNewLabel_4;
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LecRegister frame = new LecRegister();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LecRegister() {
    	setUndecorated(true);   	
        setTitle("Register New Lecturer!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 520);
        contentPane = new Panel();
        contentPane.setForeground(new Color(0, 0, 102));
        contentPane.setBackground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel_1 = new JLabel("WorkNo:");
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_1.setBounds(88, 143, 86, 30);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Name:");
        lblNewLabel_2.setForeground(Color.WHITE);
        lblNewLabel_2.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_2.setBounds(88, 188, 86, 31);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_7 = new JLabel("Department:");
        lblNewLabel_7.setForeground(Color.WHITE);
        lblNewLabel_7.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_7.setBounds(88, 294, 86, 28);
        contentPane.add(lblNewLabel_7);

        JLabel lblNewLabel_8 = new JLabel("Email:");
        lblNewLabel_8.setForeground(Color.WHITE);
        lblNewLabel_8.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_8.setBounds(88, 333, 47, 14);
        contentPane.add(lblNewLabel_8);

        JLabel lblNewLabel_9 = new JLabel("Password:");
        lblNewLabel_9.setForeground(Color.WHITE);
        lblNewLabel_9.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_9.setBounds(88, 383, 74, 14);
        contentPane.add(lblNewLabel_9);

        workNo = new JTextField();
        workNo.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        workNo.setBounds(203, 140, 264, 33);
        contentPane.add(workNo);
        workNo.setColumns(10);

        fname = new JTextField();
        fname.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        fname.setBounds(203, 185, 264, 34);
        contentPane.add(fname);
        fname.setColumns(10);

        mail = new JTextField();
        mail.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        mail.setBounds(203, 332, 183, 33);
        contentPane.add(mail);
        mail.setColumns(10);

        password = new JTextField();
        password.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        password.setBounds(203, 376, 183, 32);
        contentPane.add(password);
        password.setColumns(10);

        JButton Submit = new JButton("Submit");
        Submit.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\submiy.png"));
        Submit.setForeground(new Color(0, 0, 0));
        Submit.setBackground(new Color(255, 255, 255));
        Submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
                    String query = "insert into lecturer (	lecturer_id , Name, Surname, Department, Email, Password) values (?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setInt(1, Integer.parseInt(workNo.getText()));
                    ps.setString(2, fname.getText());
                    ps.setString(3, lname.getText());
                    ps.setString(4, (String) dpt.getSelectedItem());
                    ps.setString(5, mail.getText());
                    ps.setString(6, password.getText());

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
        Submit.setBounds(171, 424, 114, 38);
        contentPane.add(Submit);

        JLabel lblNewLabel_3 = new JLabel("Surname:");
        lblNewLabel_3.setForeground(Color.WHITE);
        lblNewLabel_3.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_3.setBounds(88, 233, 86, 32);
        contentPane.add(lblNewLabel_3);

        lname = new JTextField();
        lname.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lname.setColumns(10);
        lname.setBounds(203, 230, 264, 35);
        contentPane.add(lname);

        dpt = new JComboBox<>();
        dpt.setBounds(203, 288, 172, 34);
        dpt.addItem("Computer Science");
        dpt.addItem("Nursing");
        dpt.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        contentPane.add(dpt);

        btnNewButton = new JButton("Back");
        btnNewButton.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\Employee\\INTACT ABODE\\Images\\logout.png"));
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
        btnNewButton.setBounds(37, 424, 114, 38);
        contentPane.add(btnNewButton);

        btnUpdate = new JButton("Update");
        btnUpdate.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\refresh.png"));
        btnUpdate.setForeground(new Color(255, 255, 255));
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
                    String query = "UPDATE lecturer SET Name=?, Surname=?, Department=?, Email=?, Password=? WHERE lecturer_id=?";
                    PreparedStatement ps = conn.prepareStatement(query);
                    // Assuming tblData is a JTable declared and initialized somewhere else in the class
                    //int rowSelected = tblData.getSelectedRow();

                    //if (rowSelected != -1) {
                        ps.setInt(6, Integer.parseInt(workNo.getText()));
                        ps.setString(1, fname.getText());
                        ps.setString(2, lname.getText());
                        ps.setString(3, (String) dpt.getSelectedItem());
                        ps.setString(4, mail.getText());
                        ps.setString(5, password.getText());

                        //int i =
                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(btnUpdate,  " Record successfully updated");
                        ps.close();
                        conn.close();
                   // }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        btnUpdate.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        btnUpdate.setBackground(new Color(0, 0, 128));
        btnUpdate.setBounds(305, 424, 126, 38);
        contentPane.add(btnUpdate);

        btnRemove = new JButton("Remove");
        btnRemove.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\close.png"));
        btnRemove.setForeground(new Color(255, 255, 255));
        btnRemove.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnRemove.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        btnRemove.setBackground(new Color(128, 0, 0));
        btnRemove.setBounds(441, 424, 126, 38);
        contentPane.add(btnRemove);
        
        lblNewLabel_5 = new JLabel("Search:");
        lblNewLabel_5.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\loupe.png"));
        lblNewLabel_5.setForeground(Color.WHITE);
        lblNewLabel_5.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_5.setBounds(60, 90, 89, 26);
        contentPane.add(lblNewLabel_5);
        
        searchtxt = new JTextField();
        searchtxt.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        searchtxt.setColumns(10);
        searchtxt.setBounds(178, 95, 133, 30);
        contentPane.add(searchtxt);
        
        Panel panel = new Panel();
        panel.setForeground(new Color(0, 0, 0));
        panel.setBackground(new Color(0, 0, 51));
        panel.setBounds(0, 0, 600, 53);
        contentPane.add(panel);
        
                JLabel lblNewLabel = new JLabel("");
                panel.add(lblNewLabel);
                lblNewLabel.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\about.png"));
                lblNewLabel.setForeground(Color.WHITE);
                lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        
        lblNewLabel_4 = new JLabel("New Lecturer Registration!");
        lblNewLabel_4.setForeground(new Color(255, 255, 255));
        lblNewLabel_4.setFont(new Font("Segoe UI Semilight", Font.BOLD, 22));
        panel.add(lblNewLabel_4);
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
                if (searchValue.length() == 6) { // Check if StudentNo has 6 digits
                    try {
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
                        String query = "SELECT * FROM lecturer WHERE lecturer_id=?";
                        PreparedStatement ps = conn.prepareStatement(query);
                        ps.setInt(1, Integer.parseInt(searchValue));
                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                            workNo.setText(rs.getString("lecturer_id"));
                            fname.setText(rs.getString("Name"));
                            lname.setText(rs.getString("Surname"));
                            dpt.setSelectedItem(rs.getString("Department"));
                            mail.setText(rs.getString("Email"));
                            password.setText(rs.getString("Password"));
                        } else {
                            JOptionPane.showMessageDialog(null, "No record found for the given StudentNo.");
                        }


                        rs.close();
                        ps.close();
                        conn.close();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid StudentNo format: " + ex.getMessage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}
