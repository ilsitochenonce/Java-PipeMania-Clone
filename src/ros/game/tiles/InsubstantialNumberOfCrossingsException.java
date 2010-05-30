/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.tiles;

/**
 *
 * @author Ros
 */
class InsubstantialNumberOfCrossingsException extends RuntimeException{

    public InsubstantialNumberOfCrossingsException() {
        super("Numero di attraversamenti del tile inconsistente");
    }

    public InsubstantialNumberOfCrossingsException(String mes){
        super(mes);
    }

}
