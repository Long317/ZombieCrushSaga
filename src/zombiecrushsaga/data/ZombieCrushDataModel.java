/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.data;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import zombiecrushsaga.ZombieCrushSaga.ZombieCrushSagaPropertyType;
import zombiecrushsaga.ui.ZombieCrushMiniGame;
import zombiecrushsaga.ui.ZombieCrushPanel;
import zombiecrushsaga.ui.ZombieCrushTile;
import static zombiecrushsaga.ZombieCrushSagaConstants.*;

/**
 *
 * @author Longcheng Ni
 */
public class ZombieCrushDataModel extends MiniGameDataModel {
    //MiniGame

    private ZombieCrushMiniGame game;
    //level Grid that stores the int [][] map
    private int[][] levelGrid;
    // THIS STORES THE TILES ON THE GRID DURING THE GAME
    private ZombieCrushTile[][] tileGrid;
    //Private available move
    private int move;
    // THESE ARE THE TILES THAT ARE MOVING AROUND, AND SO WE HAVE TO UPDATE
    private ArrayList<ZombieCrushTile> movingTiles;
    //CRUSH TILES STORES HERE
    private ArrayList<ZombieCrushTile> crushTiles;
    //FLOATING POINT
    private int[][] floScore;
    //Current Level
    private int currentLevel;
    //Current Score
    private int currentScore;
    //SELECTED TILE
    private ZombieCrushTile selectedTile;
    //SELECT TILE
    private ZombieCrushTile selectTile;
    //SUBSTATE OF GAME STATE
    private String subState;
    //COUNT CRUSH TIME
    private int crushTime;
    //MAX LEVEL
    private int maxLevel;
    
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }
    //POWER UP, WE USE IT TO CHECK IF IT IS POWER UP STATE
    private boolean powerUp;

    /**
     * Constructor for dataModel
     *
     * @param game
     */
    public ZombieCrushDataModel(ZombieCrushMiniGame game) {
        this.game = game;
        this.currentScore = 0;
        movingTiles = new ArrayList();
        crushTiles = new ArrayList();
        selectedTile = null;
        selectTile = null;
        subState = NONACTIVE_STATE;
        crushTime = 1;
        maxLevel = 1;
        powerUp = false;
        game.setCrushedNumber(-1);
        floScore = new int[GridColumns][GridRows];
    }

    /**
     * check if mouse press on the sprites
     *
     * @param game minigame
     * @param x mouse click event x
     * @param y mouse click event y
     */
    @Override
    public void checkMousePressOnSprites(MiniGame game, int x, int y) {
        try {
            //IF GAME END RETURN
            if (this.subState.equals(ENDGAME_STATE)) {
                return;
            }
            //IF USER DON'T ALLOW USER MOUSE OR KEYBOARD RETURN
            if (!this.game.isEnable()) {
                return;
            }
            //If it is power up state return
            if (isPowerUp()) {
                return;
            }
            //GET COLUMN AND ROW
            int column = (x - TileWidthMargin) / TileWidth - 1;
            int row = y / TileHeight - 1;
            //IF CLICK POSITION OUT OF MAP THEN RETURN
            if (column > 8 || column < 0 || row < 0 || row > 8) {
                return;
            }
            //IF CLICK POSTION WITH NO TILE THEN RETURN
            if (getTileGrid()[column][row] == null) {
                return;
            }
            //SET SELECTED TILE
            selectTile = getTileGrid()[column][row];

            //DESELECTED THE TILE IF CLICK ON SELECTED TILE
            if (selectedTile == selectTile) {
                this.selectedTile = null;
                switchState(selectTile, false);
                return;
            }
            //SELECT TILE IF NO TILE IS SLECTED YET 
            if (selectedTile == null) {
                //PLAY SELECT TILE SONG
                game.getAudio().play(ZombieCrushSagaPropertyType.SELECT_AUDIO_CUE.toString(), true);
                //SELECT TILE
                this.selectedTile = selectTile;
                switchState(selectTile, true);
                return;
            }
            //if select Tile and selected tile is not neighbor
            if (!selectedTile.isNeighbor(selectTile)) {
                switchState(selectedTile, false);
                selectedTile = selectTile;
                switchState(selectTile, true);
                //PLAY SELECT TILE SONG
                game.getAudio().play(ZombieCrushSagaPropertyType.SELECT_AUDIO_CUE.toString(), true);
                return;
            }
            //START SWAPPING SELECTTILE AND SELECTEDTILE
            switchTwoTiles(selectTile, selectedTile);
        } catch (Exception e) {
        }
    }
    
    @Override
    public void reset(MiniGame game) {
    }

    /**
     * update all the animation move and data transition in game state
     *
     * @param game
     */
    @Override
    public void updateAll(MiniGame game) {
        // MAKE SURE THIS THREAD HAS EXCLUSIVE ACCESS TO THE DATA
        try {
            game.beginUsingData();
            //ENABLE KEYBOARD AND MOUSE LISTENER IF NO TILES MOVE
            if (movingTiles.isEmpty() && this.subState.equals(NONACTIVE_STATE)) {
                this.game.setIsEnable(true);
                
            }
            //CHECK IF SWAP DONE, YES, START CRUSHING, ELSE SWAP BACK
            if (movingTiles.isEmpty() && this.subState.equals(SWAP_STATE)) {
                checkForSwap();
                return;
            }
            //CHECK FOR CRUSH
            if (this.subState.equals(CRUSH_STATE)) {
                checkForCrush();
                return;
            }
            //AFTER CRUSH DO FALLING DOWN
            if (this.subState.equals(FALLING_STATE) && movingTiles.isEmpty()) {
                fillTheBlank();
                return;
            }
            //REPEATLY SCAN TO MAKE SURE NO CRUSH HAPPENS 
            if (this.subState.equals(AFTER_FALLING_STATE) && movingTiles.isEmpty()) {
                scanRepeat();
                return;
            }
            // WE ONLY NEED TO UPDATE AND MOVE THE MOVING TILES
            for (int i = 0; i < movingTiles.size(); i++) {
                // GET THE NEXT TILE
                movingTiles.get(i);

                // THIS WILL UPDATE IT'S POSITION USING ITS VELOCITY
                movingTiles.get(i).update(game);

                // IF IT'S REACHED ITS DESTINATION, REMOVE IT
                // FROM THE LIST OF MOVING TILES
                if (!movingTiles.get(i).isMovingToTarget()) {
                    movingTiles.remove(i);
                }
            }
            //CHECK FOR GAME ENDING STATE
            if (movingTiles.isEmpty() && this.subState.equals(NONACTIVE_STATE)) {
                isGameEnd();
            }
        } finally {
            // MAKE SURE WE RELEASE THE LOCK WHETHER THERE IS
            // AN EXCEPTION THROWN OR NOT
            game.endUsingData();
        }
    }
    
    @Override
    public void updateDebugText(MiniGame game) {
    }

    /**
     * accessor for current level
     *
     * @return current level
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * accessor for max level
     *
     * @return max level
     */
    public int getMaxLevel() {
        return maxLevel;
    }

    /**
     * getter for moving tiles
     *
     * @return moving tiles
     */
    public ArrayList<ZombieCrushTile> getMovingTiles() {
        return movingTiles;
    }

    /**
     * accessor for floScore
     *
     * @return floScore
     */
    public int[][] getFloScore() {
        return floScore;
    }

    /**
     * getter for subState
     *
     * @return subState
     */
    public String getSubState() {
        return subState;
    }

    /**
     * getter for crushTiles
     *
     * @return crushTiles
     */
    public ArrayList<ZombieCrushTile> getCrushTiles() {
        return crushTiles;
    }

    /**
     * Setter for subState
     *
     * @param subState the subState is going to be changed to this
     */
    public void setSubState(String subState) {
        this.subState = subState;
    }

    /**
     *
     * @return available move
     */
    public int getMove() {
        return move;
    }

    /**
     * accessor of power up instance
     *
     * @return
     */
    public boolean isPowerUp() {
        return powerUp;
    }

    /**
     * mutator of power up instance
     *
     * @param powerUp
     */
    public void setPowerUp(boolean powerUp) {
        this.powerUp = powerUp;
    }

    /**
     * mutator for available move
     *
     * @param move the available move going to be
     */
    public void setMove(int move) {
        this.move = move;
    }

    /**
     * Getter for SelectedTile
     *
     * @return
     */
    public ZombieCrushTile getSelectedTile() {
        return selectedTile;
    }

    /**
     * Accessor for current score
     *
     * @return current score of game
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * Mutator for current score
     *
     * @param currentScore that current score is going to be set
     */
    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    /**
     * mutator for current level
     *
     * @param currentLevel current level is going to be set to this parameter
     */
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    /**
     * accessor for gridTile
     */
    public ZombieCrushTile[][] getTileGrid() {
        return this.tileGrid;
    }

    /**
     * initialize levelGrid
     *
     * @param newGrid the level Grid passed from file
     */
    public void initLevelGrid(int[][] newGrid) {
        this.levelGrid = newGrid;
        //INITIALIZE THE TILES
        tileGrid = new ZombieCrushTile[GridColumns][GridRows];
        for (int i = 0; i < levelGrid.length; i++) {
            for (int j = 0; j < levelGrid[i].length; j++) {
                int imgIndex = (int) (Math.random() * 6);
                if (levelGrid[i][j] == 1) {
                    tileGrid[i][j] = tileFactory(VISIBLE_STATE, i, j, imgIndex);
                } else if (levelGrid[i][j] == 2) {
                    tileGrid[i][j] = tileFactory(JELLY_STATE, i, j, imgIndex);
                }
                
            }
            
        }
    }

    /**
     * Create a tile with specific state, type, and location
     *
     * @param state state of created tile
     * @param x x-coordinate of tile
     * @param y y-coordinate of tile
     * @param index determine which type of tile is
     * @return
     */
    public ZombieCrushTile tileFactory(String state, int x, int y, int index) {

        //PropertiesManager
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> tiles = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.ZOMBIE_TILES_OPTIONS);
        String imgPath = props.getProperty(ZombieCrushSagaPropertyType.IMG_PATH);

        //Get random tile type
        String imgName = tiles.get(index);
        SpriteType sT = new SpriteType(imgName);

        // LOAD THE ART
        BufferedImage img;
        img = game.loadImageWithColorKey(imgPath + imgName, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        sT.addState(SELECTED_STATE, img);
        sT.addState(JELLY_STATE, img);
        sT.addState(JELLY_SELECTED_STATE, img);
        sT.addState(CRUSH_PREFIX + VISIBLE_STATE, null);
        sT.addState(CRUSH_PREFIX + JELLY_STATE, null);
        ZombieCrushTile s = new ZombieCrushTile(sT, (float) (x + 1) * TileWidth + TileWidthMargin, (float) (y + 1) * TileHeight, 0, 0, state, imgName);
        return s;
        
    }

    /**
     * initialize the blank tile images
     */
    public void initBlankImage() {
        //PropertiesManager and get image path
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(ZombieCrushSagaPropertyType.IMG_PATH);
        ZombieCrushPanel canvas = (ZombieCrushPanel) game.getCanvas();
        //load blank tile image
        String blankTileFileName = props.getProperty(ZombieCrushSagaPropertyType.BLANK_TILE_IMAGE_NAME);
        BufferedImage blankTileImage = game.loadImage(imgPath + blankTileFileName);
        canvas.setBlankTileImage(blankTileImage);

        //load blankJelly Tile image
        String blankJellyTileFileName = props.getProperty(ZombieCrushSagaPropertyType.BLANK_TILE_JELLY_IMAGE_NAME);
        BufferedImage blankTileJelly = game.loadImage(imgPath + blankJellyTileFileName);
        canvas.setBlankTileJellyImage(blankTileJelly);

        //load blank Selected tile image
        String blankTileSelectedFileName = props.getProperty(ZombieCrushSagaPropertyType.BLANK_TILE_SELECTED_IMAGE_NAME);
        BufferedImage blankTileSelectedImage = game.loadImage(imgPath + blankTileSelectedFileName);
        canvas.setBlankTileSelectedImage(blankTileSelectedImage);

        //load blankJelly Selected Tile image
        String blankJellyTileSelectedFileName = props.getProperty(ZombieCrushSagaPropertyType.BLANK_TILE_JELLY_SELECTED_IMAGE_NAME);
        BufferedImage blankTileJellySelected = game.loadImage(imgPath + blankJellyTileSelectedFileName);
        canvas.setBlankTileJellySelectedImage(blankTileJellySelected);

        //load star image
        String starFileImage = props.getProperty(ZombieCrushSagaPropertyType.STAR_IMAGE_NAME);
        BufferedImage starImage = game.loadImage(imgPath + starFileImage);
        canvas.setStarImage(starImage);

        //load star meter image
        String starExpFileImage = props.getProperty(ZombieCrushSagaPropertyType.STAR_EXP_IMAGE_NAME);
        BufferedImage starExpImage = game.loadImage(imgPath + starExpFileImage);
        canvas.setStarMeterExp(starExpImage);

        //load number image
        String numberFileImage = props.getProperty(ZombieCrushSagaPropertyType.NUMBER_IMAGE_NAME);
        BufferedImage numberImage = game.loadImage(imgPath + numberFileImage);
        canvas.setNumber(numberImage);

        //load number image
        String colorNumFileImage = props.getProperty(ZombieCrushSagaPropertyType.COLOR_NUMBER_IMAGE_NAME);
        BufferedImage colorNumImage = game.loadImage(imgPath + colorNumFileImage);
        canvas.setColorNumber(colorNumImage);

        //load crushImages
        ArrayList<String> crushImageNames = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.CRUSH_IMAGE_OPTIONS);
        for (int i = 0; i < crushImageNames.size(); i++) {
            BufferedImage crushImage = game.loadImage(imgPath + crushImageNames.get(i));
            canvas.getCrushImages()[i] = crushImage;
        }
        //load jelly Crushed images
        ArrayList<String> jellyCrushImageNames = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.JELLY_CRUSH_IMAGE_OPTIONS);
        for (int i = 0; i < jellyCrushImageNames.size(); i++) {
            BufferedImage jellyCrushImage = game.loadImage(imgPath + jellyCrushImageNames.get(i));
            canvas.getJellyCrushImages()[i] = jellyCrushImage;
        }
    }

    /**
     * switch position of two Tile
     *
     * @param selectTile the tile that is going to be switched
     * @param selectedTile another tile that is going to be switched
     */
    private void switchPosition(ZombieCrushTile selectTile, ZombieCrushTile selectedTile) {
        try {
            game.beginUsingData();
            //Get column and row numbers of selectTile and selectedTile
            int col1 = selectedTile.getGridColumn();
            int row1 = selectedTile.getGridRow();
            int col2 = selectTile.getGridColumn();
            int row2 = selectTile.getGridRow();

            //switch two tiles
            selectTile.setGridColumn(col1);
            selectTile.setGridRow(row1);
            this.getTileGrid()[col1][row1] = selectTile;
            
            selectedTile.setGridColumn(col2);
            selectedTile.setGridRow(row2);
            this.getTileGrid()[col2][row2] = selectedTile;
        } finally {
            game.endUsingData();
        }
    }

    /**
     * switch the tile to selected state or reverse
     *
     * @param selectTile
     * @param b true: switch to selected state false: reverse
     */
    private void switchState(ZombieCrushTile selectTile, boolean b) {
        if (b) {
            if (selectTile.getState().equals(VISIBLE_STATE)) {
                
                selectTile.setState(SELECTED_STATE);
            } else {
                selectTile.setState(JELLY_SELECTED_STATE);
            }
        } else {
            if (selectTile.getState().equals(SELECTED_STATE)) {
                
                selectTile.setState(VISIBLE_STATE);
            } else {
                selectTile.setState(JELLY_STATE);
            }
        }
    }

    /**
     * switch two tiles positions
     *
     * @param selectTile
     * @param selectedTile
     */
    private void switchPositionMove(ZombieCrushTile selectTile, ZombieCrushTile selectedTile) {
        try {
            game.beginUsingData();
            selectTile.getAnimationPath().add(new Point((int) selectedTile.getX(), (int) selectedTile.getY()));
            selectedTile.getAnimationPath().add(new Point((int) selectTile.getX(), (int) selectTile.getY()));
            //Set animation target position for both tiles
            selectTile.setTargetX(selectedTile.getX());
            selectTile.setTargetY(selectedTile.getY());
            selectedTile.setTargetX(selectTile.getX());
            selectedTile.setTargetY(selectTile.getY());
            //Add tiles to the moveing Tiles
            movingTiles.add(selectTile);
            movingTiles.add(selectedTile);
            //Start animation
            for (int i = 0; i < movingTiles.size(); i++) {
                movingTiles.get(i).startMovingToTarget(MAX_VELOCITY);
            }
        } finally {
            game.endUsingData();
        }
        
    }

    /**
     * switch two tiles's state
     *
     * @param selectedTile
     * @param selectTile
     */
    private void switchState(ZombieCrushTile selectedTile, ZombieCrushTile selectTile) {
        String stateTemp = selectedTile.getState();
        selectedTile.setState(selectTile.getState());
        selectTile.setState(stateTemp);
    }

    /**
     * Scan all the rows and columns to check if there have 3 type in a row
     */
    private boolean check() {
        scanColumn();
        scanRow();
        return crushTiles.size() > 0;
    }

    /**
     * switch two tiles Position including index in grid and moving animation
     * and state
     *
     * @param selectTile
     * @param selectedTile
     */
    private void switchTwoTiles(ZombieCrushTile selectTile, ZombieCrushTile selectedTile) {
        try {
            game.beginUsingData();
            //SET SUBSTATE TO SWAP STATE
            this.subState = SWAP_STATE;
            //DISABLE MOUSE, AND KEY LISTENER
            this.game.setIsEnable(false);
            //SwitchAnimation
            switchPositionMove(selectTile, selectedTile);
            //switch Position in index
            switchPosition(selectTile, selectedTile);
            //check for same color crushed
            checkForS3();
            //deselect the selectedTile
            switchState(selectedTile, false);
            //switch state of two selectTile
            switchState(selectedTile, selectTile);
        } finally {
            game.endUsingData();
        }
    }

    /**
     * Scan the grid Columns
     */
    private void scanColumn() {
        int crushNumber = 1;
        String ID = "";
        //Check For Column
        for (int i = 0; i < this.getTileGrid().length; i++) {
            for (int j = 0; j < this.getTileGrid()[i].length; j++) {
                //SKIP EMPTY TILE
                if (this.getTileGrid()[i][j] == null) {
                    if (crushNumber >= 3) {
                        for (int k = 0; k < crushNumber; k++) {
                            crushTiles.add(this.getTileGrid()[i][j - k - 1]);
                        }
                        //CHECK IF ADD SPECIAL ZOMBIE TILE
                        checkForSpecialZombie(crushNumber, this.getTileGrid()[i][j - 2]);
                        //COUNT SCORE
                        countScore(crushNumber, i, j - 2);
                    }
                    //AFTER ADD TILES TO CRUSHTILES, SET CRUSHNUMBER TO 1
                    crushNumber = 1;
                    ID = "";
                    continue;
                }
                //IF NEXT TILE IS HAS SAME TYPE CRUSH NUMBER ADD ONE 
                if (ID.equals(this.getTileGrid()[i][j].getTileType().substring(0, 21))) {
                    crushNumber++;
                    //SPECIAL CASE CHECK FOR LAST COLUMN
                    if (j == 8 && crushNumber >= 3) {
                        for (int k = 0; k < crushNumber; k++) {
                            crushTiles.add(this.getTileGrid()[i][j - k]);
                        }
                        //CHECK IF ADD SPECIAL ZOMBIE TILE
                        checkForSpecialZombie(crushNumber, this.getTileGrid()[i][j - 2]);
                        //COUNT SCORE
                        countScore(crushNumber, i, j - 2);
                    }
                } else {
                    ID = this.getTileGrid()[i][j].getTileType().substring(0, 21);
                    //IF NEXT TILE HAS DIFFERENT TYPE AND CRUSH NUMBER GREATER THAN 3
                    if (crushNumber >= 3) {
                        for (int k = 0; k < crushNumber; k++) {
                            crushTiles.add(this.getTileGrid()[i][j - k - 1]);
                        }
                        //CHECK IF ADD SPECIAL ZOMBIE TILE
                        checkForSpecialZombie(crushNumber, this.getTileGrid()[i][j - 2]);
                        //COUNT SCORE
                        countScore(crushNumber, i, j - 2);
                    }
                    //AFTER ADD TILES TO CRUSHTILES, SET CRUSHNUMBER TO 1
                    crushNumber = 1;
                }
            }
            crushNumber = 1;
            ID = "";
        }
        
    }

    /**
     * Scan for Rows
     */
    /**
     * Scan the grid row
     */
    private void scanRow() {
        int crushNumber = 1;
        String ID = "";
        //Check For Column
        for (int i = 0; i < this.getTileGrid().length; i++) {
            for (int j = 0; j < this.getTileGrid()[i].length; j++) {
                //SKIP EMPTY TILE
                if (this.getTileGrid()[j][i] == null) {
                    if (crushNumber >= 3) {
                        for (int k = 0; k < crushNumber; k++) {
                            if (crushTiles.contains(this.getTileGrid()[j - k - 1][i])) {
                                //CHECK FOR T OR L SHAPE CRUSH
                                crushTiles.remove(getTileGrid()[j - k - 1][i]);
                                int index = getTileGrid()[j - k - 1][i].getIndex();
                                getTileGrid()[j - k - 1][i] = tileFactory(VISIBLE_STATE, j - k - 1, i, index + 6 * 2);
                                this.levelGrid[j - k - 1][i] = 1;
                                continue;
                            }
                            crushTiles.add(this.getTileGrid()[j - k - 1][i]);
                        }
                        //CHECK IF ADD SPECIAL ZOMBIE TILE
                        if (crushNumber == 4) {
                            checkForSpecialZombie(crushNumber, this.getTileGrid()[j - 2][i]);
                        } else if (crushNumber == 5) {
                            checkForSpecialZombie(crushNumber, this.getTileGrid()[j - 3][i]);
                        }
                        //COUNT SCORE
                        countScore(crushNumber, j - 2, i);
                    }
                    //AFTER ADD TILES TO CRUSHTILES, SET CRUSHNUMBER TO 1
                    crushNumber = 1;
                    ID = "";
                    continue;
                }
                //IF NEXT TILE IS HAS SAME TYPE CRUSH NUMBER ADD ONE 
                if (ID.equals(this.getTileGrid()[j][i].getTileType().substring(0, 21))) {
                    crushNumber++;
                    //SPECIAL CASE CHECK FOR LAST COLUMN
                    if (j == 8 && crushNumber >= 3) {
                        for (int k = 0; k < crushNumber; k++) {
                            if (crushTiles.contains(this.getTileGrid()[j - k][i])) {
                                //CHECK FOR T OR L SHAPE CRUSH
                                crushTiles.remove(getTileGrid()[j - k - 1][i]);
                                int index = getTileGrid()[j - k - 1][i].getIndex();
                                getTileGrid()[j - k - 1][i] = tileFactory(VISIBLE_STATE, j - k - 1, i, index + 6 * 2);
                                this.levelGrid[j - k - 1][i] = 1;
                                continue;
                            }
                            crushTiles.add(this.getTileGrid()[j - k][i]);
                        }
                        //CHECK IF ADD SPECIAL ZOMBIE TILE
                        if (crushNumber == 4) {
                            checkForSpecialZombie(crushNumber, this.getTileGrid()[j - 2][i]);
                        } else if (crushNumber == 5) {
                            checkForSpecialZombie(crushNumber, this.getTileGrid()[j - 3][i]);
                        }
                        //COUNT SCORE
                        countScore(crushNumber, j - 2, i);
                    }
                } else {
                    ID = this.getTileGrid()[j][i].getTileType().substring(0, 21);
                    //IF NEXT TILE HAS DIFFERENT TYPE AND CRUSH NUMBER GREATER THAN 3
                    if (crushNumber >= 3) {
                        for (int k = 0; k < crushNumber; k++) {
                            if (crushTiles.contains(this.getTileGrid()[j - k - 1][i])) {
                                //CHECK FOR T OR L SHAPE CRUSH
                                crushTiles.remove(getTileGrid()[j - k - 1][i]);
                                int index = getTileGrid()[j - k - 1][i].getIndex();
                                getTileGrid()[j - k - 1][i] = tileFactory(VISIBLE_STATE, j - k - 1, i, index + 6 * 2);
                                this.levelGrid[j - k - 1][i] = 1;
                                continue;
                            }
                            crushTiles.add(this.getTileGrid()[j - k - 1][i]);
                        }
                        //CHECK IF ADD SPECIAL ZOMBIE TILE
                        if (crushNumber == 4) {
                            checkForSpecialZombie(crushNumber, this.getTileGrid()[j - 2][i]);
                        } else if (crushNumber == 5) {
                            checkForSpecialZombie(crushNumber, this.getTileGrid()[j - 3][i]);
                        }
                        //COUNT SCORE
                        countScore(crushNumber, j - 2, i);
                    }
                    //AFTER ADD TILES TO CRUSHTILES, SET CRUSHNUMBER TO 1
                    crushNumber = 1;
                }
            }
            crushNumber = 1;
            ID = "";
        }
        
    }

    /**
     * crush all tiles in the crushTiles
     */
    public void crush() {
        try {
            game.beginUsingData();
            //SET SUBSTATE TO CRUSH STATE
            this.subState = CRUSH_STATE;
            //DISABLE KEYBOARD AND MOUSE LISTENER
            game.setIsEnable(false);
            //PLAY CRUSH SONG
            game.getAudio().play(ZombieCrushSagaPropertyType.MATCH_AUDIO_CUE.toString(), true);
            //UPDATE CRUSH NUMBER
            this.game.setCrushedNumber(0);
            String state;
            for (int i = 0; i < this.crushTiles.size(); i++) {
                int col = crushTiles.get(i).getGridColumn();
                int row = crushTiles.get(i).getGridRow();
                ZombieCrushTile crushedTile = getTileGrid()[col][row];
                //CHECK FOR S1 SPECIAL SWEET CRUSH
                if (crushedTile.getTileType().substring(21, 23).equals("S1")) {
                    //PLAY S1 SWEET SONG
                    game.getAudio().play(ZombieCrushSagaPropertyType.BLOCKED_TILE_AUDIO_CUE.toString(), true);
                    //PRODUCE RANDOM BOOLEAN
                    boolean b = getRandomBoolean();
                    //Crush all same column tiles or all same row tiles 
                    if (b) {
                        crushRow(col, row, true);
                    } else {
                        crushRow(col, row, false);
                    }
                }
                //CHECK FOR S2 SPECIAL SWEET CRUSH
                if (crushedTile.getTileType().substring(21, 23).equals("S2")) {
                    //PLAY S1 SWEET SONG
                    game.getAudio().play(ZombieCrushSagaPropertyType.BLOCKED_TILE_AUDIO_CUE.toString(), true);
                    //Start crush neighbor of S2 tile
                    crushNeighbor(col, row);
                }
                state = crushedTile.getState();
                this.getTileGrid()[col][row].setState(CRUSH_PREFIX + state);
                
            }
        } finally {
            game.endUsingData();
        }
    }

    /**
     * Scan every column for fill the crushed tile
     */
    private void fillTheBlank() {
        try {
            game.beginUsingData();
            //DISABLE KEYBOARD AND MOUSE LISTENER
            this.game.setIsEnable(false);
            
            ArrayList<Integer> cols = new ArrayList<>();
            //SET ALL INDEX
            for (int i = this.crushTiles.size() - 1; i >= 0; i--) {
                int col = crushTiles.get(i).getGridColumn();
                int row = crushTiles.get(i).getGridRow();
                Integer colInt = col;
                if (!cols.contains(colInt)) {
                    cols.add(col);
                }
                switchIndex(col, row);
            }
            this.crushTiles.clear();
            //CORRECT TILE STATES
            correctState();
            //SET TARGET
            for (int i = 0; i < cols.size(); i++) {
                int col = cols.get(i);
                addTargetLocation(col);
            }
            cols.clear();
            //SET GAME SUBSTATE
            this.subState = AFTER_FALLING_STATE;
        } finally {
            game.endUsingData();
        }
    }

    /**
     * move index of tiles with same column, one row down
     *
     * @param col
     * @param row
     */
    private void switchIndex(int col, int row) {
        try {
            game.beginUsingData();
            int index;
            int top = 0;
            //find top tile's row
            for (int i = 0; i < this.getTileGrid()[col].length; i++) {
                if (this.getTileGrid()[col][i] != null) {
                    top = this.getTileGrid()[col][i].getGridRow();
                    break;
                }
            }
            //move tile's index one row up
            for (int i = row; i > top; i--) {
                int swap = i - 1;
                //SKIP EMPTY TILE
                if (getTileGrid()[col][i] == null) {
                    continue;
                }
                //GET ABOVE EXISTED TILE
                while (getTileGrid()[col][swap] == null) {
                    swap--;
                    if (swap == top) {
                        break;
                    }
                }
                //START POINTING TILE
                this.getTileGrid()[col][swap].setGridRow(this.getTileGrid()[col][swap].getGridRow() + i - swap);
                this.getTileGrid()[col][swap].setGridColumn(this.getTileGrid()[col][i].getGridColumn());
                this.getTileGrid()[col][i] = this.getTileGrid()[col][swap];
            }
            //Fill top tile with random tile
            index = (int) (Math.random() * 6);
            this.getTileGrid()[col][top] = tileFactory(VISIBLE_STATE, col, top, index);
        } finally {
            game.endUsingData();
        }
    }

    /**
     * Add Target Location for the tiles
     *
     * @param col column number of the tile
     */
    private void addTargetLocation(int col) {
        for (int j = 0; j < this.getTileGrid()[col].length; j++) {
            ZombieCrushTile tempTile = this.getTileGrid()[col][j];
            //SKIP THOSE EMPTY TILES
            if (tempTile == null || (int) tempTile.getY() == (tempTile.getGridRow() + 1) * TileHeight) {
                continue;
            }
            //calculate the target location
            int x = (tempTile.getGridColumn() + 1) * TileWidth + TileWidthMargin;
            int y = (tempTile.getGridRow() + 1) * TileHeight;
            //if movingTiles already have this tile set animiation path
            if (movingTiles.contains(tempTile)) {
                tempTile.getAnimationPath().add(new Point(x, y));
            } else {
                tempTile.setTargetX((float) x);
                tempTile.setTargetY((float) y);
                tempTile.getAnimationPath().add(new Point(x, y));
                movingTiles.add(tempTile);
            }
            //Start moving to target
            for (int i = 0; i < movingTiles.size(); i++) {
                if (!movingTiles.get(i).isMovingToTarget()) {
                    movingTiles.get(i).startMovingToTarget(MAX_VELOCITY);
                }
            }
        }
    }

    /**
     * SWAP BACK TWO TILES
     */
    private void swapBack() {
        //PLAY SWAP BACK SONG
        game.getAudio().play(ZombieCrushSagaPropertyType.NO_MATCH_AUDIO_CUE.toString(), true);
        //DISABLE KEYBOARD AND MOUSE LISTENER
        this.game.setIsEnable(false);
        //add animation path
        switchPositionMove(selectTile, selectedTile);
        //switch Position in index
        switchPosition(selectTile, selectedTile);
        //switch state of two selectTile
        switchState(selectedTile, selectTile);
    }

    /**
     * Counting the scores according to crushNumber
     *
     * @param crushNumber
     */
    private void countScore(int crushNumber, int col, int row) {
        //check for jelly tile
        for (int i = 0; i < this.crushTiles.size(); i++) {
            if (crushTiles.get(i).getState().contains("JELLY")) {
                crushTime+=10;
                break;
            }
        }
        //count score for 3 tiles crushed
        if (crushNumber == 3) {
            int score = (crushNumber * 20 + currentLevel * 10) * crushTime;
            this.floScore[col][row] = score;
            this.setCurrentScore(currentScore + score);
        } //count score for 4 tiles crushed 
        else if (crushNumber == 4) {
            int score = (crushNumber * 30 + currentLevel * 10) * crushTime;
            this.floScore[col][row] = score;
            this.setCurrentScore(score + currentScore);
        } //count score for 5 tiles crushed
        else if (crushNumber == 5) {
            int score = (crushNumber * 40 + currentLevel * 10) * crushTime;
            this.floScore[col][row] = score;
            this.setCurrentScore(score + currentScore);
        }//COUNT SCORE FOR MORE THAN 5
        else if (crushNumber > 5) {
            int score = (crushNumber * 100 + currentLevel * 10) * crushTime;
            this.floScore[col][row] = score;
            this.setCurrentScore(score + currentScore);
        }
        
    }

    /**
     * After lose Actions
     */
    public void loseGame() {
        //DISPLAY LOSE IMAGE
        game.getGUIButtons().get(LOSE_BUTTON_TYPE).setState(VISIBLE_STATE);
        game.getGUIButtons().get(LOSE_BUTTON_TYPE).setEnabled(true);
        //play game lose song
        game.getAudio().stop(ZombieCrushSagaPropertyType.GAMEPLAY_SONG_CUE.toString());
        game.getAudio().play(ZombieCrushSagaPropertyType.LOSS_AUDIO_CUE.toString(), true);
        this.setMove(0);
        //SET STATE TO END GAME STATE
        this.subState = ENDGAME_STATE;
    }

    /**
     * After win Actions
     */
    public void winGame() {
        this.setMove(0);
        //Play game win song
        game.getAudio().stop(ZombieCrushSagaPropertyType.GAMEPLAY_SONG_CUE.toString());
        game.getAudio().play(ZombieCrushSagaPropertyType.WIN_AUDIO_CUE.toString(), true);
        //DISPLAY WIN IMAGE
        game.getGUIButtons().get(WIN_BUTTON_TYPE).setState(VISIBLE_STATE);
        game.getGUIButtons().get(WIN_BUTTON_TYPE).setEnabled(true);
        //Next Level is open to user
        if (currentLevel == this.maxLevel) {
            maxLevel++;
        }
        game.getFileManager().save(game.getRecord());
        //SET STATE TO END GAME STATE
        this.subState = ENDGAME_STATE;
    }

    /**
     * Check if game ends or not
     */
    private void isGameEnd() {
        //IF GAME HAS END THEN RETURN
        if (subState.equals(ENDGAME_STATE)) {
            return;
        }
        ZombieCrushLevelRecord levelRecord = this.game.getRecord().getLevelRcord(currentLevel - 1);
        if (this.getMove() <= 0) {
            //CHECK WIN SITUATION AND HOW MANY STARS USER IS GOING TO EARN
            if (currentScore >= Stars_Score[currentLevel - 1][0] && currentScore <= Stars_Score[currentLevel - 1][1]) {
                levelRecord.setStarsNumber(1);
                winGame();
            } else if (currentScore >= Stars_Score[currentLevel - 1][1] && currentScore <= Stars_Score[currentLevel - 1][2]) {
                levelRecord.setStarsNumber(2);
                winGame();
            } else if (currentScore >= Stars_Score[currentLevel - 1][2]) {
                levelRecord.setStarsNumber(3);
                winGame();
            } else {
                loseGame();
            }
        }
    }

    /**
     * check for swap,situation, if swap, start check, else swap back
     */
    private void checkForSwap() {
        //AFTER SWAP, WE START CRUSHING AND FALLING
        if (check()) {
            crush();
        }
        //if is not crushed, just swap back
        if (!this.subState.equals(CRUSH_STATE)) {
            //SWAP TWO TILES LOCATION THEN START SWAP BACK ANIMATION
            swapBack();

            //SET ALL THOSE VARIABLES TO DEFAULT STATE
            this.selectTile = null;
            this.selectedTile = null;
            
            subState = NONACTIVE_STATE;
        } else {
            //IF CRUSH, MOVES MINUS ONE
            this.setMove(getMove() - 1);
        }
    }

    /**
     * Repeat scan to make sure no crush happens
     */
    private void scanRepeat() {
        //DOUBLE CHECK IF ANY COMPOUND CRUSH HAPPEN
        this.crushTime++;
        if (check()) {
            //Play compound crush song
            game.getAudio().play(ZombieCrushSagaPropertyType.UNDO_AUDIO_CUE.toString(), true);
            crush();
        } else {
            this.crushTime = 1;
            //SET ALL THOSE VARIABLES TO DEFAULT STATE
            this.selectTile = null;
            this.selectedTile = null;
            
            subState = NONACTIVE_STATE;
        }
        
    }

    /**
     * check if produce special tile
     *
     * @param crushNumber if number is equal to 4 or 5, we will produce
     * corresponding special tile
     * @param zombieCrushTile we will produce special tile to replace this tile
     */
    private void checkForSpecialZombie(int crushNumber, ZombieCrushTile zombieCrushTile) {
        //Get column number and row number
        int col = zombieCrushTile.getGridColumn();
        int row = zombieCrushTile.getGridRow();
        //check if produce special tile
        if (crushNumber == 4) {
            crushTiles.remove(getTileGrid()[col][row]);
            int index = zombieCrushTile.getIndex();
            if (index >= 0 && index < 6) {
                getTileGrid()[col][row] = tileFactory(VISIBLE_STATE, col, row, index + 6);
                this.levelGrid[col][row] = 1;
            }
        } else if (crushNumber == 5) {
            crushTiles.remove(getTileGrid()[col][row]);
            getTileGrid()[col][row] = tileFactory(VISIBLE_STATE, col, row, 18);
            this.levelGrid[col][row] = 1;
        }
    }

    /**
     * Produce a random boolean, 50% true, 50% false
     *
     * @return
     */
    private boolean getRandomBoolean() {
        return (Math.random() < 0.5);
    }

    /**
     *
     * @param col column of special tile
     * @param row row of special tile
     * @param b true: crush tiles with same column, false: crush tiles with same
     * row
     */
    private void crushRow(int col, int row, boolean b) {
        try {
            game.beginUsingData();
            //IF TURE, CRUSH WHOLE COLUMN
            if (b) {
                for (int i = 0; i < GridRows; i++) {
                    ZombieCrushTile crushedTile = getTileGrid()[col][i];
                    //SKIP EMPTY TILE
                    if (getTileGrid()[col][i] == null) {
                        continue;
                    }
                    if (!crushTiles.contains(crushedTile)) {
                        crushTiles.add(crushedTile);
                    }
                }
            }//IF FALSE CRUSH WHOLE ROW 
            else {
                for (int i = 0; i < GridColumns; i++) {
                    ZombieCrushTile crushedTile = getTileGrid()[i][row];
                    //SKIP EMPTY TILE
                    if (getTileGrid()[i][row] == null) {
                        continue;
                    }
                    if (!crushTiles.contains(crushedTile)) {
                        crushTiles.add(crushedTile);
                    }
                }
            }
        } finally {
            game.endUsingData();
        }
    }

    /**
     * crush the neighbor of the tile in col, row location
     *
     * @param col column of special crushed tile
     * @param row row of special crushed tile
     */
    private void crushNeighbor(int col, int row) {
        try {
            int count = 0;
            game.beginUsingData();
            for (int i = col - 1; i <= col + 1; i++) {
                for (int j = row - 1; j <= row + 1; j++) {
                    count++;
                    //SKIP OUT OF BOUNDARY TILES
                    if (i >= GridRows || j >= GridColumns || i < 0 || j < 0) {
                        continue;
                    }
                    //SKIP EMPTY TILE
                    if (getTileGrid()[i][j] == null) {
                        continue;
                    }
                    if (!crushTiles.contains(getTileGrid()[i][j])) {
                        crushTiles.add(getTileGrid()[i][j]);
                    }
                }
            }
        } finally {
            game.endUsingData();
        }
    }

    /**
     * CHECK IF IT IS CRUSH_STATE, YES, THEN UPDATE CRUSH NUMBER
     */
    private void checkForCrush() {
        //UPDATE CRUSHED NUMBER
        double CrushNum = this.game.getCrushedNumber() + CRUSH_PAD;
        this.game.setCrushedNumber(CrushNum);

        //CHECK IF CRUSH DONE AND CHANGE TO FALLING STATE
        if (this.game.getCrushedNumber() >= CRUSH_END) {
            this.game.setCrushedNumber(-1);
            this.setSubState(FALLING_STATE);
            updateTileState();
            //set all floScore to 0
            for (int i = 0; i < GridColumns; i++) {
                for (int j = 0; j < GridColumns; j++) {
                    if (getFloScore()[i][j] != 0) {
                        getFloScore()[i][j] = 0;
                    }
                }
            }
        }
    }

    /**
     * Check if select tile or selectedTile is S3 tile, if yes, do special crush
     */
    private void checkForS3() {
        checkForS3Helper(selectTile, selectedTile);
        checkForS3Helper(selectedTile, selectTile);
        countScore(this.crushTiles.size(), selectedTile.getGridColumn(), selectedTile.getGridRow());
        if (!this.crushTiles.isEmpty()) {
            //PLAY S1 SWEET SONG
            game.getAudio().play(ZombieCrushSagaPropertyType.BLOCKED_TILE_AUDIO_CUE.toString(), true);
        }
    }

    /**
     * helper method for check for Special 3 method
     *
     * @param selectTile check if this tile with tileType G
     * @param selectedTile if selectTile is G type, crush all tiles with
     * selectedTile type
     */
    private void checkForS3Helper(ZombieCrushTile selectTile, ZombieCrushTile selectedTile) {
        if (selectTile.getTileType().substring(20, 21).equals("G")) {
            if (!crushTiles.contains(selectTile)) {
                this.crushTiles.add(selectTile);
            }
            //crush all selected Tile type
            String crushType = selectedTile.getTileType();
            for (int i = 0; i < getTileGrid().length; i++) {
                for (int j = 0; j < getTileGrid()[i].length; j++) {
                    //SKIP ALL NULL TILES
                    if (getTileGrid()[i][j] == null) {
                        continue;
                    }
                    //ADD SAME TILE TYPE TO CRUSH TILE
                    if (getTileGrid()[i][j].getTileType().substring(0, 21).equals(crushType.substring(0, 21))) {
                        
                        if (!crushTiles.contains(getTileGrid()[i][j])) {
                            this.crushTiles.add(getTileGrid()[i][j]);
                        }
                    }
                }
            }
        }
    }

    /**
     * correct states of tiles
     */
    private void correctState() {
        for (int i = 0; i < levelGrid.length; i++) {
            for (int j = 0; j < levelGrid[i].length; j++) {
                //SKIP EMPTY TILE
                if (levelGrid[i][j] == 0) {
                    continue;
                }
                if (levelGrid[i][j] == 1) {
                    getTileGrid()[i][j].setState(VISIBLE_STATE);
                }
                if (levelGrid[i][j] == 2) {
                    getTileGrid()[i][j].setState(JELLY_STATE);
                }
            }
        }
    }

    /**
     * update all tiles'states
     */
    private void updateTileState() {
        for (int i = 0; i < getTileGrid().length; i++) {
            for (int j = 0; j < getTileGrid()[i].length; j++) {
                //SKIP EMPTY TILE
                if (getTileGrid()[i][j] == null) {
                    continue;
                }
                //UPDATE STATES FOR CRUSHED_JELLY_STATE TILE
                if (getTileGrid()[i][j].getState().equals(CRUSH_JELLY_STATE)) {
                    this.levelGrid[i][j] = 1;
                }
            }
        }
    }
}
