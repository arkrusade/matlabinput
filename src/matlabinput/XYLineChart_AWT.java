package matlabinput;

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.sql.rowset.spi.SyncProvider;

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
    private ArrayList<XYFunction> data;//list of datasets
    private XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    static Shape smallDot = new Ellipse2D.Double(0, 0, 0, 0);
    static Shape dot = new Ellipse2D.Double(-1, -1, 2, 2);
    static Color[] defaultColors = {
        Color.RED,
        Color.BLUE,
        Color.GREEN,
        Color.YELLOW,
        Color.MAGENTA
    };

    public XYLineChart_AWT(String title, String xLabel, double[] x, String yLabel, double[] y) throws InstantiationException {
        super(title);
        if (x.length != y.length) {
            throw new InstantiationException("X set:" + xLabel + " must be the same size as Y set:" + yLabel + ".");
        }
        XYFunction init = new XYFunction(yLabel + " vs." + xLabel, x, y);
        data.add(init);

        this.title = title;
        this.xLabel = xLabel;
        this.yLabel = yLabel;

        renderer = defaultRenderer(this);
    }

    public XYLineChart_AWT(String title, String xLabel, String yLabel, ArrayList<XYFunction> set) {
        super(title);
        data = set;
        this.xLabel = xLabel;
        this.yLabel = yLabel;

        renderer = defaultRenderer(this);
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

    public void addXYSet(XYFunction set) {
        data.add(set);
    }

    private XYDataset getXYDataset() {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        data.stream().forEach((dataSeries) -> {
            dataset.addSeries(dataSeries.toSeries());
        });
        return dataset;
    }

    private static XYLineAndShapeRenderer defaultRenderer(XYLineChart_AWT instance) {
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        for (int i = 0; i < instance.data.size(); i++) {
            renderer.setSeriesShape(i, dot);
            renderer.setSeriesPaint(i, defaultColors[i%defaultColors.length]);
            renderer.setSeriesStroke(i, new BasicStroke(.5f));
        }
        return renderer;
    }
}
