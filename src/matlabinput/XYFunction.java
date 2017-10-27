/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlabinput;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author Justin Lee
 */
public class XYFunction {

    private String name;
    private double[][] data;
    private Shape dotShape = new Ellipse2D.Double(-1, -1, 2, 2);
    private Color lineColor;
    private float lineSize = .5f;

    //x goes in data[0], y goes in data[1]
    public XYFunction(String name, double[][] data) {
        this.name = name;
        this.data = data;
    }

    public XYFunction(String name, double[][] data, Shape dotShape, float lineSize) {
        this.name = name;
        this.data = data;
        this.dotShape = dotShape;
        this.lineSize = lineSize;
    }

    

    public XYFunction(String name, double[] x, double[] y) throws InstantiationException {
        if (x.length != y.length) {
            throw new InstantiationException("X set must be the same size as Y set:" + name + ".");
        }
        this.name = name;
        data = new double[2][x.length];
        data[0] = x;
        data[1] = y;
    }
    public XYFunction(String name, double[] x, double[] y, Shape dotShape, float lineSize) throws InstantiationException {
        this(name, x, y);
        this.dotShape = dotShape;
        this.lineSize = lineSize;
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

    public void setXSet(double[] x) throws IllegalArgumentException {
        if (x.length != data[0].length) {
            throw new IllegalArgumentException("new X set must be the same size as old X set");
        }
        this.data[0] = x;
    }

    public double[] getYSet() {
        return data[1];
    }

    public void setYSet(double[] y) throws IllegalArgumentException {
        if (y.length != data[1].length) {
            throw new IllegalArgumentException("new Y set must be the same size as old Y set");
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

    public Shape getDotShape() {
        return dotShape;
    }

    public void setDotShape(Shape dotShape) {
        this.dotShape = dotShape;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public float getLineSize() {
        return lineSize;
    }

    public void setLineSize(float lineSize) {
        this.lineSize = lineSize;
    }
    
}
