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
public class EnemyFormationMovingLine extends EnemyFormation{
    
    GameBoard game;
    double middleEnemy;
    
    public EnemyFormationMovingLine(GameBoard game, int level) {
        
        this.game = game;
        
        middleEnemy = Math.random()*360+70;
        
        enemies[0] = new Enemy(game.getWidth()-1,middleEnemy,1.3+level/10,game.t);
        
        enemies[1] = new Enemy(game.getWidth()+39,middleEnemy,1.3+level/10,game.t);
        enemies[2] = new Enemy(game.getWidth()+79,middleEnemy,1.3+level/10,game.t); 
        enemies[3] = new Enemy(game.getWidth()+119,middleEnemy,1.3+level/10,game.t);
        enemies[4] = new Enemy(game.getWidth()+159,middleEnemy,1.3+level/10,game.t); 
        
        for (int i = 0; i < 5; i++) {
            enemies[i].vely = 1.5;
        }
    }
    
    public void movementPattern() {
        
    }
    
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < numberOfEnemies; i++) {
            if (enemies[i] != null) {
                
                if (enemies[i].y <= 60)    
                    enemies[i].vely = 1.5;
                if (enemies[i].y >= game.getHeight()-30)
                    enemies[i].vely = -1.5;
                    
                enemies[i].actionPerformed(e);
            }
        }
    }
}
