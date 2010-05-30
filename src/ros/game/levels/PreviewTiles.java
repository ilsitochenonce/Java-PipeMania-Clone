/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ros.game.levels;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;
import ros.game.tiles.TileLoaderAndFactory;
import java.util.Random;
import java.util.Vector;
import ros.game.tiles.Tile;

/**
 *
 * @author Ros
 */
public class PreviewTiles implements AnteprimaTile {

    private Point posizione;
    
    /**
     * Numero di Tile in preview
     */
    private final int NUMTILES = 5;
    private final int SPACE = 2; //px
    /**
     * Contiene i Tile
     */
    private LinkedList<Tile> tiles;
    /**
     * Generatore di Tile per clonazione
     */
    private TileLoaderAndFactory tileGen;
    /**
     * Generatore di numeri casuali
     */
    private Random r;
    /**
     * tile non visualizzabili al livello corrente
     */
    private Vector tileNonPossibiliLivelloCorrente;

    /**
     *
     * @param tileGenerator
     */
    public PreviewTiles(TileLoaderAndFactory tileGenerator) {
        /**
         * Memorizzo il generatore di Tiles
         */
        this.tileGen = tileGenerator;
        /**
         * inizializzo il generatore di numeri casuali
         */
        r = new Random();
        posizione = new Point(Board.BOARD_BASEX + (Board.COLONNE*Board.TILE_SIZEX)+30,Board.BOARD_BASEY);
    }

    public synchronized void draw(Graphics2D g) {
        for(int i=0;i<NUMTILES;i++){
            tiles.get(i).draw(g, posizione.x, posizione.y +(i*Board.TILE_SIZEY) + (i*SPACE) + 100, false,0);
        }
    }

    private synchronized Tile generateRandomTile() {

        //genero un tipo di tile da 0 a 9
        int randomPossibleTile = r.nextInt(TileLoaderAndFactory.NUMERO_TIPI_TILE_PREVIEW);

        //controllo che il tile sia possibile a questo livello
        while (tileNonPossibiliLivelloCorrente.contains(randomPossibleTile)) {
            randomPossibleTile = r.nextInt(TileLoaderAndFactory.NUMERO_TIPI_TILE_PREVIEW);
        }

        //ritorno un clone del tile
        return tileGen.getTile(randomPossibleTile);
    }

    /**
     * Retrieves and removes the head of this queue
     * The metod insert new random tile at end
     * 
     * @param level
     * @return
     */
    public synchronized Tile getNextTile() {
        //genera un nuovo Tile Random
        tiles.addLast(generateRandomTile());
        //recupera il Tile da ritornare
        return tiles.removeFirst();
    }

    /**
     * Decide i Tile possibili del preview in base al livello.
     *
     * @param level
     */
    public synchronized void initForStartingLevel(int level) {
        if (level >= 1 & level <= 10) {
            tileNonPossibiliLivelloCorrente = new Vector();
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.EMPTY_TILE);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.START_TILE_DESTRA);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.START_TILE_SINISTRA);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.START_TILE_SOPRA);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.START_TILE_SOTTO);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.END_TILE_DESTRA);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.END_TILE_SINISTRA);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.END_TILE_SOPRA);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.END_TILE_SOTTO);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.WALL_TILE);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.DOPPIACURVA_SINISTRASOPRA);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.DOPPIACURVA_SINISTRASOTTO);

            // Genero le Tile del preview
            tiles = new LinkedList<Tile>();
            for (int i = 0; i < NUMTILES; i++) {
                tiles.addLast(generateRandomTile());
            }
        } else if (level >= 11 & level <= 30) {
            tileNonPossibiliLivelloCorrente = new Vector();
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.EMPTY_TILE);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.START_TILE_DESTRA);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.START_TILE_SINISTRA);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.START_TILE_SOPRA);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.START_TILE_SOTTO);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.END_TILE_DESTRA);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.END_TILE_SINISTRA);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.END_TILE_SOPRA);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.END_TILE_SOTTO);
            tileNonPossibiliLivelloCorrente.add(TileLoaderAndFactory.WALL_TILE);

            // Genero le Tile del preview
            tiles = new LinkedList<Tile>();
            for (int i = 0; i < NUMTILES; i++) {
                tiles.addLast(generateRandomTile());
            }

        } else {
            throw new LevelNotImplementedException(level);
        }
    }

    public synchronized void drawNextTile(Graphics2D g, int x, int y) {
        tiles.get(0).draw(g, x, y, false,0);
    }
    
}
