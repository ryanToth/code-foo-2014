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

/**
 *
 * @author Ryan
 */
public class Dots {
    
    int x, y;
    
    public Dots(int x, int y) {
        
        this.x = x;
        this.y = y;
    }
    
    public void paint(Graphics g) {
        
        Graphics2D g1 = (Graphics2D) g;
        g1.setColor(Color.getColor("light", Color.yellow));
        
        g1.drawOval(x, y, 5, 5);
    }
    
    public Rectangle getBounds() {
        
        return new Rectangle((int)x,(int)y, 5, 5);
    }
}
