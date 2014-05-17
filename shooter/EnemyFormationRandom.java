/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package shooter;

import java.awt.event.ActionEvent;

/**
 *
 * @author Ryan
 */
public class EnemyFormationRandom extends EnemyFormation {
    
    GameBoard game;
    
    public EnemyFormationRandom(GameBoard game, int level) {
        
        this.game = game;
          
        for (int i = 0; i < numberOfEnemies; i++) {
            enemies[i] = new Enemy(game.getWidth()-1+i*49,Math.random()*380+65,1.3+level/10,game.t);
            
            if ((Math.random()*100)%2 == 0)
                enemies[i].vely = 1;
            else enemies[i].vely = 1;
        }
    }

    @Override
    public void movementPattern() {
        
    }
    
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < numberOfEnemies; i++) {
            if (enemies[i] != null) {
                
                if (enemies[i].y <= 60)    
                    enemies[i].vely = 1;
                if (enemies[i].y >= game.getHeight()-30)
                    enemies[i].vely = -1;
                    
                enemies[i].actionPerformed(e);
            }
        }
    }
}
