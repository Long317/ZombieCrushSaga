/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.events;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import zombiecrushsaga.data.ZombieCrushDataModel;
import zombiecrushsaga.ui.ZombieCrushMiniGame;
import static zombiecrushsaga.ZombieCrushSagaConstants.*;

/**
 *
 * @author LongchengNi
 */
public class ZombieKeyHandler extends KeyAdapter {
    // THE MAHJONG GAME ON WHICH WE'LL RESPOND

    private ZombieCrushMiniGame game;
    ZombieCrushDataModel data;

    /**
     * This constructor simply inits the object by keeping the game for later.
     *
     * @param initGame The ZombieCrushMini game
     */
    public ZombieKeyHandler(ZombieCrushMiniGame initGame) {

        game = initGame;
        data = (ZombieCrushDataModel) (game.getDataModel());
    }

    /**
     * This method provides a custom game response to when the user presses a
     * keyboard key.
     *
     * @param ke Event object containing information about the event, like which
     * key was pressed.
     */
    @Override
    public void keyPressed(KeyEvent ke) {
        //IF GAME IS NOT ALLOW USER TO USE KEY LISTENER OR MOUSE LISTENER
        if (!game.isEnable()) {
            return;
        }
        //GENERATE A 4 TILES MATCH SITUATION
        if (ke.getKeyCode() == KeyEvent.VK_1) {
            loadTestedLevel("TESTED_4_TILES_MATCH");

        }
        //GENERATE A 5 TILES MATCH SITUATION
        if (ke.getKeyCode() == KeyEvent.VK_2) {
            loadTestedLevel("TESTED_5_TILES_MATCH");

        }
        //GENERATE A "T"-SHAPE MATCH SITUATION
        if (ke.getKeyCode() == KeyEvent.VK_3) {
            loadTestedLevel("TESTED_T_SHAPE_MATCH");

        }
        //GENERATE A "L"-SHAPE MATCH SITUATION
        if (ke.getKeyCode() == KeyEvent.VK_4) {
            loadTestedLevel("TESTED_L_SHAPE_MATCH");

        }
        //GENERATE A "L"-SHAPE MATCH SITUATION
        if (ke.getKeyCode() == KeyEvent.VK_5) {
            loadTestedLevel("TESTED_L1_SHAPE_MATCH");

        }
        //IF NOT GAME PLAY STATE RETURN
        if (!this.game.getCurrentScreenState().equals(GAME_SCREEN_STATE)) {
            return;
        }
        //CHEAT KEY FOR WIN
        if (ke.getKeyCode() == KeyEvent.VK_W) {
            data.winGame();
        }
        //CHEAT KEY FOR LOSE
        if (ke.getKeyCode() == KeyEvent.VK_L) {
            data.loseGame();
        }
    }

    private void loadTestedLevel(String testedLevel) {
        //GET THE DATA MODEL

        int[][] newGrid = game.getFileManager().loadLevel(LevelPrefix + testedLevel + LevelSuffix);
        //CREATE A NEW MAP
        for (int i = 0; i < data.getTileGrid().length; i++) {
            for (int j = 0; j < data.getTileGrid()[i].length; j++) {
                //SKIP ALL NULL TILE
                if (newGrid[i][j] == 0) {
                    continue;
                }
                data.getTileGrid()[i][j] = data.tileFactory(VISIBLE_STATE, i, j, newGrid[i][j]);
            }
        }
    }
}
