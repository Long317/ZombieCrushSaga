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
public class PowerUpHandler implements ActionListener {
    //ZOMBIE CRUSH MINIGAME

    private ZombieCrushMiniGame game;

    /**
     * constructor of power up handler
     *
     * @param game ZombiecrushMinigame
     */
    public PowerUpHandler(ZombieCrushMiniGame game) {
        this.game = game;
    }

    /**
     * When button is clicked, start power up
     *
     * @param e when button is clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //SET TURN ON OR TURN OFF POWER UP MODE
        ZombieCrushDataModel data = (ZombieCrushDataModel) game.getDataModel();
        boolean powerUp = data.isPowerUp();
        data.setPowerUp(!powerUp);
        powerUp = data.isPowerUp();
        //CHANGE CURSOR OF MOUSE IF IT IS POWER UP MODE
        if (powerUp) {
            game.changeCursor(true);
        } else {
            game.changeCursor(false);
        }
    }
}
