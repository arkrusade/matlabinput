/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlabinput;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author justinjlee99
 */
public class GraphManager {

    ArrayList<File> fileList = new ArrayList(0);
    ArrayList<VoltageFile> voltageFileList = new ArrayList(0);
    ArrayList<XYFunction> functionList = new ArrayList(0);
//    ArrayList<XYLineChart_AWT> chartList = new ArrayList(0);
    XYLineChart_AWT chart;

    //TODO: have more tools to manage visible area of chart
    //ie max y, min y, max x, min x
    //center on line/point
    private GraphManager() {
    }

    public void addVoltageFile(VoltageFile file, boolean includeVoltages) {
        if (includeVoltages) {
            functionList.add(file.getVoltageFunction());
        }
        addVoltageFile(file);
    }

    public void addVoltageFile(File file, boolean includeVoltages) throws IOException {
        VoltageFile vFile = VoltageFile.fromFile(file);
        addVoltageFile(vFile, includeVoltages);
    }

    //TODO: change to only adding file to voltageFileList
    public void addVoltageFile(VoltageFile file) {
        XYFunction spikes = file.getSpikeFunction(1, (functionList.size() + 1) / 2);//adds to end of list (top of graph)
        spikes.setDotShape(new Rectangle2D.Double(-2, -20, 4, 40));//sets spike function to rasta mark shapes
        spikes.setLineSize(0);
        functionList.add(spikes);

        if (chart == null) {
            chart = new XYLineChart_AWT(file, XYLineChart_AWT.chartType.SPIKE);
        }
    }

    public void addVoltageFile(File file) throws IOException {
        addVoltageFile(VoltageFile.fromFile(file));
    }

    public void addVoltageFile(String path) throws IOException {
        try {
            File newFile = new File(path);
            VoltageFile first = VoltageFile.fromFile(newFile);
            voltageFileList.add(first);
            addVoltageFile(first);
        } catch (IOException ex) {
            throw new IOException("failed voltage file input");
        }
    }

    public void displayChart() {
        XYFunction[] temp = new XYFunction[0];
        chart.addData(functionList.toArray(temp));
        chart.prepareChart();

        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }

    public ArrayList<File> getFileList() {
        return fileList;
    }

    public ArrayList<VoltageFile> getVoltageFileList() {
        return voltageFileList;
    }

    public ArrayList<XYFunction> getFunctionList() {
        return functionList;
    }

    public static GraphManager getInstance() {
        return GraphMangerHolder.INSTANCE;
    }

    private static class GraphMangerHolder {

        private static final GraphManager INSTANCE = new GraphManager();
    }
}
