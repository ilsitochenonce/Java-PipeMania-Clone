/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.impl;

import ros.game.tiles.Tile;

/**
 *
 * Le funzionalità che devono essere fornite dal PreviewTile
 *
 * @author ros
 */
public interface AnteprimaTile {

    public Tile getNextTile();
    void initForStartingLevel(int level);

}
