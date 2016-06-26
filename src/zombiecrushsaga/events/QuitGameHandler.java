/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 *
 * @author LongchengNi
 */
public class QuitGameHandler extends WindowAdapter implements ActionListener{
    private ZombieCrushMiniGame  MiniGame;
    
    public QuitGameHandler(ZombieCrushMiniGame miniGame) {
     MiniGame= miniGame;
    }
  /**
     * This method is called when the user clicks the window'w X. We 
     * respond by giving the player a loss if the game is still going on.
     * 
     * @param we Window event object.
     */
    @Override
    public void windowClosing(WindowEvent we)
    {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
        if (MiniGame.getDataModel().inProgress())
        {
            MiniGame.getDataModel().endGameAsLoss();
        }
        // AND CLOSE THE ALL
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       System.exit(0);
    }
    
}
