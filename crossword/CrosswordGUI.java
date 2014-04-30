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
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
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
    JButton checkAnswers = new JButton("Check Answers");
    JTextField clues;
    final WordLayout crossword;
    CluesGUI cluesWindow;
    String displayWords = "words";
    WordLayout usedCrossword;
    final int length;
    final int width;
    boolean checkAnswersChosen = false;
    String[][] answers;

    public CrosswordGUI(int x, int y, final WordLayout passedCrossword, String newDisplayWord, boolean checkTheInput, final String[][] answers) throws FileNotFoundException, BadLocationException {

        length = x;
        width = y;
        displayWords = newDisplayWord;
        checkAnswersChosen = checkTheInput;
        this.answers = answers;
        
        GridLayout layout = new GridLayout(x, y);
        panel.setPreferredSize(new Dimension(x * 30, y * 30));

        grid = new JTextArea[y][x];
        panel.setLayout(layout);

        crossword = passedCrossword;
        
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

                    new CrosswordGUI(length, width,null,displayWords,false,null);

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CrosswordGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BadLocationException ex) {
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
                    new CrosswordGUI(length, width, usedCrossword, displayWords,false,null);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CrosswordGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BadLocationException ex) {
                    Logger.getLogger(CrosswordGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        checkAnswers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent f) {
                try {
                    
                    String[][] tempAnswers;
                    
                    if (answers != null) {
                        tempAnswers = answers;
                    }
                    
                    else tempAnswers = new String[width][length];
                    
                    for (int i = 0; i < length; i++) {
                        for (int j = 0; j < width; j++) {
                            
                            if (grid[j][i].getDocument() != null) {
                                tempAnswers[j][i] = grid[j][i].getText();
                            }
                        }
                    }
                    
                    cluesWindow.delete();
                    frame.dispose();

                    new CrosswordGUI(length, width,usedCrossword,displayWords,true,tempAnswers);

                } catch (Exception ex) {
                    Logger.getLogger(CrosswordGUI.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        
        String solutionOnOff;
        
        if (displayWords.equals("words")) {
            solutionOnOff = "/Solution On/";
            checkAnswers.setEnabled(false);
        }
        
        else solutionOnOff = "/Solution Off/";
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(3, 1));
        bottomPanel.add(newCrossword, BorderLayout.CENTER);
        bottomPanel.add(displayWordsOrNumbers, BorderLayout.WEST);
        bottomPanel.add(checkAnswers, BorderLayout.EAST);
        bottomPanel.add(new JLabel(solutionOnOff), BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setCrossword(int x, int y, WordLayout crossword) throws FileNotFoundException, BadLocationException {
        
        if (crossword == null) 
            crossword = new WordLayout();
        
        usedCrossword = crossword;

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {

                grid[j][i] = new JTextArea();
                grid[j][i].setDocument(new JCrosswordBox());
                
                if (crossword.getCrossword()[i][j] != null) {

                    if (displayWords.equals("words")) {
                        grid[j][i].setText(" " + crossword.getCrossword()[i][j].toString());
                        grid[j][i].setEditable(false);
                    } 
                    
                    else {

                        if (checkAnswersChosen && answers != null && 
                                crossword.getCrossword()[i][j].letter.equals(answers[j][i].toUpperCase())) {
                            grid[j][i].setOpaque(true);
                            grid[j][i].setBackground(Color.GREEN);
                            grid[j][i].setText(answers[j][i].toUpperCase());
                        }
                        
                        else if (answers != null && !crossword.getCrossword()[i][j].wordNumber.equals(answers[j][i].toUpperCase())) {
                           
                            grid[j][i].setOpaque(true);
                            grid[j][i].setBackground(Color.RED);
                            grid[j][i].setText(answers[j][i].toUpperCase());
                        }
                            
                        else grid[j][i].setText(crossword.getCrossword()[i][j].wordNumber);
                        
                    }
                } 
                
                else {
                    grid[j][i].setOpaque(true);
                    grid[j][i].setBackground(Color.BLACK);
                }
                
                grid[j][i].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
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

    class JCrosswordBox extends PlainDocument {

        public JCrosswordBox() {
            super();
        }
        
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            
            if (str == null)
                return;
            
            if ((getLength() + str.length() <= 2)) {
                super.insertString(offset, str, attr);
            }
        }
        
        public String toString() {
            try {
                return getText(0,1);
            } catch (BadLocationException ex) {
                Logger.getLogger(CrosswordGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "error";
        }
        
    }
    
}
