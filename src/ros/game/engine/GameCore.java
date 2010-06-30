package ros.game.engine;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
    Simple abstract class used for testing. Subclasses should
    implement the draw() method.
*/
public abstract class GameCore {

    protected static final int FONT_SIZE = 30;
    protected static final Color BACK_COLOR = Color.BLACK;
    protected static final Color FRONT_COLOR = Color.RED;

    public static final DisplayMode POSSIBLE_MODES[] = {
        new DisplayMode(800, 600, 16, 60),
        new DisplayMode(800, 600, 32, 60),
        new DisplayMode(800, 600, 24, 60),
        new DisplayMode(800, 600, DisplayMode.BIT_DEPTH_MULTI, DisplayMode.REFRESH_RATE_UNKNOWN),
        new DisplayMode(800, 600, DisplayMode.BIT_DEPTH_MULTI, 60),
    };

    private boolean isRunning;
    protected WindowedScreenManager screen;

    //tempo tra un frame e l'altro
    private int periodo;
     /** Number of frames with a delay of 0 ms before the animation thread yields
     to other running threads. */
    private static final int NO_DELAYS_PER_YIELD = 2;

    /**
        Signals the game loop that it's time to quit
    */
    public void stop() {
        isRunning = false;
    }

    /**
        Calls init() and gameLoop()
    */
    public void run() {
        try {
            init();
            gameLoop();
        }
        finally {
            screen.restoreScreen();
            lazilyExit();
        }
    }

    /**
        Exits the VM from a daemon thread. The daemon thread waits
        2 seconds then calls System.exit(0). Since the VM should
        exit when only daemon threads are running, this makes sure
        System.exit(0) is only called if neccesary. It's neccesary
        if the Java Sound system is running.
    */
    public void lazilyExit() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                // first, wait for the VM exit on its own.
                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException ex) { }
                // system is still running, so force an exit
                System.exit(0);
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    /**
        Sets full screen mode and initiates and objects.
    */
    public void init() {
        // make sure Swing components don't paint themselve
        NullRepaintManager.install();

        periodo = (int) 1000.0 / 30;

        //inizializzo lo ScreenManager
        screen = new WindowedScreenManager();

        //vedo se è diponibile la risoluzione 800x600 ritorna null se non è disponibile
        //DisplayMode displayMode = screen.findFirstCompatibleMode(POSSIBLE_MODES);

        //if(displayMode == null){
        //    System.out.println("il tuo sistema non supporta la risoluzione 800x600");
        //    lazilyExit();
        //}
        //else{
        //    screen.setFullScreen( displayMode, new JFrame());
        screen.setUpWindow(new JFrame());
        //    Window window = screen.getFullScreenWindow();

        Window window = screen.getWindow();

        window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
        window.setBackground(BACK_COLOR);
        window.setForeground(FRONT_COLOR);

        isRunning = true;
        //}
    }

    public Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }

    /**
        Runs through the game loop until stop() is called.
    */
    public void gameLoop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;

        int noDelays = 0;
        int overSleepTime = 0;
        long sleepTime = 0;
        
        while (isRunning) {

            long elapsedTime = System.currentTimeMillis() - currTime;
            currTime += elapsedTime;

            // update
            update(elapsedTime);

            // draw the screen
            Graphics2D g = screen.getGraphics();

            draw(g);
            g.dispose();

            //flip to screen
            screen.update();
            
            sleepTime = (periodo - elapsedTime) - overSleepTime;

            if (sleepTime > 0) {   // some time left in this cycle
                try {
                    Thread.sleep(sleepTime);  // already in ms
                } catch (InterruptedException ex) {
                }
            } else {    // sleepTime <= 0; the frame took longer than the period

                if (++noDelays >= NO_DELAYS_PER_YIELD) {
                    Thread.yield();   // give another thread a chance to run
                    noDelays = 0;
                }
            }
        }
    }

    /**
        Updates the state of the game/animation based on the
        amount of elapsed time that has passed.
    */
    public abstract void update(long elapsedTime);

    /**
        Draws to the screen. Subclasses must override this
        method.
    */
    public abstract void draw(Graphics2D g);
}
