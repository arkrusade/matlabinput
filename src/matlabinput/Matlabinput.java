/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlabinput;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLStructure;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author justinjlee99
 */
public class Matlabinput {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            voltageFile first = voltageFile.fromString("L1C1SPOT-300um1sec100percent-02-15-22PM.mat");
            voltageFile second = voltageFile.fromString("L1C1SPOT-300um1sec100percent-02-15-28PM.mat");
            voltageFile third = voltageFile.fromString("L1C1SPOT-300um1sec100percent-02-15-34PM.mat");

            XYLineChart_AWT chart = new XYLineChart_AWT(first, 0);
            chart.setSize(new java.awt.Dimension(1920, 1080));
            RefineryUtilities.centerFrameOnScreen(chart);
            chart.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showGraph(String XString, double[] x, String YString, double[] y) {
        try {
            XYLineChart_AWT chart = new XYLineChart_AWT("Fancy Title", XString, x, YString, y);
            chart.setSize(new java.awt.Dimension(1920, 1080));
            RefineryUtilities.centerFrameOnScreen(chart);
            chart.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
