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
public class EnemyFormationTheBrick extends EnemyFormation {

    GameBoard game;
    
    public EnemyFormationTheBrick(GameBoard game, int level) {
        
        this.game = game;
        numberOfEnemies = 49;
        enemies = new Enemy[49];
        int j = 0;
        
        for (int i = 0; i < 42; i += 7) {
            
            enemies[i] = new Enemy(game.getWidth()-1+j*40,85,1.3+level/10,game.t);
            enemies[i+1] = new Enemy(game.getWidth()-1+j*40,140,1.3+level/10,game.t);
            enemies[i+2] = new Enemy(game.getWidth()-1+j*40,195,1.3+level/10,game.t);        
            enemies[i+3] = new Enemy(game.getWidth()-1+j*40,250,1.3+level/10,game.t);
            enemies[i+4] = new Enemy(game.getWidth()-1+j*40,305,1.3+level/10,game.t);
            enemies[i+5] = new Enemy(game.getWidth()-1+j*40,360,1.3+level/10,game.t);
            enemies[i+6] = new Enemy(game.getWidth()-1+j*40,415,1.3+level/10,game.t);
            j++;
        }
    }
    
    @Override
    public void movementPattern() {
        
    } 
}
