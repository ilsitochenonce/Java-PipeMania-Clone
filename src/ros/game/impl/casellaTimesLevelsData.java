/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.impl;

/**
 *
 * @author ros
 */
public class casellaTimesLevelsData {

    public static final int NUM_LEVELS = 30;

    private int[] casellaLevelsTime;

    public casellaTimesLevelsData() {
        
        casellaLevelsTime = new int[NUM_LEVELS];

        for(int i=0; i<NUM_LEVELS; i++){
            if(i<6)
                casellaLevelsTime[i] = 6000;
            else if(i<11)
                casellaLevelsTime[i] = 5000;
            else if(i<16)
                casellaLevelsTime[i] = 4000;
            else if(i<21)
                casellaLevelsTime[i] = 3000;
            else if(i<26)
                casellaLevelsTime[i] = 2000;
            else
                casellaLevelsTime[i] = 1000;
        }
    }
    
    public int getCasellaTime(int levelIndex) {
         return casellaLevelsTime[levelIndex];
    }

}
