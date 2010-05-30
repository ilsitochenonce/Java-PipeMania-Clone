/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.tiles;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import ros.game.Main;
import ros.game.state.ResourceManager;

/**
 *
 * @author Ros
 */
public class WallTile extends Tile{

    private static Image imageBase;

    @Override
    public int getTipo(){
        return TileLoaderAndFactory.WALL_TILE;
    }

    @Override
    public boolean isReplaceable() {
        return false;
    }

    @Override
    public void loadResources(ResourceManager resourceManager) {
       imageBase = resourceManager.loadImage("caselle/MURO.png");
       if(imageBase == null)
           throw new RuntimeException("immagine: caselle/MURO.png non caricata");
    }

    @Override
    public void draw(Graphics2D g, int x, int y, boolean attiva, int tempoCasella) {
        if(imageBase != null)
            g.drawImage(imageBase, x, y, null);
        else
            System.out.println("immagine muro null");
    }

    @Override
    public int getPunteggio() {
        return 0;
    }

    public Tile getNuovaIstanza() {
        if (imageBase != null) {
            return new WallTile();
        } else {
            Logger.getLogger(Main.LOGGER_NAME).log(Level.SEVERE, "Creazione nuova istanza di Wall Tile fallita - chiamare prima loadResources()");
            throw new LoadResourceNotCalledException();
        }
    }

    @Override
    public WaterDirection getdirezioneDiUscita() {
        return null;
    }

    @Override
    public WaterDirection[] getdirezioniDiEntrataLibere() {
        return new WaterDirection[0];
    }

    @Override
    public void aggiungiAttraversamento(WaterDirection attraversamento) {
        //niente
    }

}
