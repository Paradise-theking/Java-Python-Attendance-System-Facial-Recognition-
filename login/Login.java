package login;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane; 
import admin.AdminDashboard;
import dbConnect.ConnectionDB;
import lecturer.LecturerDashboard; 

 
import java.awt.Font;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.mysql.cj.xdevapi.Statement;

import javax.swing.JLabel;

public class Login extends javax.swing.JFrame {
    
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    // Method to set userId after successful login
    public static void setUserId(String id) {  
        userId = id;
    }

    // Getter method to retrieve userId
    public static String getUserId() {
        return userId;
    }
    
    public Login() {
        super("Attendance System Login");
        initComponents();
        conn = ConnectionDB.connection();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setUndecorated(true);
        jPanel1 = new Panel1();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        userID = new javax.swing.JTextField();
        userID.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        lgpassword = new javax.swing.JPasswordField();
        lgpassword.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        Login = new javax.swing.JButton();
        Login.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        Login.setForeground(new Color(255, 255, 255));
        Login.setBackground(new Color(0, 0, 128));
        Cancel = new javax.swing.JButton();
        Cancel.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        Cancel.setForeground(new Color(255, 255, 255));
        Cancel.setBackground(new Color(0, 0, 128));
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new Color(0, 0, 128));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Login", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setForeground(new Color(128, 0, 128));
        jPanel1.setAlignmentX(1.0F);
        jPanel1.setAlignmentY(1.0F);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("UserID:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Password:");

        Login.setIcon(new javax.swing.ImageIcon("C:\\Users\\PARADISE\\Desktop\\Employee\\project Image\\login1.png")); // NOI18N
        Login.setText("Login");
        Login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginActionPerformed(evt);
            }
        });

        Cancel.setIcon(new javax.swing.ImageIcon("C:\\Users\\PARADISE\\Desktop\\Employee\\project Image\\cancel1.png")); // NOI18N
        Cancel.setText("Cancel");
        Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelActionPerformed(evt);
            }
        });

        jLabel3.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Pictures\\emculog.jpeg")); // NOI18N
        
        lblNewLabel = new JLabel("Select User:");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));
        
        selectUser = new JComboBox<>();
        selectUser.addItem("admin");
        selectUser.addItem("lecturer");
        selectUser.setEditable(false);
        selectUser.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addContainerGap()
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(Login)
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addGap(116)
        							.addComponent(Cancel))))
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addGap(92)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(lblNewLabel)
        						.addComponent(jLabel1)
        						.addComponent(jLabel2))
        					.addGap(18)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(lgpassword, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
        						.addComponent(userID, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
        						.addComponent(selectUser, 0, 134, Short.MAX_VALUE)
        						.addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE))))
        			.addGap(102))
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addGap(41)
        			.addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblNewLabel)
        				.addComponent(selectUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel1)
        				.addComponent(userID, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel2)
        				.addComponent(lgpassword, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(Login)
        				.addComponent(Cancel))
        			.addContainerGap(125, Short.MAX_VALUE))
        );
        jPanel1.setLayout(jPanel1Layout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(441, 473));
        setLocationRelativeTo(null);
    }

    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {                                       
        System.exit(0);
    }                                      

    private void LoginActionPerformed(java.awt.event.ActionEvent evt) {                                      
        try {
            String userId = userID.getText();
            String password = new String(lgpassword.getPassword());
            String userType = (String) selectUser.getSelectedItem();

            String table = userType.equals("admin") ? "admin" : "lecturer";
            
            String sql = "";
            if (table.equals("admin")) {
                sql = "SELECT * FROM admin WHERE WorkNo = ? AND Password = ?";
            } else {
                sql = "SELECT * FROM lecturer WHERE lecturer_id = ? AND Password = ?";
            }

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                setUserId(userId);
                setVisible(false);
                if (userType.equals("admin")) {
                    AdminDashboard adminDashboard = new AdminDashboard();
                    adminDashboard.setVisible(true);
                } else if (userType.equals("lecturer")) {
                    LecturerDashboard lecturerDashboard = new LecturerDashboard();
                    lecturerDashboard.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Password or UserID is incorrect!");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage());
        }
    }


    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    private javax.swing.JButton Cancel; 
    private javax.swing.JButton Login;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private Panel1 jPanel1;
    private javax.swing.JPasswordField lgpassword;
    public static javax.swing.JTextField userID;
    public static String userId;
    private JLabel lblNewLabel;
    private JComboBox<String> selectUser;
}
