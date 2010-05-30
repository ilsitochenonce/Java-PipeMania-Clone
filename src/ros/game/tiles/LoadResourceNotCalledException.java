/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.tiles;

/**
 *
 * @author ros
 */
class LoadResourceNotCalledException extends RuntimeException {

    public LoadResourceNotCalledException() {
        super("Tile non clonabile - Risorse non caricate");
    }

    public LoadResourceNotCalledException(String mes){
        super(mes);
    }

}
