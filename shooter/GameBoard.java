/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package shooter;

import java.awt.Color;
import java.awt.Font;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Ryan
 */
public class GameBoard extends JPanel implements ActionListener {

    GunShip gunShip;
    Timer t = new Timer(5,this);
    LinkedList<EnemyFormation> enemies = new LinkedList<>();
    int score = 0;
    int lives = 5;
    long start = System.currentTimeMillis();
    long waveTime = System.currentTimeMillis();
    int waveDuration = 5000;
    boolean newWave = false;
    boolean newWavePause = false;
    int waveNumber = 1;
    boolean charging = false;
    boolean lastEnemyPast = false;
    boolean bossFight = false;
    boolean pause = false;
    long chargeTime = 0;
    ExtraLife extraLife = null;
    Boss boss = null;
    
    public GameBoard() {

        t.start();
        gunShip = new GunShip(this);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                gunShip.keyReleased(e);
                
                int code = e.getKeyCode();
        
                if (code  == KeyEvent.VK_SPACE)
                    charging = false;
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
                int code = e.getKeyCode();
                
                gunShip.keyPressed(e);
                
                if (!charging && code  == KeyEvent.VK_SPACE) {
                    chargeTime = System.currentTimeMillis();
                    charging = true;
                }

                if (code  == KeyEvent.VK_P) {
                    
                    pause = true;
                    Object[] options = {"Return"};
                    
                    while (JOptionPane.showOptionDialog(null, "Welcome To The Shooter", "New Game",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, options, options[0]) != 0) {
                        t.stop();
                    };
                    
                    t.start();
                    pause = false;
                }
            }
            
        });

        setFocusable(true);
    }

    public void paint(Graphics g) {
        
        repaint();
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        
        /*
        Plain Black Background
        g2.setColor(Color.black);
        g2.fill(new Rectangle(0,0,this.getWidth(),this.getHeight()));
        */
        
        URL imageurl = getClass().getResource("/shooter/cloudy_sky.png");
        Image background = Toolkit.getDefaultToolkit().getImage(imageurl);
        g.drawImage(background, 0, 0, 1000,500, null);
        
        gunShip.paint(g);
        
        if (boss != null)
            boss.paint(g);
        
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).paint(g);
        }
        
        g2.setColor(Color.black);
        
        char[] scoreWords = new char[]{'S','c','o','r','e',':',' ',' ',' ',' ',' ',' ',' ',' ',' '};
        char[] livesWords = new char[]{'L','i','v','e','s',':'};
        char[] waveWords = new char[]{'W','a','v','e',':',' ',' ',' ',' ',' ',' '};
        
        for (int i = 0; i < String.valueOf(score).length(); i++) {
            scoreWords[7+i] = String.valueOf(score).charAt(i);
        }
        
        for (int i = 0; i < String.valueOf(waveNumber).length(); i++) {
            waveWords[7+i] = String.valueOf(waveNumber).charAt(i);
        }
        
        g2.drawChars(scoreWords, 0, 15, 140, 35);
        g2.drawLine(0, 60, this.getWidth(), 60);
        
        g2.drawChars(livesWords, 0, 6, 340, 35);

        g2.drawChars(waveWords, 0, 11, 240, 35);

        int tmpLives = lives;
        
        if (lives > 3) {
            g2.drawChars(new char[]{'+', String.valueOf(lives-3).charAt(0)}, 0, 2, 455, 35);
            tmpLives = 3;
        }
        
        for (int i = 0; i < tmpLives; i++) {
            URL image = getClass().getResource("/shooter/1up.jpg");
            Image life = Toolkit.getDefaultToolkit().getImage(image);
            g.drawImage(life, 380+i*25,20,20,20, null);
        }


        //Charging
        g2.setColor(Color.black);
        g2.fill(new Rectangle(25,23,100,15));
        
        g2.setColor(Color.GREEN);
        if (System.currentTimeMillis() - gunShip.chargeTime > 800 && gunShip.charging) {
            g2.fill(new Rectangle(25,23,100,15));
        }
        else if (gunShip.charging && System.currentTimeMillis() - gunShip.chargeTime > 100) {
            g2.fill(new Rectangle(25,23,Integer.parseInt(String.valueOf(System.currentTimeMillis() - gunShip.chargeTime - 100))/7,15));
        }
        g2.setColor(Color.black);
        g2.draw(new Rectangle(25,23,100,15));
        //
        
        if (waveNumber%5 == 0 && lastEnemyPast && System.currentTimeMillis() - waveTime < waveDuration + 6000) {
            g2.setColor(Color.RED);
            g2.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,40));
            g2.drawString("BOSS FIGHT", 500, 45);
        }
        
        else if (lastEnemyPast && System.currentTimeMillis() - waveTime < waveDuration + 6000) {
            g2.setColor(Color.YELLOW);
            g2.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,40));
            g2.drawString("NEW WAVE", 500, 45);
        }
        
        if (extraLife != null) extraLife.paint(g);
        
        if (bossFight) {
            g2.setColor(Color.black);
            g2.fill(new Rectangle(525,23,100,15));
            
            g2.setColor(Color.RED);
            g2.fill(new Rectangle(525,23,100*boss.health/boss.maxHealth,15));
            
            g2.setColor(Color.black);
            g2.draw(new Rectangle(525,23,100,15));
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        
        if (waveNumber%5 == 0 && !bossFight && System.currentTimeMillis() - waveTime > waveDuration + 6000) {
            boss = new Boss(t,waveNumber);
            bossFight = true;
        }
        
        if (bossFight) {
            
            boss.actionPerformed(e);
            
            for (int k = 0; k < gunShip.bullets.size(); k++) {
                if (boss.isHit(gunShip.bullets.get(k))) {
                    
                    if (gunShip.bullets.get(k).charged)
                        score += 250;
                    else score += 50;
                    gunShip.bullets.remove(k);
                }
            }
            
            if (boss.isDead()) {
                boss = null;
                bossFight = false;
                newWave = true;
                start = System.currentTimeMillis() - 1001;
                waveNumber++;
                waveTime = System.currentTimeMillis() - 3000 - waveDuration;
            }
        }
        
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).actionPerformed(e);
        }
        
        if (extraLife != null) extraLife.actionPerformed(e);
        
        for (int i = 0; i < enemies.size(); i++) {
            for (int j = 0; j < enemies.get(i).numberOfEnemies; j++) {
                for (int k = 0; k < gunShip.bullets.size(); k++) {
                    if (enemies.get(i).enemies[j] != null && gunShip.bullets.get(k).getBounds().intersects(enemies.get(i).enemies[j].getBounds())) {
                        if (!gunShip.bullets.get(k).charged)
                            gunShip.bullets.remove(k);
                        enemies.get(i).enemies[j] = null;
                        score += 50;
                    }
                }
                
                if (enemies.get(i).enemies[j] != null && enemies.get(i).enemies[j].getBounds().intersects(gunShip.getBounds())) {
                    
                    long wait = System.currentTimeMillis();
                    
                    waveTime += 2500;
                    while(System.currentTimeMillis() - wait < 1000);
                    start = System.currentTimeMillis();
                    lives--;
                    enemies = new LinkedList<>();
                    gunShip.bullets = new LinkedList<>();
                    extraLife = null;
                    gunShip.x =25;
                    gunShip.y =210;
                    return;
                }
            }
        }
        
        if (bossFight) {
            for (int i = 0; i < boss.bullets.size(); i++) {
                if (gunShip.getBounds().intersects(boss.bullets.get(i).getBounds())) {
                    long wait = System.currentTimeMillis();
                    while(System.currentTimeMillis() - wait < 1000);
                    boss.waitToFire = System.currentTimeMillis();
                    gunShip.bullets = new LinkedList<>();
                    boss.bullets = new LinkedList<>();
                    gunShip.x =25;
                    gunShip.y =210;
                    lives--;
                    return;
                }
            }
        }
        
        double enemyWait = 1500/(waveNumber/10+1.3);
        
        if (!bossFight && System.currentTimeMillis() - start > 1000 && !newWave) {
            
            int tmp = (int)(Math.random() * 100);
            
            if (tmp < 3) {
                if (extraLife == null)
                    extraLife = new ExtraLife(this, waveNumber);
                else tmp = (int)(Math.random()*95) + 5;
            }
            if (tmp >= 3 && tmp < 19)
                enemies.add(new EnemyFormationRandom(this, waveNumber));
            else if (tmp >= 19 && tmp < 36)
                enemies.add(new EnemyFormationWall(this, waveNumber));
            else if (tmp >= 36 && tmp < 51)
                enemies.add(new EnemyFormationMovingLine(this, waveNumber));
            else if (tmp >= 51 && tmp < 66)
                enemies.add(new EnemyFormationWave(this, waveNumber));
            else if (tmp >= 66 && tmp < 83)
                enemies.add(new EnemyFormationLine(this, waveNumber));
            else if (tmp >= 83 && tmp < 95)
                enemies.add(new EnemyFormationDucks(this, waveNumber));
            else if (tmp >= 95)
                enemies.add(new EnemyFormationTheBrick(this, waveNumber));
                    
            start = System.currentTimeMillis();
        }
        
        if (extraLife != null && !extraLife.getBounds().intersects(new Rectangle(0,0,this.getWidth(),this.getHeight())))
            extraLife = null;
        
        if (extraLife != null && extraLife.getBounds().intersects(gunShip.getBounds())) {
            lives++;
            extraLife = null;
        }
        
        for (int i = enemies.size()-1; i >= 0; i--) {
            
            boolean remove = true;
            
            for (int j = enemies.get(i).numberOfEnemies-1; j >= 0; j--) {
                if (enemies.get(i).enemies[j] != null 
                        && enemies.get(i).enemies[j].getBounds().intersects(new Rectangle(0,0,this.getWidth(),this.getHeight())))
                    remove = false;
            }
            
            if (remove) enemies.remove(i);
        }
        
        if (!bossFight && System.currentTimeMillis() - start > 1000 && enemies.isEmpty() && !lastEnemyPast) {
            lastEnemyPast = true;
            waveNumber++;
        }
        
        if (!bossFight && System.currentTimeMillis() - waveTime > waveDuration) {
            
            if (!newWave)
                newWave = true;

            if (System.currentTimeMillis() - waveTime > waveDuration + 7000) {
                
                waveDuration += 5000;
                waveTime = System.currentTimeMillis();
                newWavePause = false;
                newWave = false;
                lastEnemyPast = false;
            }
        }
        
        if (lives == 0) {
            gameOver();
        }
    }
    
    public void gameOver() {

        t.stop();
        
        Object options[] = new Object[]{"Exit", "New Game"};

        int choice = JOptionPane.showOptionDialog(null, " Your score was: " + score, "Game Over",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 1) {
            GunShip gunShip = new GunShip(this);
            t = new Timer(5,this);
            LinkedList<EnemyFormation> enemies = new LinkedList<>();
            score = 0;
            lives = 3;
            start = System.currentTimeMillis();
            waveTime = System.currentTimeMillis();
            waveDuration = 5000;
            newWave = false;
            newWavePause = false;
            waveNumber = 1;
            charging = false;
            lastEnemyPast = false;
            chargeTime = 0;
            t.start();
        } 
        
        else {
            System.exit(0);
        }

    }
}