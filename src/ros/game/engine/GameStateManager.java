package ros.game.engine;

import ros.game.engine.GameState;
import ros.game.engine.ResourceManager;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.*;
import ros.game.engine.input.InputManager;

public class GameStateManager {

    public static final String EXIT_GAME = "_ExitGame";
    private Image defaultImage;
    public static final String loading = "Caricamento...";
    
    private Map gameStates;
    private GameState currentState;
    private InputManager inputManager;
    private boolean done;

    //for random coloring loading tring
    Random r;
    private boolean resourcesLoaded = false;

    public GameStateManager(InputManager inputManager,
        Image defaultImage)
    {
        this.inputManager = inputManager;
        this.defaultImage = defaultImage;
        gameStates = new HashMap();

        r = new Random();
    }

    public void addState(GameState state) {
        gameStates.put(state.getName(), state);
    }

    public boolean resourcesLoaded(){
        return resourcesLoaded;
    }

    public Iterator getStates() {
        return gameStates.values().iterator();
    }

    public void loadAllResources(ResourceManager resourceManager) {
        Iterator i = getStates();
        while (i.hasNext()) {
            GameState gameState = (GameState)i.next();
            gameState.loadResources(resourceManager);
        }

        resourcesLoaded = true;
    }


    public boolean isDone() {
        return done;
    }


    /**
        Sets the current state (by name).
    */
    public void setState(String name) {
        // clean up old state
        System.out.println("setState("+name+")" + " PreviousState: " + currentState);
        if (currentState != null) {
            currentState.stop();
        }
        inputManager.clearAllMaps();

        if (name == EXIT_GAME) {
            done = true;
        }
        else {
            // set new state
            currentState = (GameState)gameStates.get(name);
            if (currentState != null) {    
                currentState.start(inputManager);
            }
            else{
                System.out.println("Stato "+name+" non trovato");
            }
        }
    }


    /**
        Updates world, handles input.
    */
    public void update(long elapsedTime) {
        // if no state, pause a short time
        if (currentState == null) {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException ex) { }
        }
        else {
            String nextState = currentState.checkForStateChange();
            if (nextState != null) {
                setState(nextState);
            }
            else {
                currentState.update(elapsedTime);
            }
        }
    }

    /**
        Draws to the screen.
    */
    public void draw(Graphics2D g) {
        if (currentState != null) {
            currentState.draw(g);
        }
       else {
            // if no state, draw the default image to the screen
            g.drawImage(defaultImage, 0, 0, null);

            //change color ad ogni frame
            g.setColor(new Color(Math.abs(r.nextInt()%255),Math.abs(r.nextInt()%255),Math.abs(r.nextInt()%255)));
            g.drawString(loading, 470, 200);
        }
    }
}