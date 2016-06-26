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
import zombiecrushsaga.data.ZombieCrushDataModel;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 *
 * @author LongchengNi
 */
public class ScrollDownHandler implements ActionListener {

    private ZombieCrushMiniGame game;

    public ScrollDownHandler(ZombieCrushMiniGame game) {
        this.game = game;
    }

    /**
     * Scroll down the map
     *
     * @param e when button is clicked
     */
    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSaga.ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        //GET MAX LEVEL
        int maxLevel = ((ZombieCrushDataModel) game.getDataModel()).getMaxLevel();

        //SCROLL DOWN IF DO NOT REACH THE BUTTON
        if (this.game.getScollUp() > 0) {
            game.setScollUp(game.getScollUp() - 71);
            //SCROLL DOWN THE LEVEL BUTTONS
            for (int i = 0; i < maxLevel; i++) {
                float button_Y = game.getGUIButtons().get(levels.get(i)).getY();
                game.getGUIButtons().get(levels.get(i)).setY(button_Y - 71);
            }
        }
    }
}
