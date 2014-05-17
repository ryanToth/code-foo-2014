/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package shooter;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Ryan
 */
public class Shooter {

    public static void main(String[] args) {

        Object[] options = {"START"};

        if (JOptionPane.showOptionDialog(null, "Goku VS The Karp", "New Game",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]) == 0) {

            JFrame frame = new JFrame("Goku VS The Karp");
            GameBoard game = new GameBoard();
            frame.setSize(1000, 500);
            frame.add(game, BorderLayout.CENTER);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
        }

    }

}
