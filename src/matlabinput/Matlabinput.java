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

    public static voltageFile findFile(String fileName) throws FileNotFoundException {
        try {
            MatFileReader mfr = new MatFileReader(fileName);
            //TODO: test actual errors
            MLStructure x = (MLStructure) mfr.getContent().get("x");
            MLDouble voltMAT = (MLDouble) x.getField("voltage");
            MLDouble sampleRate = (MLDouble) x.getField("Samp_Rate");
            double samplesPerSecond = sampleRate.getArray()[0][0];
            double[] voltages = voltMAT.getArray()[0];
            return new voltageFile(fileName, samplesPerSecond, voltages);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new FileNotFoundException();
    }

    public static void findSpikes(voltageFile file) {
        double[] voltages = file.getVoltages();

        double[] xs = new double[voltages.length];//stores time values (x-dim)
//            double[] x10 = new double[voltages.length / 10];//stores every tenth time value
//            double[] deltas = new double[voltages.length];//stores every delta-y
//            double[] deltas10 = new double[voltages.length / 10];//stores every tenth delta-y
        ArrayList<int[]> spikes = new ArrayList();//stores every spike, with array of important times for spike
        //TODO: ensure that each int[] is same length
        //    0       1           2               3
        //    start   first peak  second peak     end
//            double[] sigdiffs = new double[voltages.length];
//            double[] steps = new double[voltages.length];
//            double[] isSpiked = new double[voltages.length];
        boolean spiked = false;
        boolean upwards = false;
        int step = 0;
        double d, d10;
        d10 = 0;
        //0 is not spike, 1 is spike started, 2 is first peak, 3 is from peak to peak, 4 is second peak, 5 is from peak to reverse
        for (int i = 1; i < voltages.length; i++) {
            xs[i] = i;

            d = voltages[i] - voltages[i - 1];
//                deltas[i] = d;
            if (i % 10 == 0) {//in tenth time value
                d10 = voltages[i] - voltages[i - 10];
//                    x10[i / 10] = i;
//                    deltas10[i / 10] = d10;
                if (step == 4 && (d10 > 0 == upwards)) {//end of second peak
                    step = 5;
                } else if (step == 5 && (d10 < 0 == upwards)) {//end of spike
                    step = 0;
                    spiked = false;
                    spikes.get(spikes.size() - 1)[3] = i;
                }
            }

            if (Math.abs(d) >= .005) {//if diff is big (either direction)
//                    sigdiffs[i] = .15 * d / Math.abs(d);
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
                if (step == 1 || step == 3) {
                    step++;
                    spikes.get(spikes.size() - 1)[step / 2] = i;//1 for first peak, 2 for second peak
                }
            }
//                steps[i] = (double) step / 50.0;
//                isSpiked[i] = (spiked ? 0.1 : 0);
        }
        try {
            XYFunction voltage = new XYFunction("Voltage", xs, voltages);
//                XYFunction deltaSet = new XYFunction("Deltas", xs, deltas);
//                XYFunction delta10Set = new XYFunction("Delta10", x10, deltas10);
//                XYFunction sigDiffSet = new XYFunction("SigDiffs", xs, sigdiffs);
//                XYFunction isSpikedSet = new XYFunction("isSpiked", xs, isSpiked);
//                XYFunction stepSet = new XYFunction("Steps", xs, steps);

            ArrayList<XYFunction> dataSet = new ArrayList();
            dataSet.add(voltage);
//                dataSet.add(deltaSet);
//                dataSet.add(delta10Set);
//                dataSet.add(endSet);
//                dataSet.add(isSpikedSet);
//                dataSet.add(spikeSet);
            file.setData(dataSet);
            XYLineChart_AWT chart = new XYLineChart_AWT(file.getName(), "Time", "yLabel", dataSet);
            chart.prepareChart();
            chart.setSize(new java.awt.Dimension(1920, 1080));
            RefineryUtilities.centerFrameOnScreen(chart);
            chart.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

//            showGraph("Time", xs, "Voltage", voltages);
//            showGraph("Time", xs, "Deltas", deltas);
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
