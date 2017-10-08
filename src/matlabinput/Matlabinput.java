/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlabinput;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
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
            VoltageFile first = VoltageFile.fromString("L1C1SPOT-300um1sec100percent-02-15-22PM.mat");
//            VoltageFile second = VoltageFile.fromString("L1C1SPOT-300um1sec100percent-02-15-28PM.mat");
//            VoltageFile third = VoltageFile.fromString("L1C1SPOT-300um1sec100percent-02-15-34PM.mat");

            XYLineChart_AWT chart = new XYLineChart_AWT(first, 1);

            XYFunction spikes = first.getSpikeFunction(3, .9);
            spikes.setDotShape(new Rectangle2D.Double(-2, -20, 4, 40));
            spikes.setLineSize(0);

            XYFunction spikes1 = first.getSpikeFunction(1, .5);
            spikes1.setDotShape(new Rectangle2D.Double(-2, -20, 4, 40));
            spikes1.setLineSize(0);

            XYFunction spikes2 = first.getSpikeFunction(2, .7);
            spikes2.setDotShape(new Rectangle2D.Double(-2, -20, 4, 40));
            spikes2.setLineSize(0);

            XYFunction offset = first.getSpikeFunction(0, .3);
            offset.setDotShape(new Rectangle2D.Double(-2, -20, 4, 40));
            offset.setLineSize(0);

            chart.addData(spikes, spikes1, spikes2, offset);

            chart.prepareChart();

//            chart.setVisible(true);
        } catch (IOException e) {
            System.out.println("Improper input");
        }
    }
}
