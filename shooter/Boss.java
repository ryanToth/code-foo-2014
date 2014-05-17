package shooter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ryan
 */
public class Boss implements ActionListener {

    int health = 10;
    int maxHealth = 10;
    double x = 1000, y = 100, velx = -0.6, vely = 1;
    double shotDelay;
    Timer t;
    Image sprite;
    LinkedList<Rectangle> bulletHits = new LinkedList<>();
    LinkedList<Long> hitTimes = new LinkedList<>();
    LinkedList<Double> movements = new LinkedList<>();
    LinkedList<Bullet> bullets = new LinkedList<>();
    long interval = System.currentTimeMillis();
    long waitToFire = System.currentTimeMillis();
    long fireBallTime = System.currentTimeMillis();
    long deadFlash = 0;
    
    public Boss(Timer t, int level) {
        
        this.t = t;
        t.start();  
        shotDelay = 4000/level + level*10;
        
        if (shotDelay < 500) shotDelay = 500;
        
        health = level * 20;
        maxHealth = health;
    }
    
    public void paint(Graphics g) {
        
        if (System.currentTimeMillis() - interval > 200) 
            mouthClosed();
        
        
        if (System.currentTimeMillis() - interval > 400) {
            mouthOpen();
            interval = System.currentTimeMillis();
        }
        
        if (health <= 0) {
            if (System.currentTimeMillis() - deadFlash >= 10) {
                mouthOpen();
                deadFlash = System.currentTimeMillis();
            }
            else sprite = null; 
        }

        g.drawImage(sprite, (int)x, (int)y, 164, 197, null);
        
        if (!bulletHits.isEmpty() && health > 0) {
            for (int i = 0; i < bulletHits.size(); i++) {
                
                URL imageurl = getClass().getResource("/shooter/boom.jpg");
                Image hit = Toolkit.getDefaultToolkit().getImage(imageurl);
                g.drawImage(hit, (int)(bulletHits.get(i).getMaxX()-5),(int)(bulletHits.get(i).getMinY()-15 + movements.get(i)),29,29, null);
            }
        }
        
        for (int i = 0; i < bullets.size(); i++)
            bullets.get(i).paint(g);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (y < 53) vely = 0.6;
        else if (y+180 > 500) vely = -0.6;
        
        if (x > 830) x += velx;
        
        if (health <= 0) vely = 0.6;
        
        y += vely;
        
        for (int i = 0; i < hitTimes.size(); i++) {
            
            double moveY = movements.remove(i);
            moveY += vely;
            movements.add(i,moveY);
                    
            if (System.currentTimeMillis() - hitTimes.get(i) > 100) {
                hitTimes.remove(i);
                bulletHits.remove(i);
                movements.remove(i);
            }
                
        }

        if (System.currentTimeMillis() - waitToFire > 2000 && health > 0) {

            if (System.currentTimeMillis() - fireBallTime > shotDelay) {
                bullets.add(new Bullet(x - 20, y + 66 , t, 0));
                fireBallTime = System.currentTimeMillis();
            }
            
        }
        
        for (int i = 0; i < bullets.size(); i++)
            bullets.get(i).actionPerformed(e);
        
    }
    
    public void mouthOpen() {
        
        URL imageurl = getClass().getResource("/shooter/gyrados.jpg");
        sprite = Toolkit.getDefaultToolkit().getImage(imageurl);
    }
    
    public void mouthClosed() {
        
        URL imageurl = getClass().getResource("/shooter/gyradosMouth.jpg");
        sprite = Toolkit.getDefaultToolkit().getImage(imageurl);
    }
    
    public boolean isHit(Bullet bullet) {
        
        boolean hit = false;
        
        if (bullet.getBounds().intersects(new Rectangle((int)x,(int)y+15,50,90)))
            hit = true;
        else if (bullet.getBounds().intersects(new Rectangle((int)x+50,(int)y,40,170)))
            hit = true;
        else if (bullet.getBounds().intersects(new Rectangle((int)x+110,(int)y+60,50,140)))
            hit = true;
        else if (bullet.getBounds().intersects(new Rectangle((int)x+90,(int)y+30,20,140)))
            hit = true;
        
        if (hit) {
            
            if (bullet.charged) health -= 10;
            else health = health - 1;
            hitTimes.add(System.currentTimeMillis());
            bulletHits.add(bullet.getBounds());
            movements.add(0.0);
        }
        
        return hit;
    }
    
    public boolean isDead() {
        
        return health <= 0 && y > 500;
    }
    
}
