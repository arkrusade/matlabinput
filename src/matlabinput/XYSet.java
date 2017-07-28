/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlabinput;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Justin Lee
 */
public class XYSet {

    private String name;
    private double[][] data;
    Shape dotShape;
    Color lineColor;
    float lineSize;

    public XYSet(String name, double[] x, double[] y) throws InstantiationException {
        if (x.length != y.length) {
            throw new InstantiationException("X set must be the same size as Y set:" + name + ".");
        }
        this.name = name;
        data = new double[2][x.length];
        data[0] = x;
        data[1] = y;
    }

    public XYSeries toSeries() {
        final XYSeries series = new XYSeries(name);
        for (int i = 0; i < data[0].length; i++) {
            series.add(data[0][i], data[1][i]);
        }
        return series;
    }

    public double[] getXSet() {
        return data[0];
    }

    public void setXSet(double[] x) throws InstantiationException {
        if (x.length != data[0].length) {
            throw new InstantiationException("new X set must be the same size as old X set");
        }
        this.data[0] = x;
    }

    public double[] getYSet() {
        return data[1];
    }

    public void setYSet(double[] y) throws InstantiationException {
        if (y.length != data[1].length) {
            throw new InstantiationException("new Y set must be the same size as old Y set");
        }
        this.data[0] = y;
    }

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
