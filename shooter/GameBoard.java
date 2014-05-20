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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;


/**
 *
 * @author Ryan
 */
public class GameBoard extends JPanel implements ActionListener {

    boolean blockHighScore = true;
    GunShip gunShip;
    Timer t = new Timer(5,this);
    LinkedList<EnemyFormation> enemies = new LinkedList<>();
    int score = 0;
    int lives = 3;
    long previousPause = 0;
    long pauseLength = 0;
    long start = System.currentTimeMillis();
    long waveTime = System.currentTimeMillis();
    int waveDuration = 5000;
    boolean newWave = false;
    long newWavePause = 0;
    int waveNumber = 1;
    boolean charging = false;
    boolean lastEnemyPast = false;
    boolean bossFight = false;
    boolean pause = false;
    long chargeTime = 0;
    String highscore = "0";
    ExtraLife extraLife = null;
    Boss boss = null;
    
    public GameBoard() throws UnsupportedEncodingException, IOException {

        if (!blockHighScore) {
            InputStream configStream = getClass().getResourceAsStream("highscore.txt");
            BufferedReader configReader = new BufferedReader(new InputStreamReader(configStream, "UTF-8"));
            boolean firstTry = true;

            while (configReader.ready() && firstTry) {
                highscore = configReader.readLine();
                firstTry = false;
            }
        }
        
        gunShip = new GunShip(this);
        t.start();
        
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
                    
                    if (!pause) pauseLength = System.currentTimeMillis();
                    
                    pause = true;
                    Object[] options = {"Return"};
                    
                    while (JOptionPane.showOptionDialog(null, "Welcome To The Shooter", "New Game",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, options, options[0]) != 0) {
                        
                            waveTime -= previousPause;
                            start -= previousPause;
                            newWavePause -= previousPause;
            
                            waveTime += System.currentTimeMillis() - pauseLength;
                            start += System.currentTimeMillis() - pauseLength;
                            newWavePause += System.currentTimeMillis() - pauseLength;
            
                            previousPause = System.currentTimeMillis() - pauseLength;
                    }
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
        
        String scoreWords = "Score: " ;
        String livesWords = "Lives: ";
        String waveWords = "Wave: ";

        scoreWords += String.valueOf(score);
        
        waveWords += String.valueOf(waveNumber);
        
        g2.setColor(Color.black);
        g2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,14));
        g2.drawString(scoreWords, 140, 35);
        g2.drawString(livesWords, 340, 35);
        g2.drawString(waveWords, 260, 35);
        
        g2.drawLine(0, 60, this.getWidth(), 60);

        int tmpLives = lives;
        
        if (lives > 3) {
            g2.drawChars(new char[]{'+', String.valueOf(lives-3).charAt(0)}, 0, 2, 460, 35);
            tmpLives = 3;
        }
        
        for (int i = 0; i < tmpLives; i++) {
            URL image = getClass().getResource("/shooter/1up.jpg");
            Image life = Toolkit.getDefaultToolkit().getImage(image);
            g.drawImage(life, 385+i*25,20,20,20, null);
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
        
        int newWaveOffSet = 0;
        
        if (lives < 4) newWaveOffSet = (4 - lives) * 10;
        
        if (waveNumber%5 == 0 && lastEnemyPast && System.currentTimeMillis() - newWavePause < 3500) {
            g2.setColor(Color.RED);
            g2.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,40));
            g2.drawString("BOSS FIGHT", 500 - newWaveOffSet, 45);
        }
        
        else if (lastEnemyPast && System.currentTimeMillis() - newWavePause < 3500) {
            g2.setColor(Color.black);
            g2.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,40));
            g2.drawString("NEW WAVE", 510 - newWaveOffSet, 45);
        }
        
        if (extraLife != null) extraLife.paint(g);
        
        if (bossFight) {
            g2.setColor(Color.black);
            g2.fill(new Rectangle(555,23,100,15));
            
            g2.setColor(Color.RED);
            g2.fill(new Rectangle(555,23,100*boss.health/boss.maxHealth,15));
            
            g2.setColor(Color.black);
            g2.draw(new Rectangle(555,23,100,15));
        }
        
        g2.setColor(Color.black);
        g2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,14));
        g2.drawString("High Score: " + highscore, 750, 35);
    }
    
    public void actionPerformed(ActionEvent e) {

        if (!pause) {
            
            double enemyWait = 1500 / (waveNumber / 10 + 1.3);
            
            gunShip.actionPerformed(e);
            
            if (waveNumber % 5 == 0 && !bossFight && System.currentTimeMillis() - newWavePause >= 3500) {
                boss = new Boss(t, waveNumber);
                bossFight = true;
            }

            if (bossFight) {

                boss.actionPerformed(e);

                for (int k = 0; k < gunShip.bullets.size(); k++) {
                    if (boss.isHit(gunShip.bullets.get(k))) {

                        if (gunShip.bullets.get(k).charged) {
                            score += 250;
                        } else {
                            score += 50;
                        }
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

            if (extraLife != null) {
                extraLife.actionPerformed(e);
            }

            for (int i = 0; i < enemies.size(); i++) {
                for (int j = 0; j < enemies.get(i).numberOfEnemies; j++) {
                    for (int k = 0; k < gunShip.bullets.size(); k++) {
                        if (enemies.get(i).enemies[j] != null && gunShip.bullets.get(k).getBounds().intersects(enemies.get(i).enemies[j].getBounds())
                                && !enemies.get(i).enemies[j].dead) {
                            if (!gunShip.bullets.get(k).charged) {
                                gunShip.bullets.remove(k);
                            }
                            enemies.get(i).enemies[j].dead = true;
                            score += 50;
                        }
                    }

                    if (enemies.get(i).enemies[j] != null && enemies.get(i).enemies[j].getBounds().intersects(gunShip.getBounds())) {

                        long wait = System.currentTimeMillis();

                        waveTime += 2500;
                        while (System.currentTimeMillis() - wait < 1000);
                        start = System.currentTimeMillis();
                        lives--;
                        enemies = new LinkedList<>();
                        gunShip.bullets = new LinkedList<>();
                        extraLife = null;
                        gunShip.x = 25;
                        gunShip.y = 210;
                        return;
                    }
                    if (enemies.get(i).enemies[j] != null && enemies.get(i).enemies[j].remove) {
                        enemies.get(i).enemies[j] = null;
                    }
                }
            }

            if (bossFight) {
                for (int i = 0; i < boss.bullets.size(); i++) {
                    if (gunShip.getBounds().intersects(boss.bullets.get(i).getBounds())) {
                        long wait = System.currentTimeMillis();
                        while (System.currentTimeMillis() - wait < 1000);
                        boss.waitToFire = System.currentTimeMillis();
                        gunShip.bullets = new LinkedList<>();
                        boss.bullets = new LinkedList<>();
                        gunShip.x = 25;
                        gunShip.y = 210;
                        lives--;
                        return;
                    }
                }
            }


            if (!bossFight && System.currentTimeMillis() - start > enemyWait && !newWave) {

                int tmp = (int) (Math.random() * 100);
                boolean up1 = false;

                if (tmp < 5) {
                    if (extraLife == null) {
                        extraLife = new ExtraLife(this, waveNumber);
                        up1 = true;
                    } else {
                        tmp = (int) (Math.random() * 95) + 5;
                        up1 = false;
                    }

                }

                if (!up1) {
                    if (tmp >= 5 && tmp < 19) {
                        enemies.add(new EnemyFormationRandom(this, waveNumber));
                    } else if (tmp >= 19 && tmp < 36) {
                        enemies.add(new EnemyFormationWall(this, waveNumber));
                    } else if (tmp >= 36 && tmp < 51) {
                        enemies.add(new EnemyFormationMovingLine(this, waveNumber));
                    } else if (tmp >= 51 && tmp < 66) {
                        enemies.add(new EnemyFormationWave(this, waveNumber));
                    } else if (tmp >= 66 && tmp < 83) {
                        enemies.add(new EnemyFormationLine(this, waveNumber));
                    } else if (tmp >= 83 && tmp < 95) {
                        enemies.add(new EnemyFormationDucks(this, waveNumber));
                    } else if (tmp >= 95) {
                        enemies.add(new EnemyFormationTheBrick(this, waveNumber));
                    }
                }
                start = System.currentTimeMillis();
            }

            if (extraLife != null && !extraLife.getBounds().intersects(new Rectangle(0, 0, this.getWidth(), this.getHeight()))) {
                extraLife = null;
            }

            if (extraLife != null && extraLife.getBounds().intersects(gunShip.getBounds())) {

                if (lives < 13) {
                    lives++;
                }
                extraLife = null;
            }

            for (int i = enemies.size() - 1; i >= 0; i--) {

                boolean remove = true;

                for (int j = enemies.get(i).numberOfEnemies - 1; j >= 0; j--) {
                    if (enemies.get(i).enemies[j] != null
                            && enemies.get(i).enemies[j].getBounds().intersects(new Rectangle(0, 0, this.getWidth(), this.getHeight()))) {
                        remove = false;
                    }
                }

                if (remove) {
                    enemies.remove(i);
                }
            }

            if (extraLife == null && !bossFight && System.currentTimeMillis() - start > enemyWait && enemies.isEmpty() && !lastEnemyPast) {
                lastEnemyPast = true;
                waveNumber++;
                newWavePause = System.currentTimeMillis();
            }

            if (!bossFight && System.currentTimeMillis() - waveTime > waveDuration) {

                if (!newWave) {
                    newWave = true;
                }

                if (lastEnemyPast && System.currentTimeMillis() - newWavePause > 3500) {

                    waveDuration += 5000;
                    newWavePause = 0;
                    waveTime = System.currentTimeMillis();
                    newWave = false;
                    lastEnemyPast = false;
                }
            }

            if (lives == 0) {
                try {
                    gameOver();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (Integer.parseInt(highscore) < score) {

                highscore = String.valueOf(score);
                
                if (!blockHighScore) {
                    BufferedWriter writer = null;
                    try {
                        writer = new BufferedWriter(new PrintWriter("src/shooter/highscore.txt", "UTF-8"));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        writer.write(highscore);
                    } catch (IOException ex) {
                        Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        writer.close();
                    } catch (IOException ex) {
                        Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }
        
        else  {
            waveTime -= previousPause;
            start -= previousPause;
            newWavePause -= previousPause;
            
            waveTime += System.currentTimeMillis() - pauseLength;
            start += System.currentTimeMillis() - pauseLength;
            newWavePause += System.currentTimeMillis() - pauseLength;
            
            previousPause = System.currentTimeMillis() - pauseLength;
        }
    }

    public void gameOver() throws FileNotFoundException, UnsupportedEncodingException {

        t.stop();
        
        Object options[] = new Object[]{"Exit", "Reset High Score", "New Game"};
        
        int choice = 1;

        while (choice != 2) {
            
            choice = JOptionPane.showOptionDialog(null, " Your score was: " + score, "Game Over",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[2]);

            if (choice == 2) {

                gunShip = new GunShip(this);
                t = new Timer(5, this);
                enemies = new LinkedList<>();
                score = 0;
                boss = null;
                lives = 3;
                start = System.currentTimeMillis();
                waveTime = System.currentTimeMillis();
                waveDuration = 5000;
                newWave = false;
                waveNumber = 1;
                charging = false;
                lastEnemyPast = false;
                chargeTime = 0;
                t.start();

            } else if (choice == 1) {

                highscore = "0";
                
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new PrintWriter("src/shooter/highscore.txt", "UTF-8"));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    writer.write("0");
                } catch (IOException ex) {
                    Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                System.exit(0);
            }
        }
    }
}