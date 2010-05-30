/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ros.game.state;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Cursor;
import javax.sound.midi.Sequence;
import ros.game.tiles.TileLoaderAndFactory;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import ros.game.sound.Sound;
import ros.game.util.Utils;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import ros.game.WindowedScreenManager;
import ros.game.database.DatabaseManager;
import ros.game.dialogs.GameOverLevelDialog;
import ros.game.dialogs.LivelloCompletatoDialog;
import ros.game.dialogs.PauseLevelDialog;
import ros.game.dialogs.StartLevelDialog;
import ros.game.dialogs.StorePointsLevelDialog;
import ros.game.input.GameAction;
import ros.game.input.InputManager;
import ros.game.levels.Board;
import ros.game.levels.BriefingProgressBar;
import ros.game.sound.MidiPlayer;
import ros.game.sound.SoundManager;

/**
 *
 * @author Ros
 */
public class StartGameState implements GameState, ActionListener {

    //SUBSTATES
    public static final byte SHOW_STARTING_LEVEL = 0; //visualizza dialogo "Level starting"
    public static final byte IN_GAME_BRIEFING = 1;    //livello iniziato - acqua non ancora nella BOARD
    public static final byte IN_GAME_NOBRIEFING = 2;  //livello iniziato - acqua nella BOARD
    public static final byte SHOW_GAME_OVER = 3;      //L'acqua non puo' continuare il flusso
    public static final byte SHOW_FINISH_LEVEL = 4;   //l'acqua è arrivata alla tile di fine
    public static final byte STORE_GAME_SCORE = 5;    //Memorizza il punteggio
    public static final byte ACCELERATED_GAME = 6;    //accellera il tempo di casella e salta l'input
    public static final byte EXIT_GAME = 7;           //ferma i timer e salta l'input (visualizza un dialogo per l'uscita)

    private byte currentSubState;

    private WindowedScreenManager screen;
    private SoundManager sound;
    private MidiPlayer music;
    private Image sfondi[];

    private boolean done;
    private int punteggio;
    private int livelloCorrente;
   
    private BriefingProgressBar briefingBar;
    private Board board;
    private TileLoaderAndFactory tileGenerator;

    //dialoghi e Action dei dialoghi
    private StartLevelDialog startLevelDialog;
    protected GameAction okShowStartLevelDialogAction;
    private PauseLevelDialog pauseLevelDialog;
    protected GameAction esciPauseLevelDialog;
    private GameOverLevelDialog gameOverLevelDialog;
    protected GameAction continuaGameOverLevelDialog;
    private StorePointsLevelDialog storePointsLevelDialog;
    private GameAction memPunteggioEContinuaStoreLevelDialog;
    private LivelloCompletatoDialog livelloFinitoDialog;
    private GameAction continuaLivelloCompletatoDialog;
    //eventi
    private GameAction mouseClickAction;
    private GameAction mouseClickAction2;

    private JButton esciButton;
    private GameAction escPressAction;
    private JButton acceleraButton;
    private GameAction invioPressAction;

    /**
     * l'azione che indica la fine del tempo di briefing
     */
    private GameAction timerAction;
    /**
     * l'azione che indica il passaggio da una casella ad un'altra
     */
    private GameAction casellaFinitaAction;

    private InputManager inputManager;

    //immagini e pannello dei bottoni
    private Image[] buttonImage;
    private JPanel buttonSpace;
    private final DatabaseManager database;
    private Image baseBottoni_Tile;

    private Sound lascia;
    private Sound piazza;
    private Sound ok;
    private Sequence musica;


    /**
     * Va al prossimo livello
     */
    private void goToNextLevel(){
        System.out.println("VAI AL PROSSIMO LIVELLO");
        System.out.flush();

        currentSubState = SHOW_STARTING_LEVEL;
        
        livelloCorrente++;

        briefingBar.initForStartingLevel(livelloCorrente);
        board.initForStartingLevel(livelloCorrente);
        
        startLevelDialog.setVisible(true);
    }

    /**
     * La funzione viene chiamata quando il gioco entra nello stato StartGameState
     *
     * @param inputManager
     */
    public void start(InputManager inputManager) {

        inputManager.mapToMouse(mouseClickAction, InputManager.MOUSE_BUTTON_1);
        inputManager.mapToMouse(mouseClickAction2, InputManager.MOUSE_BUTTON_3);
        
        this.inputManager = inputManager;

        if(esciButton == null){
            acceleraButton = createButton("Speed", "Accelera il gioco",0);
            esciButton = createButton("Esci", "Esci dal gioco",1);

            buttonSpace = new JPanel();
            buttonSpace.setOpaque(false);
            buttonSpace.add(acceleraButton);
            buttonSpace.add(esciButton);
            
            JFrame frame = screen.getWindow();
            Container contentPane = frame.getContentPane();
            contentPane.add(buttonSpace);

            // explicitly layout components (needed on some systems)
            frame.validate();
        }
        buttonSpace.setVisible(false);

        //TODO imposta il livello di partenza
        livelloCorrente = 28;
        punteggio = 0;
        goToNextLevel();
        
        done = false;

        music.play(musica, true);
    }
    
    /**
     * Costruttore<br>
     * inizializza il GameState e le suoe componenti interne:<br>
     * <ul>
     * <li>TileGenerator</li>
     * <li>LevelData</li>
     * <li>Board</li>
     * <li>PreviewTiles</li>
     * <li>ProgressBar</li>
     * </ul>
     * @param screen
     * @param soundManager
     * @param midiPlayer
     */
    public StartGameState(WindowedScreenManager screen, SoundManager soundManager, MidiPlayer midiPlayer, DatabaseManager database) {
        this.database = database;
        this.screen = screen;
        this.sound = soundManager;
        this.music = midiPlayer;

        this.tileGenerator = new TileLoaderAndFactory();
        this.livelloCorrente = 0;
        
        this.briefingBar = new BriefingProgressBar();
        this.board = new Board(tileGenerator);

        //init action
        initGameAction();

        createUI();

    }

    /**
     * Crea i dialoghi e le altre interfacce grafiche
     */
    private void createUI() {
        createDialogs();

        // add components to the "modal dialog" layer of the
        // screen's layered pane.
        screen.getWindow().getLayeredPane().add(this.briefingBar.getBriefingProgressBar(),
                JLayeredPane.MODAL_LAYER);
    }

    public String getName() {
        return "Game";
    }

    public String checkForStateChange() {
        return done ? "Menu" : null;
    }

    public void loadResources(ResourceManager resourceManager) {
        buttonImage = new Image[2];
        buttonImage[0] = resourceManager.loadImage("menu/start.png");
        buttonImage[1] = resourceManager.loadImage("menu/exit.png");

        baseBottoni_Tile = resourceManager.loadImage("sfondoBottoni_Tile.png");

        sfondi = new Image[Utils.NUMLEVELS];
        for (int i = 0; i < Utils.NUMLEVELS; i++) {
            sfondi[i] = resourceManager.loadImage("sfondi/" + i + ".jpg");
        }
        tileGenerator.loadResources(resourceManager);

        musica = resourceManager.loadSequence("sounds/loop1.MID");
        lascia = resourceManager.loadSound("sounds/scarta.wav");
        piazza = resourceManager.loadSound("sounds/place.wav");
        ok = resourceManager.loadSound("sounds/prize.wav");
    }

    public void stop() {
        done = true;
    }

    public void draw(Graphics2D g) {

        switch(this.currentSubState){
            case IN_GAME_BRIEFING:
                
                //Disegna sfondo
                g.drawImage(sfondi[livelloCorrente - 1], 0, 0, null);
                //disegna sfondo bottoni
                g.drawImage(baseBottoni_Tile, 800-233, 0, null);
                //Scrivi il livello Corrente
                g.drawString("Level " + livelloCorrente, 20, 40);
                //Scrivi il punteggio corrente
                g.drawString("Points " + punteggio, 150, 40);
                //disegno la board
                board.draw(g);
                break;
            case ACCELERATED_GAME:
                g.drawImage(sfondi[livelloCorrente - 1], 0, 0, null);
                //disegna sfondo bottoni
                g.drawImage(baseBottoni_Tile, 800-233, 0, null);
                //Scrivi il livello Corrente
                g.drawString("Level " + livelloCorrente, 20, 40);
                //Scrivi il punteggio corrente
                g.drawString("Points " + punteggio, 150, 40);
                //disegno la board
                board.draw(g);
                break;
            case EXIT_GAME:
                g.drawImage(sfondi[livelloCorrente - 1], 0, 0, null);
                //disegna sfondo bottoni
                g.drawImage(baseBottoni_Tile, 800-233, 0, null);
                //Scrivi il livello Corrente
                g.drawString("Level " + livelloCorrente, 20, 40);
                //Scrivi il punteggio corrente
                g.drawString("Points " + punteggio, 150, 40);
                break;
            case IN_GAME_NOBRIEFING:
                //Disegna sfondo
                g.drawImage(sfondi[livelloCorrente - 1], 0, 0, null);
                //disegna sfondo bottoni
                g.drawImage(baseBottoni_Tile, 800-233, 0, null);
                //Scrivi il livello Corrente
                g.drawString("Level " + livelloCorrente, 20, 40);
                //Scrivi il punteggio corrente
                g.drawString("Points " + punteggio, 150, 40);
                //disegno la board
                board.draw(g);
                break;
            case SHOW_FINISH_LEVEL:
                //Disegna sfondo
                g.drawImage(sfondi[livelloCorrente - 1], 0, 0, null);
                //Scrivi il livello Corrente
                g.drawString("Level " + livelloCorrente, 20, 40);
                //Scrivi il punteggio corrente
                g.drawString("Points " + punteggio, 150, 40);
                break;
            case SHOW_GAME_OVER:
                //Disegna sfondo
                g.drawImage(sfondi[livelloCorrente - 1], 0, 0, null);
                //Scrivi il livello Corrente
                g.drawString("Level " + livelloCorrente, 20, 40);
                //Scrivi il punteggio corrente
                g.drawString("Points " + punteggio, 150, 40);
                board.draw(g);
                break;
            case SHOW_STARTING_LEVEL:
                //Disegna sfondo
                g.drawImage(sfondi[livelloCorrente - 1], 0, 0, null);
                //Scrivi il livello Corrente
                g.drawString("Level " + livelloCorrente, 20, 40);
                //Scrivi il punteggio corrente
                g.drawString("Points " + punteggio, 150, 40);
                break;
            case STORE_GAME_SCORE:
                //Disegna sfondo
                g.drawImage(sfondi[livelloCorrente - 1], 0, 0, null);
                //Scrivi il livello Corrente
                g.drawString("Level " + livelloCorrente, 20, 40);
                //Scrivi il punteggio corrente
                g.drawString("Points " + punteggio, 150, 40);
                break;
        }
        
        //Disegna i componenti Swing
        JFrame frame = screen.getWindow();
        // the layered pane contains things like popups (tooltips,
        // popup menus) and the content pane.
        frame.getLayeredPane().paintComponents(g);
    }

    private void createDialogs() {
        livelloFinitoDialog = new LivelloCompletatoDialog();
        livelloFinitoDialog.getActionButton().addActionListener(this);
        Dimension size = livelloFinitoDialog.getPreferredSize();
        livelloFinitoDialog.setSize(size);
        //centra il dialogo
        livelloFinitoDialog.setLocation(400-(size.width/2), 300-(size.height/2));
        livelloFinitoDialog.setVisible(false);

        startLevelDialog = new StartLevelDialog();
        startLevelDialog.getActionButton().addActionListener(this);
        size = startLevelDialog.getPreferredSize();
        startLevelDialog.setSize(size);
        //centra il dialogo
        startLevelDialog.setLocation(400-(size.width/2), 300-(size.height/2));
        startLevelDialog.setVisible(false);

        gameOverLevelDialog = new GameOverLevelDialog();
        gameOverLevelDialog.getActionButton().addActionListener(this);
        size = gameOverLevelDialog.getPreferredSize();
        gameOverLevelDialog.setSize(size);
        gameOverLevelDialog.setLocation(400-(size.width/2), 300-(size.height/2));
        gameOverLevelDialog.setVisible(false);


        pauseLevelDialog = new PauseLevelDialog();
        pauseLevelDialog.getActionButton().addActionListener(this);
        size = pauseLevelDialog.getPreferredSize();
        pauseLevelDialog.setSize(size);
        pauseLevelDialog.setLocation(400-(size.width/2), 300-(size.height/2));
        pauseLevelDialog.setVisible(false);

        storePointsLevelDialog = new StorePointsLevelDialog();
        storePointsLevelDialog.getActionButton().addActionListener(this);
        size = storePointsLevelDialog.getPreferredSize();
        storePointsLevelDialog.setSize(size);
        storePointsLevelDialog.setLocation(400-(size.width/2), 300-(size.height/2));
        storePointsLevelDialog.setVisible(false);

        // add the dialog to the "modal dialog" layer of the
        // screen's layered pane.
        screen.getWindow().getLayeredPane().add(startLevelDialog,
                JLayeredPane.MODAL_LAYER);
        screen.getWindow().getLayeredPane().add(gameOverLevelDialog,
                JLayeredPane.MODAL_LAYER);
        screen.getWindow().getLayeredPane().add(pauseLevelDialog,
                JLayeredPane.MODAL_LAYER);
        screen.getWindow().getLayeredPane().add(storePointsLevelDialog,
                JLayeredPane.MODAL_LAYER);
        screen.getWindow().getLayeredPane().add(livelloFinitoDialog,
                JLayeredPane.MODAL_LAYER);     

    }

    private void initGameAction() {
        okShowStartLevelDialogAction = new GameAction("okShowStartLevelDialogAction", GameAction.DETECT_INITAL_PRESS_ONLY);
        
        mouseClickAction = new GameAction("mouse click", GameAction.DETECT_INITAL_PRESS_ONLY);
        mouseClickAction2 = new GameAction("mouse click 2", GameAction.DETECT_INITAL_PRESS_ONLY);

        escPressAction = new GameAction("esc press", GameAction.DETECT_INITAL_PRESS_ONLY);
        invioPressAction = new GameAction("invio press",  GameAction.DETECT_INITAL_PRESS_ONLY);

        memPunteggioEContinuaStoreLevelDialog = new GameAction("memorizza punteggio",  GameAction.DETECT_INITAL_PRESS_ONLY);
        continuaGameOverLevelDialog = new GameAction("vai a memorizzare il punteggio",  GameAction.DETECT_INITAL_PRESS_ONLY);
        esciPauseLevelDialog =  new GameAction("esci dal gioco",  GameAction.DETECT_INITAL_PRESS_ONLY);
        continuaLivelloCompletatoDialog =  new GameAction("vai al prossimo livello",  GameAction.DETECT_INITAL_PRESS_ONLY);

        timerAction = new GameAction("briefingFinished", GameAction.DETECT_INITAL_PRESS_ONLY);
        casellaFinitaAction = new GameAction("casellaFinished", GameAction.DETECT_INITAL_PRESS_ONLY);
    }

    /**
        Called by the AWT event dispatch thread when a button is
        pressed.
    */
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == startLevelDialog.getActionButton()) {
            okShowStartLevelDialogAction.tap();
        }
        else if(src == pauseLevelDialog.getActionButton()){
            esciPauseLevelDialog.tap();
        }
        else if(src == esciButton){
            escPressAction.tap();
        }
        else if(src == acceleraButton){
            invioPressAction.tap();
        }
        else if(src == gameOverLevelDialog.getActionButton()){
            continuaGameOverLevelDialog.tap();
        }
        else if(src == storePointsLevelDialog.getActionButton()){
            memPunteggioEContinuaStoreLevelDialog.tap();
        }
        else if(src == livelloFinitoDialog.getActionButton()){
            continuaLivelloCompletatoDialog.tap();
        }
    }

    public void update(long elapsedTime) {
        checkInput();
        switch(this.currentSubState){
            case IN_GAME_BRIEFING:
                briefingBar.update();
                board.updateMousePosition(inputManager.getMousePosition());
                break;
            case IN_GAME_NOBRIEFING:
                board.updateMousePosition(inputManager.getMousePosition());
                break;   
        }
    }

    private void checkInput() {
        switch(currentSubState){
            case EXIT_GAME:
                if(esciPauseLevelDialog.isPressed()){
                    sound.play(ok);
                    pauseLevelDialog.setVisible(false);
                    briefingBar.setVisible(false);
                    stop();
                }
                break;
            case SHOW_STARTING_LEVEL:
                if (okShowStartLevelDialogAction.isPressed()) {
                    startLevelDialog.setVisible(false);
                    buttonSpace.setVisible(true);
                    //vai al prossimo stato
                    currentSubState = StartGameState.IN_GAME_BRIEFING;
                    briefingBar.startTimer(timerAction, livelloCorrente-1);

                    //effetto sonoro
                    sound.play(ok);
                }
                break;
            case ACCELERATED_GAME:
                if(timerAction.isPressed()){
                    board.startCasellaTimer(casellaFinitaAction, livelloCorrente-1, true);
                    sound.play(ok);
                }
                if (casellaFinitaAction.isPressed()) { //passa all'altra Tile
                    casellaFinitaAction.reset();
                    if(board.isEndTileReached()){
                        currentSubState = StartGameState.SHOW_FINISH_LEVEL;
                        livelloFinitoDialog.setVisible(true);
                    }
                    else if (board.goToNextTile())
                        punteggio += board.getPunteggioTileAttraversata();
                    else{
                        currentSubState = StartGameState.SHOW_GAME_OVER;
                        buttonSpace.setVisible(false);
                        gameOverLevelDialog.setPunteggioELivello(livelloCorrente, punteggio);
                        gameOverLevelDialog.setVisible(true);
                    }
                }
                break;   
            case IN_GAME_BRIEFING:
                if(timerAction.isPressed()){
                    //vai al prossimo stato
                    currentSubState = StartGameState.IN_GAME_NOBRIEFING;
                    board.startCasellaTimer(casellaFinitaAction, livelloCorrente-1, false);
                }
                else if(mouseClickAction.isPressed()){
                    board.sostituisciTileSePossibile(inputManager.getClikedMousePosition());
                    mouseClickAction.reset();
                    sound.play(piazza);
                }
                else if(mouseClickAction2.isPressed()){
                    board.saltaTilePreviewCorrente();
                    mouseClickAction2.reset();
                    sound.play(lascia);
                }
                else if(escPressAction.isPressed()){
                    currentSubState = StartGameState.EXIT_GAME;
                    buttonSpace.setVisible(false);
                    pauseLevelDialog.setVisible(true);
                    sound.play(ok);
                }
                else if(invioPressAction.isPressed()){
                    currentSubState = StartGameState.ACCELERATED_GAME;
                    briefingBar.acceleraBiefringTimer();
                    buttonSpace.setVisible(false);
                    sound.play(ok);
                }
                break;
            case IN_GAME_NOBRIEFING:
                if (casellaFinitaAction.isPressed()) { //passa all'altra Tile
                    casellaFinitaAction.reset();
                    if(board.isEndTileReached()){
                        currentSubState = StartGameState.SHOW_FINISH_LEVEL;
                        buttonSpace.setVisible(false);
                        livelloFinitoDialog.setVisible(true);
                    }
                    else if (board.goToNextTile())
                        punteggio += board.getPunteggioTileAttraversata();
                    //sei alla casella  di fine
                    //timerCasella.stop();
                    else{
                        currentSubState = StartGameState.SHOW_GAME_OVER;
                        buttonSpace.setVisible(false);
                        gameOverLevelDialog.setPunteggioELivello(livelloCorrente, punteggio);
                        gameOverLevelDialog.setVisible(true);
                    }
                }
                else if(mouseClickAction.isPressed()){
                    board.sostituisciTileSePossibile(inputManager.getClikedMousePosition());
                    mouseClickAction.reset();
                    sound.play(piazza);
                }
                else if(mouseClickAction2.isPressed()){
                    board.saltaTilePreviewCorrente();
                    mouseClickAction2.reset();
                    sound.play(lascia);
                }
                else if(escPressAction.isPressed()){
                    currentSubState = StartGameState.EXIT_GAME;
                    buttonSpace.setVisible(false);
                    pauseLevelDialog.setVisible(true);
                    sound.play(ok);
                }
                else if(invioPressAction.isPressed()){
                    currentSubState = StartGameState.ACCELERATED_GAME;   
                    board.acceleraTimerCasella();
                    buttonSpace.setVisible(false);
                    sound.play(ok);
                }
                break;
            case SHOW_FINISH_LEVEL:
                if(continuaLivelloCompletatoDialog.isPressed()){
                    if(livelloCorrente < 30){
                        //vai al prossimo livello
                        livelloFinitoDialog.setVisible(false);

                        this.goToNextLevel();
                    }
                    else{
                        livelloFinitoDialog.setVisible(false);
                        currentSubState = StartGameState.STORE_GAME_SCORE;
                        storePointsLevelDialog.setVisible(true);
                    }
                    sound.play(ok);
                }
                break;
            case SHOW_GAME_OVER:
                if(continuaGameOverLevelDialog.isPressed()){
                    gameOverLevelDialog.setVisible(false);
                    currentSubState = StartGameState.STORE_GAME_SCORE;
                    storePointsLevelDialog.setVisible(true);
                    sound.play(ok);
                }
                break;
            case STORE_GAME_SCORE:
                if(memPunteggioEContinuaStoreLevelDialog.isPressed()){
                    storePointsLevelDialog.setVisible(false);
                    briefingBar.setVisible(false);
                    database.insertRecord(storePointsLevelDialog.getPlayerName(), punteggio, livelloCorrente);
                    sound.play(ok);
                    stop();
                }
                break;
        }
    }

    /**
        Creates a Swing JButton. The image used for the button is
        located at "../images/menu/" + name + ".png". The image is
        modified to create a "default" look (translucent) and a
        "pressed" look (moved down and to the right).
        <p>The button doesn't use Swing's look-and-feel and
        instead just uses the image.
    */
    public JButton createButton(String name, String toolTip, int indexImage) {

        // load the image
        ImageIcon iconRollover = new ImageIcon(buttonImage[indexImage]);
        int w = iconRollover.getIconWidth();
        int h = iconRollover.getIconHeight();

        // get the cursor for this button
        Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

        // make translucent default image
        Image image = screen.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        Graphics2D g = (Graphics2D)image.getGraphics();
        Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);
        g.setComposite(alpha);
        g.drawImage(iconRollover.getImage(), 0, 0, null);
        g.dispose();
        ImageIcon iconDefault = new ImageIcon(image);

        // make a pressed image
        image = screen.createCompatibleImage(w, h,
            Transparency.TRANSLUCENT);
        g = (Graphics2D)image.getGraphics();
        g.drawImage(iconRollover.getImage(), 2, 2, null);
        g.dispose();
        ImageIcon iconPressed = new ImageIcon(image);

        // create the button
        JButton button = new JButton(name);
        button.addActionListener(this);
        button.setIgnoreRepaint(true);
        button.setFocusable(false);
        button.setToolTipText(toolTip);
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setCursor(cursor);
        button.setIcon(iconDefault);
        button.setRolloverIcon(iconRollover);
        button.setPressedIcon(iconPressed);

        return button;
    }
}
