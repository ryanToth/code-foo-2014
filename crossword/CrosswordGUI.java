package crossword;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Ryan
 */
public class CrosswordGUI extends JFrame {

    JPanel panel = new JPanel();
    JFrame frame = new JFrame();
    JTextArea[][] grid;
    JButton newCrossword = new JButton("Generate New Crossword");
    JButton displayWordsOrNumbers = new JButton("Turn Solution On/Off");
    JTextField clues;
    WordLayout crossword;
    CluesGUI cluesWindow;
    String displayWords = "words";
    WordLayout usedCrossword;

    public CrosswordGUI(int x, int y, WordLayout crossword, String newDisplayWord) throws FileNotFoundException {

        final int length = x;
        final int width = y;
        displayWords = newDisplayWord;
        
        GridLayout layout = new GridLayout(x, y);
        panel.setPreferredSize(new Dimension(x * 30, y * 30));

        grid = new JTextArea[y][x];
        panel.setLayout(layout);

        setCrossword(x, y, crossword);

        frame.setTitle("Crossword Generator");

        frame.setSize(500, 650);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(panel);
        frame.pack();

        cluesWindow = new CluesGUI(usedCrossword);

        newCrossword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent f) {
                try {

                    cluesWindow.delete();
                    frame.dispose();

                    new CrosswordGUI(length, width,null,displayWords);

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CrosswordGUI.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        displayWordsOrNumbers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent f) {

                if (displayWords.equals("words")) {
                    displayWords = "numbers";
                } else {
                    displayWords = "words";
                }
                
                cluesWindow.delete();
                frame.dispose();
                
                try {
                    new CrosswordGUI(length, width, usedCrossword, displayWords);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CrosswordGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });

        
        String solutionOnOff;
        
        if (displayWords.equals("words")) solutionOnOff = "/Solution On/";
        
        else solutionOnOff = "/Solution Off/";
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(3, 1));
        bottomPanel.add(newCrossword, BorderLayout.CENTER);
        bottomPanel.add(displayWordsOrNumbers, BorderLayout.SOUTH);
        bottomPanel.add(new JLabel(solutionOnOff), BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setCrossword(int x, int y, WordLayout crossword) throws FileNotFoundException {
        
        if (crossword == null) 
            crossword = new WordLayout();
        
        usedCrossword = crossword;
        
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                
                if (crossword.getCrossword()[i][j] != null) {
                    
                    if (displayWords.equals("words")) {
                        grid[j][i] = new JTextArea("  " + crossword.getCrossword()[i][j].toString() + " ");
                        grid[j][i].setEditable(false);
                    }
                    else
                        grid[j][i] = new JTextArea("  " + crossword.getCrossword()[i][j].wordNumber + " ");
                }
                
                else {
                    grid[j][i] = new JTextArea(" ");
                    grid[j][i].setOpaque(true);
                    grid[j][i].setBackground(Color.BLACK);
                }
                grid[j][i].setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));
                panel.add(grid[j][i]);
            }
        }
        
    }
    
    public class CluesGUI extends JFrame {
    
        JFrame frame = new JFrame();
        JTextArea textArea;
        
        public CluesGUI(WordLayout crossword) {
            
            textArea = new JTextArea("");
            textArea.setText(crossword.getUsedWords());
            textArea.setEditable(false);
            JScrollPane scroller = new JScrollPane(textArea);
            frame.add(scroller);
            //frame.setSize(160, 550);
            frame.pack();
            frame.setLocation(1080,130);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Word Clues");
        }
        
        public void delete() {
            frame.dispose();
        }
        
    }

}
