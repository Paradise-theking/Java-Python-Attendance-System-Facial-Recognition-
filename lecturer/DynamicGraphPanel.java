package lecturer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DynamicGraphPanel extends JPanel {

    private JComboBox<String> courseComboBox;
    private ChartPanel chartPanel;
    private Map<String, DefaultCategoryDataset> courseData;
    private int mouseX, mouseY;
    private boolean isResizing = false;

    public DynamicGraphPanel() {
        setLayout(new BorderLayout());
        
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

        // Initialize course data (mock data)
        courseData = new HashMap<>();
        courseData.put("Course A", createDataset("Course A"));
        courseData.put("Course B", createDataset("Course B"));
        courseData.put("Course C", createDataset("Course C"));

        // Create JComboBox for course selection
        courseComboBox = new JComboBox<>(courseData.keySet().toArray(new String[0]));
        courseComboBox.addActionListener(e -> {
            String selectedCourse = (String) courseComboBox.getSelectedItem();
            updateChart(selectedCourse);
        });
        add(courseComboBox, BorderLayout.NORTH);

        // Create initial chart panel
        String initialCourse = (String) courseComboBox.getSelectedItem();
        JFreeChart chart = createChart(courseData.get(initialCourse));
        chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(new Color(0, 0, 0));
        add(chartPanel, BorderLayout.CENTER);
    }

    private DefaultCategoryDataset createDataset(String course) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Random random = new Random();

        // Generate random attendance values for weeks
        for (int week = 1; week <= 4; week++) {
            int clockedIn = random.nextInt(15) + 85; // Random value between 85 and 100
            int onTime = random.nextInt(15) + 70;   // Random value between 70 and 85
            int late = random.nextInt(10) + 50;     // Random value between 50 and 60
            int tooLate = random.nextInt(10);       // Random value between 0 and 10

            dataset.addValue(clockedIn, "Clocked In", "Week " + week);
            dataset.addValue(onTime, "On Time", "Week " + week);
            dataset.addValue(late, "Late", "Week " + week);
            dataset.addValue(tooLate, "Too Late", "Week " + week);
        }

        return dataset;
    }

    private JFreeChart createChart(DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Attendance Chart", // chart title
                "Week",             // domain axis label
                "Attendance",       // range axis label
                dataset             // data
        );

        // Customize chart appearance
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // Define colors for each attendance category
        renderer.setSeriesPaint(0, Color.GREEN);    // Clocked In
        renderer.setSeriesPaint(1, Color.BLUE);     // On Time
        renderer.setSeriesPaint(2, Color.ORANGE);   // Late
        renderer.setSeriesPaint(3, Color.RED);      // Too Late

        // Create a legend for the chart
        LegendItemCollection legendItems = new LegendItemCollection();
        legendItems.add(new LegendItem("Clocked In", null, null, null,
                Plot.DEFAULT_LEGEND_ITEM_BOX, Color.GREEN));
        legendItems.add(new LegendItem("On Time", null, null, null,
                Plot.DEFAULT_LEGEND_ITEM_BOX, Color.BLUE));
        legendItems.add(new LegendItem("Late", null, null, null,
                Plot.DEFAULT_LEGEND_ITEM_BOX, Color.ORANGE));
        legendItems.add(new LegendItem("Too Late", null, null, null,
                Plot.DEFAULT_LEGEND_ITEM_BOX, Color.RED));

        plot.setFixedLegendItems(legendItems);

        return chart;
    }

    private void updateChart(String course) {
        DefaultCategoryDataset dataset = courseData.get(course);
        JFreeChart chart = createChart(dataset);
        chartPanel.setChart(chart);
        chartPanel.repaint();
    }
}
