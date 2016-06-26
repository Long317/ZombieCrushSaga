/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static zombiecrushsaga.ZombieCrushSagaConstants.AvailableMove;
import static zombiecrushsaga.ZombieCrushSagaConstants.LevelPrefix;
import static zombiecrushsaga.ZombieCrushSagaConstants.LevelSuffix;
import static zombiecrushsaga.ZombieCrushSagaConstants.NONACTIVE_STATE;
import zombiecrushsaga.data.ZombieCrushDataModel;
import zombiecrushsaga.file.ZombieCrushFileManager;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 *
 * @author Longcheng Ni
 */
public class PlayLevelHandler implements ActionListener {

    private ZombieCrushMiniGame game;

    public PlayLevelHandler(ZombieCrushMiniGame game) {
        this.game = game;
    }

    /**
     * When button is clicked, switch screen to saga screen
     *
     * @param e when button is clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        ZombieCrushDataModel data = (ZombieCrushDataModel) game.getDataModel();

        //RESET TARGET SCORE IF NEW HIGHEST SCORE PRODUCED IN THE PREVIOUS GAME
        game.updateTargetScore();
        // UPDATE THE DATA
        ZombieCrushFileManager fileManager = game.getFileManager();
        int[][] newGrid = fileManager.loadLevel(LevelPrefix + data.getCurrentLevel() + LevelSuffix);
        data.initLevelGrid(newGrid);
        //RESTORE THE CURRENT SCORE AND AVAILABLE MOVE 
        data.setMove(AvailableMove[data.getCurrentLevel() - 1]);
        ((ZombieCrushDataModel) (game.getDataModel())).setCurrentScore(0);

        // GO TO THE GAME SCREEN
        game.switchToPlayGameScreen(true);
        //SET SUBSTATE TO NONACTIVE STATE
        data.setSubState(NONACTIVE_STATE);
    }
}
