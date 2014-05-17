package shooter;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Ryan
 */
public abstract class EnemyFormation implements ActionListener {
    
    int numberOfEnemies = 5;
    Enemy[] enemies = new Enemy[numberOfEnemies];
    
    public abstract void movementPattern();
    
    public void paint(Graphics g) {
        
        for (int i = 0; i < numberOfEnemies; i++) {
            if (enemies[i] != null)
                enemies[i].paint(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < numberOfEnemies; i++) {
            if (enemies[i] != null)
                enemies[i].actionPerformed(e);
        }
    }
    
}
