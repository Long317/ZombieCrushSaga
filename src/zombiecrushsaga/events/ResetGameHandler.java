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
public class ResetGameHandler implements ActionListener {

    private ZombieCrushMiniGame game;

    public ResetGameHandler(ZombieCrushMiniGame game) {
        this.game = game;
    }

    /**
     * When button is clicked, switch screen to saga screen
     *
     * @param e when button is clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        game.reset();
    }
}
