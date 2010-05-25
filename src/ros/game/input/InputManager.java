package ros.game.input;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
    The InputManager manages input of mouse events.
    Events are mapped to GameActions.
*/
public class InputManager implements MouseListener
{
    /**
        An invisible cursor.
    */
    public static final Cursor INVISIBLE_CURSOR =
        Toolkit.getDefaultToolkit().createCustomCursor(
            Toolkit.getDefaultToolkit().getImage(""),
            new Point(0,0),
            "invisible");

    // mouse codes
    public static final int MOUSE_BUTTON_1 = 0;
    public static final int MOUSE_BUTTON_2 = 1;
    public static final int MOUSE_BUTTON_3 = 2;

    private static final int NUM_MOUSE_CODES = 3;

    private GameAction[] mouseActions =
        new GameAction[NUM_MOUSE_CODES];

    private Component comp;

    private Point mouseClikedAtPoint;

    /**
        Creates a new InputManager that listens to input from the
        specified component.
    */
    public InputManager(Component comp) {
        mouseClikedAtPoint = new Point();
        this.comp = comp;

        // register key and mouse listeners
        comp.addMouseListener(this);
    }

    /**
     *
     * @return posizione del mouse
     */
    public Point getMousePosition(){
     return comp.getMousePosition();
    }

    /**
     *
     * @return la posizione del mouse quando è stato cliccato il mouse
     */
    public Point getClikedMousePosition(){
        return mouseClikedAtPoint;
    }

    /**
        Sets the cursor on this InputManager's input component.
    */
    public void setCursor(Cursor cursor) {
        comp.setCursor(cursor);
    }

    /**
        Maps a GameAction to a specific mouse action. The mouse
        codes are defined herer in InputManager (MOUSE_MOVE_LEFT,
        MOUSE_BUTTON_1, etc). If the mouse action already has
        a GameAction mapped to it, the new GameAction overwrites
        it.
    */
    public void mapToMouse(GameAction gameAction,
        int mouseCode)
    {
        mouseActions[mouseCode] = gameAction;
    }


    /**
        Clears all mapped keys and mouse actions to this
        GameAction.
    */
    public void clearMap(GameAction gameAction) {

        for (int i=0; i<mouseActions.length; i++) {
            if (mouseActions[i] == gameAction) {
                mouseActions[i] = null;
            }
        }

        gameAction.reset();
    }


    /**
        Clears all mapped keys and mouse actions.
    */
    public void clearAllMaps() {

        for (int i=0; i<mouseActions.length; i++) {
            mouseActions[i] = null;
        }
    }


    /**
        Gets a List of names of the keys and mouse actions mapped
        to this GameAction. Each entry in the List is a String.
    */
    public List getMaps(GameAction gameCode) {
        ArrayList list = new ArrayList();

        for (int i=0; i<mouseActions.length; i++) {
            if (mouseActions[i] == gameCode) {
                list.add(getMouseName(i));
            }
        }
        return list;
    }


    /**
        Resets all GameActions so they appear like they haven't
        been pressed.
    */
    public void resetAllGameActions() {

        for (int i=0; i<mouseActions.length; i++) {
            if (mouseActions[i] != null) {
                mouseActions[i].reset();
            }
        }
    }
    
    /**
        Gets the name of a mouse code.
    */
    public static String getMouseName(int mouseCode) {
        switch (mouseCode) {
            case MOUSE_BUTTON_1: return "Mouse Button 1";
            case MOUSE_BUTTON_2: return "Mouse Button 2";
            case MOUSE_BUTTON_3: return "Mouse Button 3";
            default: return "Unknown mouse code " + mouseCode;
        }
    }

    /**
        Gets the mouse code for the button specified in this
        MouseEvent.
    */
    public static int getMouseButtonCode(MouseEvent e) {
         switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                return MOUSE_BUTTON_1;
            case MouseEvent.BUTTON2:
                return MOUSE_BUTTON_2;
            case MouseEvent.BUTTON3:
                return MOUSE_BUTTON_3;
            default:
                return -1;
        }
    }


    private GameAction getMouseButtonAction(MouseEvent e) {
        int mouseCode = getMouseButtonCode(e);
        if (mouseCode != -1) {
             return mouseActions[mouseCode];
        }
        else {
             return null;
        }
    }

    // from the MouseListener interface
    public void mousePressed(MouseEvent e) {
        GameAction gameAction = getMouseButtonAction(e);
        if (gameAction != null) {
            mouseClikedAtPoint = e.getPoint();
            gameAction.press();
        }
        e.consume();
    }


    // from the MouseListener interface
    public void mouseReleased(MouseEvent e) {
        GameAction gameAction = getMouseButtonAction(e);
        if (gameAction != null) {
            gameAction.release();
        }
        e.consume();
    }


    // from the MouseListener interface
    public void mouseClicked(MouseEvent e) {
        e.consume();
    }

    public void mouseEntered(MouseEvent e) {
        e.consume();
    }

    public void mouseExited(MouseEvent e) {
        e.consume();
    }

}
