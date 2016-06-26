/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import properties_manager.PropertiesManager;
import zombiecrushsaga.ZombieCrushSaga;
import static zombiecrushsaga.ZombieCrushSagaConstants.points;
import zombiecrushsaga.data.ZombieCrushDataModel;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 *
 * @author LongchengNi
 */
public class CloseLevelScoreHandler implements ActionListener {

    private ZombieCrushMiniGame game;

    public CloseLevelScoreHandler(ZombieCrushMiniGame game) {
        this.game = game;
    }

    /**
     * When button is clicked, switch screen to saga screen
     *
     * @param e when button is clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSaga.ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        // GO TO THE SAGA SCREEN
        game.switchToLevelScoreScreen( false);
        //SET SCROLL UP VALUE TO 0
        game.setScollUp(0);
        //GET MAX LEVEL
        int maxLevel = ((ZombieCrushDataModel) game.getDataModel()).getMaxLevel();

        //SCROLL UP THE BUTTONS
        for (int i = 0; i < maxLevel; i++) {
            game.getGUIButtons().get(levels.get(i)).setY(points[i].y);
        }
    }
}
