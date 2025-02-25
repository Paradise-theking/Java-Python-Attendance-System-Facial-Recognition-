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
import java.text.SimpleDateFormat;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class stuRegister extends JFrame {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    private JPanel contentPane;

    private JTextField studentNo;
    private JTextField fname;
    private JTextField mail;
    private JTextField phoneNum;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JRadioButton Male, Female;
    private JTextField lname;
    private JComboBox<String> dpt;
    private JSpinner semester;
    private JTextField searchtxt;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    stuRegister frame = new stuRegister();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public stuRegister() {
        setUndecorated(true);
        setTitle("Register New Student!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 730, 620);
        contentPane = new Panel();
        contentPane.setForeground(new Color(0, 0, 0));
        contentPane.setBackground(new Color(0, 0, 128));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("StudentNo:");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel.setBounds(121, 144, 86, 14);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_2 = new JLabel("Firstname:");
        lblNewLabel_2.setForeground(Color.WHITE);
        lblNewLabel_2.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_2.setBounds(121, 193, 86, 14);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_5 = new JLabel("Gender:");
        lblNewLabel_5.setForeground(Color.WHITE);
        lblNewLabel_5.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_5.setBounds(136, 297, 58, 14);
        contentPane.add(lblNewLabel_5);

        JLabel lblNewLabel_6 = new JLabel("Semester:");
        lblNewLabel_6.setForeground(Color.WHITE);
        lblNewLabel_6.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_6.setBounds(133, 434, 74, 27);
        contentPane.add(lblNewLabel_6);

        JLabel lblNewLabel_7 = new JLabel("Department:");
        lblNewLabel_7.setForeground(Color.WHITE);
        lblNewLabel_7.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_7.setBounds(108, 387, 86, 25);
        contentPane.add(lblNewLabel_7);

        JLabel lblNewLabel_8 = new JLabel("Email:");
        lblNewLabel_8.setForeground(Color.WHITE);
        lblNewLabel_8.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_8.setBounds(149, 345, 58, 25);
        contentPane.add(lblNewLabel_8);

        JLabel lblNewLabel_9 = new JLabel("Phone No:");
        lblNewLabel_9.setForeground(Color.WHITE);
        lblNewLabel_9.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_9.setBounds(329, 437, 74, 21);
        contentPane.add(lblNewLabel_9);

        studentNo = new JTextField();
        studentNo.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        studentNo.setBounds(234, 140, 264, 27);
        contentPane.add(studentNo);
        studentNo.setColumns(10);

        fname = new JTextField();
        fname.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        fname.setBounds(234, 189, 264, 27);
        contentPane.add(fname);
        fname.setColumns(10);

        mail = new JTextField();
        mail.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        mail.setBounds(234, 346, 264, 26);
        contentPane.add(mail);
        mail.setColumns(10);

        phoneNum = new JTextField();
        phoneNum.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        phoneNum.setBounds(429, 436, 183, 27);
        contentPane.add(phoneNum);
        phoneNum.setColumns(10);

        Male = new JRadioButton("Male");
        buttonGroup.add(Male);
        Male.setFont(new Font("Tahoma", Font.BOLD, 11));
        Male.setBounds(233, 287, 109, 38);
        contentPane.add(Male);

        Female = new JRadioButton("Female");
        buttonGroup.add(Female);
        Female.setFont(new Font("Tahoma", Font.BOLD, 11));
        Female.setBounds(389, 287, 109, 38);
        contentPane.add(Female);

        JButton Submit = new JButton("Submit");
        Submit.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\submiy.png"));
        Submit.setForeground(new Color(0, 0, 0));
        Submit.setBackground(new Color(255, 255, 255));
        Submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");

                    String sql = "INSERT INTO student (student_no, FName, LName, Gender, Semester, Department, Email, PhoneNo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);

                    pstmt.setString(1, studentNo.getText());
                    pstmt.setString(2, fname.getText());
                    pstmt.setString(3, lname.getText());
                    pstmt.setString(4, Male.isSelected() ? "Male" : "Female");
                    pstmt.setInt(5, (Integer) semester.getValue());
                    pstmt.setString(6, (String) dpt.getSelectedItem());
                    pstmt.setString(7, mail.getText());
                    pstmt.setString(8, phoneNum.getText());

                    int result = pstmt.executeUpdate();

                    if (result > 0) {
                        JOptionPane.showMessageDialog(Submit, "Record successfully added.");
                    } else {
                        JOptionPane.showMessageDialog(Submit, "Record insertion failed.");
                    }

                    // Clear the form after successful submission
                    clearFields();

                    pstmt.close();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Submit, "Error: " + ex.getMessage());
                }
            }
        });
        Submit.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        Submit.setBounds(179, 488, 122, 38);
        contentPane.add(Submit);

        JLabel lblNewLabel_3 = new JLabel("Lastname:");
        lblNewLabel_3.setForeground(Color.WHITE);
        lblNewLabel_3.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblNewLabel_3.setBounds(121, 242, 86, 14);
        contentPane.add(lblNewLabel_3);

        lname = new JTextField();
        lname.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lname.setColumns(10);
        lname.setBounds(234, 238, 264, 27);
        contentPane.add(lname);

        dpt = new JComboBox<>();
        dpt.setBounds(234, 384, 202, 30);
        dpt.addItem("Computer Science");
        dpt.addItem("Nursing");
        dpt.setEditable(true);
        dpt.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        contentPane.add(dpt);

        semester = new JSpinner();
        semester.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        semester.setModel(new SpinnerNumberModel(1, 1, 8, 1));
        semester.setBounds(234, 434, 67, 27);
        contentPane.add(semester);

        JButton clear = new JButton("Clear");
        clear.setForeground(Color.WHITE);
        clear.setBackground(new Color(128, 0, 128));
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        clear.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        clear.setBounds(602, 488, 89, 38);
        contentPane.add(clear);

        searchtxt = new JTextField();
        searchtxt.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        searchtxt.setBounds(288, 83, 210, 27);
        contentPane.add(searchtxt);
        searchtxt.setColumns(10);

        searchtxt.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            public void removeUpdate(DocumentEvent e) {
                update();
            }

            public void insertUpdate(DocumentEvent e) {
                update();
            }

            public void update() {
                if (searchtxt.getText().length() == 9) {
                    performSearch();
                } else {
                    clearFields();
                }
            }
        });

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\refresh.png"));
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateRecord();
            }
        });
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        btnUpdate.setBackground(new Color(0, 0, 128));
        btnUpdate.setBounds(324, 488, 122, 38);
        contentPane.add(btnUpdate);

        JLabel lblSearchStudent = new JLabel("Search Student:");
        lblSearchStudent.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\loupe.png"));
        lblSearchStudent.setForeground(Color.WHITE);
        lblSearchStudent.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        lblSearchStudent.setBounds(121, 83, 135, 27);
        contentPane.add(lblSearchStudent);

        JLabel lblRegisterStudent = new JLabel("Register Student!");
        lblRegisterStudent.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\icons8_User_Male_50px.png"));
        lblRegisterStudent.setForeground(Color.WHITE);
        lblRegisterStudent.setFont(new Font("Segoe UI Semilight", Font.BOLD, 18));
        lblRegisterStudent.setBounds(258, 22, 240, 50);
        contentPane.add(lblRegisterStudent);

        JButton Delete = new JButton("Delete");
        Delete.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\close.png"));
        Delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");

                    // Delete the student record
                    String deleteStudentQuery = "DELETE FROM student WHERE student_no = ?";
                    PreparedStatement deleteStudentPs = conn.prepareStatement(deleteStudentQuery);
                    deleteStudentPs.setLong(1, Long.parseLong(studentNo.getText()));

                    int deleteStudentResult = deleteStudentPs.executeUpdate();

                    if (deleteStudentResult > 0) {
                        // Success message
                        JOptionPane.showMessageDialog(Delete, "Record successfully deleted.");

                        // Clear the form
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(Delete, "No record found for deletion.");
                    }

                    deleteStudentPs.close();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Delete, "Error: " + ex.getMessage());
                }
            }
        });
        Delete.setForeground(Color.WHITE);
        Delete.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        Delete.setBackground(Color.RED);
        Delete.setBounds(467, 488, 115, 38);
        contentPane.add(Delete);

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
        btnBack.setBounds(49, 488, 109, 38);
        contentPane.add(btnBack);
    }

    private void performSearch() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
            String studentQuery = "SELECT * FROM student WHERE student_no = ?";
            PreparedStatement studentPs = conn.prepareStatement(studentQuery);
            studentPs.setLong(1, Long.parseLong(searchtxt.getText()));
            ResultSet studentRs = studentPs.executeQuery();

            if (studentRs.next()) {
                studentNo.setText(studentRs.getString("student_no"));
                fname.setText(studentRs.getString("FName"));
                lname.setText(studentRs.getString("LName"));
                String gender = studentRs.getString("Gender");
                if (gender.equals("Male")) {
                    Male.setSelected(true);
                } else {
                    Female.setSelected(true);
                }
                semester.setValue(studentRs.getInt("Semester"));
                dpt.setSelectedItem(studentRs.getString("Department"));
                mail.setText(studentRs.getString("Email"));
                phoneNum.setText(studentRs.getString("PhoneNo"));

            } else {
                JOptionPane.showMessageDialog(null, "Student not found!");
            }

            studentPs.close();
            conn.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void clearFields() {
        studentNo.setText("");
        fname.setText("");
        lname.setText("");
        buttonGroup.clearSelection();
        semester.setValue(1);
        dpt.setSelectedIndex(0);
        mail.setText("");
        phoneNum.setText("");
    }

    private void updateRecord() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");
            String query = "UPDATE student SET FName=?, LName=?, Gender=?, Semester=?, Department=?, Email=?, PhoneNo=? WHERE student_no=?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, fname.getText());
            ps.setString(2, lname.getText());
            ps.setString(3, Male.isSelected() ? "Male" : "Female"); // Correctly determine gender based on selected radio button
            ps.setInt(4, (int) semester.getValue());
            ps.setString(5, (String) dpt.getSelectedItem());
            ps.setString(6, mail.getText());
            ps.setLong(7, Long.parseLong(phoneNum.getText()));
            ps.setLong(8, Long.parseLong(studentNo.getText()));

            int i = ps.executeUpdate();

            if (i > 0) {
                JOptionPane.showMessageDialog(null, "Record successfully updated"); // Changed to pass null instead of btnUpdate
            } else {
                JOptionPane.showMessageDialog(null, "Error updating record");
            }

            ps.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

}

