/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;


import java.util.Collection;
import javax.swing.JPanel;
import mini_game.Sprite;
import mini_game.SpriteType;

import zombiecrushsaga.data.ZombieCrushDataModel;
import static zombiecrushsaga.ZombieCrushSagaConstants.*;

/**
 *
 * @author LongchengNi
 */
public class ZombieCrushPanel extends JPanel {
    // THIS IS ACTUALLY OUR Mahjong Solitaire APP, WE NEED THIS
    // BECAUSE IT HAS THE GUI STUFF THAT WE NEED TO RENDER

    private ZombieCrushMiniGame game;
    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    private ZombieCrushDataModel data;
    // WE'LL USE THIS TO FORMAT SOME TEXT FOR DISPLAY PURPOSES
    private NumberFormat numberFormatter;
    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING UNSELECTED TILES
    private BufferedImage blankTileImage;
    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING SELECTED TILES
    private BufferedImage blankTileSelectedImage;
    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING JELLY TILES
    private BufferedImage blankTileJellyImage;
    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING JELLY TILES
    private BufferedImage blankTileJellySelectedImage;
    //WE'LL USE THIS AS STAR IMAGE
    private BufferedImage starImage;
    //WE'LL USE THIS TO FILL STAR METER
    private BufferedImage starMeterExp;
    //WE'LL USE THIS TO DRAW THE NUMBERS
    private BufferedImage number;
    //WE'LL USE THIS TO DRAW THE NUMBERS
    private BufferedImage colorNumber;
    //CRUSHED IMAGES, WE WILL USE FOR CRUSH EFFECT
    private BufferedImage[] crushImages;
    //JELLY CRUSHED IMAGES, WE WILL USE FOR JELLY CRUSH EFFECT
    private BufferedImage[] jellyCrushImages;

    /**
     * This constructor stores the game and data references, which we'll need
     * for rendering.
     *
     * @param initGame the Mahjong Solitaire game that is using this panel for
     * rendering.
     *
     * @param initData the Mahjong Solitaire game data.
     */
    public ZombieCrushPanel(ZombieCrushMiniGame initGame, ZombieCrushDataModel initData) {
        game = initGame;
        data = initData;
        numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(3);
        numberFormatter.setMaximumFractionDigits(3);
        crushImages = new BufferedImage[6];
        jellyCrushImages = new BufferedImage[6];
    }
    // MUTATOR METHODS
    // -setBlankTileImage
    // -setBlankTileSelectedImage
    //-setBlankTileJellyImage

    /**
     * This mutator method sets the base image to use for rendering tiles.
     *
     * @param initBlankTileImage The image to use as the base for rendering
     * tiles.
     */
    public void setBlankTileImage(BufferedImage initBlankTileImage) {
        blankTileImage = initBlankTileImage;
    }

    /**
     * setter for drawing the numbers
     *
     * @param number
     */
    public void setNumber(BufferedImage number) {
        this.number = number;
    }

    /**
     * setter for drawing the color numbers
     *
     * @param number
     */
    public void setColorNumber(BufferedImage colorNumber) {
        this.colorNumber = colorNumber;
    }

    /**
     * This mutator method sets the base image to use for rendering selected
     * tiles.
     *
     * @param initBlankTileSelectedImage The image to use as the base for
     * rendering selected tiles.
     */
    public void setBlankTileSelectedImage(BufferedImage initBlankTileSelectedImage) {
        blankTileSelectedImage = initBlankTileSelectedImage;
    }

    /**
     * This mutator method sets base image to use for render jelly tiles
     *
     * @param blankTileJellyImage The image to use as the base for render jelly
     * tiles
     */
    public void setBlankTileJellyImage(BufferedImage blankTileJellyImage) {
        this.blankTileJellyImage = blankTileJellyImage;
    }

    /**
     * Accessor method to get crush Image array, which used to display crushed
     * process
     *
     * @return
     */
    public BufferedImage[] getCrushImages() {
        return crushImages;
    }

    /**
     * Accessor method to get jelly crush Image array, which used to display
     * crushed jelly process
     *
     * @return
     */
    public BufferedImage[] getJellyCrushImages() {
        return jellyCrushImages;
    }

    /**
     * This mutator method sets star meter image to use to fill meter
     *
     * @param starMeterExp
     */
    public void setStarMeterExp(BufferedImage starMeter) {
        this.starMeterExp = starMeter;
    }

    /**
     * This mutator method sets base image to use for render selected jelly
     * tiles
     *
     * @param blankTileJellyImage The image to use as the base for render
     * selected jelly tiles
     */
    public void setBlankTileJellySelectedImage(BufferedImage blankTileJellySelectedImage) {
        this.blankTileJellySelectedImage = blankTileJellySelectedImage;
    }

    /**
     * This mutator method sets star image to use for render level stars that
     * user earns
     *
     * @param blankTileJellyImage The image to use as the base for render
     * selected jelly tiles
     */
    public void setStarImage(BufferedImage starImage) {
        this.starImage = starImage;
    }

    /**
     * This is where rendering starts. This method is called each frame, and the
     * entire game application is rendered here with the help of a number of
     * helper methods.
     *
     * @param g The Graphics context for this panel.
     */
    @Override
    public void paintComponent(Graphics g) {
        try {
            // MAKE SURE WE HAVE EXCLUSIVE ACCESS TO THE GAME DATA
            game.beginUsingData();

            // CLEAR THE PANEL
            super.paintComponent(g);

            // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
            renderBackground(g);
//           // AND THE TILES
            renderTiles(g);
            // AND THE BUTTONS AND DECOR
            renderGUIControls(g);
            // AND THE TIME AND TILES STATS
            renderStats(g);
            //RENDER METER FILLING
            renderMeterFilling(g);

            //
//            // RENDERING THE GRID WHERE ALL THE TILES GO CAN BE HELPFUL
//            // DURING DEBUGGIN TO BETTER UNDERSTAND HOW THEY RE LAID OUT
//            renderGrid(g);
//
//            // AND FINALLY, TEXT FOR DEBUGGING
//            renderDebuggingText(g);
        } finally {
            // RELEASE THE LOCK
            game.endUsingData();
        }
    }

    private void renderBackground(Graphics g) {
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        renderSprite(g, bg);
    }

    /**
     * Renders the s Sprite into the Graphics context g. Note that each Sprite
     * knows its own x,y coordinate location.
     *
     * @param g the Graphics context of this panel
     *
     * @param s the Sprite to be rendered
     */
    public void renderSprite(Graphics g, Sprite s) {
        //Render saga screen
        if (s.getState().equals(SAGA_SCREEN_STATE) && !s.getState().equals(INVISIBLE_STATE)) {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int) s.getX(), (int) s.getY(), GameWidth, GameHeight, 0,
                    img.getHeight(null) - GameHeight - game.getScollUp(),
                    GameWidth, img.getHeight(null) - game.getScollUp(), this);
        } // ONLY RENDER THE VISIBLE ONES
        else if (!s.getState().equals(INVISIBLE_STATE)) {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int) s.getX(), (int) s.getY(), bgST.getWidth(), bgST.getHeight(), null);
        }
    }

    public void renderGUIControls(Graphics g) {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> decorSprites = game.getGUIDecor().values();
        for (Sprite s : decorSprites) {
            //SKIP BACKGROUND TYPE
            if (s.getSpriteType().getSpriteTypeID().equals(BACKGROUND_TYPE)) {
                continue;
            }
            renderSprite(g, s);
        }

        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites) {
            renderSprite(g, s);
        }

    }

    /**
     * Render the stats such as target score
     *
     * @param g Graphics g
     */
    private void renderStats(Graphics g) {
        String BG_STATE = this.game.getGUIDecor().get(BACKGROUND_TYPE).getState();
        data = (ZombieCrushDataModel) this.game.getDataModel();

        //RENDER STAT FOR LEVEL_SCORE_SCREEN
        if (BG_STATE.equals(LEVEL_SCORE_SCREEN_STATE)) {
            g.setFont(STATS_FONT);
            //DRAW CURRENT LEVEL
            int currentLevel = data.getCurrentLevel();
            showNumRightPad(number, LEVEL_X, LEVEL_Y, currentLevel, g);
            //DRAW TARGET SCORE
            int TargetScore = this.game.getRecord().getLevelRcord(currentLevel - 1).getTargetScore();
            showNumRightPad(number, TARGET_SCORE_X, TARGET_SCORE_Y, TargetScore, g);

            //DRAW USER SCORE               
            if (data.getCurrentScore() != 0) {
                g.setFont(USER_SCORE_FONT);
                g.drawString(SCORE_PREFIX, USER_SCORE_X, USER_SCORE_Y + 35);
                showNumRightPad(number, USER_SCORE_X + SCORE_PREFIX.length() * 14, USER_SCORE_Y, data.getCurrentScore(), g);
            }
            //DRAW STARS
            int starNumber = this.game.getRecord().getLevelRcord(currentLevel - 1).getStarsNumber();
            if (starNumber == 1) {
                g.drawImage(starImage, StarOne_X, StarOne_Y, null);
            } else if (starNumber == 2) {
                g.drawImage(starImage, StarOne_X, StarOne_Y, null);
                g.drawImage(starImage, StarTwo_X, StarTwo_Y, null);
            } else if (starNumber == 3) {
                g.drawImage(starImage, StarOne_X, StarOne_Y, null);
                g.drawImage(starImage, StarTwo_X, StarTwo_Y, null);
                g.drawImage(starImage, StarThree_X, StarThree_Y, null);
            }
        }
        //RENDER STAT FOR GAME_SCREEN
        if (BG_STATE.equals(GAME_SCREEN_STATE)) {
            g.setFont(MOVE_FONT);
            g.setColor(Color.WHITE);
            //Draw available move
            int move = data.getMove();
            g.drawString(Integer.toString(move), MOVE_X + MOVE_X_MARGIN, MOVE_Y + MOVE_Y_MARGIN);
            //Draw current Score
            int score = data.getCurrentScore();
            g.drawString(Integer.toString(score), SCORE_X + SCORE_X_MARGIN, SCORE_Y + SCORE_Y_MARGIN);

        }
    }

    /**
     * Render the zombie tiles
     *
     * @param g Graphics g
     */
    private void renderTiles(Graphics g) {
        BufferedImage img;
        //ONLY RENDER TILES IN GAME SCREEN STATE
        String BG_STATE = this.game.getGUIDecor().get(BACKGROUND_TYPE).getState();
        if (!BG_STATE.equals(GAME_SCREEN_STATE)) {
            return;
        }

        //RENDER TILES
        ZombieCrushTile[][] gridTiles;
        gridTiles = ((ZombieCrushDataModel) (this.game.getDataModel())).getTileGrid();
        for (int i = 0; i < gridTiles.length; i++) {
            for (int j = 0; j < gridTiles[i].length; j++) {
                //DO NOTHING FOR NO TILE POSITION
                if (gridTiles[i][j] == null) {
                    continue;
                }
                String state = gridTiles[i][j].getState();
                int x = (i + 1) * TileWidth + TileWidthMargin;
                int y = (j + 1) * TileHeight;
                //DRAW BLANK TILE IMAGE
                switch (state) {
                    case VISIBLE_STATE:
                    case CRUSH_VISIBLE_STATE:
                        g.drawImage(blankTileImage, x, y, null);
                        break;
                    case JELLY_STATE:
                        g.drawImage(blankTileJellyImage, x, y, null);
                        break;
                    case JELLY_SELECTED_STATE:
                        g.drawImage(blankTileJellySelectedImage, x, y, null);
                        break;
                    case SELECTED_STATE:
                        g.drawImage(blankTileSelectedImage, x, y, null);
                        break;
                    case CRUSH_JELLY_STATE:
                        g.drawImage(blankTileImage, x, y, null);
                        //DRAW IMAGE FOR CRUSH_JELLY_STATE
                        if (data.getSubState().equals(CRUSH_STATE)) {
                            double crushNum = game.getCrushedNumber();
                            //IF CRUSH NUMBER LESS THAN 6, DRAW CRUSH IMAGES
                            if (crushNum < 6) {
                                g.drawImage(jellyCrushImages[((int) crushNum)], x, y, null);
                            }
                        }
                        break;
                }
                //DRAW ZOMBIE TILE IMAGE IF CRUSHED,DRAW NOTHING
                if (state.equals(CRUSH_JELLY_STATE) || state.equals(CRUSH_VISIBLE_STATE)) {
                    //DRAW CRUSH IMAGE
                    double CrushNum = game.getCrushedNumber();
                    if (data.getSubState().equals(CRUSH_STATE) && CrushNum < 6) {
                        g.drawImage(crushImages[((int) CrushNum)], (int) gridTiles[i][j].getX(), (int) gridTiles[i][j].getY(), null);
                    }
                    //DRAW FLOATING NUMBERS
                    if (data.getSubState().equals(CRUSH_STATE) && CrushNum >= 6) {
                        drawFloatingNumber(g);
                    }
                } else {
                    //DRAW TILE IMAGE FOR GENERAL SITUATION
                    img = gridTiles[i][j].getSpriteType().getStateImage(VISIBLE_STATE);
                    g.drawImage(img, (int) gridTiles[i][j].getX(), (int) gridTiles[i][j].getY(), null);
                }
            }
        }
    }

    /**
     * Draw liquid to filling to star meter
     *
     * @param g
     */
    private void renderMeterFilling(Graphics g) {
        String BG_STATE = this.game.getGUIDecor().get(BACKGROUND_TYPE).getState();
        //Doing nothing if it is not game screen state
        if (!BG_STATE.equals(GAME_SCREEN_STATE)) {
            return;
        }
        //fill the star meter
        int level = data.getCurrentLevel();
        int targetScore = Stars_Score[level - 1][2];
        int currentScore = data.getCurrentScore();
        double percent = (double) currentScore / (double) targetScore;
        double percentage = percent > 1 ? 1 : percent;
        int meterHeight = 512 - STAR_METER_EXP_Y;
        int imageHeight = starMeterExp.getHeight();
        //star 1 location
        int star_1_location = (int) ((1 - (Stars_Score[level - 1][0] / (double) targetScore)) * meterHeight) + STAR_METER_EXP_Y;
        //star 2 location
        int star_2_location = (int) ((1 - (Stars_Score[level - 1][1] / (double) targetScore)) * meterHeight) + STAR_METER_EXP_Y;
        //draw one star
        g.drawImage(starImage, (STAR_METER_EXP_X << 1) - 15, star_1_location - 8, (STAR_METER_EXP_X << 1) + 1, star_1_location + 8,
                0, 0, starImage.getWidth(), starImage.getHeight(), null);
        //draw two stars
        for (int i = 0; i < 2; i++) {
            g.drawImage(starImage, (STAR_METER_EXP_X << 1) - 15 + i * 15, star_2_location - 8, (STAR_METER_EXP_X << 1) + 1 + i * 15,
                    star_2_location + 8, 0, 0, starImage.getWidth(), starImage.getHeight(), null);
        }
        //draw 3 stars
        for (int i = 0; i < 3; i++) {
            g.drawImage(starImage, (STAR_METER_EXP_X << 1) - 15 + i * 15, STAR_METER_EXP_Y - 8, (STAR_METER_EXP_X << 1) + 1 + i * 15,
                    STAR_METER_EXP_Y + 8, 0, 0, starImage.getWidth(), starImage.getHeight(), null);
        }

        //Fill the meter
        g.drawImage(starMeterExp, STAR_METER_EXP_X - 20,
                STAR_METER_EXP_Y + (int) (meterHeight * (1 - percentage)), (STAR_METER_EXP_X << 1) - 20,
                STAR_METER_EXP_Y + meterHeight, 0, ((int) (percentage * imageHeight-1 )), 32,
                ((int) (percentage * imageHeight)), null);

    }

    /*
     * Show number method
     * 
     * @para int x = location's x coordinate
     * 
     * @para int y =location's y coordinate
     * 
     * @para int num = number  that we are going to draw
     * 
     * @para int maxBit
     */
    protected void showNumRightPad(Image img, int x, int y, int num, Graphics g) {
        int NUM_W = number.getWidth() / 10;
        int NUM_H = number.getHeight();
        // get all numbers from num
        String Numb = Integer.toString(num);
        for (int i = 0; i < Numb.length(); i++) {
            int Index = i;
            int bit = Numb.charAt(Index) - '0';
            g.drawImage(img, x + NUM_W * i, y, x
                    + NUM_W * (i + 1), y + NUM_H, NUM_W * bit, 0,
                    (bit + 1) * NUM_W, NUM_H, null);


        }
    }

    /**
     * drawFloating numbers
     *
     * @param g
     */
    private void drawFloatingNumber(Graphics g) {
        double crush = this.game.getCrushedNumber();
        ZombieCrushDataModel data = (ZombieCrushDataModel) game.getDataModel();
        //Draw Numbers
        for (int i = 0; i < GridColumns; i++) {
            for (int j = 0; j < GridColumns; j++) {
                if (data.getFloScore()[i][j] != 0) {
                    int x = (i + 1) * TileWidth + TileWidthMargin;
                    int y = (j + 1) * TileHeight;
                    showNumRightPad(colorNumber, x, (int) (y - (crush - 6) * 12), data.getFloScore()[i][j], g);
                }
            }
        }
    }
}
