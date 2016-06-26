/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.events;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import zombiecrushsaga.data.ZombieCrushDataModel;
import zombiecrushsaga.ui.ZombieCrushMiniGame;
import zombiecrushsaga.ui.ZombieCrushTile;
import static zombiecrushsaga.ZombieCrushSagaConstants.*;

/**
 *
 * @author LongchengNi
 */
public class ZombieMouseHandler extends MouseAdapter {
    //MiniGame the game we are playing

    private ZombieCrushMiniGame game;
    //MiniGameDataModel 
    private ZombieCrushDataModel data;

    /**
     * This constructor simply inits the object by keeping the game for later.
     *
     * @param initGame The Mahjong game that contains the back button.
     */
    public ZombieMouseHandler(ZombieCrushMiniGame initGame) {

        game = initGame;
        data = (ZombieCrushDataModel) game.getDataModel();

    }

    /**
     * if it is power up state, crush that selected tile
     *
     * @param e mouse click event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        //IF GAME IS NOT ALLOW USER TO USE KEY LISTENER OR MOUSE LISTENER
        if (!game.isEnable()) {
            return;
        }
        //If it is not power up state return
        if (!data.isPowerUp()) {
            return;
        }
        //Get clicked location
        int x = e.getX();
        int y = e.getY();
        //Get tile's column and row
        int col = (int) ((x - TileWidthMargin) / TileWidth - 1);
        int row = (int) (y / TileHeight - 1);
        //IF CLICK OUTSIDE OF TILE GRID
        if (col < 0 || col > 8 || row < 0 || row > 8) {
            return;
        }
        //IF CLICK NOTHING RETURN
        if (data.getTileGrid()[col][row] == null) {
            return;
        }
        //CRUSH SELECTED TILE
        data.getCrushTiles().add(data.getTileGrid()[col][row]);
        data.crush();
        data.setPowerUp(false);
        this.game.changeCursor(false);
    }

    /**
     * If select a tile, then drag it around its neighbor
     *
     * @param e mouse Dragged Event
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        //IF GAME IS NOT ALLOW USER TO USE KEY LISTENER OR MOUSE LISTENER
        if (!game.isEnable()) {
            return;
        }
        //If it is power up state return
        if (data.isPowerUp()) {
            return;
        }
        try {
            // LOCK THE DATA
            game.beginUsingData();
            ZombieCrushTile selectedTile = data.getSelectedTile();
            if (selectedTile == null) {
                return;
            }
            int col = selectedTile.getGridColumn();
            int row = selectedTile.getGridRow();
            int selectedX = (col + 1) * TileWidth + TileWidthMargin;
            int selectedY = (row + 1) * TileHeight;
            // GET THE COORDINATES
            int x = e.getX();
            int y = e.getY();
            if (selectedX - TileWidth <= x && x <= selectedX + TileWidth) {
                selectedTile.setX(x);
            }
            if (selectedY - TileHeight <= y && y <= selectedY + TileHeight) {
                selectedTile.setY(y);
            }

        } finally {
            // RELEASE THE DATA SO THAT THE TIMER THREAD MAY
            // APPROPRIATELY UPDATE AND RENDER THE GAME
            // WITHOUT INTERFERENCE
            game.endUsingData();
        }
    }

    /**
     * When mouse release check for if we need to call mousePressOnSprite
     * method.
     *
     * @param me mouse release event
     */
    @Override
    public void mouseReleased(MouseEvent me) {
        //IF GAME IS NOT ALLOW USER TO USE KEY LISTENER OR MOUSE LISTENER
        if (!game.isEnable()) {
            return;
        }
        //If it is power up state return
        if (data.isPowerUp()) {
            return;
        }
        try {
            // LOCK THE DATA
            game.beginUsingData();
            //IF DIDN'T SELECT ANY TILE YET JUST RETURN
            if (data.getSelectedTile() == null) {
                return;
            }
            // GET THE COORDINATES
            int x = me.getX();
            int y = me.getY();
            //GET SELECTEDTILE'S CURRENT LOCATION
            float selectedX = data.getSelectedTile().getX();
            float selectedY = data.getSelectedTile().getY();
            //GET MOUSE COL AND ROW
            int col1 = (int) ((x - TileWidthMargin) / TileWidth - 1);
            int row1 = (int) (y / TileHeight - 1);
            //IF SELECT NOTHING
            if (col1 < 0 || col1 > 8 || row1 < 0 || row1 > 8) {
                return;
            }
            //GET SELECTED TILE'S ORIGINAL ROW AND COL
            int col = data.getSelectedTile().getGridColumn();
            int row = data.getSelectedTile().getGridRow();
            //GET SELECTED TILE'S ORIGINAL X AND Y
            int selected_X = (col + 1) * TileWidth + TileWidthMargin;
            int selected_Y = (row + 1) * TileHeight;
            data.getSelectedTile().setX(selected_X);
            data.getSelectedTile().setY(selected_Y);
            ZombieCrushTile releasedTile = data.getTileGrid()[col1][row1];
            //CHECK IF RELEASED TILE THE SAME AS SELECTED TILE,RETURN 
            //SELECTED TILE TO ITS ORIGINAL LOCATION
            if (releasedTile == data.getSelectedTile()) {
                data.getSelectedTile().setX(selected_X);
                data.getSelectedTile().setY(selected_Y);
                return;
            }
            //CHECK IF RELEASE TILE THE NEIGHBOR OF THE SELECTED TILE
            if (releasedTile.isNeighbor(data.getSelectedTile())) {
                data.checkMousePressOnSprites(game, x, y);
            }
            //CHECK IF RELEASE TILE THE NEIGHBOR OF THE SELECTED TILE
            data.checkMousePressOnSprites(game, (int) selectedX, (int) selectedY);
            //DISABLE KEYBAORD AND MOUSE LISTENER
            this.game.setIsEnable(false);
        } catch (Exception e) {
        } finally {
            // RELEASE THE DATA SO THAT THE TIMER THREAD MAY
            // APPROPRIATELY UPDATE AND RENDER THE GAME
            // WITHOUT INTERFERENCE
            game.endUsingData();
        }
    }
}
