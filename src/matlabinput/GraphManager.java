/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlabinput;

import java.util.ArrayList;

/**
 *
 * @author Justin Lee
 */
public class GraphManager {

    //TODO: enum for spike 1,2,3,4
    
    /*
    
    */
    private ArrayList<VoltageFile> files = new ArrayList();
    private ArrayList<XYFunction> functions = new ArrayList<>();
    private ArrayList<XYLineChart_AWT> charts = new ArrayList<>();

    public void addFile(VoltageFile file) {
        files.add(file);
    }

    public ArrayList<VoltageFile> getFiles() {
        return files;
    }

    public void addGraph(Graph type, XYFunction function) {
        function.setDotShape(type.getShape());
        function.setLineSize(type.getLineSize());
        functions.add(function);
    }
    public ArrayList<XYFunction> getFunctions() {
        return functions;
    }

    public ArrayList<XYLineChart_AWT> getCharts() {
        return charts;
    }

    public static GraphManager getInstance() {
        return GraphsHolder.INSTANCE;
    }

    private static class GraphsHolder {

        private static final GraphManager INSTANCE = new GraphManager();
    }
}
