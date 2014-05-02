/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pacman;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Timer;

/**
 *
 * @author Ryan
 */
public class Ghost implements ActionListener {
    
    double x = 270;
    double y = 270;
    double velx = 0;
    double vely = 0;
    Color color;
    Timer t = new Timer(9,this);
    Maze maze;
    GameBoard game;
    
    public Ghost(Maze maze, GameBoard game, Color color) {
        
        t.start();
        this.maze = maze;
        this.game = game;
        this.color = color;
        
        int direction = (int)(Math.random()*10);
        
        if (direction%2 == 0) velx = 1;
        else velx = -1;
    }
    
    public void paint(Graphics g) {
        
        Graphics2D g1 = (Graphics2D) g;
        
        g1.setColor(color);
        g1.fill(new RoundRectangle2D.Double(x,y,30,30,15,15));
        
        g1.setColor(Color.white);
        g1.fill(new Ellipse2D.Double(x+2, y+5, 10, 10));
        g1.fill(new Ellipse2D.Double(x+17, y+5, 10, 10));
        
        g1.setColor(Color.BLACK);
        g1.drawLine((int)x+5,(int)y+20,(int)x+25,(int)y+20);
        
        g1.drawOval((int)x+6, (int)y+9, 1, 1);
        g1.drawOval((int)x+21, (int)y+9, 1, 1);
        
        g1.drawOval((int)x+2, (int)y+5, 10, 10);
        g1.drawOval((int)x+17, (int)y+5, 10, 10);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        boolean change = false;
        
        if (y+vely+30 <= game.getHeight() && y+vely >= 0) {
            
            y += vely;
            
            if (maze.doesCollide(getBounds()))
                y -= vely;
        }
        
        if (x+velx+30 <= 570 && x+velx >= 0) {
            
            x += velx;
            
            if (maze.doesCollide(getBounds()))
                x -= velx;
        }
        
        if (x+velx+30 > 569 && y < game.getHeight()/2+30 && y > game.getHeight()/2-30) {
            x = 1;
            y = 360;
        }

        if (x+velx < 1.5 && y < game.getHeight()/2+30 && y > game.getHeight()/2-30) {
            x = 539;
            y = 360;
        }
        
        if (velx > 0 || velx < 0) {
                int direction = (int)(Math.random()*10);
            
                if (direction%3 == 0 && !maze.doesCollide(new Rectangle((int)x,(int)(y+1),30,30))) {
                    vely = 1;
                    velx = 0;
                    change = true;
                }
                else if (direction%3 == 1 && !maze.doesCollide(new Rectangle((int)x,(int)(y-1),30,30))) {
                    vely = -1;
                    velx = 0;
                    change = true;
                }
        }
        
        if ((vely > 0 || vely < 0) && !change) {
                int direction = (int)(Math.random()*10);
            
                if (direction%3 == 0 && !maze.doesCollide(new Rectangle((int)(x+1),(int)y,30,30))) {
                    velx = 1;
                    vely = 0;
                }
                else if (direction%3 == 1 && !maze.doesCollide(new Rectangle((int)(x-1),(int)y,30,30))) {
                    velx = -1;
                    vely = 0;
                }
        }
        
    }
    
    public Rectangle getBounds() {
        
        return new Rectangle((int)x,(int)y,30,30);
        
    }
}
