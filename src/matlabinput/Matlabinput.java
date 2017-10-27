/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlabinput;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.jfree.ui.RefineryUtilities;
import components.FileInputManager;
/**
 *
 * @author justinjlee99
 */
public class Matlabinput {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean ui = true;
        if (ui) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    //Turn off metal's use of bold fonts
                    UIManager.put("swing.boldMetal", Boolean.FALSE);
                    FileInputManager fin = new FileInputManager();
                    fin.showNew();
                }
            });
        } else {
            try {
//            GraphManager.getInstance().addVoltageFileByPath("L1C1SPOT-300um1sec100percent-02-15-28PM.mat");
//            GraphManager.getInstance().addVoltageFileByPath("L1C1SPOT-300um1sec100percent-02-15-34PM.mat");
//            GraphManager.getInstance().displayChart();
//        try {
//            voltageFile first = voltageFile.fromString("L1C1SPOT-300um1sec100percent-02-15-22PM.mat");
                VoltageFile second = VoltageFile.fromString("L1C1SPOT-300um1sec100percent-02-15-28PM.mat");
//            voltageFile third = voltageFile.fromString("L1C1SPOT-300um1sec100percent-02-15-34PM.mat");
//
                XYLineChart_AWT chart = new XYLineChart_AWT(second, XYLineChart_AWT.chartType.VOLTAGE);
//
                XYFunction spikes = second.getSpikeFunction(3, .9);
                spikes.setDotShape(new Rectangle2D.Double(-2, -20, 4, 40));
                spikes.setLineSize(0);

                XYFunction spikes1 = second.getSpikeFunction(1, .5);
                spikes1.setDotShape(new Rectangle2D.Double(-2, -20, 4, 40));
                spikes1.setLineSize(0);

                XYFunction spikes2 = second.getSpikeFunction(2, .7);
                spikes2.setDotShape(new Rectangle2D.Double(-2, -20, 4, 40));
                spikes2.setLineSize(0);

                XYFunction offset = second.getSpikeFunction(0, .3);
                offset.setDotShape(new Rectangle2D.Double(-2, -20, 4, 40));
                offset.setLineSize(0);
//
                chart.addData(second.getVoltageFunction(), spikes, spikes1, spikes2, offset);
//
                chart.prepareChart();

                RefineryUtilities.centerFrameOnScreen(chart);
                chart.setVisible(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
            } catch (IOException ex) {
                Logger.getLogger(Matlabinput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
