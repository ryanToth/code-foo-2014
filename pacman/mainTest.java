/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pacman;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Ryan
 */
public class mainTest {
    
    public static void main (String[] args) {
        
        Object[] options = { "START"};
        
        JOptionPane.showOptionDialog(null, "Welcome To Pac-Man", "New Game",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
            null, options, options[0]);
        
        JFrame frame = new JFrame("Pac-Man");
        GameBoard game = new GameBoard();
        frame.add(game, BorderLayout.CENTER);
        frame.setSize(770,780);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
    }
    
}
