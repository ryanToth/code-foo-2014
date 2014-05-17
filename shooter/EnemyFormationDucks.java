/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package shooter;


/**
 *
 * @author Ryan
 */
public class EnemyFormationDucks extends EnemyFormation{

    GameBoard game;
    
    public EnemyFormationDucks(GameBoard game, int level) {
        
        this.game = game;
        double middleEnemy = Math.random()*230+120;
        
        //Middle Enemy
        enemies[0] = new Enemy(game.getWidth()-1,middleEnemy,1.3+level/10,game.t);
        //Bottom Guys
        enemies[1] = new Enemy(game.getWidth()+49,middleEnemy+30,1.3+level/10,game.t);
        enemies[2] = new Enemy(game.getWidth()+89,middleEnemy+60,1.3+level/10,game.t);        
        //Top Guys
        enemies[3] = new Enemy(game.getWidth()+49,middleEnemy-30,1.3+level/10,game.t);
        enemies[4] = new Enemy(game.getWidth()+89,middleEnemy-60,1.3+level/10,game.t);
    }
    
    @Override
    public void movementPattern() {
        
    }
   
}
