package ros.game;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;

/**
 *
 * @author ros
 */
public class WindowedScreenManager {


    private GraphicsDevice device;
    private Window window;
    /**
        Creates a new ScreenManager object.
    */
    public WindowedScreenManager() {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = environment.getDefaultScreenDevice();
    }

 /**
   *  make windows at 800x600 etc
   *
   */
    public void setUpWindow(final JFrame window)
    {
        this.window = window;

        window.setUndecorated(true);
        window.setTitle("JavaPipeMania");
        window.setResizable(false);
        window.setIgnoreRepaint(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setSize(800, 600);
        window.setLocationRelativeTo(null);
        //window.validate();
        window.setVisible(true);

       try {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    window.createBufferStrategy(2);
                }
            });
        }
        catch (InterruptedException ex) {
            // ignore
        }
        catch (InvocationTargetException  ex) {
            // ignore
        }
         
    }

    /**
        Gets the graphics context for the display. The
        ScreenManager uses double buffering, so applications must
        call update() to show any graphics drawn.
        <p>
        The application must dispose of the graphics object.
    */
    public Graphics2D getGraphics() {
        if (window != null) {
            //return (Graphics2D) window.getGraphics();
            BufferStrategy strategy = window.getBufferStrategy();
            return (Graphics2D)strategy.getDrawGraphics();
        }
        else {
            return null;
        }
    }


    /**
        Updates the display.
    */
    public void update() {
        if (window != null) {
            BufferStrategy strategy = window.getBufferStrategy();
            if (!strategy.contentsLost()) {
                strategy.show();
            }
        }
        // Sync the display on some systems.
        // (on Linux, this fixes event queue problems)
        Toolkit.getDefaultToolkit().sync();
    }


    /**
        Returns the window currently used in full screen mode.
        Returns null if the device is not in full screen mode.
    */
    public JFrame getWindow() {
        return (JFrame) window;
    }


    /**
        Returns the width of the window currently used in full
        screen mode. Returns 0 if the device is not in full
        screen mode.
    */
    public int getWidth() {
         if (window != null) {
        return window.getWidth();
        }
        else {
            return 0;
        }
    }


    /**
        Returns the height of the window currently used in full
        screen mode. Returns 0 if the device is not in full
        screen mode.
    */
    public int getHeight() {
        if (window != null) {
            return window.getHeight();
        }
        else {
            return 0;
        }
    }


    /**
        Restores the screen's display mode.
    */
    public void restoreScreen() {
        if (window != null) {
            window.dispose();
        }
    }

    /**
        Creates an image compatible with the current display.
    */
    public BufferedImage createCompatibleImage(int w, int h,
        int transparancy)
    {
        if (window != null) {
            GraphicsConfiguration gc =
                window.getGraphicsConfiguration();
            return gc.createCompatibleImage(w, h, transparancy);
        }
        return null;
    }
}
