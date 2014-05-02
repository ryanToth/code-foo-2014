package pacman;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;

/**
 *
 * @author Ryan
 */
public class Maze extends JComponent {
    
    private GameBoard game;
    public int numBorders = 42;
    public Rectangle[] borders = new Rectangle[numBorders];
   
    public Maze(GameBoard game) {

        this.game = game;

        setBorders();
    }

    public void paint(Graphics g) {

        super.paint(g);
        
        Graphics2D g1 = (Graphics2D) g;
        g1.setColor(Color.BLUE);
        
        setBorders();
        
        //The Border
        g1.drawRect(0,00,569,757);
        
        //Top Half
        g1.drawRect(30,30,90,90);
        g1.drawRect(150,30,90,90);
        g1.drawRect(270, 0, 30, 120);
        g1.drawRect(330,30,90,90);
        g1.drawRect(450,30,90,90);
        
        g1.drawRect(30,150,90,60);
        g1.drawRect(150,150,30,210);
        g1.drawRect(210,150,150,60);
        g1.drawRect(390,150,30,210);
        g1.drawRect(450,150,90,60);
        
        g1.drawRect(0,240,120,120);
        g1.drawRect(180,240,60,30);
        g1.drawRect(270,210,30,60);
        g1.drawRect(330,240,60,30);
        g1.drawRect(450,240,120,120);
        
        //Middle
        g1.drawRect(210,300,150,120);
        g1.drawRect(240,330,90,60);
        
        //Bottom Half
        g1.drawRect(0,390,120,120);
        g1.drawRect(150,390,30,120);
        g1.drawRect(210,450,150,60);
        g1.drawRect(390,390,30,120);
        g1.drawRect(450,390,120,120);
        
        g1.drawRect(30,540,90,30);
        g1.drawRect(150,540,90,30);
        g1.drawRect(270,510,30,60);
        g1.drawRect(330,540,90,30);
        g1.drawRect(450,540,90,30);
        
        g1.drawRect(90,570,30,90);
        g1.drawRect(450,570,30,90);
        
        g1.drawRect(0,600,60,60);
        g1.drawRect(150,600,30,90);
        g1.drawRect(210,600,150,60);
        g1.drawRect(390,600,30,90);
        g1.drawRect(510,600,60,60);
        
        g1.drawRect(30,690,210,30);
        g1.drawRect(270,660,30,60);
        g1.drawRect(330,690,210,30);
        
        
        //Lines where boxes connect
        g1.setColor(Color.BLACK);
        g1.drawLine(180, 240, 180, 270);
        g1.drawLine(270, 210, 300, 210);
        g1.drawLine(390, 240, 390, 270);
        g1.drawLine(270, 510, 300, 510);
        g1.drawLine(90, 570, 119, 570);
        g1.drawLine(451, 570, 479, 570);
        g1.drawLine(270,660,300,660);
        g1.drawLine(150,690,180,690);
        g1.drawLine(390,690,420,690);
    }
    
    public boolean doesCollide(Rectangle rect) {
        
        for (int i = 0; i < numBorders; i++) {
            if (rect.getBounds().intersects(borders[i]))
                return true;
        }

        return false;
    }
    
    private void setBorders() {
        
        borders[0] = new Rectangle(30, 30, 90, 90);
        borders[1] = new Rectangle(150, 30, 90, 90);
        borders[2] = new Rectangle(270, 0, 30, 120);
        borders[3] = new Rectangle(330, 30, 90, 90);
        borders[4] = new Rectangle(450, 30, 90, 90);
        borders[5] = new Rectangle(30, 150, 90, 60);
        borders[6] = new Rectangle(150, 150, 30, 210);
        borders[7] = new Rectangle(210, 150, 150, 60);
        borders[8] = new Rectangle(390, 150, 30, 210);
        borders[9] = new Rectangle(450, 150, 90, 60);
        borders[10] = new Rectangle(0, 240, 120, 120);
        borders[11] = new Rectangle(180, 240, 60, 30);
        borders[12] = new Rectangle(270, 210, 30, 60);
        borders[13] = new Rectangle(330, 240, 60, 30);
        borders[14] = new Rectangle(450, 240, 120, 120);
        borders[15] = new Rectangle(0,390,120,120);
        borders[16] = new Rectangle(150,390,30,120);
        borders[17] = new Rectangle(270, 0, 30, 120);
        borders[18] = new Rectangle(210,450,150,60);
        borders[19] = new Rectangle(390,390,30,120);
        borders[20] = new Rectangle(450,390,120,120);
        borders[21] = new Rectangle(30,540,90,30);
        borders[22] = new Rectangle(150,540,90,30);
        borders[23] = new Rectangle(270,510,30,60);
        borders[24] = new Rectangle(330,540,90,30);
        borders[25] = new Rectangle(450,540,90,30);
        borders[26] = new Rectangle(90,570,30,90);
        borders[27] = new Rectangle(450,570,30,90);
        borders[28] = new Rectangle(0,600,60,60);
        borders[29] = new Rectangle(150,600,30,90);
        borders[30] = new Rectangle(210,600,150,60);
        borders[31] = new Rectangle(390,600,30,90);
        borders[32] = new Rectangle(510,600,60,60);
        borders[33] = new Rectangle(30,690,210,30);
        borders[34] = new Rectangle(270,660,30,60);
        borders[35] = new Rectangle(330,690,210,30);
        borders[36] = new Rectangle(210, 300, 150, 120);
        borders[37] = new Rectangle(240, 330, 90, 60);
        borders[38] = new Rectangle(0,-1,570,1);
        borders[39] = new Rectangle(0,750,570,1);
        borders[40] = new Rectangle(-1,0,1,game.getHeight());
        borders[41] = new Rectangle(570,0,1,game.getHeight());
    }
    
}
