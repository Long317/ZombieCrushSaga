/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

/**
 *
 * @author LongchengNi
 */
public class ZombieCrushSagaConstants {
    //VOLOCITY OF MOVING TILES

    public static final int MAX_VELOCITY = 5;
    //TO CONTROL CRUSH ANIMATION SPEED
    public static final double CRUSH_PAD = 0.25;
    //TO CONTROL CRUSH ANIMATION TIME
    public static final double CRUSH_END = 10;
    //SUBSTATE OF THE ANIMATION
    public static final String FALLING_STATE = "FALLING_STATE";
    public static final String AFTER_FALLING_STATE = "AFTER_FALLING_STATE";
    public static final String SWAP_STATE = "SWAP_STATE";
    public static final String CRUSH_STATE = "CRUSH_STATE";
    public static final String NONACTIVE_STATE = "NONACTIVE_STATE";
    public static final String ENDGAME_STATE = "ENDGAME_STATE";
    //LEVEL PREFIX AND SUFFIX
    public static final String LevelPrefix = "./data/./zomjong/Level_";
    public static final String LevelSuffix = ".zom";
    public static final String CRUSH_PREFIX = "CRUSH_";
    public static final String SCORE_PREFIX = "Score: ";
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    // GAME HEIGHT AND WIDTH AND DIMENTION OF GRID
    public static final int GameHeight = 655;
    public static final int GameWidth = 760;
    public static final int GameLocationMargin = 50;
    public static final int GridColumns = 9;
    public static final int GridRows = 9;
    public static final int TileWidth = 72;
    public static final int TileHeight = 65;
    public static final int TileWidthMargin = 38;
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE TWO
    public static final String SPLASH_SCREEN_STATE = "SPLASH_SCREEN_STATE";
    public static final String SAGA_SCREEN_STATE = "SAGA_SCREEN_STATE";
    public static final String LEVEL_SCORE_SCREEN_STATE = "LEVEL_SCORE_SCREEN_STATE";
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";
    public static final String ABOUT_SCREEN_STATE = "ABOUT_SCREEN_STATE";
    //Sprite states States
    public static final String INVISIBLE_STATE = "INVISIBLE_STATE";
    public static final String VISIBLE_STATE = "VISIBLE_STATE";
    public static final String SELECTED_STATE = "SELECTED_STATE";
    public static final String INCORRECTLY_SELECTED_STATE = "NOT_AVAILABLE_STATE";
    public static final String MOUSE_OVER_STATE = "MOUSE_OVER_STATE";
    public static final String JELLY_STATE = "JELLY_STATE";
    public static final String JELLY_SELECTED_STATE = "JELLY_SELECTED_STATE";
    public static final String CRUSH_VISIBLE_STATE = "CRUSH_VISIBLE_STATE";
    public static final String CRUSH_JELLY_STATE = "CRUSH_JELLY_STATE";
    // IN-GAME UI CONTROL TYPES
    public static final String LOSE_BUTTON_TYPE = "LOSE_BUTTON_TYPE";
    public static final String WIN_BUTTON_TYPE = "WIN_BUTTON_TYPE";
    public static final String LEVEL_SELECT_BUTTON_TYPE = "LEVEL_SELECT_BUTTON_TYPE";
    public static final String STAR_METER_TYPE = "STAR_METER_TYPE";
    public static final String PLAY_GAME_BUTTON_TYPE = "PLAY_GAME_BUTTON_TYPE";
    public static final String RESET_GAME_BUTTON_TYPE = "RESET_GAME_BUTTON_TYPE";
    public static final String QUIT_GAME_BUTTON_TYPE = "QUIT_GAME_BUTTON_TYPE";
    public static final String SAGA_QUIT_GAME_BUTTON_TYPE = "SAGA_QUIT_GAME_BUTTON_TYPE";
    public static final String SCROLL_UP_BUTTON_TYPE = "SCROLL_UP_BUTTON_TYPE";
    public static final String SCROLL_DOWN_BUTTON_TYPE = "SCROLL_DOWN_BUTTON_TYPE";
    public static final String RETURN_TO_SPLASH_BUTTON_TYPE = "RETURN_TO_SPLASH_BUTTON_TYPE";
    public static final String PLAY_LEVEL_BUTTON_TYPE = "PLAY_LEVEL_BUTTON_TYPE";
    public static final String CLOSE_LEVEL_SCORE_BUTTON_TYPE = "QUIT_LEVEL_SCORE_BUTTON_TYPE";
    public static final String QUIT_LEVEL_BUTTON_TYPE = "QUIT_LEVEL_BUTTON_TYPE";
    public static final String MOVE_TYPE = "MOVE_TYPE";
    public static final String SCORE_TYPE = "SCORE_TYPE";
    public static final String POWER_UP_BUTTON_TYPE = "POWER_UP_BUTTON_TYPE";
    //CONTROL POSTION IN GAME SCREEN
    public static final int PLAY_GAME_BUTTON_X = 50;
    public static final int PLAY_GAME_BUTTON_Y = 250;
    public static final int RESET_GAME_BUTTON_X = 50;
    public static final int RESET_GAME_BUTTON_Y = 350;
    public static final int QUIT_GAME_BUTTON_X = 50;
    public static final int QUIT_GAME_BUTTON_Y = 450;
    public static final int SCROLL_UP_BUTTON_X = GameWidth - 75 - 15;
    public static final int SCROLL_UP_BUTTON_Y = GameHeight / 2 - 60;
    public static final int SCROLL_DOWN_BUTTON_X = GameWidth - 75 - 15;
    public static final int SCROLL_DOWN_BUTTON_Y = GameHeight / 2 + 60;
    public static final int RETURN_TO_SPLASH_BUTTON_X = GameWidth - 110;
    public static final int RETURN_TO_SPLASH_BUTTON_Y = GameHeight - 55;
    public static final int SAGA_QUIT_GAME_BUTTON_X = GameWidth - 60;
    public static final int SAGA_QUIT_GAME_BUTTON_Y = GameHeight - 55;
    public static final int PLAY_LEVEL_BUTTON_X = GameWidth / 2 - 90;
    public static final int PLAY_LEVEL_BUTTON_Y = 400;
    public static final int CLOSE_LEVEL_SCORE_BUTTON_X = GameWidth / 2 - 90;
    public static final int CLOSE_LEVEL_SCORE_BUTTON_Y = 450;
    public static final int QUIT_LEVEL_BUTTON_X = 10;
    public static final int QUIT_LEVEL_BUTTON_Y = GameHeight - 80;
    public static final int TARGET_SCORE_X = 370;
    public static final int TARGET_SCORE_Y = 267;
    public static final int LEVEL_X = TARGET_SCORE_X + 30;
    public static final int LEVEL_Y = TARGET_SCORE_Y + 50;
    public static final int MOVE_X = 0;
    public static final int MOVE_Y = 56;
    public static final int MOVE_X_MARGIN = 35;
    public static final int MOVE_Y_MARGIN = 35;
    public static final int SCORE_X = 0;
    public static final int SCORE_Y = MOVE_Y + 40;
    public static final int SCORE_X_MARGIN = 25;
    public static final int SCORE_Y_MARGIN = 35;
    public static final int USER_SCORE_X = 260;
    public static final int USER_SCORE_Y = LEVEL_Y + 40;
    public static final int StarOne_X = 268;
    public static final int StarOne_Y = 190;
    public static final int StarTwo_X = 350;
    public static final int StarTwo_Y = 140;
    public static final int StarThree_X = 440;
    public static final int StarThree_Y = 190;
    public static final int STAR_METER_X = 5;
    public static final int STAR_METER_Y = 265;
    public static final int STAR_METER_EXP_X = 35;
    public static final int STAR_METER_EXP_Y = 278;
    public static final int POWER_UP_BUTTON_X = 2;
    public static final int POWER_UP_BUTTON_Y = 180;
    public static final int WIN_BUTTON_X = 255;
    public static final int WIN_BUTTON_Y =222 ;
    public static final int LOSE_BUTTON_X =200;
    public static final int LOSE_BUTTON_Y =233;
    //Fonts
    public static final Font STATS_FONT = new Font(Font.MONOSPACED, Font.BOLD, 38);
    public static final Font USER_SCORE_FONT = new Font(Font.SERIF, Font.ITALIC, 35);
    public static final Font MOVE_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 25);
    //LEVEL BUTTONS POSITION IN GAME SCREEN
    public static final Point[] points = new Point[]{
        new Point(40, 543),
        new Point(239, 548),
        new Point(410, 548),
        new Point(558, 548),
        new Point(600, 515 - 52),
        new Point(558, 438 - 52),
        new Point(424, 432 - 52),
        new Point(272, 440 - 52),
        new Point(122, 442 - 52),
        new Point(33, 378 - 52),};
    //AVAILABLE MOVE FOR EACH LEVEL
    public static final int[] AvailableMove = new int[]{
        6, 15, 18, 15, 20, 25, 50, 20, 25, 40};
    //STAR SCORES FOR EACH LEVEL
    public static final int[][] Stars_Score = new int[][]{
        {300, 400, 500}, {1900, 2100, 2400}, {4000, 6000, 8000},
        {4500, 6000, 9000}, {5000, 8000, 12000}, {9000, 11000, 13000},
        {60000, 75000, 85000}, {20000, 30000, 45000}, {22000, 44000, 66000},
        {40000, 70000, 100000}};
    //Colors
    public static final Color COLOR_KEY = new Color(255, 174, 201);
}
