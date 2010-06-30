package ros.game.engine;

import ros.game.engine.GameCore;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import ros.game.engine.database.DatabaseManager;
import ros.game.engine.input.InputManager;
import ros.game.engine.sound.MidiPlayer;
import ros.game.engine.sound.SoundManager;
import ros.game.impl.states.MenuGameState;
import ros.game.impl.states.StartGameState;

/**
 * @author Rosario
 *
 * This is the Main class.
 * Set the resolution and the full screen.
 *
 * Store the states of the games in the GameStateManager.
 * Load the resources.
 *
 */
public class Main extends GameCore{

    public static final String LOGGER_NAME = "ros.game.pipemaniaclone";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Main().run();
    }

    static final Logger log = Logger.getLogger(LOGGER_NAME);

    // uncompressed, 44100Hz, 16-bit, mono, signed, little-endian
    private static final AudioFormat PLAYBACK_FORMAT =
        new AudioFormat(44100, 16, 1, true, false);
    
    private ResourceManager resourceManager;
    private GameStateManager gameStateManager;
    private InputManager inputManager;
    private SoundManager soundManager;
    private MidiPlayer midiPlayer;
    private DatabaseManager database;

    @Override
    public void init(){

        //crea un file di log
        try {
            log.addHandler(new FileHandler("log"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.setLevel(Level.INFO);

        log.info("init database");
        //Check Database - if not exist create one
        database = new DatabaseManager();
        
        log.info("init sound");
        soundManager = new SoundManager(PLAYBACK_FORMAT, 8);

        log.info("init midi");
        midiPlayer = new MidiPlayer();

        log.info("init gamecore");
        super.init();
        
        log.info("init input manager");
        //inputManager = new InputManager(screen.getFullScreenWindow());
        inputManager = new InputManager(screen.getWindow());
        
        log.info("init resource manager");
        resourceManager = new ResourceManager(screen.getWindow().getGraphicsConfiguration(), soundManager, midiPlayer);

        log.info("init game states");
        gameStateManager = new GameStateManager(inputManager, resourceManager.loadImage("Splash.jpg"));
        gameStateManager.addState(new MenuGameState(screen, soundManager,midiPlayer, database));
        gameStateManager.addState(new StartGameState(screen, soundManager,midiPlayer, database));

        // load resources (in separate thread)
        new Thread() {
            @Override
            public void run() {
                log.info("loading resources");
                gameStateManager.loadAllResources(resourceManager);
                log.info("setting to Loading State");
                gameStateManager.setState("Menu");
            }
        }.start();

    }

    /**
        Closes any resurces used by the MainClass.
    */
    @Override
    public void stop() {
        log.info("stopping game");
        super.stop();
        log.info("closing midi player");
        midiPlayer.close();
        log.info("closing sound manager");
        soundManager.close();
    }

    @Override
    public void update(long elapsedTime) {
        if (gameStateManager.isDone()) {
            stop();
        }
        else {
        // elapsedTime = timeSmoothie.getTime(elapsedTime);
            gameStateManager.update(elapsedTime);
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        gameStateManager.draw(g);
    }

}
