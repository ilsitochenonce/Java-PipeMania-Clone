/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.tiles;

import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import ros.game.Main;
import ros.game.ResourceManager;
import ros.game.util.ImageTools;

/**
 *
 * @author Ros
 */
class StartTile  implements Tile {

    /**
     * START_TILE_SINISTRA
     * START_TILE_DESTRA
     * START_TILE_SOPRA
     * START_TILE_SOTTO
     */
    private int tipo;

    /**
     * memorizza il timestamp dell'inizio di un attraversamento (per la gestione dell'animazione
     */
    private long timestampInizioAttraversamento;
    //variabili per l'animazione
    private int millisecondiTrascorsi;
    private int tempoFrame;
    private int numeroFrame; //da 1 a 10

    private boolean attraversata;

    //base
    private static Image imageBaseSinistra;
    private static Image imageBaseDestra;
    private static Image imageBaseSopra;
    private static Image imageBaseSotto;
    private static Image imageBaseSinistraAttraversata;
    private static Image imageBaseDestraAttraversata;
    private static Image imageBaseSopraAttraversata;
    private static Image imageBaseSottoAttraversata;

    //animazione
    private static Image versoDestra[];
    //le image seguenti vengono create ruotando la base
    private static Image versoSinistra[];
    private static Image versoSopra[];
    private static Image versoSotto[];

    public StartTile(int tipo) {
        this.tipo = tipo;
        this.attraversata = false;
    }

    public boolean isAttraversata(){
        return attraversata;
    }
    
    public void setAttraversata(){
        attraversata = true;
    }

    @Override
    public int getTipo(){
        return tipo;
    }

    @Override
    public void draw(Graphics2D g, int x, int y, boolean attiva, int tempoCasella) {
        if (!attiva) { //Senza animazione
            if(!attraversata){
                switch (tipo) {
                    case TileLoaderAndFactory.START_TILE_DESTRA:
                        g.drawImage(imageBaseDestra, x, y, null);
                        break;
                    case TileLoaderAndFactory.START_TILE_SINISTRA:
                        g.drawImage(imageBaseSinistra, x, y, null);
                        break;
                    case TileLoaderAndFactory.START_TILE_SOPRA:
                        g.drawImage(imageBaseSopra, x, y, null);
                        break;
                    case TileLoaderAndFactory.START_TILE_SOTTO:
                        g.drawImage(imageBaseSotto, x, y, null);
                        break;
                }
            }
            else{
                 switch (tipo) {
                    case TileLoaderAndFactory.START_TILE_DESTRA:
                        g.drawImage(imageBaseDestraAttraversata, x, y, null);
                        break;
                    case TileLoaderAndFactory.START_TILE_SINISTRA:
                        g.drawImage(imageBaseSinistraAttraversata, x, y, null);
                        break;
                    case TileLoaderAndFactory.START_TILE_SOPRA:
                        g.drawImage(imageBaseSopraAttraversata, x, y, null);
                        break;
                    case TileLoaderAndFactory.START_TILE_SOTTO:
                        g.drawImage(imageBaseSottoAttraversata, x, y, null);
                        break;
                }
            }
        }
        else{//animazione

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
            switch (tipo) {
                case TileLoaderAndFactory.START_TILE_DESTRA:
                    g.drawImage(versoDestra[numeroFrame-1], x, y, null);
                    break;
                case TileLoaderAndFactory.START_TILE_SINISTRA:
                    g.drawImage(versoSinistra[numeroFrame-1], x, y, null);
                    break;
                case TileLoaderAndFactory.START_TILE_SOPRA:
                    g.drawImage(versoSopra[numeroFrame-1], x, y, null);
                    break;
                case TileLoaderAndFactory.START_TILE_SOTTO:
                    g.drawImage(versoSotto[numeroFrame-1], x, y, null);
                    break;
            }

        }
       
    }

    public Tile getNuovaIstanza() {
        if (imageBaseSinistra != null) {
            return new StartTile(this.tipo);
        } else {
            Logger.getLogger(Main.LOGGER_NAME).log(Level.SEVERE, "Creazione nuova istanza di Start Tile fallita - chiamare prima loadResources()");
            return null;
        }
    }

    @Override
    public boolean isReplaceable() {
        return false;
    }

    @Override
    public int getPunteggio() {
        if (attraversata)
            return 20;
        else
           return 0;
    }

    @Override
    public void loadResources(ResourceManager resourceManager) {
        imageBaseSinistra = resourceManager.loadImage("caselle/START_SINISTRA.png");
        imageBaseSinistraAttraversata = resourceManager.loadImage("caselle/START_SINISTRA_ATTRAVERSATA.png");

        //creo la trasformazione da applicare
        AffineTransform at =  AffineTransform.getRotateInstance(Math.PI / 2.0, imageBaseSinistra.getWidth(null)/2.0, imageBaseSinistra.getHeight(null)/2.0);
        AffineTransformOp atRotazione = new AffineTransformOp( at, AffineTransformOp.TYPE_BICUBIC );

        //creo le altre immagini per rotazione
	imageBaseSopra = atRotazione.filter(ImageTools.toBufferedImage(imageBaseSinistra), null);
        imageBaseDestra = atRotazione.filter(ImageTools.toBufferedImage(imageBaseSopra), null );
        imageBaseSotto = atRotazione.filter(ImageTools.toBufferedImage(imageBaseDestra), null );

        imageBaseSopraAttraversata = atRotazione.filter(ImageTools.toBufferedImage(imageBaseSinistraAttraversata), null );
        imageBaseDestraAttraversata = atRotazione.filter(ImageTools.toBufferedImage(imageBaseSopraAttraversata), null );
        imageBaseSottoAttraversata = atRotazione.filter(ImageTools.toBufferedImage(imageBaseDestraAttraversata), null );

        //animazione
        versoSinistra  = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        versoDestra  = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        versoSopra  = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];
        versoSotto  = new Image[Tile.NUMERO_FRAME_ANIMAZIONE];

        for(int i=1;i<=Tile.NUMERO_FRAME_ANIMAZIONE;i++){
            versoSinistra[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/START4_"+i+".png");
        }

        //rotazione
        for(int i=1;i<=Tile.NUMERO_FRAME_ANIMAZIONE;i++){
            versoSopra[i-1] = atRotazione.filter(ImageTools.toBufferedImage(versoSinistra[i-1]), null );
            versoDestra[i-1] = atRotazione.filter(ImageTools.toBufferedImage(versoSopra[i-1]), null );
            versoSotto[i-1] = atRotazione.filter(ImageTools.toBufferedImage(versoDestra[i-1]), null );
        }

        //test();
    }
/**
     * Test function
     *
     * crea una finestra con le immagini del tile
     *
     * @return
     */
    private void test(){
        JFrame x = new JFrame();
        x.getContentPane().setLayout(new GridLayout(8, 6));
        JLabel l0 = new JLabel(new ImageIcon(imageBaseSinistra));
        JLabel l1 = new JLabel(new ImageIcon(imageBaseDestra));
        JLabel l2 = new JLabel(new ImageIcon(imageBaseSopra));
        JLabel l3 = new JLabel(new ImageIcon(imageBaseSotto));
        JLabel l4 = new JLabel(new ImageIcon(imageBaseSinistraAttraversata));
        JLabel l5 = new JLabel(new ImageIcon(imageBaseDestraAttraversata));
        JLabel l6 = new JLabel(new ImageIcon(imageBaseSopraAttraversata));
        JLabel l7 = new JLabel(new ImageIcon(imageBaseSottoAttraversata));
        JLabel l8 = new JLabel(new ImageIcon(versoDestra[0]));
        JLabel l9 = new JLabel(new ImageIcon(versoDestra[1]));
        JLabel l10 = new JLabel(new ImageIcon(versoDestra[2]));
        JLabel l11 = new JLabel(new ImageIcon(versoDestra[3]));
        JLabel l12 = new JLabel(new ImageIcon(versoDestra[4]));
        JLabel l13 = new JLabel(new ImageIcon(versoDestra[5]));
        JLabel l14 = new JLabel(new ImageIcon(versoDestra[6]));
        JLabel l15 = new JLabel(new ImageIcon(versoDestra[7]));
        JLabel l16 = new JLabel(new ImageIcon(versoDestra[8]));
        JLabel l17 = new JLabel(new ImageIcon(versoDestra[9]));
        JLabel l18 = new JLabel(new ImageIcon(versoSinistra[0]));
        JLabel l19 = new JLabel(new ImageIcon(versoSinistra[1]));
        JLabel l20 = new JLabel(new ImageIcon(versoSinistra[2]));
        JLabel l21 = new JLabel(new ImageIcon(versoSinistra[3]));
        JLabel l22 = new JLabel(new ImageIcon(versoSinistra[4]));
        JLabel l23 = new JLabel(new ImageIcon(versoSinistra[5]));
        JLabel l24 = new JLabel(new ImageIcon(versoSinistra[6]));
        JLabel l25 = new JLabel(new ImageIcon(versoSinistra[7]));
        JLabel l26 = new JLabel(new ImageIcon(versoSinistra[8]));
        JLabel l27 = new JLabel(new ImageIcon(versoSinistra[9]));
        JLabel l28 = new JLabel(new ImageIcon(versoSopra[0]));
        JLabel l29 = new JLabel(new ImageIcon(versoSopra[1]));
        JLabel l30 = new JLabel(new ImageIcon(versoSopra[2]));
        JLabel l31 = new JLabel(new ImageIcon(versoSopra[3]));
        JLabel l32 = new JLabel(new ImageIcon(versoSopra[4]));
        JLabel l33 = new JLabel(new ImageIcon(versoSopra[5]));
        JLabel l34 = new JLabel(new ImageIcon(versoSopra[6]));
        JLabel l35 = new JLabel(new ImageIcon(versoSopra[7]));
        JLabel l36 = new JLabel(new ImageIcon(versoSopra[8]));
        JLabel l37 = new JLabel(new ImageIcon(versoSopra[9]));
        JLabel l38 = new JLabel(new ImageIcon(versoSotto[0]));
        JLabel l39 = new JLabel(new ImageIcon(versoSotto[1]));
        JLabel l40 = new JLabel(new ImageIcon(versoSotto[2]));
        JLabel l41 = new JLabel(new ImageIcon(versoSotto[3]));
        JLabel l42 = new JLabel(new ImageIcon(versoSotto[4]));
        JLabel l43 = new JLabel(new ImageIcon(versoSotto[5]));
        JLabel l44 = new JLabel(new ImageIcon(versoSotto[6]));
        JLabel l45 = new JLabel(new ImageIcon(versoSotto[7]));
        JLabel l46 = new JLabel(new ImageIcon(versoSotto[8]));
        JLabel l47 = new JLabel(new ImageIcon(versoSotto[9]));

        x.getContentPane().add(l0);
        x.getContentPane().add(l1);
        x.getContentPane().add(l2);
        x.getContentPane().add(l3);
        x.getContentPane().add(l4);
        x.getContentPane().add(l5);
        x.getContentPane().add(l6);
        x.getContentPane().add(l7);
        x.getContentPane().add(l8);
        x.getContentPane().add(l9);
        x.getContentPane().add(l10);
        x.getContentPane().add(l11);
        x.getContentPane().add(l12);
        x.getContentPane().add(l13);
        x.getContentPane().add(l14);
        x.getContentPane().add(l15);
        x.getContentPane().add(l16);
        x.getContentPane().add(l17);
        x.getContentPane().add(l18);
        x.getContentPane().add(l19);
        x.getContentPane().add(l20);
        x.getContentPane().add(l21);
        x.getContentPane().add(l22);
        x.getContentPane().add(l23);
        x.getContentPane().add(l24);
        x.getContentPane().add(l25);
        x.getContentPane().add(l26);
        x.getContentPane().add(l27);
        x.getContentPane().add(l28);
        x.getContentPane().add(l29);
        x.getContentPane().add(l30);
        x.getContentPane().add(l31);
        x.getContentPane().add(l32);
        x.getContentPane().add(l33);
        x.getContentPane().add(l34);
        x.getContentPane().add(l35);
        x.getContentPane().add(l36);
        x.getContentPane().add(l37);
        x.getContentPane().add(l38);
        x.getContentPane().add(l39);
        x.getContentPane().add(l40);
        x.getContentPane().add(l41);
        x.getContentPane().add(l42);
        x.getContentPane().add(l43);
        x.getContentPane().add(l44);
        x.getContentPane().add(l45);
        x.getContentPane().add(l46);
        x.getContentPane().add(l47);
        x.setBounds(100, 100, 800, 600);
        x.setVisible(true);
    }

    @Override
    public WaterDirection getdirezioneDiUscita() {
        switch(tipo){
            case TileLoaderAndFactory.START_TILE_SINISTRA:
                return WaterDirection.LEFT;
            case TileLoaderAndFactory.START_TILE_DESTRA:
                return WaterDirection.RIGHT;
            case TileLoaderAndFactory.START_TILE_SOPRA:
                return WaterDirection.TOP;
            case TileLoaderAndFactory.START_TILE_SOTTO:
                return WaterDirection.BOTTOM;
            default:
                return null;
        }
    }

    @Override
    public WaterDirection[] getdirezioniDiEntrataLibere() {
        return new WaterDirection[0];
    }

    @Override
    public void aggiungiAttraversamento(WaterDirection attraversamento) {
        this.attraversata = true;
        timestampInizioAttraversamento = System.currentTimeMillis();
    }
}
