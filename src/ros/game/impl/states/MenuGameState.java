/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.impl.states;

import ros.game.engine.ResourceManager;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.midi.Sequence;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import ros.game.engine.WindowedScreenManager;
import ros.game.engine.database.DatabaseManager;
import ros.game.engine.input.GameAction;
import ros.game.engine.input.InputManager;
import ros.game.engine.sound.MidiPlayer;
import ros.game.engine.sound.Sound;
import ros.game.engine.sound.SoundManager;
import ros.game.engine.GameState;
import ros.game.engine.GameStateManager;

/**
 *
 * @author Ros
 */
public class MenuGameState implements GameState, ActionListener{

    private String stateChange;
    private WindowedScreenManager screen;
    private SoundManager sound;
    private MidiPlayer music;
    private DatabaseManager database;

    //Music
    private Sequence musica;
    //sound
    private Sound btnSound;

    //Background and Menu button Images
    private Image backImage;
    private Image buttonImage[];

    //Menu Buttons and Actions
    private JPanel  buttonSpace;
    private JButton playButton;   //0
    private JButton configButton; //1
    private JButton top15Button;  //2
    private JButton quitButton;   //3
    protected GameAction ShowConfigAction;
    protected GameAction showTop15Action;
    protected GameAction playAction;
    protected GameAction quitAction;

    //Score Panel Button and Action (for show High Score)
    private JPanel top15Panel;
    private JScrollPane scroll;
    private JPanel centerPanel;
    private JButton hideHighScoreBtn;
    private GameAction hideTop15Action;

    //Config Panel Button and Action (for set music and sound)
    private JPanel configPanel;
    private JButton hideConfigBtn;
    private JToggleButton toggleMusicBtn;
    private JToggleButton toggleSoundBtn;
    private GameAction toggleMusicAction;
    private GameAction toggleSoundAction;
    private GameAction hideConfigAction;

    public MenuGameState(WindowedScreenManager screen, SoundManager soundManager, MidiPlayer midiPlayer, DatabaseManager database) {
        this.database = database;
        this.screen = screen;
        this.sound = soundManager;
        this.music = midiPlayer;

        initGameActions();

        createHighScorePanel();
        createConfigPanel();
    }
    
    public String getName() {
        return "Menu";
    }

    public String checkForStateChange() {
        return stateChange;
    }

    public void loadResources(ResourceManager resourceManager) {
         backImage = resourceManager.loadImage("Splash.jpg");

         buttonImage = new Image[4];
         buttonImage[0] = resourceManager.loadImage("menu/start.png");
         buttonImage[1] = resourceManager.loadImage("menu/config.png");
         buttonImage[2] = resourceManager.loadImage("menu/scores.png");
         buttonImage[3] = resourceManager.loadImage("menu/exit.png");

         musica = resourceManager.loadSequence("sounds/loop.MID");
         btnSound = resourceManager.loadSound("sounds/prize.wav");
    }

    public void start(InputManager inputManager) {
        //per permettere di ritornare allo stato di menu
        stateChange = null;
        createHighScorePanel();
        
        // create buttons
        playButton = createButton("Start", "Start new game",0);
        configButton = createButton("Options", "Change Settings",1);
        top15Button = createButton("Top 15", "Watch the best scores",2);
        quitButton = createButton("Quit", "Quit game",3);

        buttonSpace = new JPanel();
        buttonSpace.setOpaque(false);
        buttonSpace.add(playButton);
        buttonSpace.add(configButton);
        buttonSpace.add(top15Button);
        buttonSpace.add(quitButton);

        //JFrame frame = screen.getFullScreenWindow();
        JFrame frame = screen.getWindow();
        Container contentPane = frame.getContentPane();

        // make sure the content pane is transparent
        if (contentPane instanceof JComponent) {
            ((JComponent)contentPane).setOpaque(false);
        }

        // add components to the screen's content pane
        contentPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        contentPane.add(buttonSpace);

        // explicitly layout components (needed on some systems)
        frame.validate();

        //play music
        music.setPaused(false);
        music.play(musica, true);

    }

    public void stop() {
       //
    }

    public void update(long elapsedTime) {
       checkInput();
    }

    public void draw(Graphics2D g) {
        
        g.drawImage(backImage, 0, 0, null);
 
        g.setColor(Color.red);
        g.drawString("Loaded!",470,200);

        JFrame frame = screen.getWindow();

        // the layered pane contains things like popups (tooltips,
        // popup menus) and the content pane.
        frame.getLayeredPane().paintComponents(g);

        //System.out.println(screen.getWidth()+"x"+screen.getHeight());
    }

    /**
    *   Creates a Swing JButton. The image used for the button is
    *   located at "../images/menu/" + name + ".png". The image is
    *   modified to create a "default" look (translucent) and a
    *   "pressed" look (moved down and to the right).
    *   <p>The button doesn't use Swing's look-and-feel and
    *   instead just uses the image.
    */
    public JButton createButton(String name, String toolTip, int indexImage) {

        // load the image
        ImageIcon iconRollover = new ImageIcon(buttonImage[indexImage]);
        int w = iconRollover.getIconWidth();
        int h = iconRollover.getIconHeight();

        // get the cursor for this button
        Cursor cursor =
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

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
    
    /**
        Called by the AWT event dispatch thread when a button is
        pressed.
    */
    public void actionPerformed(ActionEvent e) {
         Object src = e.getSource();
        if (src == quitButton) {
            quitAction.tap();
        }
        else if (src == configButton) {
            ShowConfigAction.tap();
        }
        else if(src == top15Button){
            showTop15Action.tap();
        }
        else if (src == playButton) {
            playAction.tap();
        }
        else if(src == hideHighScoreBtn){
            hideTop15Action.tap();
        }
        else if(src == hideConfigBtn){
            hideConfigAction.tap();
        }
        else if(src == toggleMusicBtn){
            toggleMusicAction.tap();
        }
        else if(src == toggleSoundBtn){
            toggleSoundAction.tap();
        }
    }

    private void checkInput() {
        if (showTop15Action.isPressed()) {
            top15Panel.setVisible(true);
            sound.play(btnSound);
        }
        else if (hideTop15Action.isPressed()) {
            top15Panel.setVisible(false);
            sound.play(btnSound);
        }
        else if (quitAction.isPressed()) {
            stateChange = GameStateManager.EXIT_GAME;
            sound.play(btnSound);
            return;
        }
        else if (playAction.isPressed()) {
            sound.play(btnSound);
            music.stop();

            configPanel.setVisible(false);
            top15Panel.setVisible(false);
            buttonSpace.setVisible(false);
            
            stateChange = "Game";
            return;
        }
        else if (ShowConfigAction.isPressed()) {
            configPanel.setVisible(true);
            sound.play(btnSound);
        }
        else if (hideConfigAction.isPressed()) {
           configPanel.setVisible(false);
           sound.play(btnSound);
        }
        else if (toggleMusicAction.isPressed()) {
           if(music.isMusicEnable()){
               music.setEnable(false);
               music.stop();
               toggleMusicBtn.setText("Music Off");
           }
           else{
               music.setEnable(true);
               sound.play(btnSound);
               music.play(musica, true);
               toggleMusicBtn.setText("Music On");
           }
           
        }
        else  if (toggleSoundAction.isPressed()) {
            if(sound.isSoundEnable()){
               sound.setEnable(false);
               toggleSoundBtn.setText("Sound Off");
            }
           else{
               sound.setEnable(true);
               toggleSoundBtn.setText("Sound On");
               sound.play(btnSound);
           }
            
        }
         
    }

    private synchronized void createConfigPanel() {

        //create the Title of Dialog
        JLabel title = new JLabel("Config");
        title.setHorizontalAlignment(JLabel.CENTER);

        // create the panel containing JToggleButtons.
        JPanel centerPanel1 = new JPanel(new GridLayout(2,1));

        //set buttons
        toggleMusicBtn = new JToggleButton("Music On");
        toggleMusicBtn.addActionListener(this);
        toggleSoundBtn = new JToggleButton("Sound On");
        toggleSoundBtn.addActionListener(this);

        centerPanel1.add(toggleMusicBtn);
        centerPanel1.add(toggleSoundBtn);

        // create the panel containing the OK button
        JPanel bottomPanel = new JPanel(new FlowLayout());
        hideConfigBtn = new JButton("OK");
        hideConfigBtn.setFocusable(false);
        hideConfigBtn.addActionListener(this);
        bottomPanel.add(hideConfigBtn);

        // create the Dialog border
        Border border = BorderFactory.createLineBorder(Color.black);

        configPanel = new JPanel(new BorderLayout());
        configPanel.add(title, BorderLayout.NORTH);
        configPanel.add(centerPanel1, BorderLayout.CENTER);
        configPanel.add(bottomPanel, BorderLayout.SOUTH);
        configPanel.setBorder(border);
        configPanel.setVisible(false);
        Dimension dimensione = configPanel.getPreferredSize();
        configPanel.setSize(dimensione.width+30, dimensione.height+30);


        // center the dialog
        configPanel.setLocation(550,260);

        // add the dialog to the "modal dialog" layer of the
        // screen's layered pane.
        screen.getWindow().getLayeredPane().add(configPanel,
                JLayeredPane.MODAL_LAYER);
    }

    private synchronized void createHighScorePanel() {
        top15Panel = new JPanel(new BorderLayout());
        
        //create the Title of Dialog
        JLabel title = new JLabel("Top 15");
        title.setHorizontalAlignment(JLabel.CENTER);

        // create the panel containing Table
        centerPanel = new JPanel(new FlowLayout());

        //enlarge table size
        scroll = new JScrollPane(database.getTop15Table());
        Dimension dimensione = scroll.getPreferredSize();
        dimensione.setSize(dimensione.width-80, dimensione.height-140);
        scroll.setPreferredSize(dimensione);

        centerPanel.add(scroll);

        top15Panel.add(centerPanel, BorderLayout.CENTER);
        top15Panel.validate();

        // create the panel containing the OK button
        JPanel bottomPanel = new JPanel(new FlowLayout());
        hideHighScoreBtn = new JButton("OK");
        hideHighScoreBtn.setFocusable(false);
        hideHighScoreBtn.addActionListener(this);
        bottomPanel.add(hideHighScoreBtn);

        // create the Dialog border
        Border border = BorderFactory.createLineBorder(Color.black);
        
        top15Panel.add(title, BorderLayout.NORTH);
        top15Panel.add(bottomPanel, BorderLayout.SOUTH);
        top15Panel.setBorder(border);
        top15Panel.setVisible(false);
        top15Panel.setSize(top15Panel.getPreferredSize());

        // center the dialog
        top15Panel.setLocation(50,200);

        // add the dialog to the "modal dialog" layer of the
        // screen's layered pane.
        screen.getWindow().getLayeredPane().add(top15Panel,
                JLayeredPane.MODAL_LAYER);
    }

    private void initGameActions() {
        quitAction = new GameAction("quit", GameAction.DETECT_INITAL_PRESS_ONLY);
        showTop15Action = new GameAction("top15", GameAction.DETECT_INITAL_PRESS_ONLY);
        hideTop15Action = new GameAction("hideHighScoreDialog", GameAction.DETECT_INITAL_PRESS_ONLY);
        ShowConfigAction = new GameAction("config", GameAction.DETECT_INITAL_PRESS_ONLY);
        playAction = new GameAction("startGame", GameAction.DETECT_INITAL_PRESS_ONLY);
        toggleMusicAction = new GameAction("toggleMusic", GameAction.DETECT_INITAL_PRESS_ONLY);
        toggleSoundAction = new GameAction("toggleSound", GameAction.DETECT_INITAL_PRESS_ONLY);
        hideConfigAction = new GameAction("hideConfigDialog", GameAction.DETECT_INITAL_PRESS_ONLY);
    }
}
