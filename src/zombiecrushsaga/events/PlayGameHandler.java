/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import zombiecrushsaga.ui.ZombieCrushMiniGame;


/**
 *
 * @author Longcheng Ni
 */
public class PlayGameHandler implements ActionListener{
    private ZombieCrushMiniGame game;
    
    public PlayGameHandler(ZombieCrushMiniGame game){
    this.game = game;
    }
    
    /**
     *When button is clicked, switch screen to saga screen 
     * @param e when button is clicked 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
                  
          // GO TO THE SAGA SCREEN
            game.switchToSagaScreen(true);
    }
    
}
