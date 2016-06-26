/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.ui;

import java.awt.Point;
import java.util.ArrayList;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import static zombiecrushsaga.ZombieCrushSagaConstants.*;

/**
 *
 * @author LongchengNi
 */
public class ZombieCrushTile extends Sprite implements Cloneable {
    //TYPE OF THE TILE WHICH REPRESENT DIFERENT TYPE OF TILES

    private String tileType;
    // WHEN WE PUT A TILE IN THE GRID WE TELL IT WHAT COLUMN AND ROW
    // WHICH IS INDEX IN THE GRID TILE
    private int gridColumn;
    private int gridRow;
    // THIS IS true WHEN THIS TILE IS MOVING, WHICH HELPS US FIGURE
    // OUT WHEN IT HAS REACHED A DESTINATION NODE
    private boolean movingToTarget;
    // THE TARGET COORDINATES IN WHICH IT IS CURRENTLY HEADING
    private float targetX;
    private float targetY;
    //ANIMATION PATH, WHEN TILE START ANIMATED, IT WILL PASS ALL POINTS IN
    //THIS ANIMATION PATH
    private ArrayList<Point> animationPath;


    /*
     * This constructor initializes this tile for use, including all the
     * sprite-related data from its ancestor class, Sprite.
     */
    public ZombieCrushTile(SpriteType initSpriteType,
            float initX, float initY,
            float initVx, float initVy,
            String initState, String initTileType) {
        // SEND ALL THE Sprite DATA TO A Sprite CONSTRUCTOR
        super(initSpriteType, initX, initY, initVx, initVy, initState);

        // INIT THE TILE TYPE
        this.tileType = initTileType;
        //Initialize the Column and row
        this.gridColumn = (int) ((x - TileWidthMargin) / TileWidth - 1);
        this.gridRow = (int) (y / TileHeight - 1);
        animationPath = new ArrayList<Point>();

    }

    /**
     * Accessor for TileType
     *
     * @return
     */
    public String getTileType() {
        return tileType;
    }

    /**
     * Accessor for MovingToTarget
     *
     * @return
     */
    public boolean isMovingToTarget() {
        return movingToTarget;
    }

    /**
     * Accessor for animation path
     *
     * @return animation path node list
     */
    public ArrayList<Point> getAnimationPath() {
        return animationPath;
    }

    /**
     * mutator for TargetX
     *
     * @param targetX
     */
    public void setTargetX(float targetX) {
        this.targetX = targetX;
    }

    /**
     * mutator for TargetY
     *
     * @param targetY
     */
    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }

    /**
     * Accessor of tile column number
     *
     * @return gridColumn that is the first index in the gridTile [][]
     */
    public int getGridColumn() {
        return gridColumn;
    }

    /**
     * Accessor of tile row number
     *
     * @return gridRow that is the second index in the gridTile[][]
     */
    public int getGridRow() {
        return gridRow;
    }

    /**
     * Mutator for GridColumn
     *
     * @param gridColumn the Column of this tile is going to be in the gridTile
     */
    public void setGridColumn(int gridColumn) {
        this.gridColumn = gridColumn;
    }

    /**
     * Mutator for GridRow
     *
     * @param gridRow the row of this tile is going to be in the gridTile
     */
    public void setGridRow(int gridRow) {
        this.gridRow = gridRow;
    }

    /**
     * Check is two tiles are neighbor
     */
    public boolean isNeighbor(ZombieCrushTile selectTile) {
        int ColDiff = Math.abs(selectTile.getGridColumn() - this.getGridColumn());
        int RowDiff = Math.abs(selectTile.getGridRow() - this.getGridRow());
        return (ColDiff + RowDiff == 1);
    }

    /**
     * Start moving this tiles with MID_VOLOCITY
     *
     * @param MAX_VOLOCITY
     */
    public void startMovingToTarget(int max_VOLOCITY) {
        // LET ITS POSITIONG GET UPDATED
        movingToTarget = true;

        // CALCULATE THE ANGLE OF THE TRAJECTORY TO THE TARGET
        float diffX = targetX - x;
        float diffY = targetY - y;
        float tanResult = diffY / diffX;
        float angleInRadians = (float) Math.atan(tanResult);

        // COMPUTE THE X VELOCITY COMPONENT
        vX = (float) (max_VOLOCITY * Math.cos(angleInRadians));

        // CLAMP THE VELOCTY IN CASE OF NEGATIVE ANGLES
        if ((diffX < 0) && (vX > 0)) {
            vX *= -1;
        }
        if ((diffX > 0) && (vX < 0)) {
            vX *= -1;
        }

        // COMPUTE THE Y VELOCITY COMPONENT
        vY = (float) (max_VOLOCITY * Math.sin(angleInRadians));

        // CLAMP THE VELOCITY IN CASE OF NEGATIVE ANGLES
        if ((diffY < 0) && (vY > 0)) {
            vY *= -1;
        }
        if ((diffY > 0) && (vY < 0)) {
            vY *= -1;
        }
    }
    // METHODS OVERRIDDEN FROM Sprite
    // -update

    /**
     * Called each frame, this method ensures that this tile is updated
     * according to the path it is on.
     *
     * @param game The Mahjong game this tile is part of.
     */
    @Override
    public void update(MiniGame game) {
        if (calculateDistanceToTarget() <= MAX_VELOCITY) {
            this.animationPath.remove(new Point((int) targetX, (int) targetY));
            if (!animationPath.isEmpty()) {
                targetX = (float) this.getAnimationPath().get(0).x;
                targetY = (float) this.getAnimationPath().get(0).y;
                this.startMovingToTarget(MAX_VELOCITY);
            } else {
                vX = 0;
                vY = 0;
                x = targetX;
                y = targetY;
                movingToTarget = false;
            }
        } // OTHERWISE, JUST DO A NORMAL UPDATE, WHICH WILL CHANGE ITS POSITION
        // USING ITS CURRENT VELOCITY.
        else {
            super.update(game);
        }
    }

    /**
     * count distance to target
     *
     * @return distance between current location and target location
     */
    private float calculateDistanceToTarget() {
        float dif_X = this.targetX - this.x;
        float dif_Y = this.targetY - this.y;
        float distance = (float) Math.sqrt(dif_X * dif_X + dif_Y * dif_Y);
        return distance;
    }

    /**
     * get type index of the tile
     *
     * @return index of the tile which determine which type it is
     */
    public int getIndex() {
        char typeLetter = tileType.charAt(tileType.length() - 5);
        //If index is greater than 5 then return a random index number within 5
        if (typeLetter - 'A' > 5) {
            return (int) (Math.random() + 6);
        }
        //return index number
        return typeLetter - 'A';
    }
}
