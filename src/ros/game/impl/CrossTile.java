/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.impl;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import ros.game.Main;
import ros.game.state.ResourceManager;

/**
 *
 * @author Ros
 */
public class CrossTile  extends Tile{

    /**
     * Gli attraversamenti vengono aggiunti prima di
     * iniziare l'attraversamento
     */
    private Vector<Crossings> attraversamentiRealizzati;

    /**
     * no animation images
     */
    private static Image imageBase;
    private static Image imageCross1Orizzontale;
    private static Image imageCross1Verticale;
    private static Image imageCross2;

    /**
     * animation images
     */
    private static Image frames_sinistradestra_non_attraversato[];
    private static Image frames_destrasinistra_non_attraversato[];
    private static Image frames_soprasotto_non_attraversato[];
    private static Image frames_sottosopra_non_attraversato[];
    private static Image frames_sinistradestra_attraversato[];
    private static Image frames_destrasinistra_attraversato[];
    private static Image frames_soprasotto_attraversato[];
    private static Image frames_sottosopra_attraversato[];

    /**
     * Si può ottenere una nuova istanza della classe chiamando il metodo getTile(int type)
     * della classe TileLoaderAndFactory
     *
     */
    public CrossTile() {
        this.attraversamentiRealizzati = new Vector<Crossings>();
    }

    @Override
    public void draw(Graphics2D g, int t, int x, int y, boolean attiva) {
       if (!attiva) { //Senza animazione
            switch (attraversamentiRealizzati.size()) {
                case 0:
                    g.drawImage(imageBase, x, y, null);
                    break;
                case 1:
                    switch(attraversamentiRealizzati.elementAt(0)){
                        case SINISTRADESTRA:
                            g.drawImage(imageCross1Orizzontale, x, y, null);
                            break;
                        case DESTRASINISTRA:
                            g.drawImage(imageCross1Orizzontale, x, y, null);
                            break;
                        case SOPRASOTTO:
                            g.drawImage(imageCross1Verticale, x, y, null);
                            break;
                        case SOTTOSOPRA:
                            g.drawImage(imageCross1Verticale, x, y, null);
                            break;
                        default:
                            throw new CrossingNotPossibleForTile();
                    }
                    break;
                case 2:
                      g.drawImage(imageCross2, x, y, null);
                    break;
                default:
                    System.out.println("#attraversamenti: " +attraversamentiRealizzati.size()+ " questo non dovrebbe essere possibile");
                    throw new InsubstantialNumberOfCrossingsException();
            }
        }
        else { //DRAW ANIMATION TILES
        //
        }
    }

    @Override
    public int getTipo(){
        return TileLoaderAndFactory.INCROCIO;
    }

    public Tile getNuovaIstanza() {
        if (imageBase != null) {
            return new CrossTile();
        } else {
            Logger.getLogger(Main.LOGGER_NAME).log(Level.SEVERE, "Creazione nuova istanza di Cross Tile fallita - chiamare prima loadResources()");
            return null;
        }
    }

    @Override
    /**
     * La board esegue il controllo nel caso la tile sia attiva( in attraversamento)
     * prima di effettuare questo
     */
    public boolean isReplaceable() {
         if(attraversamentiRealizzati.size() > 0)
            return false;
        else
            return true;
    }

    @Override
    public int getPunteggio() {
        if (attraversamentiRealizzati.size() == 0)
            return 0;
        else if (attraversamentiRealizzati.size() == 1)
            return 20;
        else if(attraversamentiRealizzati.size() == 2)
            return 40;
        else
            throw new InsubstantialNumberOfCrossingsException();
    }

    @Override
    public void loadResources(ResourceManager resourceManager) {
        //carica le immagini base

        //nessun attraversamento
        imageBase = resourceManager.loadImage("caselle/INCROCIO.png");
        //attrtaversamento completato orizzontalmente
        imageCross1Orizzontale =  resourceManager.loadImage("caselle/INCROCIO_1_ORIZZONTALE.png");
        //attrtaversamento completato verticalmente
        imageCross1Verticale =  resourceManager.loadImage("caselle/INCROCIO_1_VERTICALE.png");
        //attrtaversamenti esauriti
        imageCross2 =  resourceManager.loadImage("caselle/INCROCIO_2.png");

        //carica le immagini dell'animazione

        //Da sinistra a destra senza attraversamenti realizzati
        frames_sinistradestra_non_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        //Da destra a sinistra senza attraversamenti realizzati
        frames_destrasinistra_non_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        //Da sopra a sotto senza attraversamenti realizzati
        frames_soprasotto_non_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        //Da sopra a sotto senza attraversamenti realizzati
        frames_sottosopra_non_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];

        //Da sinistra a destra con 1 attraversamento realizzato
        frames_sinistradestra_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        //Da destra a sinistra con 1 attraversamento realizzato
        frames_destrasinistra_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        //Da sopra a sotto con 1 attraversamento realizzato
        frames_soprasotto_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        //Da sotto a sopra con 1 attraversamento realizzato
        frames_sottosopra_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];

        for(int i=1;i<=Tile.NUMERO_FRAME_ANIMAZIONE;i++){
            //non attraversati
            //TODO un unico array (cambiare la funzione draw ruotando l'immagine)
            frames_sinistradestra_non_attraversato[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/INCROCIO_DA_LEFT_"+i+"_0.png");
            frames_destrasinistra_non_attraversato[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/INCROCIO_DA_RIGHT_"+i+"_0.png");
            frames_soprasotto_non_attraversato[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/INCROCIO_DA_TOP_"+i+"_0.png");
            frames_sottosopra_non_attraversato[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/INCROCIO_DA_BOTTOM_"+i+"_0.png");
            //attraversati
            frames_sinistradestra_attraversato[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/INCROCIO_DA_LEFT_"+i+"_1.png");
            frames_destrasinistra_attraversato[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/INCROCIO_DA_RIGHT_"+i+"_1.png");
            frames_soprasotto_attraversato[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/INCROCIO_DA_TOP_"+i+"_1.png");
            frames_sottosopra_attraversato[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/INCROCIO_DA_BOTTOM_"+i+"_1.png");
        }
    }

    @Override
    public WaterDirection getdirezioneDiUscita() {
        switch(attraversamentiRealizzati.size()){
            case 0:
                return null;
            case 1:
                if(attraversamentiRealizzati.get(0) == Crossings.SOTTOSOPRA )
                    return WaterDirection.TOP;
                else if(attraversamentiRealizzati.get(0) == Crossings.SOPRASOTTO )
                     return WaterDirection.BOTTOM;
                else if(attraversamentiRealizzati.get(0) == Crossings.DESTRASINISTRA)
                    return WaterDirection.LEFT;
                else
                    return WaterDirection.RIGHT;
            case 2:
                if(attraversamentiRealizzati.get(1) == Crossings.SOTTOSOPRA)
                    return WaterDirection.TOP;
                else if(attraversamentiRealizzati.get(1) == Crossings.SOPRASOTTO)
                     return WaterDirection.BOTTOM;
                else if(attraversamentiRealizzati.get(1) == Crossings.DESTRASINISTRA)
                    return WaterDirection.LEFT;
                else
                    return WaterDirection.RIGHT;
            default:
                return null;
        }
    }

    @Override
    public WaterDirection[] getdirezioniDiEntrataLibere() {
        switch(attraversamentiRealizzati.size()){
            case 0:
                return Arrays.asList(WaterDirection.LEFT, WaterDirection.RIGHT,WaterDirection.BOTTOM,WaterDirection.TOP).toArray(new WaterDirection[4]);
            case 1:
                if(attraversamentiRealizzati.get(0) == Crossings.SOTTOSOPRA | attraversamentiRealizzati.get(0) == Crossings.SOPRASOTTO)
                    return Arrays.asList(WaterDirection.LEFT, WaterDirection.RIGHT).toArray(new WaterDirection[2]);
                else
                    return Arrays.asList(WaterDirection.TOP, WaterDirection.BOTTOM).toArray(new WaterDirection[2]);
            case 2:
                return new WaterDirection[0];
            default:
                return new WaterDirection[0];
        }
    }

    @Override
    public void aggiungiAttraversamento(WaterDirection direzioneDiEntrata) {
        switch (direzioneDiEntrata) {
            case BOTTOM:
                attraversamentiRealizzati.add(Crossings.SOTTOSOPRA);
                break;
            case LEFT:
                attraversamentiRealizzati.add(Crossings.SINISTRADESTRA);
                break;
            case RIGHT:
                attraversamentiRealizzati.add(Crossings.DESTRASINISTRA);
                break;
            case TOP:
                attraversamentiRealizzati.add(Crossings.SOPRASOTTO);
                break;
        }
    }


}
