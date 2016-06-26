/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.events;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import mini_game.Sprite;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import zombiecrushsaga.ZombieCrushSaga;
import zombiecrushsaga.ui.ZombieCrushMiniGame;
import static zombiecrushsaga.ZombieCrushSagaConstants.*;

/**
 *
 * @author LongchengNi
 */
public class ScrollUpHandler implements ActionListener {

    private ZombieCrushMiniGame game;

    public ScrollUpHandler(ZombieCrushMiniGame game) {
        this.game = game;
    }

    /**
     * Scroll down the map
     *
     * @param e when button is clicked
     */
    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        //GET LEVEL BUTTONS TYPE
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSaga.ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        //Get Saga Screen image
        Sprite s = this.game.getGUIDecor().get(BACKGROUND_TYPE);
        SpriteType bgST = s.getSpriteType();
        Image img = bgST.getStateImage(SAGA_SCREEN_STATE);
     
        //IF DO NOT REACH THE TOP, SCROLL UP
        if (this.game.getScollUp() < img.getHeight(null)-GameHeight) {
             game.setScollUp(game.getScollUp()+71);
        //SCROLL UP THE LEVEL BUTTONS
        for (int i = 0; i < levels.size(); i++) {
            float button_Y = game.getGUIButtons().get(levels.get(i)).getY();
            game.getGUIButtons().get(levels.get(i)).setY(button_Y +71 );
        }
        }

    }
}
