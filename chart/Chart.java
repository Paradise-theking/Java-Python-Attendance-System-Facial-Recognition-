package chart;

import chart.blankchart.BlankPlotChart;
import chart.blankchart.BlankPlotChatRender;
import chart.blankchart.SeriesSize;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Chart extends javax.swing.JPanel {

    DecimalFormat df = new DecimalFormat("#,##0.##");
    private List<ModelLegend> legends = new ArrayList<>();
    private List<ModelChart> model = new ArrayList<>();
    private final int seriesSize = 18;
    private final int seriesSpace = 10;
    private final Animator animator;
    private float animate;
    private String showLabel;
    private Point labelLocation = new Point();

    public Chart() {
        initComponents();
        setOpaque(false);
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                animate = fraction;
                repaint();
            }
        };
        animator = new Animator(800, target);
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        blankPlotChart.setBlankPlotChatRender(new BlankPlotChatRender() {
            @Override
            public String getLabelText(int index) {
                return model.get(index).getLabel();
            }

            @Override
            public void renderSeries(BlankPlotChart chart, Graphics2D g2, SeriesSize size, int index) {
                double totalSeriesWidth = (seriesSize * legends.size()) + (seriesSpace * (legends.size() - 1));
                double x = (size.getWidth() - totalSeriesWidth) / 2;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
                for (int i = 0; i < legends.size(); i++) {
                    ModelLegend legend = legends.get(i);
                    double seriesValues = chart.getSeriesValuesOf(model.get(index).getValues()[i], size.getHeight()) * animate;
                    int s = seriesSize / 2;
                    int sy = seriesSize / 3;
                    int px[] = {(int) (size.getX() + x), (int) (size.getX() + x + s), (int) (size.getX() + x + s), (int) (size.getX() + x)};
                    int py[] = {(int) (size.getY() + size.getHeight() - seriesValues), (int) (size.getY() + size.getHeight() - seriesValues + sy), (int) (size.getY() + size.getHeight() + sy), (int) (size.getY() + size.getHeight())};
                    GradientPaint gra = new GradientPaint((int) (size.getX() + x) - s, 0, legend.getColorLight(), (int) (size.getX() + x + s), 0, legend.getColor());
                    g2.setPaint(gra);
                    g2.fillPolygon(px, py, px.length);
                    int px1[] = {(int) (size.getX() + x + s), (int) (size.getX() + x + seriesSize), (int) (size.getX() + x + seriesSize), (int) (size.getX() + x + s)};
                    int py1[] = {(int) (size.getY() + size.getHeight() - seriesValues + sy), (int) (size.getY() + size.getHeight() - seriesValues), (int) (size.getY() + size.getHeight()), (int) (size.getY() + size.getHeight() + sy)};
                    g2.setColor(legend.getColorLight());
                    g2.fillPolygon(px1, py1, px1.length);
                    int px2[] = {(int) (size.getX() + x), (int) (size.getX() + x + s), (int) (size.getX() + x + seriesSize), (int) (size.getX() + x + s)};
                    int py2[] = {(int) (size.getY() + size.getHeight() - seriesValues), (int) (size.getY() + size.getHeight() - seriesValues - sy), (int) (size.getY() + size.getHeight() - seriesValues), (int) (size.getY() + size.getHeight() - seriesValues + sy)};
                    g2.fillPolygon(px2, py2, px2.length);
                    x += seriesSpace + seriesSize;
                }
                if (showLabel != null) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                    Dimension s = getLabelWidth(showLabel, g2);
                    int space = 3;
                    int spaceTop = 5;
                    g2.setColor(new Color(30, 30, 30));
                    g2.fillRoundRect(labelLocation.x - s.width / 2 - 3, labelLocation.y - s.height - space * 2 - spaceTop, s.width + space * 2, s.height + space * 2, 10, 10);
                    g2.setColor(new Color(200, 200, 200));
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                    g2.drawString(showLabel, labelLocation.x - s.width / 2, labelLocation.y - spaceTop - space * 2);
                }
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }

            @Override
            public boolean mouseMoving(BlankPlotChart chart, MouseEvent evt, Graphics2D g2, SeriesSize size, int index) {
                double totalSeriesWidth = (seriesSize * legends.size()) + (seriesSpace * (legends.size() - 1));
                double x = (size.getWidth() - totalSeriesWidth) / 2;
                for (int i = 0; i < legends.size(); i++) {
                    double seriesValues = chart.getSeriesValuesOf(model.get(index).getValues()[i], size.getHeight()) * animate;
                    int s = seriesSize / 2;
                    int sy = seriesSize / 3;
                    int px[] = {(int) (size.getX() + x), (int) (size.getX() + x + s), (int) (size.getX() + x + seriesSize), (int) (size.getX() + x + seriesSize), (int) (size.getX() + x + s), (int) (size.getX() + x)};
                    int py[] = {(int) (size.getY() + size.getHeight() - seriesValues), (int) (size.getY() + size.getHeight() - seriesValues - sy), (int) (size.getY() + size.getHeight() - seriesValues), (int) (size.getY() + size.getHeight()), (int) (size.getY() + size.getHeight() + sy), (int) (size.getY() + size.getHeight())};
                    if (new Polygon(px, py, px.length).contains(evt.getPoint())) {
                        double data = model.get(index).getValues()[i];
                        showLabel = df.format(data);
                        labelLocation.setLocation((int) (size.getX() + x + s), (int) (size.getY() + size.getHeight() - seriesValues - sy));
                        chart.repaint();
                        return true;
                    }
                    x += seriesSpace + seriesSize;
                }
                return false;
            }
        });
    }

    public void addLegend(String name, Color color, Color color1) {
        ModelLegend data = new ModelLegend(name, color, color1);
        legends.add(data);
        panelLegend.add(new LegendItem(data));
        panelLegend.repaint();
        panelLegend.revalidate();
    }

    public void addData(ModelChart data) {
        model.add(data);
        blankPlotChart.setLabelCount(model.size());
        double max = data.getMaxValues();
        if (max > blankPlotChart.getMaxValues()) {
            blankPlotChart.setMaxValues(max);
        }
    }

    public void clear() {
        animate = 0;
        showLabel = null;
        blankPlotChart.setLabelCount(0);
        model.clear();
        repaint();
    }

    public void start() {
        showLabel = null;
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    private Dimension getLabelWidth(String text, Graphics2D g2) {
        FontMetrics ft = g2.getFontMetrics();
        Rectangle2D r2 = ft.getStringBounds(text, g2);
        return new Dimension((int) r2.getWidth(), (int) r2.getHeight());
    }
    
    
    public void setChartData(double[] data) {
        // Clear existing data
        model.clear();

        // Add new data based on attendanceData
        for (double value : data) {
            // Assuming you have a ModelChart constructor that takes a value and label
            ModelChart chartData = new ModelChart(value, "Label"); // Replace "Label" with appropriate label
            model.add(chartData);
        }

        // Update chart UI
        blankPlotChart.setLabelCount(model.size()); // Update label count
        // You may want to update other UI elements or trigger a repaint here

        // Start animation or update as necessary
        start(); // Start animation if it's not running already
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        blankPlotChart = new chart.blankchart.BlankPlotChart();
        panelLegend = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        panelLegend.setOpaque(false);
        panelLegend.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(blankPlotChart, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
        					.addContainerGap())
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(panelLegend, GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
        					.addGap(232))))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(47)
        			.addComponent(blankPlotChart, GroupLayout.PREFERRED_SIZE, 187, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
        			.addComponent(panelLegend, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
        			.addGap(20))
        );
        this.setLayout(layout);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private chart.blankchart.BlankPlotChart blankPlotChart;
    private javax.swing.JPanel panelLegend;
    // End of variables declaration//GEN-END:variables
}
