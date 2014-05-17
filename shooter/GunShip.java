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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.Timer;

/**
 *
 * @author Ryan
 */
public class GunShip implements KeyListener, ActionListener{

    
    double x = 25;
    GameBoard game;
    double y = 210, vely = 0, velx = 0;
    Timer t;
    LinkedList<Bullet> bullets = new LinkedList<>();
    long chargeTime = 0;
    boolean chargingSprite = false;
    boolean charging = false;
    long spriteTimeChange = System.currentTimeMillis();
    long spritePulseTime;
    boolean chargedShotReady = false;
    private Image sprite;
    private Image cloud;
    
    public GunShip(GameBoard game) {
        this.game = game;
        t = new Timer(5,this);
        t.start();
    }
    
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP) {
            up();
        } else if (code  == KeyEvent.VK_DOWN) {
            down();
        } else if (code  == KeyEvent.VK_LEFT) {
            left();
        } else if (code  == KeyEvent.VK_RIGHT) {
            right();
        }else if (code  == KeyEvent.VK_SPACE && !charging) {
            
            shoot(false);
            
            if (!charging) {
                chargeTime = System.currentTimeMillis();
                charging = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        int code = e.getKeyCode();
        
        if (code  == KeyEvent.VK_DOWN || code == KeyEvent.VK_UP)
            vely = 0;
        
        if (code  == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT)
            velx = 0;
        
        if (code  == KeyEvent.VK_SPACE && charging) {
            
            if (System.currentTimeMillis() - chargeTime > 800) {
                shoot(true);
                chargedShotReady = false;
            }
            
            else if (System.currentTimeMillis() - chargeTime > 500) shoot(false);
            
            charging = false;
            chargingSprite = charging;
            spritePulseTime = System.currentTimeMillis();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (y+100 < 500 && y > 40)
            y += vely;
        
        if (y <= 55) y = 56;
        
        if (y+100 >= 500) {
            y = 399;
        }
        
        if (charging && System.currentTimeMillis() - chargeTime > 100) 
            chargingSprite = true;
        
        if (x + velx > 0 && x + velx < game.getWidth()-50)
            x += velx;
        
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).actionPerformed(e);
            
            if (bullets.get(i).x > game.getWidth()+50)
                bullets.remove(i);
        }
        
        if (System.currentTimeMillis() - chargeTime > 800 && charging) {
            chargedShotReady = true;
        }
        
        
    }
    
    public void up() {
        vely = -1.8;
    }
    
    public void down() {
        vely = 1.8;
    }
    
    public void left() {
        velx = -1.8;
    }
    
    public void right() {
        velx = 1.8;
    }
    
    public void shoot(boolean charged) {
        if (!charged)
            bullets.add(new Bullet(x+49,y+37,t));
        else
            bullets.add(new Bullet(x+49,y+37,t,true));
    }
    
    public void paint(Graphics g) {

        URL spriteurl;
        
        try {
            if (!chargingSprite) {
                if (System.currentTimeMillis() - spriteTimeChange < 40) {
                    spriteurl = getClass().getResource("/shooter/Not Shooting Goku Sprite.jpg");
                    sprite = Toolkit.getDefaultToolkit().getImage(spriteurl);
                }
                else if (System.currentTimeMillis() - spriteTimeChange < 80) {
                    spriteurl = getClass().getResource("/shooter/Not Shooting Goku Sprite 3.png");
                    sprite = Toolkit.getDefaultToolkit().getImage(spriteurl);
                }
                else if (System.currentTimeMillis() - spriteTimeChange < 120) {
                    spriteurl = getClass().getResource("/shooter/Not Shooting Goku Sprite 2.png");
                    sprite = Toolkit.getDefaultToolkit().getImage(spriteurl);
                }
                else if (System.currentTimeMillis() - spriteTimeChange >= 160){
                    spriteurl = getClass().getResource("/shooter/Not Shooting Goku Sprite 3.png");
                    sprite = Toolkit.getDefaultToolkit().getImage(spriteurl);
                    
                    if (System.currentTimeMillis() - spriteTimeChange >= 200)
                        spriteTimeChange = System.currentTimeMillis();
                }
            }
            else if (chargedShotReady) {
                
                if (System.currentTimeMillis() - spritePulseTime < 60)
                    spriteurl = getClass().getResource("/shooter/Shooting Goku Sprite.jpg");
                else {
                    spriteurl = getClass().getResource("/shooter/Shooting Goku Sprite Pulse.jpg");
                    
                    if (System.currentTimeMillis() - spritePulseTime >= 120)
                        spritePulseTime = System.currentTimeMillis();
                }
                
                sprite = Toolkit.getDefaultToolkit().getImage(spriteurl);
            }
            else {
                spriteurl = getClass().getResource("/shooter/Shooting Goku Sprite Pulse.jpg");
                sprite = Toolkit.getDefaultToolkit().getImage(spriteurl);
            }
            
            URL imageurl = getClass().getResource("/shooter/cloud.png");
            cloud = Toolkit.getDefaultToolkit().getImage(imageurl);
            
            
        } catch (Exception ex) {
            System.out.println("Error: Image Not Found");
        }
        
        if (!chargingSprite)
            g.drawImage(sprite,(int)x,(int)y,55,70,null);
        else
            g.drawImage(sprite,(int)x,(int)y,77,70,null);
        
        g.drawImage(cloud,(int)x-10,(int)y+62,66,27,null);

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).paint(g);
        }
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y+25,40,45);
    }
}
