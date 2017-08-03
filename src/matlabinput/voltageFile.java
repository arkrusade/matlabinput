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

/**
 *
 * @author Justin Lee
 */
public class voltageFile {
    private final String name;
    private final double samplesPerSecond;
    private double[] voltages;
    private ArrayList<int[]> spikes;
    ArrayList<XYFunction> data;

    
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
    public voltageFile(String name, double samplesPerSecond, double[] voltages) {
        this.name = name;
        this.samplesPerSecond = samplesPerSecond;
        this.voltages = voltages;
    }

    public voltageFile(String name, double samplesPerSecond, double[] voltages, ArrayList<int[]> spikes) {
        this.name = name;
        this.samplesPerSecond = samplesPerSecond;
        this.voltages = voltages;
        this.spikes = spikes;
    }

    public voltageFile(String name, double samplesPerSecond, double[] voltages, ArrayList<int[]> spikes, ArrayList<XYFunction> extras) {
        this.name = name;
        this.samplesPerSecond = samplesPerSecond;
        this.voltages = voltages;
        this.spikes = spikes;
        this.data = extras;
    }

    public String getName() {
        return name;
    }

    public double getSamplesPerSecond() {
        return samplesPerSecond;
    }
    
    public double[] getVoltages() {
        return voltages;
    }

    public void setVoltages(double[] voltages) {
        this.voltages = voltages;
    }

    public void findSpikes() {

        double[] xs = new double[voltages.length];//stores time values (x-dim)
//            double[] x10 = new double[voltages.length / 10];//stores every tenth time value
//            double[] deltas = new double[voltages.length];//stores every delta-y
//            double[] deltas10 = new double[voltages.length / 10];//stores every tenth delta-y
        ArrayList<int[]> findingSpikes = new ArrayList();//stores every spike, with array of important times for spike
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
                    findingSpikes.get(findingSpikes.size() - 1)[3] = i;
                }
            }

            if (Math.abs(d) >= .005) {//if diff is big (either direction)
//                    sigdiffs[i] = .15 * d / Math.abs(d);
                if (step == 0) {//to go to step 1, must have large diff and not in spike
                    spiked = true;
                    int[] newSpike = new int[2];
                    newSpike[0] = i;
                    findingSpikes.add(newSpike);
                    upwards = d > 0;//if spike initially positive, upwards is true, else false
                    step = 1;
                } else if (step == 2 && (d10 < 0 == upwards)) {//to go to step 3, must be in opposite direction with large diff
                    step = 3;
                }
            } else if (spiked) {//to go to step 2 or 4, must be small diff
                if (step == 1 || step == 3) {
                    step++;
                    findingSpikes.get(findingSpikes.size() - 1)[step / 2] = i;//1 for first peak, 2 for second peak
                }
            }
//                steps[i] = (double) step / 50.0;
//                isSpiked[i] = (spiked ? 0.1 : 0);
        }
        this.spikes = findingSpikes;
    }
    
    public ArrayList<int[]> getSpikes() {
        return spikes;
    }

    public void setSpikes(ArrayList<int[]> spikes) {
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
     * Removes the first occurrences of XYFunctions with the specified value for name in <tt>data</tt>
     *
     * @param  name the name of the extra to remove from <tt>data</tt>
     * @return <tt>true</tt> if it removes an element
     */
    public boolean removeData(String name) {
        for (XYFunction dat : data) {
            if(dat.getName().equals(name))
            {
                data.remove(dat);
                return true;
            }
        }
        return false;
    }
}
