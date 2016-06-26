/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga;


import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 *
 * @author LongchengNi
 */
public class ZombieCrushSaga {
    // THIS HAS THE FULL USER INTERFACE AND ONCE IN EVENT
    // HANDLING MODE, BASICALLY IT BECOMES THE FOCAL
    // POINT, RUNNING THE UI AND EVERYTHING ELSE

    static ZombieCrushMiniGame miniGame = new ZombieCrushMiniGame();
    // WE'LL LOAD ALL THE UI AND ART PROPERTIES FROM FILES,
    // BUT WE'LL NEED THESE VALUES TO START THE PROCESS
    static String PROPERTY_TYPES_LIST = "property_types.txt";
    static String UI_PROPERTIES_FILE_NAME = "properties.xml";
    static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";
    static String DATA_PATH = "./data/";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // LOAD THE SETTINGS FOR STARTING THE APP
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(ZombieCrushSagaPropertyType.UI_PROPERTIES_FILE_NAME, UI_PROPERTIES_FILE_NAME);
            props.addProperty(ZombieCrushSagaPropertyType.PROPERTIES_SCHEMA_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);
            props.addProperty(ZombieCrushSagaPropertyType.DATA_PATH.toString(), DATA_PATH);
            props.loadProperties(UI_PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);
            // THEN WE'LL LOAD THE MAHJONG FLAVOR AS SPECIFIED BY THE PROPERTIES FILE
            String gameFlavorFile = props.getProperty(ZombieCrushSagaPropertyType.GAME_FLAVOR_FILE_NAME);
            props.loadProperties(gameFlavorFile, PROPERTIES_SCHEMA_FILE_NAME);

            // NOW WE CAN LOAD THE UI, WHICH WILL USE ALL THE FLAVORED CONTENT
            String appTitle = props.getProperty(ZombieCrushSagaPropertyType.GAME_TITLE_TEXT);
            int fps = Integer.parseInt(props.getProperty(ZombieCrushSagaPropertyType.FPS));
            miniGame.initMiniGame(appTitle, fps);
            miniGame.startGame();


        } // THERE WAS A PROBLEM LOADING THE PROPERTIES FILE
        catch (InvalidXMLFileFormatException ixmlffe) {
            // LET THE ERROR HANDLER PROVIDE THE RESPONSE
        }
    }

    /**
     * ZombieCrushSaga PropertyType represents the types of data that will need
     * to be extracted from XML files.
     */
    public enum ZombieCrushSagaPropertyType {
        /* SETUP FILE NAMES */

        UI_PROPERTIES_FILE_NAME,
        PROPERTIES_SCHEMA_FILE_NAME,
        GAME_FLAVOR_FILE_NAME,
        RECORD_FILE_NAME,
        /* DIRECTORIES FOR FILE LOADING */
        AUDIO_PATH,
        DATA_PATH,
        IMG_PATH,
        /* WINDOW DIMENSIONS & FRAME RATE */
        WINDOW_WIDTH,
        WINDOW_HEIGHT,
        FPS,
        GAME_WIDTH,
        GAME_HEIGHT,
        GAME_LEFT_OFFSET,
        GAME_TOP_OFFSET,
        /* GAME TEXT */
        GAME_TITLE_TEXT,
        EXIT_REQUEST_TEXT,
        INVALID_XML_FILE_ERROR_TEXT,
        ERROR_DIALOG_TITLE_TEXT,
        /* ERROR TYPES */
        AUDIO_FILE_ERROR,
        LOAD_LEVEL_ERROR,
        RECORD_SAVE_ERROR,
        /* BUTTON IMAGE FILE NAMES */
        WIN_IMAGE_NAME,
        LOSE_IMAGE_NAME,
        POWER_UP_IMAGE_NAME,
        POWER_UP_MOUSE_OVER_IMAGE_NAME,
        PLAY_LEVEL_BUTTON_IMAGE_NAME,
        PLAY_LEVEL_BUTTON_MOUSE_OVER_IMAGE_NAME,
        CLOSE_LEVEL_SCORE_BUTTON_IMAGE_NAME,
        CLOSE_LEVEL_SCORE_BUTTON_MOUSE_OVER_IMAGE_NAME,
        QUIT_LEVEL_BUTTON_IMAGE_NAME,
        QUIT_LEVEL_BUTTON_MOUSE_OVER_IMAGE_NAME,
        SAGA_QUIT_GAME_BUTTON_IMAGE_NAME,
        SAGA_QUIT_GAME_BUTTON_MOUSE_OVER_IMAGE_NAME,
        PLAY_GAME_BUTTON_IMAGE_NAME,
        PLAY_GAME_BUTTON_MOUSE_OVER_IMAGE_NAME,
        RESET_BUTTON_IMAGE_NAME,
        RESET_BUTTON_MOUSE_OVER_IMAGE_NAME,
        QUIT_GAME_BUTTON_IMAGE_NAME,
        QUIT_GAME_BUTTON_MOUSE_OVER_IMAGE_NAME,
        SCROLL_UP_BUTTON_IMAGE_NAME,
        SCROLL_UP_BUTTON_MOUSE_OVER_IMAGE_NAME,
        SCROLL_DOWN_BUTTON_IMAGE_NAME,
        SCROLL_DOWN_BUTTON_MOUSE_OVER_IMAGE_NAME,
        RETURN_TO_SPLASH_BUTTON_IMAGE_NAME,
        RETURN_TO_SPLASH_BUTTON_MOUSE_OVER_IMAGE_NAME,
        //DECOR IMAGE
        WINDOW_ICON,
        NUMBER_IMAGE_NAME,
        COLOR_NUMBER_IMAGE_NAME,
        STAR_METER_IMAGE_NAME,
        STAR_EXP_IMAGE_NAME,
        STAR_IMAGE_NAME,
        MOVE_IMAGE_NAME,
        SCORE_IMAGE_NAME,
        SPLASH_SCREEN_IMAGE_NAME,
        SAGA_SCREEN_IMAGE_NAME,
        LEVEL_SCORE_SCREEN_IMAGE_NAME,
        GAME_SCREEN_IMAGE_NAME,
        ABOUT_SCREEN_IMAGE_NAME,
        //TILE IMAGE
        BLANK_TILE_IMAGE_NAME,
        BLANK_TILE_SELECTED_IMAGE_NAME,
        BLANK_TILE_JELLY_IMAGE_NAME,
        BLANK_TILE_JELLY_SELECTED_IMAGE_NAME,
        // AND THE DIALOGS
        STATS_DIALOG_IMAGE_NAME,
        WIN_DIALOG_IMAGE_NAME,
        LOSS_DIALOG_IMAGE_NAME,
        /* TILE LOADING STUFF */
        LEVEL_OPTIONS,
        CRUSH_IMAGE_OPTIONS,
        JELLY_CRUSH_IMAGE_OPTIONS,
        LEVEL_IMAGE_OPTIONS,
        LEVEL_MOUSE_OVER_IMAGE_OPTIONS,
        ZOMBIE_TILES_OPTIONS,
        /* AUDIO CUES */
        SELECT_AUDIO_CUE,
        MATCH_AUDIO_CUE,
        NO_MATCH_AUDIO_CUE,
        BLOCKED_TILE_AUDIO_CUE,
        UNDO_AUDIO_CUE,
        WIN_AUDIO_CUE,
        LOSS_AUDIO_CUE,
        SPLASH_SCREEN_SONG_CUE,
        GAMEPLAY_SONG_CUE
    }
}
