/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.tiles;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import ros.game.Main;
import ros.game.ResourceManager;
import ros.game.util.ImageTools;

/**
 *
 * @author Ros
 */
public class DoubleCrossingTile implements  Tile{

    /*
     DOPPIACURVA_SINISTRASOPRA - 1
     SOPPIACURVA_SINISTRASOTTO - 2
    */
    int tipo;
   /**
     * memorizza il timestamp dell'inizio di un attraversamento (per la gestione dell'animazione
     */
    private long timestampInizioAttraversamento;
    //variabili per l'animazione
    private int millisecondiTrascorsi;
    private int tempoFrame;
    private int numeroFrame; //da 1 a 10
     
    /**
     * Gli attraversamenti vengono aggiunti prima di
     * iniziare l'attraversamento
     */
    private Vector<Crossings> attraversamentiRealizzati;

    /**
     * no animation images
     */
    private static Image imageBase_1;
    private static Image imageSinistraSopra_1; //vale anche per SopraSinistra
    private static Image imageDestraSotto_1; //vale anche per SottoDestra
    private static Image image2attraversamenti_1;
    
    private static Image imageBase_2;
    private static Image imageSinistraSotto_2; //vale anche per SottoSinistra
    private static Image imageDestraSopra_2; //vale anche per SopraDestra
    private static Image image2attraversamenti_2;
    
    /**
     * animation images tipo 1
     */
    private static Image frames_sinistrasopra_non_attraversato[];
    private static Image frames_soprasinistra_non_attraversato[];
    private static Image frames_sottodestra_non_attraversato[];
    private static Image frames_destrasotto_non_attraversato[];
    
    private static Image frames_sinistrasopra_attraversato[];
    private static Image frames_soprasinistra_attraversato[];
    private static Image frames_sottodestra_attraversato[];
    private static Image frames_destrasotto_attraversato[];

    /**
     * animation images tipo 2
     */
    private static Image frames_destrasopra_non_attraversato[];
    private static Image frames_sopradestra_non_attraversato[];
    private static Image frames_sinistrasotto_non_attraversato[];
    private static Image frames_sottosinistra_non_attraversato[];
    
    private static Image frames_destrasopra_attraversato[];
    private static Image frames_sopradestra_attraversato[];
    private static Image frames_sinistrasotto_attraversato[];
    private static Image frames_sottosinistra_attraversato[];


    /**
     * Nuove istanza della classe si ottengono dal TileLoaderAndFactory col metodo getTile(int tipo)
     */
    public DoubleCrossingTile(int tipo )
    {
        this.tipo = tipo;
        this.attraversamentiRealizzati = new Vector<Crossings>();
    }

    @Override
    public int getTipo(){
        return tipo;
    }
    
    public Tile getNuovaIstanza() {
        if (imageBase_1 != null) {
            return new DoubleCrossingTile(this.tipo );
        } else {
            Logger.getLogger(Main.LOGGER_NAME).log(Level.SEVERE, "Creazione nuova istanza di Double Crossing Tile fallita - chiamare prima loadResources()");
            return null;
        }
    }

    @Override
    public boolean isReplaceable() {
        if(attraversamentiRealizzati.size() > 0)
            return false;
        else
            return true;
    }
    
    public int getPunteggio(){
        switch(attraversamentiRealizzati.size()){
            case 0: return 0;
            case 1: return 20;
            case 2: return 200;
            default:
                throw new InsubstantialNumberOfCrossingsException();
        }
    }

    @Override
    public void draw(Graphics2D g,int x, int y, boolean attiva, int tempoCasella) {
        if (!attiva) { //DRAW NOT ANIMATION TILE
            switch (attraversamentiRealizzati.size()) {
                case 0:
                    if(tipo == TileLoaderAndFactory.DOPPIACURVA_SINISTRASOPRA)
                        g.drawImage(imageBase_1, x, y, null);
                    else
                        g.drawImage(imageBase_2, x, y, null);
                    break;
                case 1:
                    switch(attraversamentiRealizzati.elementAt(0)){
                        case DESTRASOPRA:
                            g.drawImage(imageDestraSopra_2, x, y, null);
                            break;
                        case SOPRADESTRA:
                            g.drawImage(imageDestraSopra_2, x, y, null);
                            break;
                        case DESTRASOTTO:
                            g.drawImage(imageDestraSotto_1, x, y, null);
                            break;
                        case SOTTODESTRA:
                            g.drawImage(imageDestraSotto_1, x, y, null);
                            break;
                        case SOTTOSINISTRA:
                            g.drawImage(imageSinistraSotto_2, x, y, null);
                            break;
                        case SINISTRASOTTO:
                            g.drawImage(imageSinistraSotto_2, x, y, null);
                            break;
                        case SINISTRASOPRA:
                            g.drawImage(imageSinistraSopra_1, x, y, null);
                            break;
                        case SOPRASINISTRA:
                            g.drawImage(imageSinistraSopra_1, x, y, null);
                            break;
                        default:
                            throw new CrossingNotPossibleForTile();
                    }
                    break;
                case 2:
                    if(tipo == TileLoaderAndFactory.DOPPIACURVA_SINISTRASOPRA)
                        g.drawImage(image2attraversamenti_1, x, y, null);
                    else
                        g.drawImage(image2attraversamenti_2, x, y, null);
                    break;
                default:
                    throw new InsubstantialNumberOfCrossingsException();
            }
        }
        else { //DRAW ANIMATION TILES
           //CALCOLO IL FRAME DELL'ANIMAZIONE
            millisecondiTrascorsi = (int) (System.currentTimeMillis() - timestampInizioAttraversamento);
            tempoFrame = tempoCasella/10;
            for(int i=1;i <=10; i++ ){
                  if(millisecondiTrascorsi < tempoFrame*i){
                     numeroFrame = i;
                     break;
                  }
            }
            /////////FINE CALCOLO FRAME

            switch (attraversamentiRealizzati.size()) {
                case 1:
                    switch(attraversamentiRealizzati.elementAt(0)){
                        case DESTRASOPRA:
                            g.drawImage(frames_destrasopra_non_attraversato[numeroFrame-1], x, y, null);
                            break;
                        case SOPRADESTRA:
                            g.drawImage(frames_sopradestra_non_attraversato[numeroFrame-1], x, y, null);
                            break;
                        case DESTRASOTTO:
                            g.drawImage(frames_destrasotto_non_attraversato[numeroFrame-1], x, y, null);
                            break;
                        case SOTTODESTRA:
                            g.drawImage(frames_sottodestra_non_attraversato[numeroFrame-1], x, y, null);
                            break;
                        case SOTTOSINISTRA:
                            g.drawImage(frames_sottosinistra_non_attraversato[numeroFrame-1], x, y, null);
                            break;
                        case SINISTRASOTTO:
                            g.drawImage(frames_sinistrasotto_non_attraversato[numeroFrame-1], x, y, null);
                            break;
                        case SINISTRASOPRA:
                            g.drawImage(frames_sinistrasopra_non_attraversato[numeroFrame-1], x, y, null);
                            break;
                        case SOPRASINISTRA:
                            g.drawImage(frames_soprasinistra_non_attraversato[numeroFrame-1], x, y, null);
                            break;
                        default:
                            throw new CrossingNotPossibleForTile();
                    }
                    break;
                case 2:
                   switch(attraversamentiRealizzati.elementAt(1)){
                        case DESTRASOPRA:
                            g.drawImage(frames_destrasopra_attraversato[numeroFrame-1], x, y, null);
                            break;
                        case SOPRADESTRA:
                            g.drawImage(frames_sopradestra_attraversato[numeroFrame-1], x, y, null);
                            break;
                        case DESTRASOTTO:
                            g.drawImage(frames_destrasotto_attraversato[numeroFrame-1], x, y, null);
                            break;
                        case SOTTODESTRA:
                            g.drawImage(frames_sottodestra_attraversato[numeroFrame-1], x, y, null);
                            break;
                        case SOTTOSINISTRA:
                            g.drawImage(frames_sottosinistra_attraversato[numeroFrame-1], x, y, null);
                            break;
                        case SINISTRASOTTO:
                            g.drawImage(frames_sinistrasotto_attraversato[numeroFrame-1], x, y, null);
                            break;
                        case SINISTRASOPRA:
                            g.drawImage(frames_sinistrasopra_attraversato[numeroFrame-1], x, y, null);
                            break;
                        case SOPRASINISTRA:
                            g.drawImage(frames_soprasinistra_attraversato[numeroFrame-1], x, y, null);
                            break;
                        default:
                            throw new CrossingNotPossibleForTile();
                    }
                    break;
            }
        }
    }

    @Override
    public void loadResources(ResourceManager resourceManager) {
        //carica le immagini base

        //nessun attraversamento
        imageBase_1 = resourceManager.loadImage("caselle/DOPPIACURVA1.png");
        //attraversamento completato SINISTRASOPRA O SOPRASINISTRA
        imageSinistraSopra_1 =  resourceManager.loadImage("caselle/DOPPIACURVA1_1_SINISTRASOPRA.png");
        //attraversamento completato DESTRASOTTO O SOTTODESTRA
        imageDestraSotto_1 =  resourceManager.loadImage("caselle/DOPPIACURVA1_1_SOTTODESTRA.png");
        //attraversamenti esauriti
        image2attraversamenti_1 =  resourceManager.loadImage("caselle/DOPPIACURVA1_2.png");

        imageBase_2 = resourceManager.loadImage("caselle/DOPPIACURVA2.png");
        imageSinistraSotto_2 = resourceManager.loadImage("caselle/DOPPIACURVA2_1_SINISTRASOTTO.png");
        imageDestraSopra_2 = resourceManager.loadImage("caselle/DOPPIACURVA2_1_DESTRASOPRA.png");
        image2attraversamenti_2 = resourceManager.loadImage("caselle/DOPPIACURVA2_2.png");

        //carica le immagini dell'animazione
        
        frames_sinistrasotto_attraversato= new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        frames_soprasinistra_attraversato= new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        frames_destrasopra_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        frames_destrasotto_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        frames_soprasinistra_non_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        frames_sinistrasotto_non_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        frames_destrasotto_non_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        frames_destrasopra_non_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];

        for(int i=1;i<=Tile.NUMERO_FRAME_ANIMAZIONE;i++){
            //non attraversati
            //TODO un unico array (cambiare la funzione draw ruotando l'immagine)
            frames_soprasinistra_non_attraversato[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/DOPPIACURVA1_DA_TOP_"+i+"_0.png");
            frames_sinistrasotto_non_attraversato[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/DOPPIACURVA2_DA_LEFT_"+i+"_0.png");
            frames_destrasotto_non_attraversato[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/DOPPIACURVA1_DA_RIGHT_"+i+"_0.png");
            frames_destrasopra_non_attraversato[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/DOPPIACURVA2_DA_RIGHT_"+i+"_0.png");
            //attraversati
            frames_soprasinistra_attraversato[i - 1] = resourceManager.loadImage("caselle/ANIMATIONS/DOPPIACURVA1_DA_TOP_"+i+"_1.png");
            frames_sinistrasotto_attraversato[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/DOPPIACURVA2_DA_LEFT_"+i+"_1.png");
            frames_destrasotto_attraversato[i - 1] = resourceManager.loadImage("caselle/ANIMATIONS/DOPPIACURVA1_DA_RIGHT_" + i + "_1.png");
            frames_destrasopra_attraversato[i - 1] = resourceManager.loadImage("caselle/ANIMATIONS/DOPPIACURVA2_DA_RIGHT_" + i + "_1.png");
        }

         //per rotazione
        frames_sinistrasopra_non_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        frames_sottodestra_non_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        frames_sinistrasopra_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        frames_sottodestra_attraversato = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        frames_sopradestra_non_attraversato= new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        frames_sottosinistra_non_attraversato= new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        frames_sopradestra_attraversato= new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        frames_sottosinistra_attraversato= new Image[Tile.NUMERO_FRAME_ANIMAZIONE];

                //creo la trasformazione da applicare
        AffineTransform at = AffineTransform.getRotateInstance(Math.PI / 2.0, imageBase_1.getWidth(null) / 2.0,
                imageBase_1.getHeight(null) / 2.0);
        AffineTransformOp atRotazione = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);

        for (int i = 1; i <= Tile.NUMERO_FRAME_ANIMAZIONE; i++) {
            
            frames_sottodestra_non_attraversato[i - 1] = atRotazione.filter(ImageTools.toBufferedImage(frames_destrasopra_non_attraversato[i - 1]), null);
            frames_sottodestra_attraversato[i - 1] = atRotazione.filter(ImageTools.toBufferedImage(frames_destrasopra_attraversato[i - 1]), null);
            frames_sottosinistra_non_attraversato[i - 1] = atRotazione.filter(ImageTools.toBufferedImage(frames_destrasotto_non_attraversato[i - 1]), null);
            frames_sottosinistra_attraversato[i - 1] = atRotazione.filter(ImageTools.toBufferedImage(frames_destrasotto_attraversato[i - 1]), null);
            frames_sinistrasopra_attraversato[i-1] = atRotazione.filter(ImageTools.toBufferedImage(frames_sottosinistra_attraversato[i - 1]), null);
            frames_sinistrasopra_non_attraversato[i - 1] = atRotazione.filter(ImageTools.toBufferedImage(frames_sottosinistra_non_attraversato[i - 1]), null);
            
            frames_sopradestra_non_attraversato[i - 1] = atRotazione.filter(ImageTools.toBufferedImage(frames_sinistrasopra_non_attraversato[i - 1]), null);
            frames_sopradestra_attraversato[i - 1] = atRotazione.filter(ImageTools.toBufferedImage(frames_sinistrasopra_attraversato[i - 1]), null);
        }
    }

    @Override
    public WaterDirection getdirezioneDiUscita() {
        switch(attraversamentiRealizzati.size()){
            case 0:
                return null;
            case 1:
                switch (tipo) {
                    case TileLoaderAndFactory.DOPPIACURVA_SINISTRASOPRA:
                        if (attraversamentiRealizzati.get(0) == Crossings.SINISTRASOPRA) {
                            return WaterDirection.TOP;
                        } else if(attraversamentiRealizzati.get(0) == Crossings.SOPRASINISTRA) {
                            return WaterDirection.LEFT;
                        }
                        else if(attraversamentiRealizzati.get(0) == Crossings.SOTTODESTRA) {
                            return WaterDirection.RIGHT;
                        }
                        else
                            return WaterDirection.BOTTOM;
                    case TileLoaderAndFactory.DOPPIACURVA_SINISTRASOTTO:
                        if (attraversamentiRealizzati.get(0) == Crossings.SINISTRASOTTO) {
                            return WaterDirection.BOTTOM;
                        } else if(attraversamentiRealizzati.get(0) == Crossings.SOTTOSINISTRA) {
                            return WaterDirection.LEFT;
                        }
                        else if(attraversamentiRealizzati.get(0) == Crossings.DESTRASOPRA) {
                            return WaterDirection.TOP;
                        }
                        else
                            return WaterDirection.RIGHT;
                    default:
                        return null;
                }
            case 2:
                switch (tipo) {
                    case TileLoaderAndFactory.DOPPIACURVA_SINISTRASOPRA:
                        if (attraversamentiRealizzati.get(1) == Crossings.SINISTRASOPRA) {
                            return WaterDirection.TOP;
                        } else if(attraversamentiRealizzati.get(1) == Crossings.SOPRASINISTRA) {
                            return WaterDirection.LEFT;
                        }
                        else if(attraversamentiRealizzati.get(1) == Crossings.SOTTODESTRA) {
                            return WaterDirection.RIGHT;
                        }
                        else
                            return WaterDirection.BOTTOM;
                    case TileLoaderAndFactory.DOPPIACURVA_SINISTRASOTTO:
                        if (attraversamentiRealizzati.get(1) == Crossings.SINISTRASOTTO) {
                            return WaterDirection.BOTTOM;
                        } else if(attraversamentiRealizzati.get(1) == Crossings.SOTTOSINISTRA) {
                            return WaterDirection.LEFT;
                        }
                        else if(attraversamentiRealizzati.get(1) == Crossings.DESTRASOPRA) {
                            return WaterDirection.TOP;
                        }
                        else
                            return WaterDirection.RIGHT;
                    default:
                        return null;
                }
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
                switch(tipo){
                    case TileLoaderAndFactory.DOPPIACURVA_SINISTRASOPRA:
                        if (attraversamentiRealizzati.get(0) == Crossings.SINISTRASOPRA | attraversamentiRealizzati.get(0) == Crossings.SOPRASINISTRA) {
                            return Arrays.asList(WaterDirection.BOTTOM, WaterDirection.RIGHT).toArray(new WaterDirection[2]);
                        } else {
                            return Arrays.asList(WaterDirection.LEFT, WaterDirection.TOP).toArray(new WaterDirection[2]);
                        }
                    case TileLoaderAndFactory.DOPPIACURVA_SINISTRASOTTO:
                        if (attraversamentiRealizzati.get(0) == Crossings.SINISTRASOTTO| attraversamentiRealizzati.get(0) == Crossings.SOTTOSINISTRA) {
                            return Arrays.asList(WaterDirection.TOP, WaterDirection.RIGHT).toArray(new WaterDirection[2]);
                        } else {
                            return Arrays.asList(WaterDirection.LEFT, WaterDirection.BOTTOM).toArray(new WaterDirection[2]);
                        }
                }
            case 2:
                return new WaterDirection[0];
            default:
                return new WaterDirection[0];
        }
    }

    @Override
    public void aggiungiAttraversamento(WaterDirection direzioneDiEntrata) {
        switch(tipo){
            case TileLoaderAndFactory.DOPPIACURVA_SINISTRASOPRA:
                switch(direzioneDiEntrata){
                    case BOTTOM:
                        attraversamentiRealizzati.add(Crossings.SOTTODESTRA);
                        timestampInizioAttraversamento = System.currentTimeMillis();
                        break;
                    case LEFT:
                        attraversamentiRealizzati.add(Crossings.SINISTRASOPRA);
                        timestampInizioAttraversamento = System.currentTimeMillis();
                        break;
                    case RIGHT:
                        attraversamentiRealizzati.add(Crossings.DESTRASOTTO);
                        timestampInizioAttraversamento = System.currentTimeMillis();
                        break;
                    case TOP:
                        attraversamentiRealizzati.add(Crossings.SOPRASINISTRA);
                        timestampInizioAttraversamento = System.currentTimeMillis();
                        break;
                }
                break;
            case TileLoaderAndFactory.DOPPIACURVA_SINISTRASOTTO:
                switch(direzioneDiEntrata){
                    case BOTTOM:
                        attraversamentiRealizzati.add(Crossings.SOTTOSINISTRA);
                        timestampInizioAttraversamento = System.currentTimeMillis();
                        break;
                    case LEFT:
                        attraversamentiRealizzati.add(Crossings.SINISTRASOTTO);
                        timestampInizioAttraversamento = System.currentTimeMillis();
                        break;
                    case RIGHT:
                        attraversamentiRealizzati.add(Crossings.DESTRASOPRA);
                        timestampInizioAttraversamento = System.currentTimeMillis();
                        break;
                    case TOP:
                        attraversamentiRealizzati.add(Crossings.SOPRADESTRA);
                        timestampInizioAttraversamento = System.currentTimeMillis();
                        break;
                }
                break;
        }

    }
}
