package ros.game.engine;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.sound.midi.Sequence;
import ros.game.engine.sound.MidiPlayer;
import ros.game.engine.sound.Sound;
import ros.game.engine.sound.SoundManager;


/**
    The ResourceManager class loads resources like images and
    sounds.
*/
public class ResourceManager {

    private GraphicsConfiguration gc;
    private SoundManager soundManager;
    private MidiPlayer midiPlayer;

    /**
        Creates a new ResourceManager with the specified
        GraphicsConfiguration.
    */
    public ResourceManager(GraphicsConfiguration gc,
        SoundManager soundManager, MidiPlayer midiPlayer)
    {
        this.gc = gc;
        this.soundManager = soundManager;
        this.midiPlayer = midiPlayer;

        try {
            java.util.Enumeration e = getClass().getClassLoader().getResources("ros.game.state.ResourceManager");
            while (e.hasMoreElements()) {
                System.out.println(e.nextElement());
            }

        }
        catch (IOException ex) {

        }

    }


    /**
        Gets an image from the images/ directory.
    */
    public Image loadImage(String name) {
        String filename = "resources/imgs/" + name;

        //System.out.println(filename);
        //System.out.flush();
        
        return new ImageIcon(getResource(filename)).getImage();
    }

    public URL getResource(String filename) {
        return getClass().getClassLoader().getResource(filename);
    }

    public InputStream getResourceAsStream(String filename) {
        String filename1 = "resources/" + filename;
        return getClass().getClassLoader().
            getResourceAsStream(filename1);
    }


    public Sound loadSound(String name) {
        return soundManager.getSound(getResourceAsStream(name));
    }


    public Sequence loadSequence(String name) {
        return midiPlayer.getSequence(getResourceAsStream(name));
    }


}
