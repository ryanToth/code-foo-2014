package pacman;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author Ryan
 */
public class PacMan implements ActionListener, KeyListener {

    Timer t = new Timer(5, this);
    private GameBoard game;
    private Maze maze;
    private Ghost ghost1;
    private Ghost ghost2;
    private Ghost ghost3;
    private Ghost ghost4;
    long start;
    private int size = 30;
    double x = 539, y = 360, velx = 0, vely = 0;
    boolean[][] dotArray;
    boolean[][] startDotArray;
    Dots dot[][] = new Dots[25][19];
    boolean startUp = true;
    int score = 0;
    boolean ghostAppear1 = false;
    boolean ghostAppear2 = false;
    boolean ghostAppear3 = false;
    boolean ghostAppear4 = false;
    int lives = 3;
    String direction = "left";
    long mouthClose = System.currentTimeMillis();
    char[] scoreWords = new char[]{'S', 'c', 'o', 'r', 'e', ':', ' ', ' ', ' ', ' ', ' '};
    boolean gameOver;
    boolean youWin;

    public PacMan(GameBoard game) {

        this.game = game;
        maze = new Maze(game);
        t.start();
        setDots();
        start = System.currentTimeMillis();
        youWin = false;
        gameOver = false;
    }

    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        
        if (!gameOver && !youWin) {

            g2.setColor(Color.yellow);
            g2.fill(new Ellipse2D.Double(x, y, size, size));

            g2.setColor(Color.black);

            if (System.currentTimeMillis() - mouthClose > 200) {

                if (direction.equals("left")) {
                    g2.fill(new Ellipse2D.Double(x - 10, y + 8, 25, 15));
                }

                if (direction.equals("right")) {
                    g2.fill(new Ellipse2D.Double(x + 15, y + 8, 25, 15));
                }

                if (direction.equals("up")) {
                    g2.fill(new Ellipse2D.Double(x + 8, y - 10, 15, 25));
                }

                if (direction.equals("down")) {
                    g2.fill(new Ellipse2D.Double(x + 8, y + 15, 15, 25));
                }

                if (System.currentTimeMillis() - mouthClose > 400) {
                    mouthClose = System.currentTimeMillis();
                }

            }
        }
        
        maze.paint(g);

        for (int x = 0; x < 19; x++) {
            for (int y = 0; y < 25; y++) {
                if (dotArray[y][x]) {
                    new Dots(30 * x + 15, 30 * y + 15).paint(g);
                    dot[y][x] = new Dots(30 * x + 15, 30 * y + 15);
                }
            }
        }
        
        if (!gameOver && !youWin) {

            if (!ghostAppear1) {
                ghost1 = new Ghost(maze, game, Color.ORANGE);
                ghostAppear1 = true;
            }

            ghost1.paint(g);

            if (System.currentTimeMillis() - start > 2000) {

                if (!ghostAppear2) {
                    ghost2 = new Ghost(maze, game, Color.PINK);
                    ghostAppear2 = true;
                }

                ghost2.paint(g);
            }

            if (System.currentTimeMillis() - start > 4000) {

                if (!ghostAppear3) {
                    ghost3 = new Ghost(maze, game, Color.CYAN);
                    ghostAppear3 = true;
                }

                ghost3.paint(g);
            }

            if (System.currentTimeMillis() - start > 6000) {

                if (!ghostAppear4) {
                    ghost4 = new Ghost(maze, game, Color.RED);
                    ghostAppear4 = true;
                }

                ghost4.paint(g);
            }
        }
        
        g2.setColor(Color.WHITE);

        for (int i = 0; i < String.valueOf(score).toCharArray().length; i++) {
            scoreWords[7 + i] = String.valueOf(score).toCharArray()[i];
        }

        g2.drawChars(scoreWords, 0, 11, 600, 100);

        g2.drawChars(new char[]{'L', 'i', 'v', 'e', 's', ':'}, 0, 6, 600, 150);

        g2.setColor(Color.YELLOW);

        for (int i = 0; i < lives; i++) {
            g2.fill(new Ellipse2D.Double(645 + i * 30, 135, 20, 20));
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        win();
        
        if (lives == 0) {
            gameOver();
        }
        
        if(youWin) {
            youWin();
        }

        if (y + vely + 30 <= game.getHeight() && y + vely >= 0) {

            y += vely;

            if (maze.doesCollide(getBounds())) {
                y -= vely;
            }
        }

        if (x + velx + 30 <= 570 && x + velx >= 0) {

            x += velx;

            if (maze.doesCollide(getBounds())) {
                x -= velx;
            }
        }

        if (x + velx + 30 > 569 && y < game.getHeight() / 2 + 30 && y > game.getHeight() / 2 - 30) {
            x = 1;
            y = 360;
        }

        if (x + velx < 1.5 && y < game.getHeight() / 2 + 30 && y > game.getHeight() / 2 - 30) {
            x = 539;
            y = 360;
        }

        if (dead()) {
            long wait = System.currentTimeMillis();
            while (System.currentTimeMillis() - wait < 2000);
            reset();
        }

        removeDot();
    }

    public void up() {
        vely = -0.75;
        velx = 0;
        direction = "up";
    }

    public void down() {
        vely = 0.75;
        velx = 0;
        direction = "down";
    }

    public void left() {
        vely = 0;
        velx = -0.75;
        direction = "left";
    }

    public void right() {
        vely = 0;
        velx = 0.75;
        direction = "right";
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP) {
            up();
        } else if (code == KeyEvent.VK_DOWN) {
            down();
        } else if (code == KeyEvent.VK_LEFT) {
            left();
        } else if (code == KeyEvent.VK_RIGHT) {
            right();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public Rectangle getBounds() {

        return new Rectangle((int) x + 5, (int) y + 5, size - 10, size - 10);
    }

    public void setDots() {

        dotArray = new boolean[25][];

        dotArray[0] = new boolean[]{true, true, true, true, true, true, true, true, true, false, true, true, true, true, true, true, true, true, true};
        dotArray[1] = new boolean[]{true, false, false, false, true, false, false, false, true, false, true, false, false, false, true, false, false, false, true};
        dotArray[2] = new boolean[]{true, false, false, false, true, false, false, false, true, false, true, false, false, false, true, false, false, false, true};
        dotArray[3] = new boolean[]{true, false, false, false, true, false, false, false, true, false, true, false, false, false, true, false, false, false, true};
        dotArray[4] = new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
        dotArray[5] = new boolean[]{true, false, false, false, true, false, true, false, false, false, false, false, true, false, true, false, false, false, true};
        dotArray[6] = new boolean[]{true, false, false, false, true, false, true, false, false, false, false, false, true, false, true, false, false, false, true};
        dotArray[7] = new boolean[]{true, true, true, true, true, false, true, true, true, false, true, true, true, false, true, true, true, true, true};
        dotArray[8] = new boolean[]{false, false, false, false, true, false, false, false, true, false, true, false, false, false, true, false, false, false, false};
        dotArray[9] = new boolean[]{false, false, false, false, true, false, true, true, true, true, true, true, true, false, true, false, false, false, false};
        dotArray[10] = new boolean[]{false, false, false, false, true, false, true, false, false, false, false, false, true, false, true, false, false, false, false};
        dotArray[11] = new boolean[]{false, false, false, false, true, false, true, false, false, false, false, false, true, false, true, false, false, false, false};
        dotArray[12] = new boolean[]{true, true, true, true, true, true, true, false, false, false, false, false, true, true, true, true, true, true, true};
        dotArray[13] = new boolean[]{false, false, false, false, true, false, true, false, false, false, false, false, true, false, true, false, false, false, false};
        dotArray[14] = new boolean[]{false, false, false, false, true, false, true, true, true, true, true, true, true, false, true, false, false, false, false};
        dotArray[15] = new boolean[]{false, false, false, false, true, false, true, false, false, false, false, false, true, false, true, false, false, false, false};
        dotArray[16] = new boolean[]{false, false, false, false, true, false, true, false, false, false, false, false, true, false, true, false, false, false, false};
        dotArray[17] = new boolean[]{true, true, true, true, true, true, true, true, true, false, true, true, true, true, true, true, true, true, true};
        dotArray[18] = new boolean[]{true, false, false, false, true, false, false, false, true, false, true, false, false, false, true, false, false, false, true};
        dotArray[19] = new boolean[]{true, true, true, false, true, true, true, true, true, true, true, true, true, true, true, false, true, true, true};
        dotArray[20] = new boolean[]{false, false, true, false, true, false, true, false, false, false, false, false, true, false, true, false, true, false, false};
        dotArray[21] = new boolean[]{false, false, true, false, true, false, true, false, false, false, false, false, true, false, true, false, true, false, false};
        dotArray[22] = new boolean[]{true, true, true, true, true, false, true, true, true, false, true, true, true, false, true, true, true, true, true};
        dotArray[23] = new boolean[]{true, false, false, false, false, false, false, false, true, false, true, false, false, false, false, false, false, false, true};
        dotArray[24] = new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};

        startDotArray = dotArray;
    }

    void removeDot() {

        for (int x = 0; x < 19; x++) {
            for (int y = 0; y < 25; y++) {
                if (dot[y][x] != null && getBounds().intersects(dot[y][x].getBounds())) {
                    dotArray[y][x] = false;
                    score += 25;
                    dot[y][x] = null;
                }
            }
        }
    }

    public boolean dead() {

        if (ghost1 != null && getBounds().intersects(ghost1.getBounds())) {
            return true;
        }
        if (ghost2 != null && getBounds().intersects(ghost2.getBounds())) {
            return true;
        }
        if (ghost3 != null && getBounds().intersects(ghost3.getBounds())) {
            return true;
        }
        if (ghost4 != null && getBounds().intersects(ghost4.getBounds())) {
            return true;
        }
        return false;
    }

    public void reset() {

        velx = 0;
        vely = 0;
        x = 539;
        y = 360;
        start = System.currentTimeMillis();
        ghostAppear1 = false;
        ghostAppear2 = false;
        ghostAppear3 = false;
        ghostAppear4 = false;
        ghost1 = null;
        ghost2 = null;
        ghost3 = null;
        ghost4 = null;
        direction = "left";
        lives--;
    }

    public void gameOver() {

        gameOver = true;
        t.stop();
        
        Object options[] = new Object[]{"Exit", "New Game"};

        int choice = JOptionPane.showOptionDialog(null, " Your score was: " + score, "Game Over",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 1) {
            game.newGame();
        } 
        
        else {
            System.exit(0);
        }

    }
    
    public void youWin() {
        
        t.stop();
        
        Object options[] = new Object[]{"Exit", "New Game"};

        int choice = JOptionPane.showOptionDialog(null, " Your score was: " + score, "Congratulations! You Win!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 1) {
            game.newGame();
        } 
        
        else
            System.exit(0);    
    }
    
    public boolean win() {
        
        boolean win = true;
        
        for (int x = 0; x < 19; x++) {
            for (int y = 0; y < 25; y++) {
                if (dotArray[y][x]) {
                    win = false;
                }
            }
        }
        
        youWin = win;
        return win;
    }

}
