/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.impl;

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
public class EmptyTile extends Tile {

    private static Image imageBase;
    
    @Override
    public int getTipo(){
        return TileLoaderAndFactory.EMPTY_TILE;
    }

    @Override
    public boolean isReplaceable() {
        return true;
    }

    @Override
    public void draw(Graphics2D g, int t, int x , int y, boolean attiva) {
        g.drawImage(imageBase, x, y, null);
    }

    @Override
    public void loadResources(ResourceManager resourceManager) {
         imageBase = resourceManager.loadImage("caselle/VUOTA.png");
    }

    @Override
    public int getPunteggio() {
        return 0;
    }

	public Tile getNuovaIstanza() {
        if (imageBase != null) {
            return new EmptyTile();
        } else {          
            Logger.getLogger(Main.LOGGER_NAME).log(Level.SEVERE, "Creazione nuova istanza di Empty Tile fallita - chiamare prima loadResources()");
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
