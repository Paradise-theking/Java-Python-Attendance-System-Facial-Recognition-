package chart;
import com.raven.shadow.ShadowRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class Card extends JPanel {

    private JLabel lbIcon;
    private JLabel lbTitle;
    private JLabel lbDescription;
    private JLabel lbValues;
    private int mouseX, mouseY;
    private boolean isResizing = false;

    public Card() {
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
                //isResizing = false;
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

    public void setData(ModelCard data) {
        lbTitle.setText(data.getTitle());
        lbValues.setText(data.getValues());
        lbDescription.setText(data.getDescription());
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

    public void setIcon(Icon icon) {
        lbIcon.setIcon(icon);
    }

    public Icon getIcon() {
        return lbIcon.getIcon();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbIcon = new JLabel();
        lbTitle = new JLabel();
        lbDescription = new JLabel();
        lbValues = new JLabel();

        lbIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbIcon.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/profit.png"))); // NOI18N

        lbTitle.setFont(new Font("sansserif", Font.BOLD, 18));
        lbTitle.setForeground(new Color(190, 190, 190));
        lbTitle.setText("Title");

        lbDescription.setForeground(new Color(190, 190, 190));
        lbDescription.setText("Description");
        lbDescription.setBorder(BorderFactory.createEmptyBorder(1, 1, 10, 1));

        lbValues.setFont(new Font("sansserif", Font.BOLD, 15));
        lbValues.setForeground(new Color(190, 190, 190));
        lbValues.setText("0");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lbTitle)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbIcon, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(lbDescription)
                            .addComponent(lbValues))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbValues)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbDescription, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbTitle)
                        .addGap(0, 0, 0)
                        .addComponent(lbIcon, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
    }// </editor-fold>//GEN-END:initComponents
}
