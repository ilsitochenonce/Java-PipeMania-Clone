/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.impl;

/**
 *
 * @author Ros
 */
class CrossingNotPossibleForTile extends RuntimeException{

    public CrossingNotPossibleForTile() {
        super("Attraversamento del tile errato");
    }

}
