/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ros.game.impl;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import ros.game.engine.Main;
import ros.game.impl.tiles.PosizioneTileVicina;
import ros.game.impl.tiles.Tile;
import ros.game.impl.tiles.TileLoaderAndFactory;
import ros.game.impl.tiles.WaterDirection;
import ros.game.engine.input.GameAction;

/**
 *
 * @author Ros
 */
public class Board implements ActionListener{

    public static final int COLONNE = 11;
    public static final int RIGHE = 8;

    //angolo in alto a sinistra ed in basso a destra della TUBATURA
    public static final int BOARD_BASEX = 25;
    public static final int BOARD_BASEY = 95;

    //dimensioni dei TILE
    public static final int TILE_SIZEX = 60;
    public static final int TILE_SIZEY = 60;
    
    private Tile[][] board;
    private PreviewTiles preview;
    private casellaTimesLevelsData datiTempoLivelli;
    private Timer timerCasella;

    /**
     * vettori di utilità per calcolare la posizione dei Tile di inizio e fine
     */
    private final Vector<Point> BORDOSUPERIORE = new Vector<Point>(Arrays.asList(new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0), new Point(4, 0), new Point(5, 0), new Point(6, 0), new Point(7, 0), new Point(8, 0), new Point(9, 0), new Point(COLONNE-1, 0) ));
    private final Vector<Point> BORDOINFERIORE = new Vector<Point>(Arrays.asList(new Point(0, RIGHE-1), new Point(1, RIGHE-1), new Point(2, RIGHE-1), new Point(3, RIGHE-1), new Point(4, RIGHE-1), new Point(5, RIGHE-1), new Point(6, RIGHE-1), new Point(7, RIGHE-1), new Point(8, RIGHE-1), new Point(9, RIGHE-1), new Point(COLONNE-1, RIGHE-1) ));
    private final Vector<Point> BORDOSINISTRO = new Vector<Point>(Arrays.asList(new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(0, 3), new Point(0, 4), new Point(0, 5), new Point(0, 6), new Point(0, RIGHE-1) ));
    private final Vector<Point> BORDODESTRO = new Vector<Point>(Arrays.asList(new Point(COLONNE-1, 0), new Point(COLONNE-1, 1), new Point(COLONNE-1, 2), new Point(COLONNE-1, 3), new Point(COLONNE-1, 4), new Point(COLONNE-1, 5), new Point(COLONNE-1, 6), new Point(COLONNE-1, RIGHE-1) ));

    /**
     * per randomizzare la scelta della posizione della Tile di inizio e di fine
     */
    private Random r;

    /**
     * coordinate tile corrente (Il tile corrente è il tile in attraversamento)
     */
    private Point tileCorrentePoint;
    //le coordinate del tile di inizio
    private Point startTilePoint;
    //le coordinate del tile di fine
    private Point endTilePoint;
    /**
     * Le coordinate del tile sotto il mouse (vale (-1,-1) se il mouse non è sopra nessun tile)
     */
    private Point tileSottoIlMousePoint;
    
    private final TileLoaderAndFactory tileGen;

    private int acceleredTimerDelay = 500;
    
    private GameAction casellaFinitaAction;

    private int punteggioUltimaTileAttraversata; // per memorizzare il punteggio delle tile attraversate
    public Board(TileLoaderAndFactory tileGenerator) {

        punteggioUltimaTileAttraversata = 0;
        
        timerCasella = new Timer(0, this);

        //make tmpbriefing and casellatime
        datiTempoLivelli = new casellaTimesLevelsData();

        this.preview = new PreviewTiles(tileGenerator);
        
        tileCorrentePoint = new Point(-1,-1);
        startTilePoint = new Point(-1,-1);
        endTilePoint = new Point(-1,-1);
        tileSottoIlMousePoint = new Point(-1,-1);
        
        r = new Random();
        
        this.tileGen = tileGenerator; //memorizzo il tile loader che non posso comunque utilizzare prima del loadResources
        board = new Tile[COLONNE][RIGHE];
    }

    public synchronized void draw(Graphics2D g) {
        //TODO non disegnare la tile sotto il mouse

        //disegno le tile
        for (int x = 0; x < COLONNE; x++) {
            for (int y = 0; y < RIGHE; y++) {
                if (tileCorrentePoint.x == x && tileCorrentePoint.y == y) {
                    board[x][y].draw(g, (x * TILE_SIZEX) + BOARD_BASEX, (y * TILE_SIZEY) + BOARD_BASEY, true, timerCasella.getDelay());
                } else {
                    board[x][y].draw(g, (x * TILE_SIZEX) + BOARD_BASEX, (y * TILE_SIZEY) + BOARD_BASEY, false, 0);
                }
            }
        }
        
        //disegno la tile sottoilmouse
        if (tileSottoIlMousePoint.x != -1 & tileSottoIlMousePoint.y != -1) {
            if (board[tileSottoIlMousePoint.x][tileSottoIlMousePoint.y].isReplaceable())//se la tile sotto il mouse è sostituibile
            {
                if(!tileSottoIlMousePoint.equals(tileCorrentePoint)){ //se non è il tile corrente
                    preview.drawNextTile(g, (tileSottoIlMousePoint.x * TILE_SIZEX) + BOARD_BASEX, (tileSottoIlMousePoint.y * TILE_SIZEY) + BOARD_BASEY);
                }
            }
        }
        //disegno il preview
        preview.draw(g);
    }

    /**
     * inizializza la Board ad ogni livello
     * 
     */
    public void initForStartingLevel(int level) {

        punteggioUltimaTileAttraversata = 0;

        //azzera tutti i tile
        for(int x = 0; x < COLONNE; x++){
            for(int y = 0; y < RIGHE; y++){
                board[x][y] = tileGen.getTile(TileLoaderAndFactory.EMPTY_TILE);
            }
        }

        tileCorrentePoint = new Point(-1,-1);
        
        Vector<Point> copiaDestro = (Vector<Point>) this.BORDODESTRO.clone();
        Vector<Point> copiaSinistro = (Vector<Point>) this.BORDOSINISTRO.clone();
        Vector<Point> copiaSuperiore = (Vector<Point>) this.BORDOSUPERIORE.clone();
        Vector<Point> copiaInferiore = (Vector<Point>) this.BORDOINFERIORE.clone();
        
        //imposta un punto di partenza
        Tile startTile = tileGen.getRandomStartTile();
        if(startTile.getTipo() == TileLoaderAndFactory.START_TILE_DESTRA){
            //se lo startTile va verso destra escludo il bordo destro
            startTilePoint = getRandomBoardPosition(this.BORDODESTRO);
        }
        else if(startTile.getTipo() == TileLoaderAndFactory.START_TILE_SINISTRA){
            startTilePoint = getRandomBoardPosition(this.BORDOSINISTRO);
        }
        else if(startTile.getTipo() == TileLoaderAndFactory.START_TILE_SOPRA){
            startTilePoint = getRandomBoardPosition(this.BORDOSUPERIORE);
        }
        else{
            startTilePoint = getRandomBoardPosition(this.BORDOINFERIORE);
        }
        this.board[startTilePoint.x][startTilePoint.y] = startTile;

        //imposta un punto di fine
        Tile endTile = tileGen.getRandomEndTile();
        if(endTile.getTipo() == TileLoaderAndFactory.END_TILE_DESTRA){
            //dovrò escludere il bordo + il punto di start + il punto di fronte a start
            copiaDestro.add(startTilePoint);
            Point[] dascartare = tileIntorno(startTilePoint);
            for(int i=0;i<dascartare.length;i++)
                copiaDestro.add(dascartare[i]);
            endTilePoint = getRandomBoardPosition(copiaDestro);
        }
        else if(endTile.getTipo() == TileLoaderAndFactory.END_TILE_SINISTRA){    
            copiaSinistro.add(startTilePoint);
            Point[] dascartare = tileIntorno(startTilePoint);
            for(int i=0;i<dascartare.length;i++)
                copiaSinistro.add(dascartare[i]);
            endTilePoint = getRandomBoardPosition(copiaSinistro);
        }
        else if(endTile.getTipo() == TileLoaderAndFactory.END_TILE_SOPRA){
            copiaSuperiore.add(startTilePoint);
            Point[] dascartare = tileIntorno(startTilePoint);
            for(int i=0;i<dascartare.length;i++)
                copiaSuperiore.add(dascartare[i]);
            endTilePoint = getRandomBoardPosition(copiaSuperiore);
        }
        else{
            copiaInferiore.add(startTilePoint);
            Point[] dascartare = tileIntorno(startTilePoint);
            for(int i=0;i<dascartare.length;i++)
                copiaInferiore.add(dascartare[i]);
            endTilePoint = getRandomBoardPosition(copiaInferiore);
        }
        this.board[endTilePoint.x][endTilePoint.y] = endTile;

        if(level >= 21 & level <= 30){
            Vector<Point> StartEndDaScartare = new Vector<Point>();
            StartEndDaScartare.add(startTilePoint);
            StartEndDaScartare.add(endTilePoint);
            Point[] dascartare = tileIntorno(startTilePoint);
            for(int i=0;i<dascartare.length;i++)
                StartEndDaScartare.add(dascartare[i]);
            dascartare = tileIntorno(endTilePoint);
            for(int i=0;i<dascartare.length;i++)
                StartEndDaScartare.add(dascartare[i]);
            
             //inserisci 2 muri
            for(int i=0; i<2; i++){
                //il muro può stare dovunque tranne che difronte a start e a end
                Point posizioneMuro = getRandomBoardPosition(StartEndDaScartare);
                this.board[posizioneMuro.x][posizioneMuro.y] = tileGen.getTile(TileLoaderAndFactory.WALL_TILE);
            }
        }
        preview.initForStartingLevel(level);

        Logger.getLogger(Main.LOGGER_NAME).log(Level.INFO, "EndTile: "+endTilePoint+"StartTile: "+startTilePoint);
    }
    
    /**
     * calcola la posizione di fronte ad un Tile data la direzione e gli indici del tile di partenza
     * 
     * @param indiceTileDiPartenza (il punto di partenza)
     * @param direzione (la direzione verso la quale calcolare)
     * @return
     */
    private Point tileDiFronte(Point indiceTileDiPartenza, WaterDirection direzione){
        if (indiceTileDiPartenza != null && direzione != null) {
            switch (direzione) {
                case BOTTOM:
                    return new Point(indiceTileDiPartenza.x, indiceTileDiPartenza.y+1);
                case LEFT:
                    return new Point(indiceTileDiPartenza.x-1, indiceTileDiPartenza.y);
                case RIGHT:
                    return new Point(indiceTileDiPartenza.x+1, indiceTileDiPartenza.y);
                case TOP:
                    return new Point(indiceTileDiPartenza.x, indiceTileDiPartenza.y-1);
            }
        }
        return null;
    }

    /**
     *
     * @param indiceTileDiPartenza
     * @return
     */
    private Point[] tileIntorno(Point indiceTileDiPartenza){
        Point[] returnvalues = new Point[8];
        if (indiceTileDiPartenza != null ) {
            returnvalues[PosizioneTileVicina.DESTRA.ordinal()]        = new Point(indiceTileDiPartenza.x+1, indiceTileDiPartenza.y);
            returnvalues[PosizioneTileVicina.SINISTRA.ordinal()]      = new Point(indiceTileDiPartenza.x-1, indiceTileDiPartenza.y);
            returnvalues[PosizioneTileVicina.SOPRA.ordinal()]         = new Point(indiceTileDiPartenza.x, indiceTileDiPartenza.y-1);
            returnvalues[PosizioneTileVicina.SOTTO.ordinal()]         = new Point(indiceTileDiPartenza.x, indiceTileDiPartenza.y+1);
            returnvalues[PosizioneTileVicina.SOPRADESTRA.ordinal()]   = new Point(indiceTileDiPartenza.x+1, indiceTileDiPartenza.y-1);
            returnvalues[PosizioneTileVicina.SOPRASINISTRA.ordinal()] = new Point(indiceTileDiPartenza.x-1, indiceTileDiPartenza.y-1);
            returnvalues[PosizioneTileVicina.SOTTODESTRA.ordinal()]   = new Point(indiceTileDiPartenza.x+1, indiceTileDiPartenza.y+1);
            returnvalues[PosizioneTileVicina.SOTTOSINISTRA.ordinal()] = new Point(indiceTileDiPartenza.x-1, indiceTileDiPartenza.y+1);

            return returnvalues;
        }
        return null;
    }

    /**
     *
     * @param puntiEsclusi (un array di punti da escludere)  null o un array vuoto -> non esclude nessun punto
     * @return un punto nella Board
     */
    private Point getRandomBoardPosition(Vector<Point> puntiEsclusi) {

        //se non devo escludere nessun punto
        if(puntiEsclusi == null | puntiEsclusi.size() == 0){
            return new Point(r.nextInt(COLONNE), r.nextInt(RIGHE));
        }
        else{
            //devo escludere i punti contenuti nell'array
            Point tmp = new Point();
            do{
             tmp.setLocation(r.nextInt(COLONNE), r.nextInt(RIGHE));

            }while(puntiEsclusi.contains(tmp));

            return tmp;
                
        }
    }

    /**
     * 
     * @param compCoord
     * @return l'indice della board o (-1,-1) se le coordinate sono fuori
     */
    private Point componetnCoordinateToBoardIndex(Point compCoord){
        if(compCoord == null)
            return new Point(-1,-1);
        if (compCoord.x >= BOARD_BASEX && compCoord.x <= BOARD_BASEX + COLONNE * TILE_SIZEX
                && compCoord.y >= BOARD_BASEY && compCoord.y <= BOARD_BASEY + RIGHE * TILE_SIZEY) {
            //il mouse è nella board: calcola l'indice del tile
            int xindex = -1;
            int yindex = -1;
            for (int x = 0; x < COLONNE; x++) {
                if (compCoord.x < BOARD_BASEX + TILE_SIZEX * (x + 1)) {
                    xindex = x;
                    break;
                }
            }
            for (int y = 0; y < RIGHE; y++) {
                if (compCoord.y < BOARD_BASEY + TILE_SIZEY * (y + 1)) {
                    yindex = y;
                    break;
                }
            }

            return new Point(xindex,yindex);
        }
        else{
            return new Point(-1,-1);
        }
    }

    /**
     * aggiorna il tile che sta sotto il mouse
     * 
     * @param mouseCoordinatePx
     */
    public void updateMousePosition(Point mouseCoordinate) {
        tileSottoIlMousePoint = this.componetnCoordinateToBoardIndex(mouseCoordinate);
    }

    public void sostituisciTileSePossibile(Point clikedMousePosition) {
        //è sostituibile?
        Point tileCliccata = this.componetnCoordinateToBoardIndex(clikedMousePosition);
        if (tileCliccata.x != -1) {
            if (board[tileCliccata.x][tileCliccata.y].isReplaceable())//se la tile sotto il mouse è sostituibile
            {
                if (!tileCliccata.equals(tileCorrentePoint)) { //se non è il tile corrente
                    board[tileCliccata.x][tileCliccata.y] = preview.getNextTile();
                }
            }
        }

    }

    public void startCasellaTimer(GameAction casellaFinitaAction, int levelIndex, boolean isAcceleredGame) {
        System.out.println("start casella timer - accelerato: "+ isAcceleredGame + " delay: "+ datiTempoLivelli.getCasellaTime(levelIndex));

        if(!isAcceleredGame){
            this.casellaFinitaAction = casellaFinitaAction;
            timerCasella.setDelay(datiTempoLivelli.getCasellaTime(levelIndex));
            timerCasella.start();
        }
        else{
            this.casellaFinitaAction = casellaFinitaAction;
            timerCasella.setDelay(acceleredTimerDelay);
            timerCasella.start();
        }
    }

    /**
     * evento "fine del tempo di casella"
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == timerCasella){
            System.out.println("tempo casella finito");
            System.out.flush();
            casellaFinitaAction.tap();
        }
    }

    /**
     * 
     * @return 
     */
    public boolean isEndTileReached(){
        if(tileCorrentePoint.x != -1){
            if(board[tileCorrentePoint.x][tileCorrentePoint.y].getTipo() == TileLoaderAndFactory.END_TILE_DESTRA |
                board[tileCorrentePoint.x][tileCorrentePoint.y].getTipo() == TileLoaderAndFactory.END_TILE_SINISTRA |
                board[tileCorrentePoint.x][tileCorrentePoint.y].getTipo() == TileLoaderAndFactory.END_TILE_SOPRA |
                board[tileCorrentePoint.x][tileCorrentePoint.y].getTipo() == TileLoaderAndFactory.END_TILE_SOTTO){
            stopTimerCasella();
            return true;
        }
        else
            return false;
        }
        else
            return false;
    }


    public int getPunteggioTileAttraversata(){
            return punteggioUltimaTileAttraversata;
    }

    /**
     * Posso andare al prossimo Tile?
     *
     * @return true o false
     */
    public boolean goToNextTile() {

        if (tileCorrentePoint.x == -1) { //se è il primo tile
            tileCorrentePoint = startTilePoint;
            board[tileCorrentePoint.x][tileCorrentePoint.y].aggiungiAttraversamento(null);
            punteggioUltimaTileAttraversata = board[tileCorrentePoint.x][tileCorrentePoint.y].getPunteggio();
            return true;
        } else {
            WaterDirection uscita = board[tileCorrentePoint.x][tileCorrentePoint.y].getdirezioneDiUscita();
            System.out.println("uscita"+uscita);
            //controllo se il tile verso la direzione di uscita è allineato
            Point difronte = new Point();

            difronte = this.tileDiFronte(tileCorrentePoint, uscita);
            System.out.println("difronte: "+difronte);
            if (difronte != null) {
                if (difronte.x >= 0 & difronte.x < COLONNE & difronte.y >= 0 & difronte.y < RIGHE) {
                    WaterDirection entrataDisponibile = WaterDirection.allineato(uscita, board[difronte.x][difronte.y].getdirezioniDiEntrataLibere());

                    System.out.println("entrata "+entrataDisponibile);
                    if (entrataDisponibile != null) {
                        tileCorrentePoint = difronte;
                        board[tileCorrentePoint.x][tileCorrentePoint.y].aggiungiAttraversamento(entrataDisponibile);
                        punteggioUltimaTileAttraversata = board[tileCorrentePoint.x][tileCorrentePoint.y].getPunteggio();
                        return true;
                    } else {
                        stopTimerCasella();
                        return false;
                    }
                } else { //sei andato fuori dalla board
                    stopTimerCasella();
                    return false;
                }
            } else {
                stopTimerCasella();
                return false;
            }
        }
    }

    public void saltaTilePreviewCorrente() {
        preview.getNextTile();
    }

    public void acceleraTimerCasella() {
        System.out.println("acceleraTimerCasella");
        timerCasella.stop();
        timerCasella.setDelay(acceleredTimerDelay);
        timerCasella.start();
    }

    private void stopTimerCasella(){
        System.out.println("Stop Timer Casella");
        timerCasella.stop();
    }
}
