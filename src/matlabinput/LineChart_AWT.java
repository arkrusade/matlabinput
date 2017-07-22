package matlabinput;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class LineChart_AWT extends ApplicationFrame {
    private String XString, YString, title;
    private double[] x,     y;
    private int samples;
    private double[][] coords;
    public String getXString() {
        return XString;
    }

    public String getYString() {
        return YString;
    }

    public double[] getXSet() {
        return x;
    }

    public double[] getYSet() {
        return y;
    }
    public double[][] getCoords() {
        return coords;
    }
    
    public LineChart_AWT(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Years", "Number of Schools",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);
    }
    
    public LineChart_AWT(String XString, double[] x, String YString, double[] y) throws InstantiationException {
        super(YString+" vs."+XString);
        if (x.length != y.length) {
            throw new InstantiationException("X set must be the same size as Y set");
        }
        samples = x.length;
        this.XString = XString;
        this.YString = YString;
        title = XString+" vs."+YString;
        this.x = x;
        this.y = y;
    }
    
    public void setUpChart() {
        JFreeChart lineChart = ChartFactory.createLineChart(
                title,
                XString, YString,
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < samples; i++) {
            dataset.addValue(y[i], YString, x[i]+"");            
        }
        return dataset;
    }

    public static void main(String[] args) {
        LineChart_AWT chart = new LineChart_AWT(
                "School Vs Years",
                "Numer of Schools vs years");

        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
}
