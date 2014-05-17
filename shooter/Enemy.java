/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package shooter;

import java.awt.Graphics;
import java.awt.Graphics2D;
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
public class Enemy implements ActionListener {

    double x = 0, y = 0, vely = 0, velx = 0;
    Timer t;
    
    public Enemy(double x, double y, double velx, Timer t) {
        
        this.y = y;
        this.x = x;
        this.velx = -velx;
        this.t = t;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        x += velx;
        y+= vely;
    }
    
    public void paint(Graphics g) {
        
        URL imageurl = getClass().getResource("/shooter/magikarp.jpg");
        Image background = Toolkit.getDefaultToolkit().getImage(imageurl);
        g.drawImage(background, (int)x,(int)y,60,60, null);
        
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,60,60);
    }
    
}
