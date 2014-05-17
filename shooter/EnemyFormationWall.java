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
public class EnemyFormationWall extends EnemyFormation {

    GameBoard game;
    
    public EnemyFormationWall(GameBoard game, int level) {
        
        this.game = game;
        numberOfEnemies = 7;
        enemies = new Enemy[7];
        
        enemies[0] = new Enemy(game.getWidth()-1,85,1.3+level/10,game.t);
        enemies[1] = new Enemy(game.getWidth()-1,140,1.3+level/10,game.t);
        enemies[2] = new Enemy(game.getWidth()-1,195,1.3+level/10,game.t);        
        enemies[3] = new Enemy(game.getWidth()-1,250,1.3+level/10,game.t);
        enemies[4] = new Enemy(game.getWidth()-1,305,1.3+level/10,game.t);
        enemies[5] = new Enemy(game.getWidth()-1,360,1.3+level/10,game.t);
        enemies[6] = new Enemy(game.getWidth()-1,415,1.3+level/10,game.t);
    }
    
    @Override
    public void movementPattern() {
        
    } 
}
