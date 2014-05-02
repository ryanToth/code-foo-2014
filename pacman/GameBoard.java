package pacman;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Ryan
 */
public class GameBoard extends JPanel {
    
    PacMan pacman = new PacMan(this);

    public GameBoard() {

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                pacman.keyPressed(e);
            }
        });

        setFocusable(true);
    }

    public void paint(Graphics g) {
        
        repaint();
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        pacman.paint(g2);

    }
    
    public void newGame() {
        pacman = null;
        pacman = new PacMan(this);
    }


}
