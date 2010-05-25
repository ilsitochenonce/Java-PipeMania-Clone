/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testScreen;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

/**
 *
 * @author ros
 */
public class testDisplaymodes {
    private final GraphicsDevice device;
    private final DisplayMode[] displayModes;

    public testDisplaymodes() {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = environment.getDefaultScreenDevice();
        displayModes = device.getDisplayModes();

        for(int i=0;i<displayModes.length;i++){
            int bitDepth = displayModes[i].getBitDepth();
            String valueBitDepth = null;
            if (bitDepth == DisplayMode.BIT_DEPTH_MULTI) {
                valueBitDepth = "Multi";
            } else {
                valueBitDepth = Integer.toString(bitDepth);
            }
            System.out.println(displayModes[i].getWidth()+"x"+displayModes[i].getHeight()+"@"+displayModes[i].getRefreshRate()+"."+valueBitDepth);
        }
        System.out.flush();
    }

 public static void main(String[] args) {
        new testDisplaymodes();
    }
}
