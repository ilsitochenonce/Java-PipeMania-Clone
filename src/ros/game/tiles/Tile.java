/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.tiles;

import java.awt.Graphics2D;
import ros.game.ResourceManager;

/**
 *
 * @author Ros
 */
public interface Tile {

    public static final int NUMERO_FRAME_ANIMAZIONE = 10;
    
    public abstract void draw(Graphics2D g, int x , int y, boolean attiva,int tempoCasella);

    public abstract Tile getNuovaIstanza();

    public abstract int getTipo();
    
    /**
     * Il metodo controlla se il tile cliccato è di un tipo
     * sostituibile e che non sia già stato attraversato.
     *
     * La board dovrà inoltre controllare che il tile non sia quello attivo.
     * (Il tile attivo è sicuramente non sostituibile)
     * 
     * @return true se il tile è sostituibile false altrimenti
     */
    public abstract boolean isReplaceable();

    /**
     * ritorna il punteggio alla fine di un livello.
     *
     * Un tubo vuoto ritorna 0;
     * Un tubo non atraversato ritorna 0;
     * Un tubo attraversato 1 volta ritorna 10;
     * Un tubo attraversato 2 volte ritorna 50;
     *
     * @return punteggio
     */
    public abstract int getPunteggio();

    /**
     * se il tile è in attraversamento ritorna la direzione di uscita dell'acqua o null altrimenti
     *
     * @return
     */
    public abstract WaterDirection getdirezioneDiUscita();
    
    /**
     * ritorna un array di direzioni aperte
     * 
     * @return
     */
    public abstract WaterDirection[] getdirezioniDiEntrataLibere();

    /**
     * memorizza l'inizio dell'attraversamento (non serve memorizzarne la fine)
     *
     * @param attraversamento
     */
    public abstract void aggiungiAttraversamento(WaterDirection attraversamento);
    
    public abstract void loadResources(ResourceManager resourceManager);
}
