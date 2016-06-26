/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import zombiecrushsaga.data.ZombieCrushDataModel;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 *
 * @author LongchengNi
 */
public class LevelSelectHandler implements ActionListener {

    private ZombieCrushMiniGame game;
    private int level;

    public LevelSelectHandler(ZombieCrushMiniGame game, int level) {
        this.level = level;
        this.game = game;
    }

    /**
     * When button is clicked, switch screen to saga screen
     *
     * @param e when button is clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // UPDATE THE DATA
        ZombieCrushDataModel dataModel = (ZombieCrushDataModel) game.getDataModel();

        //RESET CURRENT LEVEL, SCORE AND STARS NUMBER
        dataModel.setCurrentLevel(level);
        dataModel.setCurrentScore(0);

        //SWITCH TO LEVEL SCORE SCREEN
        game.switchToLevelScoreScreen(true);

    }
}
