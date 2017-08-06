/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlabinput;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLStructure;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Justin Lee
 */
public class voltageFile {

    final static String yString = "Voltage";
    final static String xString = "Time";

    private final String name;
    private double samplesPerSecond;
//    private double[] voltages;
//    private double[] xSet;
    private XYFunction voltageFunction;
    private ArrayList<Spike> spikes;
    ArrayList<XYFunction> data;

    public voltageFile(String name, double samplesPerSecond, double[] voltages) {
        this.name = name;
        this.samplesPerSecond = samplesPerSecond;
        double[][] x = new double[2][voltages.length];
        for (int i = 0; i < x.length; i++) {
            x[0][i] = i;
            x[1][i] = voltages[i];
        }
        this.voltageFunction = new XYFunction("Voltages", x);
        this.findSpikes();
    }
//
//    public voltageFile(String name, double samplesPerSecond, double[] xSet, double[] voltages) {
//        this(name, samplesPerSecond, xSet, voltages, new ArrayList<>(0), new ArrayList<>(0));
//        this.findSpikes();
//    }

//    public voltageFile(String name, double samplesPerSecond, double[] voltages, ArrayList<Spike> spikes) {
//        this.name = name;
//        this.samplesPerSecond = samplesPerSecond;
//        this.voltages = voltages;
//        this.spikes = spikes;
//    }
    private voltageFile(String name, double samplesPerSecond, double[] xSet, double[] voltages, ArrayList<Spike> spikes, ArrayList<XYFunction> data) throws InstantiationException {
        this(name, samplesPerSecond, new XYFunction(voltageFile.yString, xSet, voltages), spikes, data);
    }

    public voltageFile(String name, double samplesPerSecond, XYFunction voltageFunction, ArrayList<Spike> spikes, ArrayList<XYFunction> data) {
        this.name = name;
        this.samplesPerSecond = samplesPerSecond;
        this.voltageFunction = voltageFunction;
        this.spikes = spikes;
        this.data = data;
    }

//TODO: check these for errors when input from mfr file
    //ie: that voltage and Samp_Rate exist, and are proper variable types
    public static voltageFile fromFile(File file) throws IOException {
        MatFileReader mfr = new MatFileReader(file);
        MLStructure x = (MLStructure) mfr.getContent().get("x");
        MLDouble voltMAT = (MLDouble) x.getField("voltage");
        MLDouble sampleRate = (MLDouble) x.getField("Samp_Rate");
        double samplesPerSecond = sampleRate.getArray()[0][0];
        double[] voltages = voltMAT.getArray()[0];
        return new voltageFile(file.getName(), samplesPerSecond, voltages);
    }

    public static voltageFile fromString(String fileName) throws IOException {
        MatFileReader mfr = new MatFileReader(fileName);
        MLStructure x = (MLStructure) mfr.getContent().get("x");
        MLDouble voltMAT = (MLDouble) x.getField("voltage");
        MLDouble sampleRate = (MLDouble) x.getField("Samp_Rate");
        double samplesPerSecond = sampleRate.getArray()[0][0];
        double[] voltages = voltMAT.getArray()[0];
        return new voltageFile(fileName, samplesPerSecond, voltages);
    }

    public String getName() {
        return name;
    }

    public double getSamplesPerSecond() {
        return samplesPerSecond;
    }

    public XYFunction getVoltageFunction() {
        return voltageFunction;
    }

    public void setVoltageFunction(XYFunction voltageFunction) {
        this.voltageFunction = voltageFunction;
    }

    public double[] getVoltages() {
        return voltageFunction.getYSet();
    }

    public void setVoltages(double[] voltages) throws IllegalArgumentException {
        this.voltageFunction.setYSet(voltages);
    }

    private void findSpikes() {
        double[] voltages = getVoltages();
//        double[] xs = new double[voltages.length];//stores time values (x-dim)
//            double[] x10 = new double[voltages.length / 10];//stores every tenth time value
//            double[] deltas = new double[voltages.length];//stores every delta-y
//            double[] deltas10 = new double[voltages.length / 10];//stores every tenth delta-y
        ArrayList<Spike> findingSpikes = new ArrayList();//stores every spike, with array of important times for spike

//            double[] sigdiffs = new double[voltages.length];
//            double[] steps = new double[voltages.length];
//            double[] isSpiked = new double[voltages.length];
        boolean spiked = false;
        boolean upwards = false;
        int step = 0;
        double d, d10;
        d10 = 0;

        //when step is this value: 
        //0 is not spike, 1 is spike started, 2 is first peak, 3 is from peak to peak, 4 is second peak, 5 is from peak to reverse
        for (int i = 1; i < voltages.length; i++) {
//            xs[i] = i;
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
                    findingSpikes.get(findingSpikes.size() - 1).setStep(3, i);
                }
            }

            if (Math.abs(d) >= .005) {//if diff is big (either direction)
//                    sigdiffs[i] = .15 * d / Math.abs(d);
                if (step == 0) {//to go to step 1, must have large diff and not in spike
                    spiked = true;
                    Spike newSpike = new Spike();
                    newSpike.setStart(i);
                    findingSpikes.add(newSpike);
                    upwards = d > 0;//if spike initially positive, upwards is true, else false
                    step = 1;
                } else if (step == 2 && (d10 < 0 == upwards)) {//to go to step 3, must be in opposite direction with large diff
                    step = 3;
                }
            } else if (spiked) {//to go to step 2 or 4, must be small diff
                if (step == 1 || step == 3) {
                    step++;
                    findingSpikes.get(findingSpikes.size() - 1).setStep(step / 2, i);//1 for first peak, 2 for second peak
                }
            }
//                steps[i] = (double) step / 50.0;
//                isSpiked[i] = (spiked ? 0.1 : 0);
        }
        this.spikes = findingSpikes;
    }

    public XYFunction getSpikeFunction(int step) {
        double[][] xy = new double[2][spikes.size()];
        for (int i = 0; i < xy[0].length; i++) {
            xy[0][i] = spikes.get(i).getStep(step);
            xy[1][i] = step;
        }
        return new XYFunction("Spikes", xy);
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }

    //returns double[] of spikes for a specific step

    public double[] getSpikes(int step) {
        double[] spikesStep = new double[spikes.size()];
        for (int i = 0; i < spikes.size(); i++) {
            spikesStep[i] = spikes.get(i).getStep(step);
        }
        return spikesStep;
    }

    //TODO: keep manual override?
    public void setSpikes(ArrayList<Spike> spikes) {
        this.spikes = spikes;
    }

    public ArrayList<XYFunction> getData() {
        return data;
    }

    public void setData(ArrayList<XYFunction> data) {
        this.data = data;
    }

    public void addData(XYFunction data) {
        this.data.add(data);
    }

    /**
     * Removes the first occurrences of XYFunctions with the specified value for
     * name in <tt>data</tt>
     *
     * @param name the name of the extra to remove from <tt>data</tt>
     * @return <tt>true</tt> if it removes an element
     */
    public boolean removeData(String name) {
        for (XYFunction dat : data) {
            if (dat.getName().equals(name)) {
                data.remove(dat);
                return true;
            }
        }
        return false;
    }
}
