/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static zombiecrushsaga.ZombieCrushSagaConstants.*;
import zombiecrushsaga.data.ZombieCrushDataModel;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 *
 * @author LongchengNi
 */
public class QuitLevelHandler implements ActionListener {

    private ZombieCrushMiniGame game;

    public QuitLevelHandler(ZombieCrushMiniGame game) {
        this.game = game;
    }

    /**
     * When button is clicked, switch screen to saga screen
     *
     * @param e when button is clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //Turn off power up mode
        ZombieCrushDataModel data = new ZombieCrushDataModel(game);
        data.setPowerUp(false);
        game.changeCursor(false);
        //clear all moving tiles
        data.getCrushTiles().clear();
        data.getMovingTiles().clear();
        data.setSubState(NONACTIVE_STATE);
        // GO TO THE LEVEL_SCORE SCREEN
        game.switchToPlayGameScreen(false);
        //HIDE WIN LOSE OR QUIT BUTTONS
        game.getGUIButtons().get(WIN_BUTTON_TYPE).setEnabled(false);
        game.getGUIButtons().get(WIN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        game.getGUIButtons().get(LOSE_BUTTON_TYPE).setEnabled(false);
        game.getGUIButtons().get(LOSE_BUTTON_TYPE).setState(INVISIBLE_STATE);
        //SET SUBSTATE TO NONACTIVE STATE
        data.setSubState(NONACTIVE_STATE);
    }
}
