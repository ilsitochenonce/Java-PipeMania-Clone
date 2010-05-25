/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.levels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import ros.game.input.GameAction;

/**
 *
 * @author ros
 */
public class BriefingProgressBar implements ActionListener  {

    private long startTimerMillis;

    private Timer timerBiefring;
    private int[] briefingLevelsTime;

    private JProgressBar briefingProgressBar;
    private GameAction briefingFinishAction;

    public BriefingProgressBar() {
        briefingProgressBar = new JProgressBar();
        briefingProgressBar.setForeground(Color.blue);
        briefingProgressBar.setBackground(Color.white);
        briefingProgressBar.setMinimum(0);
        briefingProgressBar.setBounds(Board.BOARD_BASEX, 50, Board.TILE_SIZEX*(Board.COLONNE-2), 20);
        briefingProgressBar.setVisible(false);
        briefingProgressBar.setStringPainted(true);

        int tmpBriefing = 20000;
        briefingLevelsTime = new int[casellaTimesLevelsData.NUM_LEVELS];
        for(int i=0; i<casellaTimesLevelsData.NUM_LEVELS; i++){
            briefingLevelsTime[i] = tmpBriefing;
            tmpBriefing -= 500;
        }

    }

    public void initForStartingLevel(int level) {
        briefingProgressBar.setMaximum(briefingLevelsTime[level-1]);
        briefingProgressBar.setValue(0);
        briefingProgressBar.setVisible(true);
    }

    public void setVisible(boolean bool){
        briefingProgressBar.setVisible(bool);
    }

    public JProgressBar getBriefingProgressBar() {
        return briefingProgressBar;
    }

    public void startTimer(GameAction timerBiefringAction, int levelIndex){
        System.out.println("START BIEFRING TIMER - LevelIndex: "+ levelIndex + " delay: "+ briefingLevelsTime[levelIndex]);
        System.out.flush();
        this.briefingFinishAction = timerBiefringAction;
        timerBiefring = new Timer(briefingLevelsTime[levelIndex], this);
        timerBiefring.start();
        startTimerMillis = System.currentTimeMillis();
    }

    /**
     * aggiorna lo stato della progress bar
     */
    public void update(){
        briefingProgressBar.setValue((int) (System.currentTimeMillis() - startTimerMillis));
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == timerBiefring){
            System.out.println("tempo biefring finito");
            System.out.flush();

            briefingFinishAction.tap();
            timerBiefring.stop();
        }
    }

    public void acceleraBiefringTimer() {
        timerBiefring.stop();
        briefingProgressBar.setValue(briefingProgressBar.getMaximum());
        briefingFinishAction.tap();
    }

    private void stopTimer(){
        timerBiefring.stop();
    }
}
