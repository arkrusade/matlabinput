package matlabinput;

import java.awt.Color;
import java.awt.BasicStroke;
import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class XYLineChart_AWT extends ApplicationFrame {

    private String title, xLabel, yLabel;
    private ArrayList<XYFunction> functions = new ArrayList<>(0);//list of datasets
    private XYLineAndShapeRenderer renderer;
    static Color[] defaultColors = {
        Color.RED,
        Color.ORANGE,
        Color.YELLOW,
        Color.GREEN,
        Color.BLUE,
        Color.MAGENTA
    };

    public enum chartType {
        SPIKE("Spikes over time", "Chart number", "Time"), VOLTAGE("Voltage over time", "Voltage", "Time");

        String title, xString, yString;

        chartType(String title, String x, String y) {
            this.title = title;
            xString = x;
            yString = y;
        }
    }

    //takes voltageFile and shows the voltage, spikes (only one step per spike), and data as visual functions
    //TODO: chart should not be reliant on voltagefile
    public XYLineChart_AWT(voltageFile file, chartType type) {
        this(file.getName(), type.xString, type.yString, new ArrayList<>());
        //TODO: maybe? have to add voltage and spikes as XYFunctions

    }

    public XYLineChart_AWT(String title, String xLabel, String yLabel, double[][] xy) throws InstantiationException {
        super(title);

        XYFunction init = new XYFunction(yLabel + " vs." + xLabel, xy[0], xy[1]);
        functions.add(init);

        this.title = title;
        this.xLabel = xLabel;
        this.yLabel = yLabel;

    }

    public XYLineChart_AWT(String title, String xLabel, double[] x, String yLabel, double[] y) throws InstantiationException {
        super(title);
        if (x.length != y.length) {
            throw new InstantiationException("X set:" + xLabel + " must be the same size as Y set:" + yLabel + ".");
        }
        XYFunction init = new XYFunction(yLabel + " vs." + xLabel, x, y);
        functions.add(init);

        this.title = title;
        this.xLabel = xLabel;
        this.yLabel = yLabel;

    }

    public XYLineChart_AWT(String title, String xLabel, String yLabel, ArrayList<XYFunction> set) {
        super(title);
        functions = set;
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
        this.setExtendedState(MAXIMIZED_BOTH);
        setContentPane(chartPanel);
    }

    public void setRenderer(XYLineAndShapeRenderer newRenderer) {
        renderer = newRenderer;
    }

    public XYLineAndShapeRenderer getRenderer() {
        return renderer;
    }

    public void addXYSet(XYFunction set) {
        functions.add(set);
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
        functions.add(function);
    }

    public void addData(int index, XYFunction function) {
        functions.add(index, function);
    }

    public void addData(XYFunction... functions) {
        this.functions.addAll(Arrays.asList(functions));
    }

    public void addData(int index, XYFunction... functions) {
        this.functions.addAll(index, Arrays.asList(functions));
    }

    public void setFunctions(ArrayList<XYFunction> functions) {
        this.functions = functions;
    }

    private XYDataset getXYDataset() {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        functions.stream().forEach((dataSeries) -> {
            dataset.addSeries(dataSeries.toSeries());
        });
        return dataset;
    }

    private static XYLineAndShapeRenderer defaultRenderer(XYLineChart_AWT instance) {
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        for (int i = 0; i < instance.functions.size(); i++) {
            renderer.setSeriesShape(i, instance.functions.get(i).getDotShape());
            renderer.setSeriesPaint(i, instance.functions.get(i).getLineColor() != null ? instance.functions.get(i).getLineColor() : defaultColors[i % defaultColors.length]);
            renderer.setSeriesStroke(i, new BasicStroke(instance.functions.get(i).getLineSize()));
        }
        return renderer;
    }
}
