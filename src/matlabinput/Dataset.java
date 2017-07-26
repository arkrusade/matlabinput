/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlabinput;

/**
 *
 * @author Justin Lee
 */
public class Dataset {

    private String title, xTitle, yTitle;
    private double[][] data;

    public Dataset(String xTitle, double[] x, String yTitle, double[] y) throws InstantiationException {
        if (x.length != y.length) {
            throw new InstantiationException("X set must be the same size as Y set");
        }
        this.xTitle = xTitle;
        this.yTitle = yTitle;
        title = xTitle + " vs." + yTitle;
        data = new double[2][x.length];
        data[0] = x;
        data[1] = y;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXTitle() {
        return xTitle;
    }

    public void setXTitle(String xTitle) {
        this.xTitle = xTitle;
    }

    public String getYTitle() {
        return yTitle;
    }

    public void setYTitle(String yTitle) {
        this.yTitle = yTitle;
    }

}
