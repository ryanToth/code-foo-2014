package crossword;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author Ryan
 */
public class WordLayout {
    
    //Modify this String to get words from a different file
    String fileToGetWordsFrom = "crossword.txt";
    int x = 20;
    int y = 20;
    Letter[][] crossword = new Letter[y][x];
    LinkedList<Word> words = new LinkedList<>();
    
    public WordLayout() throws FileNotFoundException {
        
        File file = new File(fileToGetWordsFrom);
        Scanner scn = new Scanner(file);
        
        while(scn.hasNext()) 
            words.add(new Word(scn.next()));
        
        buildLayout();
        printCrossword();
    }
    
    public Word grabWord() {
        
        return words.remove((int)(Math.random()*(words.size() - 1)));
        
    }
    
    public void buildLayout() {
        
        LinkedList<Word> usedWords = new LinkedList<>();
        
        Word startWord = grabWord();
        
        while(startWord.length() > x) {
            
            startWord = grabWord();
        }
        
        startWord.setStartIndex(0,0);
        startWord.setOrientation("horizontal");
        
        for (int i = 0; i < startWord.length(); i++) {
            crossword[0][i] = startWord.letters.get(i);
            startWord.setEndIndex(i, 0);
        }

        usedWords.add(startWord);
        
        boolean keepGoing = true;
        int[] position = null;
        int times = 0;
        
        while (times < words.size()) {
            
            startWord = grabWord();
            
            for(int i = 0; i < usedWords.size(); i++)
                position = usedWords.get(i).canAddToCrossword(startWord, crossword);
            
            if (position == null) {
                words.add(startWord);
                times++;
            }
            
            else keepGoing = false;
        
            if (!keepGoing) {
            
                if (startWord.orientation.equals("vertical")) {
            
                    startWord.setStartIndex(position[0], position[1]);
                    int j = 0;

                    for (int i = position[1]; j < startWord.length(); i++) {
                        crossword[i][position[0]] = startWord.letters.get(j);
                        startWord.setEndIndex(position[0], i);
                        j++;
                    }
                }
        
                else {
            
                    startWord.setStartIndex(position[0], position[1]);
                    int j = 0;
            
                    for (int i = position[0]; j < startWord.length(); i++) {
                        crossword[position[1]][i] = startWord.letters.get(j);
                        startWord.setEndIndex(i, position[1]);
                        j++;
                    }
                }

                keepGoing = true;
                usedWords.add(startWord);
                times = 0;
            }
        }
    }
    
    public void printCrossword() {
        
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                
                if (crossword[i][j] == null) 
                    System.out.print("- ");
                else 
                    System.out.print(crossword[i][j] + " ");
                
                if (j == y-1) System.out.println("");
            }
        }
    }
}
