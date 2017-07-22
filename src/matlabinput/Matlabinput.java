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
            MatFileReader mfr = new MatFileReader("L1C1SPOT-300um1sec100percent-02-15-22PM.mat");
            Map<String, MLArray> mlArrayRetrived = mfr.getContent();
            MLStructure x = (MLStructure) mfr.getContent().get("x");
            MLDouble voltMAT = (MLDouble) x.getField("voltage");
            double[][] temp = voltMAT.getArray();
            double[] voltages = temp[0];
            
            double[] xs = new double[voltages.length];
            for (int i = 0; i < voltages.length; i++) {
                xs[i] = i;                
            }
            showGraph("Time", xs, "Voltage", voltages);
            
//            for (int j = 0; j < samples[0].length; j++) {
//                System.out.print(samples[0][j] + "\t");
//            }
//            System.out.println("");
//            double max, min;
//            max = xValues[0][0];
//            min = max;
//            for (int j = 0; j < xValues[0].length; j++) {
//                double d = xValues[0][j];
//                max = Math.max(max, d);
//                min = Math.min(min, d);
//                if (Math.abs(d) > .2) {
//                    System.out.print(j + ":" + d + "\t");
//                }
//            }
//            System.out.println("");
//            System.out.println(max + ":" + min);

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("IOException");
        }

    }

    public static void showGraph(String XString, double[] x, String YString, double[] y) {
        try {
            XYLineChart_AWT chart = new XYLineChart_AWT(XString, x, YString, y);
            chart.setSize(new java.awt.Dimension(1920, 1080));
            RefineryUtilities.centerFrameOnScreen(chart);
            chart.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
