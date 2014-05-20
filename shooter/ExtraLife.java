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

    double x, y, velx = 0, vely;
    Timer t;
    GameBoard game;
    
    public ExtraLife(GameBoard game, int level) {
        
        this.game = game;
        x = game.getWidth()-1;
        y = Math.random()*400+70;
        velx = -1.3 - level/10;
        t = game.t;
        
        if ((Math.random()*100) <= 45)
            vely = Math.random()*1.5 + 0.5;
        else vely = 0;
    }
            
    @Override
    public void actionPerformed(ActionEvent e) {
        
        x += velx;
        y += vely;
        
        if (y <= 69) {    
            vely = -vely;
            y = 70;
        }
        if (y >= game.getHeight()- 40) {
            vely = -vely;
            y = game.getHeight()- 41;
        }
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
