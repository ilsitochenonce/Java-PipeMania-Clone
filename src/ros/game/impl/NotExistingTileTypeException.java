/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.impl;

/**
 *
 * @author Ros
 */
class NotExistingTileTypeException  extends RuntimeException{

    public NotExistingTileTypeException() {
        super("Tipo Tile non esistente");
    }

    public NotExistingTileTypeException(String mes){
        super(mes);
    }

}
