/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.impl;

/**
 *
 * @author ros
 */
class LevelNotImplementedException  extends RuntimeException {

    public LevelNotImplementedException(int livello) {
         super("Livello non implementato - " + livello);
    }

}
