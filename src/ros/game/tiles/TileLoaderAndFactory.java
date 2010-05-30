package ros.game.tiles;

import java.util.Random;
import ros.game.state.ResourceManager;

/**
 * @author Ros
 *
 * Carica le immagini statiche dei Tile.
 *
 * Nuove istanze di Tile sono ritornate dal metodo getTile(int tipoTile)
 *
 */
public class TileLoaderAndFactory {

    public static final int CURVA_SINISTRASOPRA = 0;
    public static final int CURVA_DESTRASOPRA = 1;
    public static final int CURVA_SINISTRASOTTO = 2;
    public static final int CURVA_DESTRASOTTO = 3;
    public static final int ORIZZONTALE = 4;
    public static final int VERTICALE = 5;
    public static final int INCROCIO = 6;

    public static final int DOPPIACURVA_SINISTRASOPRA = 7;
    public static final int DOPPIACURVA_SINISTRASOTTO = 8;

    public static final int WALL_TILE = 9;
    public static final int EMPTY_TILE = 10;
    public static final int START_TILE_SINISTRA = 11;
    public static final int START_TILE_DESTRA = 12;
    public static final int START_TILE_SOPRA = 13;
    public static final int START_TILE_SOTTO = 14;
    public static final int END_TILE_SINISTRA = 15;
    public static final int END_TILE_DESTRA = 16;
    public static final int END_TILE_SOPRA = 17;
    public static final int END_TILE_SOTTO = 18;

    public static final int NUMERO_TIPI_TILE_PREVIEW = 9;
    public static final int NUMERO_TOTALE_TIPI_TILE = 19;
    
    //per generare uno startTile o endTile a caso
    Random r;

    //Start e End Tile
    StartTile start_sinistra;
    StartTile start_destra;
    StartTile start_sopra;
    StartTile start_sotto;
    EndTile end_sinistra;
    EndTile end_destra;
    EndTile end_sopra;
    EndTile end_sotto;
    //muro
    WallTile muro;
    //vuota
    EmptyTile vuota;
    //1 attraversamento possibile
    SingleCrossingTile curva_sinistrasopra;
    SingleCrossingTile curva_destrasopra;
    SingleCrossingTile curva_sinistrasotto;
    SingleCrossingTile curva_destrasotto;
    SingleCrossingTile single_orizzontale;
    SingleCrossingTile single_verticale;
    //2 attraversamenti possibili
    CrossTile incrocio;
    DoubleCrossingTile doppiacurva_sinistrasopra;
    DoubleCrossingTile doppiacurva_sinistrasotto;

    public TileLoaderAndFactory() {
        r = new Random();

        vuota = new EmptyTile();
        muro = new WallTile();
        start_sinistra = new StartTile(START_TILE_SINISTRA);
        start_destra = new StartTile(START_TILE_DESTRA);
        start_sopra = new StartTile(START_TILE_SOPRA);
        start_sotto = new StartTile(START_TILE_SOTTO);
        end_sinistra = new EndTile(END_TILE_SINISTRA);
        end_destra = new EndTile(END_TILE_DESTRA);
        end_sopra = new EndTile(END_TILE_SOPRA);
        end_sotto = new EndTile(END_TILE_SOTTO);
        curva_sinistrasopra = new SingleCrossingTile(CURVA_SINISTRASOPRA);
        curva_destrasopra = new SingleCrossingTile(CURVA_DESTRASOPRA);
        curva_sinistrasotto = new SingleCrossingTile(CURVA_SINISTRASOTTO);
        curva_destrasotto = new SingleCrossingTile(CURVA_DESTRASOTTO);
        single_orizzontale = new SingleCrossingTile(ORIZZONTALE);
        single_verticale = new SingleCrossingTile(VERTICALE);
        incrocio = new CrossTile();
        doppiacurva_sinistrasopra = new DoubleCrossingTile(DOPPIACURVA_SINISTRASOPRA);
        doppiacurva_sinistrasotto = new DoubleCrossingTile(DOPPIACURVA_SINISTRASOTTO);
    }

    public Tile getRandomStartTile(){
        int starttype = r.nextInt(4);
        switch(starttype){
            case 0:
                return getTile(START_TILE_DESTRA);
            case 1:
                return getTile(START_TILE_SINISTRA);
            case 2:
                return getTile(START_TILE_SOPRA);
            default:
                return getTile(START_TILE_SOTTO);
        }
    }

    public Tile getRandomEndTile(){
        int starttype = r.nextInt(4);
        switch(starttype){
            case 0:
                return getTile(END_TILE_DESTRA);
            case 1:
                return getTile(END_TILE_SINISTRA);
            case 2:
                return getTile(END_TILE_SOPRA);
            default:
                return getTile(END_TILE_SOTTO);
        }
    }

    public Tile getTile(int tipoTile){
        switch (tipoTile) {
            case EMPTY_TILE:
                return vuota.getNuovaIstanza();
            case WALL_TILE:
                return muro.getNuovaIstanza();
            case START_TILE_SINISTRA:
                return start_sinistra.getNuovaIstanza();
            case START_TILE_DESTRA:
                return start_destra.getNuovaIstanza();
            case START_TILE_SOPRA:
                return start_sopra.getNuovaIstanza();
            case START_TILE_SOTTO:
                return start_sotto.getNuovaIstanza();
            case END_TILE_SINISTRA:
                return end_sinistra.getNuovaIstanza();
            case END_TILE_DESTRA:
                return end_destra.getNuovaIstanza();
            case END_TILE_SOPRA:
                return end_sopra.getNuovaIstanza();
            case END_TILE_SOTTO:
                return end_sotto.getNuovaIstanza();
            case CURVA_SINISTRASOPRA:
                return curva_sinistrasopra.getNuovaIstanza();
            case CURVA_DESTRASOPRA:
                return curva_destrasopra.getNuovaIstanza();
            case CURVA_SINISTRASOTTO:
                return curva_sinistrasotto.getNuovaIstanza();
            case CURVA_DESTRASOTTO:
                return curva_destrasotto.getNuovaIstanza();
            case ORIZZONTALE:
                return single_orizzontale.getNuovaIstanza();
            case VERTICALE:
                return single_verticale.getNuovaIstanza();
            case DOPPIACURVA_SINISTRASOPRA:
                return doppiacurva_sinistrasopra.getNuovaIstanza();
            case DOPPIACURVA_SINISTRASOTTO:
                return doppiacurva_sinistrasotto.getNuovaIstanza();
            case INCROCIO:
                return incrocio.getNuovaIstanza();
            default:
                throw new NotExistingTileTypeException();
        }
    }

    public void loadResources(ResourceManager resourceManager) {
        vuota.loadResources(resourceManager);
        muro.loadResources(resourceManager);
        start_sinistra.loadResources(resourceManager);
        //start_destra.loadResources(resourceManager);
        //start_sopra.loadResources(resourceManager);
        //start_sotto.loadResources(resourceManager);
        end_sinistra.loadResources(resourceManager);
        //end_destra.loadResources(resourceManager);
        //end_sopra.loadResources(resourceManager);
        //end_sotto.loadResources(resourceManager);
        curva_sinistrasopra.loadResources(resourceManager);
        //curva_destrasopra.loadResources(resourceManager);
        //curva_sinistrasotto.loadResources(resourceManager);
        //curva_destrasotto.loadResources(resourceManager);
        //single_orizzontale.loadResources(resourceManager);
        //single_verticale.loadResources(resourceManager);
        incrocio.loadResources(resourceManager);
        doppiacurva_sinistrasopra.loadResources(resourceManager);
        //doppiacurva_sinistrasotto.loadResources(resourceManager);
    }
    
}
