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
public class EnemyFormationLine extends EnemyFormation {
    
    GameBoard game;
    
    public EnemyFormationLine(GameBoard game, int level) {
        
        this.game = game;
        double middleEnemy = Math.random()*330+60;
        
        //Middle Enemy
        enemies[0] = new Enemy(game.getWidth()-1,middleEnemy,1.3+level/10,game.t);
        //Bottom Guys
        enemies[1] = new Enemy(game.getWidth()+44,middleEnemy,1.3+level/10,game.t);
        enemies[2] = new Enemy(game.getWidth()+94,middleEnemy,1.3+level/10,game.t);        
        //Top Guys
        enemies[3] = new Enemy(game.getWidth()+144,middleEnemy,1.3+level/10,game.t);
        enemies[4] = new Enemy(game.getWidth()+194,middleEnemy,1.3+level/10,game.t);
    }
    
    @Override
    public void movementPattern() {
        
    }
    
}
