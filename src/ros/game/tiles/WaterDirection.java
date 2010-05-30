/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.tiles;

/**
 *
 * @author Ros
 */
public enum WaterDirection {

    LEFT,
    RIGHT,
    TOP,
    BOTTOM;

     /**
      *
      * @param uscita - direzione rispetto alla quale calcolare l'allinemento
      * @param direzioniDiEntrataLibere -  direzioni ripetto alle quali calcolare l'allineamento
      * @returnnull se non sono allineate
     */
    public static WaterDirection allineato(WaterDirection uscita, WaterDirection[] direzioniDiEntrataLibere ) {
        if(direzioniDiEntrataLibere == null)
            return null;
        else if(direzioniDiEntrataLibere.length == 0)
            return null;
        else{
            for(int i=0;i<direzioniDiEntrataLibere.length;i++){
                if(opposto(uscita, direzioniDiEntrataLibere[i]))
                    return direzioniDiEntrataLibere[i];
            }
            return null;
        }
    }

    private static boolean opposto(WaterDirection a, WaterDirection b){
        switch(a){
            case BOTTOM:
                if(b == WaterDirection.TOP)
                    return true;
                else
                    return false;
            case LEFT:
                if(b == WaterDirection.RIGHT)
                    return true;
                else
                    return false;
            case RIGHT:
                if(b == WaterDirection.LEFT)
                    return true;
                else
                    return false;
            case TOP:
                if(b == WaterDirection.BOTTOM)
                    return true;
                else
                    return false;
                default: return false;
        }
    }
}
