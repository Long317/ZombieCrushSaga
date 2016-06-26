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
import zombiecrushsaga.ui.ZombieCrushMiniGame;
import static zombiecrushsaga.ZombieCrushSagaConstants.*;

/**
 *
 * @author LongchengNi
 */
public  class ReturnToSplashScreenHandler  implements ActionListener{
    private ZombieCrushMiniGame game;
    
    public ReturnToSplashScreenHandler(ZombieCrushMiniGame game){
    this.game = game;
    }
    
    /**
     *When button is clicked, switch screen to saga screen 
     * @param e when button is clicked 
     */
    @Override
    public synchronized void actionPerformed(ActionEvent e) {
         PropertiesManager props = PropertiesManager.getPropertiesManager();
          ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSaga.ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        // GO TO THE SAGA SCREEN
        game.switchToSagaScreen(false);
                 //SET SCROLL UP VALUE TO 0
        game.setScollUp(0);
        for (int i = 0; i < levels.size(); i++) {
            game.getGUIButtons().get(levels.get(i)).setY(points[i].y);
        }
    }
    
}
