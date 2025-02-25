package chart;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.raven.shadow.ShadowRenderer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Card1 extends JPanel {
    private JLabel lbTitle;
    private JComboBox<String> course_code;
    public static String source_code; // Static field for source code

    private int mouseX, mouseY;
    private boolean isResizing = false;

    public Card1() {
        initComponents();
        setOpaque(false);
        setBackground(new Color(0, 79, 181));
        // setSize(300, 150);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                isResizing = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isResizing = false;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isResizing) {
                    int deltaX = e.getX() - mouseX;
                    int deltaY = e.getY() - mouseY;
                    int newWidth = getWidth() + deltaX;
                    int newHeight = getHeight() + deltaY;
                    setSize(newWidth, newHeight);
                    revalidate();
                }
            }
        });
    }

    public void initComponents() {
        lbTitle = new JLabel();
        lbTitle.setFont(new Font("sansserif", Font.BOLD, 18));
        lbTitle.setForeground(new Color(190, 190, 190));
        lbTitle.setText("Title");

        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        course_code = new JComboBox<>(comboBoxModel);
        course_code.setFont(new Font("Segoe UI Semilight", Font.BOLD, 14));

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20)
                                .addComponent(lbTitle)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(course_code, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(270, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20)
                                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lbTitle)
                                        .addComponent(course_code, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(70))
        );

        // Populate combo box with lecturer's courses
        populateCourseComboBox();
    }

    private void populateCourseComboBox() {
        try {
            // Database connection setup
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "");

            // Get lecturer's ID from Login.getUserId() assuming it returns the lecturer's ID
            String userId = login.Login.getUserId();
            if (userId == null || userId.isEmpty()) {
                // Handle case where userId is null or empty
                JOptionPane.showMessageDialog(null, "User ID is null or empty. Cannot proceed.");
                return;
            }
            int lecturerId = Integer.parseInt(userId);

            // Prepare SQL statement to get courses taught by the lecturer
            String query = "SELECT DISTINCT t.course_code " +
                    "FROM timetable t " +
                    "WHERE t.lecturer_id = ?";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, lecturerId);

            // Execute query and populate the combo box
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String courseCode = rs.getString("course_code");
                course_code.addItem(courseCode);
            }

            // Close resources
            rs.close();
            preparedStatement.close();
            con.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching courses: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs.create();
        int shadowSize = 10;
        int width = getWidth() - shadowSize * 2;
        int height = getHeight() - shadowSize * 2;
        createShadow(g2, create(0, 0, width, height), shadowSize);
        g2.dispose();
    }

    private void createShadow(Graphics2D g2, Shape shape, int shadowSize) {
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fill(shape);
        g2.drawImage(new ShadowRenderer(shadowSize, 0.35f, new Color(30, 30, 30)).createShadow(img), 0, 0, null);
    }

    private Shape create(int x, int y, int width, int height) {
        int border = 30;
        Area area = new Area(new RoundRectangle2D.Double(x, y, width, height, border, border));
        area.add(new Area(new Rectangle2D.Double(x, height - border + y, border, border)));
        area.add(new Area(new Rectangle2D.Double(x + width - border, y, border, border)));
        return area;
    }

    // Static method to set source_code
    public static void setSourceCode(String sourceCode) {
        source_code = sourceCode;
    }

    // Static method to get source_code
    public static String getSourceCode() {
        return source_code;
    }

	public static String getCourseCode() {
		return source_code;
	}
}
