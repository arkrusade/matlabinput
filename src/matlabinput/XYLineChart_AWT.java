package matlabinput;

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

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

    private String title, xLabel, yLabel;
    private ArrayList<XYSet> data;//list of datasets
    private XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

    public XYLineChart_AWT(String title, String xLabel, double[] x, String yLabel, double[] y) throws InstantiationException {
        super(title);
        if (x.length != y.length) {
            throw new InstantiationException("X set:"+xLabel+" must be the same size as Y set:"+yLabel+".");
        }
        XYSet init = new XYSet(yLabel + " vs." + xLabel, x, y);
        data.add(init);

        this.title = title;
        this.xLabel = xLabel;
        this.yLabel = yLabel;

        renderer = defaultRenderer();
    }

    public XYLineChart_AWT(String title, String xLabel, String yLabel, ArrayList<XYSet> set) {
        super(title);
        data = set;
        this.xLabel = xLabel;
        this.yLabel = yLabel;

        renderer = defaultRenderer();
    }

    public void prepareChart() {
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                title,
                xLabel,
                yLabel,
                getXYDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(xylineChart);
        final XYPlot plot = xylineChart.getXYPlot();

        plot.setRenderer(renderer);
        setContentPane(chartPanel);
    }

    public void setRenderer(XYLineAndShapeRenderer newRenderer) {
        renderer = newRenderer;
    }

    public XYLineAndShapeRenderer getRenderer() {
        return renderer;
    }

    public void addXYSet(XYSet set) {
        data.add(set);
    }

    private XYDataset getXYDataset() {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        data.stream().forEach((dataSeries) -> {
            dataset.addSeries(dataSeries.toSeries());
        });
        return dataset;
    }

    private static XYLineAndShapeRenderer defaultRenderer() {
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        double size = 0.0;
        double delta = size / 2;
        double size1 = 2.0;
        double delta1 = size1 / 2;
        Shape smallDot = new Ellipse2D.Double(-delta, -delta, size, size);
        Shape dot = new Ellipse2D.Double(-delta1, -delta1, size1, size1);
        renderer.setSeriesShape(0, smallDot);
        renderer.setSeriesShape(1, dot);
        renderer.setSeriesShape(2, dot);
        renderer.setSeriesShape(3, dot);
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.GREEN);
        renderer.setSeriesPaint(2, Color.YELLOW);
        renderer.setSeriesPaint(3, Color.MAGENTA);
        renderer.setSeriesStroke(0, new BasicStroke(.5f));
        renderer.setSeriesStroke(1, new BasicStroke(.0f));
        renderer.setSeriesStroke(2, new BasicStroke(0f));
        renderer.setSeriesStroke(3, new BasicStroke(.5f));
        return renderer;
    }
}
