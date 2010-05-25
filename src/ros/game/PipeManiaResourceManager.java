/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ros.game;

import java.awt.GraphicsConfiguration;
import ros.game.sound.MidiPlayer;
import ros.game.sound.SoundManager;
import ros.game.state.ResourceManager;

/**
 *
 * @author Ros
 */
public class PipeManiaResourceManager extends ResourceManager {

    /**
    The ResourceManager class loads and manages tile Images and
    "host" Sprites used in the game. Game Sprites are cloned from
    "host" Sprites.
     */
    public PipeManiaResourceManager(GraphicsConfiguration gc,
            SoundManager soundManager, MidiPlayer midiPlayer)
    {
        super(gc, soundManager, midiPlayer);
    }

    public void loadResources() {
    }
}
