/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.ui;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import mini_game.MiniGame;

import mini_game.Sprite;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import zombiecrushsaga.ZombieCrushSaga.ZombieCrushSagaPropertyType;
import zombiecrushsaga.data.ZombieCrushDataModel;
import zombiecrushsaga.data.ZombieCrushRecord;
import zombiecrushsaga.file.ZombieCrushFileManager;
import static zombiecrushsaga.ZombieCrushSagaConstants.*;
import zombiecrushsaga.data.ZombieCrushLevelRecord;
import zombiecrushsaga.events.LevelSelectHandler;
import zombiecrushsaga.events.PlayGameHandler;
import zombiecrushsaga.events.PlayLevelHandler;
import zombiecrushsaga.events.QuitGameHandler;
import zombiecrushsaga.events.CloseLevelScoreHandler;
import zombiecrushsaga.events.PowerUpHandler;
import zombiecrushsaga.events.QuitLevelHandler;
import zombiecrushsaga.events.ResetGameHandler;
import zombiecrushsaga.events.ReturnToSplashScreenHandler;
import zombiecrushsaga.events.ScrollDownHandler;
import zombiecrushsaga.events.ScrollUpHandler;
import zombiecrushsaga.events.ZombieKeyHandler;
import zombiecrushsaga.events.ZombieMouseHandler;

/**
 *
 * @author LongchengNi
 */
public class ZombieCrushMiniGame extends MiniGame {
    // THE PLAYER RECORD FOR EACH LEVEL, WHICH LIVES BEYOND ONE SESSION

    private ZombieCrushRecord record;
    // HANDLES ERROR CONDITIONS
    private ZombieCrushErrorHandler errorHandler;
    // MANAGES LOADING OF LEVELS AND THE PLAYER RECORDS FILES
    private ZombieCrushFileManager fileManager;
    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;
    //ROLL UP VALUE,
    private int srollUp = 0;
    //PropertiesManager
    PropertiesManager props = PropertiesManager.getPropertiesManager();
    //CRUSHED STATE NUMBER
    private double crushedNumber;
    //IF IT IS TURE ENABLE ALL KEY AND MOUSE LISTENER. OTHERWISE DISABLE THEM
    protected boolean isEnable;

    /**
     * Accessor for isEnable
     *
     * @return true enable all panel listner false disable them
     */
    public boolean isEnable() {
        return isEnable;
    }
    /**
     * Accessor for current screen state
     * @return  Current screen state
     */
    public String getCurrentScreenState() {
        return currentScreenState;
    }
    /**
     * setter for isEnable
     *
     * @param isEnable
     */
    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * getter for CrushedNumber
     *
     * @return CrushedNumber
     */
    public double getCrushedNumber() {
        return crushedNumber;
    }

    /**
     * setter for crushedNumber
     *
     * @param crushedNumber we will set crushedNumber to this value
     */
    public void setCrushedNumber(double crushedNumber) {
        this.crushedNumber = crushedNumber;
    }

    /**
     * switch To play Game Screen
     */
    public synchronized void switchToPlayGameScreen(boolean enable) {
        currentScreenState = GAME_SCREEN_STATE;
        String opstate = INVISIBLE_STATE;
        String state = VISIBLE_STATE;
        
        if (!enable) {
            currentScreenState = LEVEL_SCORE_SCREEN_STATE;
            opstate = VISIBLE_STATE;
            state = INVISIBLE_STATE;
        }
        //switch game state
        if (currentScreenState.equals(GAME_SCREEN_STATE)) {
            data.beginGame();
        } else {
            data.notStartGame();
        }
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(currentScreenState);

        //ACTIVATE BUTTONS
        guiButtons.get(QUIT_LEVEL_BUTTON_TYPE).setState(state);
        guiButtons.get(QUIT_LEVEL_BUTTON_TYPE).setEnabled(enable);
        guiButtons.get(POWER_UP_BUTTON_TYPE).setState(state);
        guiButtons.get(POWER_UP_BUTTON_TYPE).setEnabled(enable);
        guiDecor.get(MOVE_TYPE).setState(state);
        guiDecor.get(MOVE_TYPE).setEnabled(enable);
        guiDecor.get(STAR_METER_TYPE).setState(state);
        guiDecor.get(STAR_METER_TYPE).setEnabled(enable);
        guiDecor.get(SCORE_TYPE).setState(state);
        guiDecor.get(SCORE_TYPE).setEnabled(enable);

        // DEACTIVATE BUTTONS
        guiButtons.get(PLAY_LEVEL_BUTTON_TYPE).setState(opstate);
        guiButtons.get(PLAY_LEVEL_BUTTON_TYPE).setEnabled(!enable);
        guiButtons.get(CLOSE_LEVEL_SCORE_BUTTON_TYPE).setState(opstate);
        guiButtons.get(CLOSE_LEVEL_SCORE_BUTTON_TYPE).setEnabled(!enable);
        //PLAY GAME START SONG
        if (enable) {
            audio.play(ZombieCrushSagaPropertyType.GAMEPLAY_SONG_CUE.toString(), true);
            audio.stop(ZombieCrushSagaPropertyType.SPLASH_SCREEN_SONG_CUE.toString());
        } else {
            audio.play(ZombieCrushSagaPropertyType.SPLASH_SCREEN_SONG_CUE.toString(), true);
            audio.stop(ZombieCrushSagaPropertyType.GAMEPLAY_SONG_CUE.toString());
        }
    }

    /**
     * switch To Level Score Screen
     */
    public synchronized void switchToLevelScoreScreen(boolean enable) {
        currentScreenState = LEVEL_SCORE_SCREEN_STATE;
        String opstate = INVISIBLE_STATE;
        String state = VISIBLE_STATE;
        if (!enable) {
            currentScreenState = SAGA_SCREEN_STATE;
            opstate = VISIBLE_STATE;
            state = INVISIBLE_STATE;
        }

        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(currentScreenState);
        //GET MAX LEVEL
        int maxLevel = ((ZombieCrushDataModel) data).getMaxLevel();

        //DEACTIVATE LEVEL BUTTONS
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        for (int i = 0; i < maxLevel; i++) {
            guiButtons.get(levels.get(i)).setState(opstate);
            guiButtons.get(levels.get(i)).setEnabled(!enable);
        }

        //DEACTIVATE THE SCROLL UP AND DOWN BUTTONS
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setState(opstate);
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setEnabled(!enable);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setState(opstate);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(!enable);
        guiButtons.get(RETURN_TO_SPLASH_BUTTON_TYPE).setState(opstate);
        guiButtons.get(RETURN_TO_SPLASH_BUTTON_TYPE).setEnabled(!enable);
        guiButtons.get(SAGA_QUIT_GAME_BUTTON_TYPE).setState(opstate);
        guiButtons.get(SAGA_QUIT_GAME_BUTTON_TYPE).setEnabled(!enable);

        //ACTIVATE PLAY LEVEL BUTTON
        guiButtons.get(PLAY_LEVEL_BUTTON_TYPE).setEnabled(enable);
        guiButtons.get(PLAY_LEVEL_BUTTON_TYPE).setState(state);
        guiButtons.get(CLOSE_LEVEL_SCORE_BUTTON_TYPE).setEnabled(enable);
        guiButtons.get(CLOSE_LEVEL_SCORE_BUTTON_TYPE).setState(state);
        //RESET TARGET SCORE IF NEW HIGHEST SCORE PRODUCED IN THE PREVIOUS GAME
        if (!enable) {
            updateTargetScore();
        }
    }

    /**
     * Switch Screen from other screen to saga Screen
     */
    public synchronized void switchToSagaScreen(boolean enable) {
        currentScreenState = SAGA_SCREEN_STATE;
        String opstate = INVISIBLE_STATE;
        String state = VISIBLE_STATE;
        if (enable) {
            //INITIALIZE THE BLANK TILE IMAGE
            ((ZombieCrushDataModel) data).initBlankImage();
        }
        if (!enable) {
            currentScreenState = SPLASH_SCREEN_STATE;
            opstate = VISIBLE_STATE;
            state = INVISIBLE_STATE;
        }

        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(currentScreenState);

        //ACTIVATE THE SCROLL UP AND DOWN BUTTONS AND RETURN TO SPLASH SCREEN BUTTON
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setState(state);
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setEnabled(enable);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setState(state);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(enable);
        guiButtons.get(RETURN_TO_SPLASH_BUTTON_TYPE).setState(state);
        guiButtons.get(RETURN_TO_SPLASH_BUTTON_TYPE).setEnabled(enable);
        guiButtons.get(SAGA_QUIT_GAME_BUTTON_TYPE).setState(state);
        guiButtons.get(SAGA_QUIT_GAME_BUTTON_TYPE).setEnabled(enable);
        //GET MAX LEVEL
        int maxLevel = ((ZombieCrushDataModel) data).getMaxLevel();

        //ACTIVATE LEVEL BUTTONS
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        for (int i = 0; i < maxLevel; i++) {
            guiButtons.get(levels.get(i)).setState(state);
            guiButtons.get(levels.get(i)).setEnabled(enable);
        }

        // DEACTIVATE SPLASH BUTTONS
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(opstate);
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(!enable);
        guiButtons.get(RESET_GAME_BUTTON_TYPE).setState(opstate);
        guiButtons.get(RESET_GAME_BUTTON_TYPE).setEnabled(!enable);
        guiButtons.get(QUIT_GAME_BUTTON_TYPE).setState(opstate);
        guiButtons.get(QUIT_GAME_BUTTON_TYPE).setEnabled(!enable);
        
    }

    /**
     * Override methods initAudioContent() initData() initGUIControls()
     * initGUIHandlers() reset() updateGUI()
     */
    /**
     * initialize audio contents
     */
    @Override
    public void initAudioContent() {
        try {

            // LOAD ALL THE AUDIO
            loadAudioCue(ZombieCrushSagaPropertyType.SELECT_AUDIO_CUE);
            loadAudioCue(ZombieCrushSagaPropertyType.MATCH_AUDIO_CUE);
            loadAudioCue(ZombieCrushSagaPropertyType.NO_MATCH_AUDIO_CUE);
            loadAudioCue(ZombieCrushSagaPropertyType.BLOCKED_TILE_AUDIO_CUE);
            loadAudioCue(ZombieCrushSagaPropertyType.UNDO_AUDIO_CUE);
            loadAudioCue(ZombieCrushSagaPropertyType.WIN_AUDIO_CUE);
            loadAudioCue(ZombieCrushSagaPropertyType.LOSS_AUDIO_CUE);
            loadAudioCue(ZombieCrushSagaPropertyType.SPLASH_SCREEN_SONG_CUE);
            loadAudioCue(ZombieCrushSagaPropertyType.GAMEPLAY_SONG_CUE);

            // PLAY THE WELCOME SCREEN SONG
            audio.play(ZombieCrushSagaPropertyType.SPLASH_SCREEN_SONG_CUE.toString(), true);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException e) {
            errorHandler.processError(ZombieCrushSagaPropertyType.AUDIO_FILE_ERROR);
        }
    }
    
    @Override
    public void initData() {
        // INIT OUR ERROR HANDLER
        errorHandler = new ZombieCrushErrorHandler(window);

        // INIT OUR FILE MANAGER
        fileManager = new ZombieCrushFileManager(this);

        // INIT OUR DATA MANAGER
        data = new ZombieCrushDataModel(this);

        // LOAD THE PLAYER'S RECORD FROM A FILE
        record = fileManager.loadRecord();

        // LOAD THE GAME DIMENSIONS
        data.setGameDimensions(GameWidth, GameHeight);

        // THIS WILL CHANGE WHEN WE LOAD A LEVEL
        boundaryLeft = Integer.parseInt(props.getProperty(ZombieCrushSagaPropertyType.GAME_LEFT_OFFSET.toString()));
        boundaryTop = Integer.parseInt(props.getProperty(ZombieCrushSagaPropertyType.GAME_TOP_OFFSET.toString()));
        boundaryRight = GameWidth - boundaryLeft;
        boundaryBottom = GameHeight;
        isEnable = true;
        
    }

    /**
     * initialize GUI controls including all buttons and decors
     */
    @Override
    public void initGUIControls() {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        SpriteType sT;
        Sprite s;

        // FIRST PUT THE ICON IN THE WINDOW
        String imgPath = props.getProperty(ZombieCrushSagaPropertyType.IMG_PATH);
        String windowIconFile = props.getProperty(ZombieCrushSagaPropertyType.WINDOW_ICON);
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);

        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new ZombieCrushPanel(this, (ZombieCrushDataModel) data);

        //LOAD LEVELS
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        ArrayList<String> levelImageNames = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_IMAGE_OPTIONS);
        ArrayList<String> levelMouseOverImageNames = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_MOUSE_OVER_IMAGE_OPTIONS);
        for (int i = 0; i < levels.size(); i++) {
            sT = new SpriteType(LEVEL_SELECT_BUTTON_TYPE);
            img = loadImageWithColorKey(imgPath + levelImageNames.get(i), COLOR_KEY);
            sT.addState(VISIBLE_STATE, img);
            img = loadImageWithColorKey(imgPath + levelMouseOverImageNames.get(i), COLOR_KEY);
            sT.addState(MOUSE_OVER_STATE, img);
            s = new Sprite(sT, points[i].x, points[i].y, 0, 0, INVISIBLE_STATE);
            guiButtons.put(levels.get(i), s);
        }

        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        img = loadImage(imgPath + props.getProperty(ZombieCrushSagaPropertyType.SPLASH_SCREEN_IMAGE_NAME));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(SPLASH_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(ZombieCrushSagaPropertyType.SAGA_SCREEN_IMAGE_NAME));
        sT.addState(SAGA_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_SCREEN_IMAGE_NAME));
        sT.addState(LEVEL_SCORE_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(ZombieCrushSagaPropertyType.GAME_SCREEN_IMAGE_NAME));
        sT.addState(GAME_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(ZombieCrushSagaPropertyType.ABOUT_SCREEN_IMAGE_NAME));
        sT.addState(ABOUT_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, SPLASH_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);

        //AVAIABLE MOVE DECOR
        String availableMove = props.getProperty(ZombieCrushSagaPropertyType.MOVE_IMAGE_NAME);
        sT = new SpriteType(MOVE_TYPE);
        img = loadImage(imgPath + availableMove);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, MOVE_X, MOVE_Y, 0, 0, INVISIBLE_STATE);
        guiDecor.put(MOVE_TYPE, s);

        //SCORE DECOR
        String scoreDecor = props.getProperty(ZombieCrushSagaPropertyType.SCORE_IMAGE_NAME);
        sT = new SpriteType(SCORE_TYPE);
        img = loadImage(imgPath + scoreDecor);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, SCORE_X, SCORE_Y, 0, 0, INVISIBLE_STATE);
        guiDecor.put(SCORE_TYPE, s);

        //STAR METER DECOR
        String starDecor = props.getProperty(ZombieCrushSagaPropertyType.STAR_METER_IMAGE_NAME);
        sT = new SpriteType(STAR_METER_TYPE);
        img = loadImage(imgPath + starDecor);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, STAR_METER_X, STAR_METER_Y, 0, 0, INVISIBLE_STATE);
        guiDecor.put(STAR_METER_TYPE, s);

        //ADD PLAY GAME BUTTON
        ButtonFactory(PLAY_GAME_BUTTON_TYPE,
                ZombieCrushSagaPropertyType.PLAY_GAME_BUTTON_IMAGE_NAME,
                ZombieCrushSagaPropertyType.PLAY_GAME_BUTTON_MOUSE_OVER_IMAGE_NAME,
                PLAY_GAME_BUTTON_X, PLAY_GAME_BUTTON_Y, VISIBLE_STATE);

        //ADD RESET GAME BUTTONF
        ButtonFactory(RESET_GAME_BUTTON_TYPE,
                ZombieCrushSagaPropertyType.RESET_BUTTON_IMAGE_NAME,
                ZombieCrushSagaPropertyType.RESET_BUTTON_MOUSE_OVER_IMAGE_NAME,
                RESET_GAME_BUTTON_X, RESET_GAME_BUTTON_Y, VISIBLE_STATE);

        //ADD QUIT GAME BUTTON      
        ButtonFactory(QUIT_GAME_BUTTON_TYPE,
                ZombieCrushSagaPropertyType.QUIT_GAME_BUTTON_IMAGE_NAME,
                ZombieCrushSagaPropertyType.QUIT_GAME_BUTTON_MOUSE_OVER_IMAGE_NAME,
                QUIT_GAME_BUTTON_X, QUIT_GAME_BUTTON_Y, VISIBLE_STATE);

        //ADD SROLLUP GAME BUTTON
        ButtonFactory(SCROLL_UP_BUTTON_TYPE,
                ZombieCrushSagaPropertyType.SCROLL_UP_BUTTON_IMAGE_NAME,
                ZombieCrushSagaPropertyType.SCROLL_UP_BUTTON_MOUSE_OVER_IMAGE_NAME,
                SCROLL_UP_BUTTON_X, SCROLL_UP_BUTTON_Y, INVISIBLE_STATE);

        //ADD SROLLDOWN GAME BUTTON
        ButtonFactory(SCROLL_DOWN_BUTTON_TYPE,
                ZombieCrushSagaPropertyType.SCROLL_DOWN_BUTTON_IMAGE_NAME,
                ZombieCrushSagaPropertyType.SCROLL_DOWN_BUTTON_MOUSE_OVER_IMAGE_NAME,
                SCROLL_DOWN_BUTTON_X, SCROLL_DOWN_BUTTON_Y, INVISIBLE_STATE);

        //ADD RETURN TO SPLASH SCREEN BUTTON
        ButtonFactory(RETURN_TO_SPLASH_BUTTON_TYPE,
                ZombieCrushSagaPropertyType.RETURN_TO_SPLASH_BUTTON_IMAGE_NAME,
                ZombieCrushSagaPropertyType.RETURN_TO_SPLASH_BUTTON_MOUSE_OVER_IMAGE_NAME,
                RETURN_TO_SPLASH_BUTTON_X, RETURN_TO_SPLASH_BUTTON_Y, INVISIBLE_STATE);

        //ADD QUIT GAME BUTTON IN SAGA SCREEN
        ButtonFactory(SAGA_QUIT_GAME_BUTTON_TYPE,
                ZombieCrushSagaPropertyType.SAGA_QUIT_GAME_BUTTON_IMAGE_NAME,
                ZombieCrushSagaPropertyType.SAGA_QUIT_GAME_BUTTON_MOUSE_OVER_IMAGE_NAME,
                SAGA_QUIT_GAME_BUTTON_X, SAGA_QUIT_GAME_BUTTON_Y, INVISIBLE_STATE);

        //PLAY LEVEL Button
        ButtonFactory(PLAY_LEVEL_BUTTON_TYPE,
                ZombieCrushSagaPropertyType.PLAY_LEVEL_BUTTON_IMAGE_NAME,
                ZombieCrushSagaPropertyType.PLAY_LEVEL_BUTTON_MOUSE_OVER_IMAGE_NAME,
                PLAY_LEVEL_BUTTON_X, PLAY_LEVEL_BUTTON_Y, INVISIBLE_STATE);
        //CLOSE LEVEL SCORE Button
        ButtonFactory(CLOSE_LEVEL_SCORE_BUTTON_TYPE,
                ZombieCrushSagaPropertyType.CLOSE_LEVEL_SCORE_BUTTON_IMAGE_NAME,
                ZombieCrushSagaPropertyType.CLOSE_LEVEL_SCORE_BUTTON_MOUSE_OVER_IMAGE_NAME,
                CLOSE_LEVEL_SCORE_BUTTON_X, CLOSE_LEVEL_SCORE_BUTTON_Y, INVISIBLE_STATE);
        //QUIT LEVEL BUTTON
        ButtonFactory(QUIT_LEVEL_BUTTON_TYPE,
                ZombieCrushSagaPropertyType.QUIT_LEVEL_BUTTON_IMAGE_NAME,
                ZombieCrushSagaPropertyType.QUIT_LEVEL_BUTTON_MOUSE_OVER_IMAGE_NAME,
                QUIT_LEVEL_BUTTON_X, QUIT_LEVEL_BUTTON_Y, INVISIBLE_STATE);
        //POWER_UP BUTTON
        ButtonFactory(POWER_UP_BUTTON_TYPE,
                ZombieCrushSagaPropertyType.POWER_UP_IMAGE_NAME,
                ZombieCrushSagaPropertyType.POWER_UP_MOUSE_OVER_IMAGE_NAME,
                POWER_UP_BUTTON_X, POWER_UP_BUTTON_Y, INVISIBLE_STATE);
        //POWER_UP BUTTON
        ButtonFactory(WIN_BUTTON_TYPE,
                ZombieCrushSagaPropertyType.WIN_IMAGE_NAME,
                ZombieCrushSagaPropertyType.WIN_IMAGE_NAME,
                WIN_BUTTON_X, WIN_BUTTON_Y, INVISIBLE_STATE);
        //POWER_UP BUTTON
        ButtonFactory(LOSE_BUTTON_TYPE,
                ZombieCrushSagaPropertyType.LOSE_IMAGE_NAME,
                ZombieCrushSagaPropertyType.LOSE_IMAGE_NAME,
                LOSE_BUTTON_X, LOSE_BUTTON_Y, INVISIBLE_STATE);
 
    }

    /**
     * initialize GUIhandlers to window and all buttons
     */
    @Override
    public void initGUIHandlers() {

        // WE'LL HAVE A CUSTOM RESPONSE FOR WHEN THE USER CLOSES THE WINDOW
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationToMiddle(window);
        QuitGameHandler qgh = new QuitGameHandler(this);
        window.addWindowListener(qgh);

        // LEVEL BUTTON EVENT HANDLERS
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        for (int i = 0; i < levels.size(); i++) {
            LevelSelectHandler lsh = new LevelSelectHandler(this, i + 1);
            guiButtons.get(levels.get(i)).setActionListener(lsh);
            guiButtons.get(levels.get(i)).setEnabled(false);
        }
        //SET HANDLER FOR QUIT GAME BUTTON
        guiButtons.get(QUIT_GAME_BUTTON_TYPE).setActionListener(qgh);
        guiButtons.get(SAGA_QUIT_GAME_BUTTON_TYPE).setActionListener(qgh);
        //SET HANDLER FOR PLAY GAME BUTTON  
        PlayGameHandler pgh = new PlayGameHandler(this);
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setActionListener(pgh);

        //SET HANDLER FOR RESET GAME BUTTON
        ResetGameHandler rgh = new ResetGameHandler(this);
        guiButtons.get(RESET_GAME_BUTTON_TYPE).setActionListener(rgh);

        //SET HANDLER FOR SCROLL UP BUTTON
        ScrollUpHandler suh = new ScrollUpHandler(this);
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setActionListener(suh);

        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        ZombieKeyHandler zkh = new ZombieKeyHandler(this);
        this.setKeyListener(zkh);

        //MOUSE HANDLER -PROVIDE FOR MOUSE DRAG FUNCTION
        ZombieMouseHandler zmh = new ZombieMouseHandler(this);
        this.getCanvas().addMouseListener(zmh);
        this.getCanvas().addMouseMotionListener(zmh);

        //SET HANDLER FOR SCROLL DOWN BUTTON
        ScrollDownHandler sdh = new ScrollDownHandler(this);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setActionListener(sdh);

        //SET HANDLER FOR RETURN TO SPLASH SCREEN
        ReturnToSplashScreenHandler rtss = new ReturnToSplashScreenHandler(this);
        guiButtons.get(RETURN_TO_SPLASH_BUTTON_TYPE).setActionListener(rtss);

        //SET PLAYLEVEL HANDLER
        PlayLevelHandler plh = new PlayLevelHandler(this);
        guiButtons.get(PLAY_LEVEL_BUTTON_TYPE).setActionListener(plh);

        //SET CLOSE LEVEL SCORE HANDLER
        CloseLevelScoreHandler clh = new CloseLevelScoreHandler(this);
        guiButtons.get(CLOSE_LEVEL_SCORE_BUTTON_TYPE).setActionListener(clh);

        //SET QUIT LEVEL HANDLER
        QuitLevelHandler qlh = new QuitLevelHandler(this);
        guiButtons.get(QUIT_LEVEL_BUTTON_TYPE).setActionListener(qlh);
        guiButtons.get(LOSE_BUTTON_TYPE).setActionListener(qlh);
        guiButtons.get(WIN_BUTTON_TYPE).setActionListener(qlh);
        
        PowerUpHandler puh = new PowerUpHandler(this);
        guiButtons.get(POWER_UP_BUTTON_TYPE).setActionListener(puh);
    }

    /**
     * Delete game history and create a new game record
     *
     * @param game Zombie Crush Mini Game
     */
    @Override
    public void reset() {
        //Create a new Record
        ZombieCrushRecord record = new ZombieCrushRecord(this);
        //Add 10 levels with default state
        for (int i = 0; i < 10; i++) {
            ZombieCrushLevelRecord rec = new ZombieCrushLevelRecord(i + 1);
            record.addLevelRcord(rec.getLevelNumber(), rec);
        }
        ((ZombieCrushDataModel) data).setMaxLevel(1);
        getFileManager().save(record);
        this.record = getFileManager().loadRecord();
        
    }
    
    @Override
    public void updateGUI() {
        // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext()) {
            Sprite button = buttonsIt.next();
            // ARE WE ENTERING A BUTTON?
            switch (button.getState()) {
                case VISIBLE_STATE:
                    if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                        button.setState(MOUSE_OVER_STATE);
                    }
                    break;
                case MOUSE_OVER_STATE:
                    if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                        button.setState(VISIBLE_STATE);
                    }
                    break;
            }
        }
        
    }

    /**
     * This helper method loads the audio file associated with audioCueType,
     * which should have been specified via an XML properties file.
     */
    private void loadAudioCue(ZombieCrushSagaPropertyType audioCueType)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException,
            InvalidMidiDataException, MidiUnavailableException {
        String audioPath = props.getProperty(ZombieCrushSagaPropertyType.AUDIO_PATH);
        String cue = props.getProperty(audioCueType.toString());
        audio.loadAudio(audioCueType.toString(), audioPath + cue);
    }

    /**
     * Mutator for RollUp
     */
    public void setScollUp(int srollUp) {
        this.srollUp = srollUp;
    }

    /**
     * accessor for RollUp
     */
    public int getScollUp() {
        return this.srollUp;
    }

    /**
     * @return record of the game
     */
    public ZombieCrushRecord getRecord() {
        return record;
    }

    /**
     *
     * @param BUTTON_TYPE TYPE OF THE BUTTON
     * @param VISIBLE_IMG IMG FOR BUTTON'S VISIBLE STATE
     * @param MOUSE_OVER_IMG IMG FOR BUTTONS'MOUSE_OVER STATE
     * @param x X-COORDINATE FOR SPRITE
     * @param y Y-CORORDINATE FOR SPRITE
     * @param state ORIGINAL STATE OF BUTTON
     */
    public void ButtonFactory(String BUTTON_TYPE, ZombieCrushSagaPropertyType VISIBLE_IMG, ZombieCrushSagaPropertyType MOUSE_OVER_IMG, int x, int y, String state) {
        String imgPath = props.getProperty(ZombieCrushSagaPropertyType.IMG_PATH);
        BufferedImage img;
        String visible_img = props.getProperty(VISIBLE_IMG);
        SpriteType sT = new SpriteType(BUTTON_TYPE);
        img = loadImage(imgPath + visible_img);
        sT.addState(VISIBLE_STATE, img);
        String mouse_over_img = props.getProperty(MOUSE_OVER_IMG);
        img = loadImage(imgPath + mouse_over_img);
        sT.addState(MOUSE_OVER_STATE, img);
        Sprite s = new Sprite(sT, x, y, 0, 0, state);
        if (state.equals(INVISIBLE_STATE)) {
            s.setEnabled(false);
        }
        guiButtons.put(BUTTON_TYPE, s);
    }

    /**
     * ACCESSOR FOR FILE MANAGER
     *
     * @return ZombieCrushFileManager
     */
    public ZombieCrushFileManager getFileManager() {
        return this.fileManager;
    }

    /**
     * Set Window Location to middle
     *
     * @param window frame of the game
     */
    private void setLocationToMiddle(JFrame window) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        double screenHeight = tk.getScreenSize().getHeight();
        double screenWidth = tk.getScreenSize().getWidth();
        window.setLocation((int) (screenWidth - GameWidth) / 2, (int) (screenHeight - GameHeight - GameLocationMargin) / 2);
        
    }

    /**
     * update target score
     */
    public void updateTargetScore() {
        int currentLevel = ((ZombieCrushDataModel) data).getCurrentLevel();
        int currentScore = ((ZombieCrushDataModel) data).getCurrentScore();
        int TargetScore = getRecord().getLevelRcord(currentLevel - 1).getTargetScore();
        if (currentScore > TargetScore) {
            getRecord().getLevelRcord(currentLevel - 1).setTargetScore(currentScore);
        }
        this.fileManager.save(record);
    }

    /**
     * This method is used to change the cursor of mouse
     *
     * @param true change to custom cursor false change to default cursor
     */
    public void changeCursor(boolean b) {
        if (b) {
            //get hammer image
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Sprite s = getGUIButtons().get(POWER_UP_BUTTON_TYPE);
            Image image = s.getSpriteType().getStateImage(VISIBLE_STATE);
            //create hammer mouse cursor
            Cursor c = toolkit.createCustomCursor(image, new Point(getCanvas().getX(),
                    getCanvas().getY()), "img");
            //set hammer mouse cursor
            getCanvas().setCursor(c);
        } else {
            //set default mouse cursor
            Cursor dc = new Cursor(Cursor.DEFAULT_CURSOR);
            getCanvas().setCursor(dc);
        }
    }
}
