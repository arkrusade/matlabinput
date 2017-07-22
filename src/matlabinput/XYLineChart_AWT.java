package matlabinput;

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class XYLineChart_AWT extends ApplicationFrame {

    private String XString, YString, title;
    private double[] x, y;
    private int samples;
    private double[][] coords;

    public XYLineChart_AWT(String XString, double[] x, String YString, double[] y) throws InstantiationException {
        super(YString + " vs." + XString);
        if (x.length != y.length) {
            throw new InstantiationException("X set must be the same size as Y set");
        }
        samples = x.length;
        this.XString = XString;
        this.YString = YString;
        title = XString + " vs." + YString;
        this.x = x;
        this.y = y;
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                title,
                XString,
                YString,
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(xylineChart);
        final XYPlot plot = xylineChart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
      renderer.setSeriesPaint( 0 , Color.RED );
      double size = 0.0;
      double delta = size/2;
      Shape smallDot = new Ellipse2D.Double(-delta, -delta, size, size);
      renderer.setSeriesShape(0, smallDot);
//      renderer.setSeriesPaint( 1 , Color.GREEN );
//      renderer.setSeriesPaint( 2 , Color.YELLOW );
        renderer.setSeriesStroke(0, new BasicStroke(.5f));
//      renderer.setSeriesStroke( 1 , new BasicStroke( 2.0f ) );
//      renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
        plot.setRenderer(renderer);
        setContentPane(chartPanel);
    }

    private XYDataset createDataset() {
        final XYSeries data = new XYSeries(YString);
        for (int i = 0; i < samples; i++) {
            data.add(x[i], y[i]);
        }

//        final XYSeries chrome = new XYSeries("Chrome");
//        chrome.add(1.0, 4.0);
//        chrome.add(2.0, 5.0);
//        chrome.add(3.0, 6.0);
//
//        final XYSeries iexplorer = new XYSeries("InternetExplorer");
//        iexplorer.add(3.0, 4.0);
//        iexplorer.add(4.0, 5.0);
//        iexplorer.add(5.0, 4.0);
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(data);
//      dataset.addSeries( chrome );          
//      dataset.addSeries( iexplorer );
        return dataset;
    }

    public static void main(String[] args) {
        double[] x = {1.0, 2.1, 3.2};
        double[] y = {3, 1, 4};

        try {
            XYLineChart_AWT chart = new XYLineChart_AWT("XString", x, "YString", y);
            chart.setSize(new java.awt.Dimension(560, 367));
            RefineryUtilities.centerFrameOnScreen(chart);
            chart.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
