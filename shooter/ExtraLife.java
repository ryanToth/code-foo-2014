/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package shooter;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.Timer;

/**
 *
 * @author Ryan
 */
public class ExtraLife implements ActionListener {

    double x, y, velx = 0, vely = 0;
    Timer t;
    GameBoard game;
    
    public ExtraLife(GameBoard game, int level) {
        this.game = game;
        
        x = game.getWidth()-1;
        y = 400;
        velx = -1.3 - level/10;
        t = game.t;
    }
            
    @Override
    public void actionPerformed(ActionEvent e) {
        
        x += velx;
        
        if (y <= 60)    
            vely = 1.5;
        if (y >= game.getHeight()-30)
            vely = -1.5;
        
        y += vely;
    }
    
    public void paint(Graphics g) {
        
        URL imageurl = getClass().getResource("/shooter/1up.jpg");
        Image life = Toolkit.getDefaultToolkit().getImage(imageurl);
        g.drawImage(life, (int)x,(int)y,30,30, null);
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,30,30);
    }
    
}
