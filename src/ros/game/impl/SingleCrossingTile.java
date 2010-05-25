package ros.game.impl;

import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import ros.game.Main;
import ros.game.state.ResourceManager;
import ros.game.util.ImageTools;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author Ros
 */
class SingleCrossingTile extends Tile {

    /**
     * CURVA_SINISTRASOPRA 
     * CURVA_DESTRASOPRA
     * CURVA_SINISTRASOTTO
     * CURVA_DESTRASOTTO
     * ORIZZONTALE
     * VERTICALE
     */
    private int tipo;

    private Crossings attraversamento;

    //base
    private static Image baseCurvaSinistraSopra;
    private static Image baseCurvaSinistraSopraAttraversata;
    private static Image baseVerticale;
    private static Image baseVerticaleAttraversata;

    private static Image baseCurvaSinistraSotto;
    private static Image baseCurvaSinistraSottoAttraversata;
    private static Image baseCurvaDestraSopra;
    private static Image baseCurvaDestraSopraAttraversata;
    private static Image baseCurvaDestraSotto;
    private static Image baseCurvaDestraSottoAttraversata;
    private static Image baseOrizzontale;
    private static Image baseOrizzontaleAttraversata;
    
    //animazione
    private static Image orizzontaleVersoDestra[];
    private static Image orizzontaleVersoSinistra[];
    private static Image verticaleVersoSopra[];
    private static Image verticaleVersoSotto[];

    private static Image curvaSinistraSopraVersoSopra[];
    private static Image curvaSinistraSopraVersoSinistra[];
    
    private static Image curvaSinistraSottoVersoSotto[];
    private static Image curvaSinistraSottoVersoSinistra[];
    private static Image curvaDestraSopraVersoSopra[];
    private static Image curvaDestraSopraVersoDestra[];
    private static Image curvaDestraSottoVersoSotto[];
    private static Image curvaDestraSottoVersoDestra[];

    public SingleCrossingTile(int tipo) {
        this.tipo = tipo;
        this.attraversamento = null;
    }

    @Override
    public int getTipo(){
        return tipo;
    }
    
    @Override
    public void draw(Graphics2D g, int t, int x, int y, boolean attiva) {
         if (!attiva) { //Senza animazione
            if(attraversamento == null){
                switch (tipo) {
                    case TileLoaderAndFactory.CURVA_DESTRASOPRA:
                        g.drawImage(baseCurvaDestraSopra, x, y, null);
                        break;
                    case TileLoaderAndFactory.CURVA_DESTRASOTTO:
                        g.drawImage(baseCurvaDestraSotto, x, y, null);
                        break;
                    case TileLoaderAndFactory.CURVA_SINISTRASOPRA:
                        g.drawImage(baseCurvaSinistraSopra, x, y, null);
                        break;
                    case TileLoaderAndFactory.CURVA_SINISTRASOTTO:
                        g.drawImage(baseCurvaSinistraSotto, x, y, null);
                        break;
                    case TileLoaderAndFactory.ORIZZONTALE:
                        g.drawImage(baseOrizzontale, x, y, null);
                        break;
                    case TileLoaderAndFactory.VERTICALE:
                        g.drawImage(baseVerticale, x, y, null);
                        break;
                }    
            }
            else {
                switch (tipo) {
                    case TileLoaderAndFactory.CURVA_DESTRASOPRA:
                        g.drawImage(baseCurvaDestraSopraAttraversata, x, y, null);
                        break;
                    case TileLoaderAndFactory.CURVA_DESTRASOTTO:
                        g.drawImage(baseCurvaDestraSottoAttraversata, x, y, null);
                        break;
                    case TileLoaderAndFactory.CURVA_SINISTRASOPRA:
                        g.drawImage(baseCurvaSinistraSopraAttraversata, x, y, null);
                        break;
                    case TileLoaderAndFactory.CURVA_SINISTRASOTTO:
                        g.drawImage(baseCurvaSinistraSottoAttraversata, x, y, null);
                        break;
                    case TileLoaderAndFactory.ORIZZONTALE:
                        g.drawImage(baseOrizzontaleAttraversata, x, y, null);
                        break;
                    case TileLoaderAndFactory.VERTICALE:
                        g.drawImage(baseVerticaleAttraversata, x, y, null);
                        break;
                }
            }
         }else { //DRAW ANIMATION TILES
             throw new NotImplementedException();
        }
    }

    @Override
    public boolean isReplaceable() {
        if(attraversamento != null)
            return false;
        else
            return true;
    }

    @Override
    public int getPunteggio() {
        if (attraversamento != null)
            return 20;
        else
           return 0;
    }

    @Override
    public void loadResources(ResourceManager resourceManager) {
        baseCurvaSinistraSopra = resourceManager.loadImage("caselle/CURVA_SINISTRASOPRA.png");
        baseCurvaSinistraSopraAttraversata = resourceManager.loadImage("caselle/CURVA_SINISTRASOPRA_ATTRAVERSATA.png");
        baseVerticale = resourceManager.loadImage("caselle/VERTICALE.png");
        baseVerticaleAttraversata = resourceManager.loadImage("caselle/VERTICALE_ATTRAVERSATA.png");
        
        //creo la trasformazione da applicare
        AffineTransform at =  AffineTransform.getRotateInstance(Math.PI / 2.0, baseCurvaSinistraSopra.getWidth(null)/2.0,
                baseCurvaSinistraSopra.getHeight(null)/2.0);
        AffineTransformOp atRotazione = new AffineTransformOp( at, AffineTransformOp.TYPE_BICUBIC );

        //creo le altre immagini per rotazione
	baseCurvaDestraSopra = atRotazione.filter(ImageTools.toBufferedImage(baseCurvaSinistraSopra), null);
        baseCurvaDestraSotto = atRotazione.filter(ImageTools.toBufferedImage(baseCurvaDestraSopra), null);
        baseCurvaSinistraSotto = atRotazione.filter(ImageTools.toBufferedImage(baseCurvaDestraSotto), null);
        baseCurvaDestraSopraAttraversata = atRotazione.filter(ImageTools.toBufferedImage(baseCurvaSinistraSopraAttraversata), null);
        baseCurvaDestraSottoAttraversata = atRotazione.filter(ImageTools.toBufferedImage(baseCurvaDestraSopraAttraversata), null);
        baseCurvaSinistraSottoAttraversata = atRotazione.filter(ImageTools.toBufferedImage(baseCurvaDestraSottoAttraversata), null);
        baseOrizzontale = atRotazione.filter(ImageTools.toBufferedImage(baseVerticale), null);
        baseOrizzontaleAttraversata = atRotazione.filter(ImageTools.toBufferedImage(baseVerticaleAttraversata), null);
        
        //ANIMAZIONE
        orizzontaleVersoDestra = new Image[NUMERO_FRAME_ANIMAZIONE];
        orizzontaleVersoSinistra = new Image[NUMERO_FRAME_ANIMAZIONE];
        verticaleVersoSopra = new Image[NUMERO_FRAME_ANIMAZIONE];
        verticaleVersoSotto = new Image[NUMERO_FRAME_ANIMAZIONE];

        for(int i=1;i<=Tile.NUMERO_FRAME_ANIMAZIONE;i++){
            verticaleVersoSopra[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/VERTICALE_DA_BOTTOM_"+i+".png");
            verticaleVersoSotto[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/VERTICALE_DA_TOP_"+i+".png");
        }
        for(int i=1;i<=Tile.NUMERO_FRAME_ANIMAZIONE;i++){
            orizzontaleVersoDestra[i-1] = atRotazione.filter(ImageTools.toBufferedImage(verticaleVersoSopra[i-1]), null );
            orizzontaleVersoSinistra[i-1] = atRotazione.filter(ImageTools.toBufferedImage(verticaleVersoSotto[i-1]), null );
        }

        curvaSinistraSopraVersoSopra = new Image[NUMERO_FRAME_ANIMAZIONE];
        curvaSinistraSopraVersoSinistra = new Image[NUMERO_FRAME_ANIMAZIONE];

        for(int i=1;i<=Tile.NUMERO_FRAME_ANIMAZIONE;i++){
            curvaSinistraSopraVersoSopra[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/CURVA4_DA_LEFT_"+i+".png");
            curvaSinistraSopraVersoSinistra[i-1] = resourceManager.loadImage("caselle/ANIMATIONS/CURVA4_DA_TOP_"+i+".png");
        }

        curvaSinistraSottoVersoSotto = new Image[NUMERO_FRAME_ANIMAZIONE];
        curvaSinistraSottoVersoSinistra = new Image[NUMERO_FRAME_ANIMAZIONE];
        curvaDestraSopraVersoSopra = new Image[NUMERO_FRAME_ANIMAZIONE];
        curvaDestraSopraVersoDestra = new Image[NUMERO_FRAME_ANIMAZIONE];
        curvaDestraSottoVersoSotto = new Image[NUMERO_FRAME_ANIMAZIONE];
        curvaDestraSottoVersoDestra = new Image[NUMERO_FRAME_ANIMAZIONE];

        for(int i=1;i<=Tile.NUMERO_FRAME_ANIMAZIONE;i++){
            curvaDestraSopraVersoDestra[i-1] = atRotazione.filter(ImageTools.toBufferedImage(curvaSinistraSopraVersoSopra[i-1]), null );
            curvaDestraSopraVersoSopra[i-1] = atRotazione.filter(ImageTools.toBufferedImage(curvaSinistraSopraVersoSinistra[i-1]), null );
            curvaDestraSottoVersoSotto[i-1] = atRotazione.filter(ImageTools.toBufferedImage(curvaDestraSopraVersoDestra[i-1]), null );
            curvaDestraSottoVersoDestra[i-1] = atRotazione.filter(ImageTools.toBufferedImage(curvaDestraSopraVersoSopra[i-1]), null );
            curvaSinistraSottoVersoSotto[i-1] = atRotazione.filter(ImageTools.toBufferedImage(curvaDestraSottoVersoDestra[i-1]), null );
            curvaSinistraSottoVersoSinistra[i-1] = atRotazione.filter(ImageTools.toBufferedImage(curvaDestraSottoVersoSotto[i-1]), null );
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
        x.getContentPane().setLayout(new GridLayout(7, 13));
        JLabel l0 = new JLabel(new ImageIcon(baseCurvaDestraSopra));
        JLabel l1 = new JLabel(new ImageIcon(baseCurvaDestraSopraAttraversata));
        JLabel l2 = new JLabel(new ImageIcon(baseCurvaDestraSotto));
        JLabel l3 = new JLabel(new ImageIcon(baseCurvaDestraSottoAttraversata));
        JLabel l4 = new JLabel(new ImageIcon(baseCurvaSinistraSopra));
        JLabel l5 = new JLabel(new ImageIcon(baseCurvaSinistraSopraAttraversata));
        JLabel l6 = new JLabel(new ImageIcon(baseCurvaSinistraSotto));
        JLabel l7 = new JLabel(new ImageIcon(baseCurvaSinistraSottoAttraversata));
        JLabel l8 = new JLabel(new ImageIcon(baseOrizzontale));
        JLabel l9 = new JLabel(new ImageIcon(baseOrizzontaleAttraversata));
        JLabel l10 = new JLabel(new ImageIcon(baseVerticale));
        JLabel l11 = new JLabel(new ImageIcon(baseVerticaleAttraversata));
        JLabel l12 = new JLabel(new ImageIcon(curvaDestraSopraVersoDestra[0]));
        JLabel l13 = new JLabel(new ImageIcon(curvaDestraSopraVersoDestra[1]));
        JLabel l14 = new JLabel(new ImageIcon(curvaDestraSopraVersoDestra[2]));
        JLabel l15 = new JLabel(new ImageIcon(curvaDestraSopraVersoDestra[3]));
        JLabel l16 = new JLabel(new ImageIcon(curvaDestraSopraVersoDestra[4]));
        JLabel l17 = new JLabel(new ImageIcon(curvaDestraSopraVersoDestra[5]));
        JLabel l17a = new JLabel(new ImageIcon(curvaDestraSopraVersoDestra[6]));
        JLabel l17b = new JLabel(new ImageIcon(curvaDestraSopraVersoDestra[7]));
        JLabel l17c = new JLabel(new ImageIcon(curvaDestraSopraVersoDestra[8]));
        JLabel l17d = new JLabel(new ImageIcon(curvaDestraSopraVersoDestra[9]));
        JLabel l18 = new JLabel(new ImageIcon(curvaDestraSopraVersoSopra[0]));
        JLabel l19 = new JLabel(new ImageIcon(curvaDestraSopraVersoSopra[1]));
        JLabel l20 = new JLabel(new ImageIcon(curvaDestraSopraVersoSopra[2]));
        JLabel l21 = new JLabel(new ImageIcon(curvaDestraSopraVersoSopra[3]));
        JLabel l22 = new JLabel(new ImageIcon(curvaDestraSopraVersoSopra[4]));
        JLabel l23 = new JLabel(new ImageIcon(curvaDestraSopraVersoSopra[5]));
        JLabel l24 = new JLabel(new ImageIcon(curvaDestraSopraVersoSopra[6]));
        JLabel l25 = new JLabel(new ImageIcon(curvaDestraSopraVersoSopra[7]));
        JLabel l26 = new JLabel(new ImageIcon(curvaDestraSopraVersoSopra[8]));
        JLabel l27 = new JLabel(new ImageIcon(curvaDestraSopraVersoSopra[9]));
        JLabel l28 = new JLabel(new ImageIcon(curvaDestraSottoVersoDestra[0]));
        JLabel l29 = new JLabel(new ImageIcon(curvaDestraSottoVersoDestra[1]));
        JLabel l30 = new JLabel(new ImageIcon(curvaDestraSottoVersoDestra[2]));
        JLabel l31 = new JLabel(new ImageIcon(curvaDestraSottoVersoDestra[3]));
        JLabel l32 = new JLabel(new ImageIcon(curvaDestraSottoVersoDestra[4]));
        JLabel l33 = new JLabel(new ImageIcon(curvaDestraSottoVersoDestra[5]));
        JLabel l34 = new JLabel(new ImageIcon(curvaDestraSottoVersoDestra[6]));
        JLabel l35 = new JLabel(new ImageIcon(curvaDestraSottoVersoDestra[7]));
        JLabel l36 = new JLabel(new ImageIcon(curvaDestraSottoVersoDestra[8]));
        JLabel l37 = new JLabel(new ImageIcon(curvaDestraSottoVersoDestra[9]));
        JLabel l38 = new JLabel(new ImageIcon(curvaDestraSottoVersoSotto[0]));
        JLabel l39 = new JLabel(new ImageIcon(curvaDestraSottoVersoSotto[1]));
        JLabel l40 = new JLabel(new ImageIcon(curvaDestraSottoVersoSotto[2]));
        JLabel l41 = new JLabel(new ImageIcon(curvaDestraSottoVersoSotto[3]));
        JLabel l42 = new JLabel(new ImageIcon(curvaDestraSottoVersoSotto[4]));
        JLabel l43 = new JLabel(new ImageIcon(curvaDestraSottoVersoSotto[5]));
        JLabel l44 = new JLabel(new ImageIcon(curvaDestraSottoVersoSotto[6]));
        JLabel l45 = new JLabel(new ImageIcon(curvaDestraSottoVersoSotto[7]));
        JLabel l46 = new JLabel(new ImageIcon(curvaDestraSottoVersoSotto[8]));
        JLabel l47 = new JLabel(new ImageIcon(curvaDestraSottoVersoSotto[9]));
        JLabel l38a = new JLabel(new ImageIcon(curvaSinistraSopraVersoSinistra[0]));
        JLabel l39a = new JLabel(new ImageIcon(curvaSinistraSopraVersoSinistra[1]));
        JLabel l40a = new JLabel(new ImageIcon(curvaSinistraSopraVersoSinistra[2]));
        JLabel l41a = new JLabel(new ImageIcon(curvaSinistraSopraVersoSinistra[3]));
        JLabel l42a = new JLabel(new ImageIcon(curvaSinistraSopraVersoSinistra[4]));
        JLabel l43a = new JLabel(new ImageIcon(curvaSinistraSopraVersoSinistra[5]));
        JLabel l44a = new JLabel(new ImageIcon(curvaSinistraSopraVersoSinistra[6]));
        JLabel l45a = new JLabel(new ImageIcon(curvaSinistraSopraVersoSinistra[7]));
        JLabel l46a = new JLabel(new ImageIcon(curvaSinistraSopraVersoSinistra[8]));
        JLabel l47a = new JLabel(new ImageIcon(curvaSinistraSopraVersoSinistra[9]));
        JLabel l38b = new JLabel(new ImageIcon(curvaSinistraSopraVersoSopra[0]));
        JLabel l39b = new JLabel(new ImageIcon(curvaSinistraSopraVersoSopra[1]));
        JLabel l40b = new JLabel(new ImageIcon(curvaSinistraSopraVersoSopra[2]));
        JLabel l41b = new JLabel(new ImageIcon(curvaSinistraSopraVersoSopra[3]));
        JLabel l42b = new JLabel(new ImageIcon(curvaSinistraSopraVersoSopra[4]));
        JLabel l43b = new JLabel(new ImageIcon(curvaSinistraSopraVersoSopra[5]));
        JLabel l44b = new JLabel(new ImageIcon(curvaSinistraSopraVersoSopra[6]));
        JLabel l45b = new JLabel(new ImageIcon(curvaSinistraSopraVersoSopra[7]));
        JLabel l46b = new JLabel(new ImageIcon(curvaSinistraSopraVersoSopra[8]));
        JLabel l47b = new JLabel(new ImageIcon(curvaSinistraSopraVersoSopra[9]));
        JLabel l38c = new JLabel(new ImageIcon(curvaSinistraSottoVersoSinistra[0]));
        JLabel l39c = new JLabel(new ImageIcon(curvaSinistraSottoVersoSinistra[1]));
        JLabel l40c = new JLabel(new ImageIcon(curvaSinistraSottoVersoSinistra[2]));
        JLabel l41c = new JLabel(new ImageIcon(curvaSinistraSottoVersoSinistra[3]));
        JLabel l42c = new JLabel(new ImageIcon(curvaSinistraSottoVersoSinistra[4]));
        JLabel l43c = new JLabel(new ImageIcon(curvaSinistraSottoVersoSinistra[5]));
        JLabel l44c = new JLabel(new ImageIcon(curvaSinistraSottoVersoSinistra[6]));
        JLabel l45c = new JLabel(new ImageIcon(curvaSinistraSottoVersoSinistra[7]));
        JLabel l46c = new JLabel(new ImageIcon(curvaSinistraSottoVersoSinistra[8]));
        JLabel l47c = new JLabel(new ImageIcon(curvaSinistraSottoVersoSinistra[9]));
        JLabel l38d = new JLabel(new ImageIcon(curvaSinistraSottoVersoSotto[0]));
        JLabel l39d = new JLabel(new ImageIcon(curvaSinistraSottoVersoSotto[1]));
        JLabel l40d = new JLabel(new ImageIcon(curvaSinistraSottoVersoSotto[2]));
        JLabel l41d = new JLabel(new ImageIcon(curvaSinistraSottoVersoSotto[3]));
        JLabel l42d = new JLabel(new ImageIcon(curvaSinistraSottoVersoSotto[4]));
        JLabel l43d = new JLabel(new ImageIcon(curvaSinistraSottoVersoSotto[5]));
        JLabel l44d = new JLabel(new ImageIcon(curvaSinistraSottoVersoSotto[6]));
        JLabel l45d = new JLabel(new ImageIcon(curvaSinistraSottoVersoSotto[7]));
        JLabel l46d = new JLabel(new ImageIcon(curvaSinistraSottoVersoSotto[8]));
        JLabel l47d = new JLabel(new ImageIcon(curvaSinistraSottoVersoSotto[9]));

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
        x.getContentPane().add(l17a);
        x.getContentPane().add(l17b);
        x.getContentPane().add(l17c);
        x.getContentPane().add(l17d);
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
        x.getContentPane().add( l38a);
        x.getContentPane().add( l39a);
        x.getContentPane().add(l40a);
        x.getContentPane().add( l41a );
        x.getContentPane().add( l42a );
        x.getContentPane().add( l43a);
        x.getContentPane().add( l44a );
        x.getContentPane().add( l45a);
        x.getContentPane().add( l46a);
        x.getContentPane().add( l47a);
        x.getContentPane().add( l38b);
        x.getContentPane().add( l39b);
        x.getContentPane().add( l40b );
        x.getContentPane().add( l41b);
        x.getContentPane().add( l42b );
        x.getContentPane().add( l43b );
        x.getContentPane().add( l44b);
        x.getContentPane().add( l45b );
        x.getContentPane().add( l46b );
        x.getContentPane().add( l47b);
        x.getContentPane().add( l38c );
        x.getContentPane().add( l39c );
        x.getContentPane().add( l40c );
        x.getContentPane().add( l41c );
        x.getContentPane().add( l42c );
        x.getContentPane().add( l43c );
        x.getContentPane().add( l44c);
        x.getContentPane().add( l45c );
        x.getContentPane().add( l46c );
        x.getContentPane().add( l47c );
        x.getContentPane().add( l38d );
        x.getContentPane().add( l39d);
        x.getContentPane().add( l40d );
        x.getContentPane().add( l41d );
        x.getContentPane().add( l42d );
        x.getContentPane().add( l43d );
        x.getContentPane().add( l44d );
        x.getContentPane().add( l45d);
        x.getContentPane().add( l46d);
        x.getContentPane().add( l47d);
        x.setBounds(100, 100, 800, 600);
        x.setVisible(true);
    }

    @Override
    public Tile getNuovaIstanza() {
         if (baseCurvaSinistraSopra != null) {
            return new SingleCrossingTile(this.tipo);
        } else {
            Logger.getLogger(Main.LOGGER_NAME).log(Level.SEVERE, "Creazione nuova istanza di Single Crossing Tile fallita - chiamare prima loadResources()");
            return null;
        }
    }

    @Override
    public WaterDirection getdirezioneDiUscita() {
        if (attraversamento != null) {
            switch (tipo) {
                case TileLoaderAndFactory.CURVA_SINISTRASOPRA:
                    if(attraversamento == Crossings.SINISTRASOPRA)
                        return WaterDirection.TOP;
                    else
                        return WaterDirection.LEFT;
                case TileLoaderAndFactory.CURVA_DESTRASOPRA:
                    if(attraversamento == Crossings.DESTRASOPRA)
                        return WaterDirection.TOP;
                    else
                        return WaterDirection.RIGHT;
                case TileLoaderAndFactory.CURVA_SINISTRASOTTO:
                    if(attraversamento == Crossings.SINISTRASOTTO)
                        return WaterDirection.BOTTOM;
                    else
                        return WaterDirection.LEFT;
                case TileLoaderAndFactory.CURVA_DESTRASOTTO:
                    if(attraversamento == Crossings.DESTRASOTTO)
                        return WaterDirection.BOTTOM;
                    else
                        return WaterDirection.RIGHT;
                case TileLoaderAndFactory.ORIZZONTALE:
                    if(attraversamento == Crossings.DESTRASINISTRA)
                        return WaterDirection.LEFT;
                    else
                        return WaterDirection.RIGHT;
                case TileLoaderAndFactory.VERTICALE:
                    if(attraversamento == Crossings.SOPRASOTTO)
                        return WaterDirection.BOTTOM;
                    else
                        return WaterDirection.TOP;
                default: return null;
            }
        }
        else
            return null;
    }

    @Override
    public WaterDirection[] getdirezioniDiEntrataLibere() {
        if(attraversamento == null){
            switch (tipo) {
                case TileLoaderAndFactory.CURVA_SINISTRASOPRA:
                    return Arrays.asList(WaterDirection.TOP, WaterDirection.LEFT).toArray(new WaterDirection[2]);
                case TileLoaderAndFactory.CURVA_DESTRASOPRA:
                    return Arrays.asList(WaterDirection.TOP, WaterDirection.RIGHT).toArray(new WaterDirection[2]);
                case TileLoaderAndFactory.CURVA_SINISTRASOTTO:
                    return Arrays.asList(WaterDirection.BOTTOM, WaterDirection.LEFT).toArray(new WaterDirection[2]);
                case TileLoaderAndFactory.CURVA_DESTRASOTTO:
                    return Arrays.asList(WaterDirection.RIGHT, WaterDirection.BOTTOM).toArray(new WaterDirection[2]);
                case TileLoaderAndFactory.ORIZZONTALE:
                    return Arrays.asList(WaterDirection.RIGHT, WaterDirection.LEFT).toArray(new WaterDirection[2]);
                case TileLoaderAndFactory.VERTICALE:
                    return Arrays.asList(WaterDirection.TOP, WaterDirection.BOTTOM).toArray(new WaterDirection[2]);
                default:
                    return new WaterDirection[0];
            }
        }
        else{
            return new WaterDirection[0];
        }
    }

    @Override
    public void aggiungiAttraversamento(WaterDirection direzioneDiEntrata) {

        switch (tipo) {
            case TileLoaderAndFactory.CURVA_SINISTRASOPRA:
                switch (direzioneDiEntrata) {
                    case LEFT:
                        attraversamento = Crossings.SINISTRASOPRA;
                        break;
                    case TOP:
                        attraversamento = Crossings.SOPRASINISTRA;
                        break;
                }
                break;
            case TileLoaderAndFactory.CURVA_DESTRASOPRA:
                switch (direzioneDiEntrata) {
                    case RIGHT:
                        attraversamento = Crossings.DESTRASOPRA;
                        break;
                    case TOP:
                        attraversamento = Crossings.SOPRADESTRA;
                        break;
                }
                break;
            case TileLoaderAndFactory.CURVA_SINISTRASOTTO:
                switch (direzioneDiEntrata) {
                    case LEFT:
                        attraversamento = Crossings.SINISTRASOTTO;
                        break;
                    case BOTTOM:
                        attraversamento = Crossings.SOTTOSINISTRA;
                        break;
                }
                break;
            case TileLoaderAndFactory.CURVA_DESTRASOTTO:
                switch (direzioneDiEntrata) {
                    case RIGHT:
                        attraversamento = Crossings.DESTRASOTTO;
                        break;
                    case BOTTOM:
                        attraversamento = Crossings.SOTTODESTRA;
                        break;
                }
                break;
            case TileLoaderAndFactory.ORIZZONTALE:
                switch (direzioneDiEntrata) {
                    case LEFT:
                        attraversamento = Crossings.SINISTRADESTRA;
                        break;
                    case RIGHT:
                        attraversamento = Crossings.DESTRASINISTRA;
                        break;
                }
                break;
            case TileLoaderAndFactory.VERTICALE:
                switch (direzioneDiEntrata) {
                    case BOTTOM:
                        attraversamento = Crossings.SOTTOSOPRA;
                        break;
                    case TOP:
                        attraversamento = Crossings.SOPRASOTTO;
                        break;
                }
                break;
        }

    }
}
