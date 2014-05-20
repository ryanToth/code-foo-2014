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
public class Enemy implements ActionListener {

    double x = 0, y = 0, vely = 0, velx = 0;
    boolean dead = false, remove = false, beginCount = false;
    long explosion = 0;
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
        
        if (dead && !beginCount) {
            beginCount = true;
            explosion = System.currentTimeMillis();
        }
        
        if (dead && System.currentTimeMillis() - explosion > 50) remove = true;
        
    }
    
    public void paint(Graphics g) {
        
        URL imageurl;
        int size;
        int offset = 0;
        
        if (!dead) {
            imageurl = getClass().getResource("/shooter/magikarp.jpg");
            size = 60;
        }
        else {
            imageurl = getClass().getResource("/shooter/boom.jpg");
            size = 40;
            offset = 10;
        }
        
        Image background = Toolkit.getDefaultToolkit().getImage(imageurl);
        g.drawImage(background, (int)x+offset,(int)y+offset,size,size, null);
        
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,60,60);
    }
    
}
