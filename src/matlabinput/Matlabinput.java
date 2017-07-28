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
        processMatFile("L1C1SPOT-300um1sec100percent-02-15-22PM.mat");
        processMatFile("L1C1SPOT-300um1sec100percent-02-15-28PM.mat");
        processMatFile("L1C1SPOT-300um1sec100percent-02-15-34PM.mat");
    }

    public static void processMatFile(String fileName) {
        try {
            MatFileReader mfr = new MatFileReader(fileName);
            Map<String, MLArray> mlArrayRetrived = mfr.getContent();
            MLStructure x = (MLStructure) mfr.getContent().get("x");
            MLDouble voltMAT = (MLDouble) x.getField("voltage");
            double[][] temp = voltMAT.getArray();
            double[] voltages = temp[0];

            double[] xs = new double[voltages.length];
            double[] x10 = new double[voltages.length / 10];
            double[] deltas = new double[voltages.length];
            double[] deltas10 = new double[voltages.length / 10];
            ArrayList<int[]> spikes = new ArrayList();
            double[] sigdiffs = new double[voltages.length];
            double[] ends = new double[voltages.length];
            double[] isSpiked = new double[voltages.length];
            boolean spiked = false;
            boolean upwards = false;
            int step = 0;
            double d, d10;
            d10 = 0;
            //0 is not spike, 1 is spike started, 2 is first peak, 3 is from peak to peak, 4 is second peak, 5 is from peak to reverse
            for (int i = 1; i < voltages.length; i++) {
                xs[i] = i;

                d = voltages[i] - voltages[i - 1];
                deltas[i] = d;
                if (i % 10 == 0) {
                    d10 = voltages[i] - voltages[i - 10];
                    x10[i / 10] = i;
                    deltas10[i / 10] = d10;
                    if (step == 4 && (d10 > 0 == upwards)) {
                        step = 5;
                    } else if (step == 5 && (d10 < 0 == upwards)) {
                        step = 0;
                        spiked = false;
                        spikes.get(spikes.size()-1)[1] = i;
                    }
                }

                if (Math.abs(d) >= .005) {
                    sigdiffs[i] = .15 * d / Math.abs(d);
                    if (step == 0) {//to go to step 1, must have large diff and not in spike
                        spiked = true;
                        int[] newSpike = new int[2];
                        newSpike[0] = i;
                        spikes.add(newSpike);
                        upwards = d > 0;//if spike initially positive, upwards is true, else false
                        step = 1;
                    } else if (step == 2 && (d10 < 0 == upwards)) {//to go to step 3, must be in opposite direction with large diff
                        step = 3;
                    }
                } else if (spiked) {//to go to step 2 or 4, must be small diff
                    if (step == 3 || step == 1) {
                        step++;
                    }
                }
                ends[i] = (double) step / 50.0;
                isSpiked[i] = (spiked ? 0.1 : 0);
            }
            try {
                XYSet voltage = new XYSet("Voltage", xs, voltages);
                XYSet deltaSet = new XYSet("Deltas", xs, deltas);
                XYSet delta10Set = new XYSet("Delta10", x10, deltas10);
                XYSet spikeSet = new XYSet("Spikes", xs, sigdiffs);
                XYSet isSpikedSet = new XYSet("isSpiked", xs, isSpiked);
                XYSet endSet = new XYSet("Ends", xs, ends);

                ArrayList<XYSet> dataSet = new ArrayList();
                dataSet.add(voltage);
//                dataSet.add(deltaSet);
//                dataSet.add(delta10Set);
//                dataSet.add(endSet);
                dataSet.add(isSpikedSet);
//                dataSet.add(spikeSet);
                XYLineChart_AWT chart = new XYLineChart_AWT(fileName, "Time", "yLabel", dataSet);
                chart.prepareChart();
                chart.setSize(new java.awt.Dimension(1920, 1080));
                RefineryUtilities.centerFrameOnScreen(chart);
                chart.setVisible(true);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

//            showGraph("Time", xs, "Voltage", voltages);
//            showGraph("Time", xs, "Deltas", deltas);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("IOException");
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
