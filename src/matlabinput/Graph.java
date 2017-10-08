/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlabinput;

import com.sun.glass.ui.Size;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Justin Lee
 */
public enum Graph {

    /**
     *
     */
    SPIKE(new Rectangle2D.Double(-2, -20, 4, 40), 0.0f), VOLTAGE(new Ellipse2D.Double(-1, -1, 2, 2), 0.5f);
    private Shape dot;
    private float size;
    Graph(Shape dot, float size) {
        this.dot = dot;
        this.size = size;
    }
    public Shape getShape() {
        return dot;
    }

    public float getLineSize() {
        return size;
    }
    
}
