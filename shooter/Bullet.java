/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package shooter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.net.URL;
import javax.swing.Timer;

/**
 *
 * @author Ryan
 */
public class Bullet implements ActionListener {
    
    double x, y, velx;
    boolean charged = false, fireBall = false;
    Timer t;
    
    public Bullet(double x, double y, Timer t) {
        
        this.x = x;
        this.y = y; 
        this.t = t;
        velx = 4;
    }
    
    public Bullet(double x, double y, Timer t, boolean charged) {
        
        this.x = x;
        this.y = y; 
        this.t = t;
        this.charged = charged;
        velx = 4;
    }
    
    public Bullet(double x, double y, Timer t, int dragon) {
        
        this.x = x;
        this.y = y; 
        this.t = t;
        fireBall = true;
        velx = -4;
    }
    
    public void paint(Graphics g) {
        
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setColor(Color.red);
        
        if (fireBall) {
            URL imageurl = getClass().getResource("/shooter/hyperBeam.jpg");
            Image hit = Toolkit.getDefaultToolkit().getImage(imageurl);
            g.drawImage(hit, (int)x,(int)y,60,44, null);
        }
        else if (!charged)
            g2.fill(new Ellipse2D.Double(x,y,15,3));
        else if (charged)
            g2.fill(new Ellipse2D.Double(x,y-2,50,7));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        x += velx;
        
    }
    
    public Rectangle getBounds() {
        
        Rectangle tmp = null;

        if (fireBall) tmp = new Rectangle((int)x,(int)y,60,44);
        else if (!charged) tmp = new Rectangle((int)x,(int)y,15,3);
        else if (charged) tmp = new Rectangle((int)x,(int)y,50,7);
        
        return tmp;
    }
}
