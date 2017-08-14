package matlabinput;

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
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
    private ArrayList<XYFunction> data = new ArrayList<>(0);//list of datasets
    private XYLineAndShapeRenderer renderer;
    static Color[] defaultColors = {
        Color.RED,
        Color.ORANGE,
        Color.YELLOW,
        Color.GREEN,
        Color.BLUE,
        Color.MAGENTA
    };

    //takes VoltageFile and shows the voltage, spikes (only one step per spike), and data as visual functions
    public XYLineChart_AWT(VoltageFile file, int step) {
        this(file.getName(), VoltageFile.xString, VoltageFile.yString, file.getData());
        //have to add voltage and spikes as XYFunctions

    }

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

    }

    public XYLineChart_AWT(String title, String xLabel, String yLabel, ArrayList<XYFunction> set) {
        super(title);
        data = set;
        this.xLabel = xLabel;
        this.yLabel = yLabel;

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

        if (renderer != null) {

            plot.setRenderer(renderer);
        } else {
            plot.setRenderer(defaultRenderer(this));
        }
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

    public String getxLabel() {
        return xLabel;
    }

    public void setxLabel(String xLabel) {
        this.xLabel = xLabel;
    }

    public String getyLabel() {
        return yLabel;
    }

    public void setyLabel(String yLabel) {
        this.yLabel = yLabel;
    }

    public void addData(XYFunction function) {
        data.add(function);
    }

    public void addData(int index, XYFunction function) {
        data.add(index, function);
    }

    public void addData(XYFunction... functions) {
        this.data.addAll(Arrays.asList(functions));
    }
    public void addData(int index, XYFunction... functions) {
        this.data.addAll(index, Arrays.asList(functions));
    }

    public void setData(ArrayList<XYFunction> data) {
        this.data = data;
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
            renderer.setSeriesShape(i, instance.data.get(i).getDotShape());
            renderer.setSeriesPaint(i, instance.data.get(i).getLineColor() != null ? instance.data.get(i).getLineColor() : defaultColors[i % defaultColors.length]);
            renderer.setSeriesStroke(i, new BasicStroke(instance.data.get(i).getLineSize()));
        }
        return renderer;
    }
}
