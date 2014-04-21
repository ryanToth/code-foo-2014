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
    String fileToGetWordsFrom = "meaningful-words.txt";
    
    //The dimensions of the crossword
    int x = 20;
    int y = 20;
    //The crossword itsef
    Letter[][] crossword = new Letter[y][x];
    //The list of words that can be used in the crossword
    LinkedList<Word> words = new LinkedList<>();
    //Words that have been used in th crossword
    LinkedList<Word> usedWords = new LinkedList<>();
    //Words that don't fit in the crossword. It is reset everytime a new word is put into the crossword
    LinkedList<Word> wordsThatDoNotFit = new LinkedList<>();
    //The number associated with the word being put into the crossword
    int wordNumber = 1;
    
    public WordLayout() throws FileNotFoundException {
        
        //The list of words available to use in the crossword are gotten here
        try {
        File file = new File(fileToGetWordsFrom);

        Scanner scn = new Scanner(file);
        
        while(scn.hasNext()) 
            words.add(new Word(scn.next()));
        //The structure of the crossword is built here
        buildLayout();
        }
        
        catch (Exception e) {
            System.out.println(e + ": System shut down");
            System.exit(0);
        }
    }
    //Acts like a bag and grabs a random word from the word list read from file
    public Word grabWord() {
        
        return words.remove((int)(Math.random()*(words.size() - 1)));      
    }
    
    public void buildLayout() {
        
        //The first word is gotten 
        Word startWord = grabWord();
        //Makes sure the word can fit given the width of the puzzle
        while(startWord.length() > x) {
            
            startWord = grabWord();
        }
        
        startWord.setStartIndex(0,0);
        startWord.setOrientation("horizontal");
        //The word is put into the crossword array
        for (int i = 0; i < startWord.length(); i++) {
            crossword[0][i] = startWord.letters.get(i);
            startWord.setEndIndex(i, 0);
        }
        //The number associatd with the first word is set here
        startWord.letters.get(0).wordNumber = String.valueOf(wordNumber);
        wordNumber++;
        //The word is added to the used words list where the crossword's words are kept
        usedWords.add(startWord);
        
        //keepGoing will be set to false whenever a word that can be put into the crossword is found
        boolean keepGoing = true;
        int[] position = null;

        //While there are still words to try and put it into the crossword
        while (!words.isEmpty()) {
            
            startWord = grabWord();
            
            for(int i = 0; i < usedWords.size(); i++) {
                position = usedWords.get(i).canAddToCrossword(startWord, crossword, this);
                if (position != null) i = usedWords.size();
            }
            
            if (position == null) {
                //If the word won't fit into the crossword, remove it from potential words list and put it into a different list
                wordsThatDoNotFit.add(startWord);
            }
            //If a word is found that can be put into the crossword, it is added here
            else keepGoing = false;
        
            if (!keepGoing) {
                //If a "down" word is being put into the crossword
                if (startWord.orientation.equals("vertical")) {
            
                    startWord.setStartIndex(position[0], position[1]);
                    int j = 0;
                    //Gets the number associated with the word being put into the crossword
                    //If two words share a first letter, the first if statement happens
                    if (crossword[position[1]][position[0]] != null && !crossword[position[1]][position[0]].wordNumber.equals(""))
                        startWord.letters.get(0).wordNumber = crossword[position[1]][position[0]].wordNumber;
                    //If two words do not share the same a first letter, a new number is associated with the new word
                    else {
                        startWord.letters.get(0).wordNumber = String.valueOf(wordNumber);
                        wordNumber++;
                    }
                    //Here the new word is placed into the crossword array based on the starting psition getten in
                    //the canAddToCrossword function and the word's length
                    for (int i = position[1]; j < startWord.length(); i++) {
                        
                        if (crossword[i][position[0]] == null || (crossword[i][position[0]] != null && crossword[i][position[0]].wordNumber.equals("")))
                            crossword[i][position[0]] = startWord.letters.get(j);
                        startWord.setEndIndex(position[0], i);
                        j++;
                    }
                }
                //If an "up" word is being added to the crossword. Works in the same was as the horizontal word
                //placing happens, the for loop conditions just need to be diffdrent because of the orientation of
                //the new word
                else {
            
                    startWord.setStartIndex(position[0], position[1]);
                    int j = 0;
            
                    if (crossword[position[1]][position[0]] != null && !crossword[position[1]][position[0]].wordNumber.equals(""))
                        startWord.letters.get(0).wordNumber = crossword[position[1]][position[0]].wordNumber;
                    
                    else {
                        startWord.letters.get(0).wordNumber = String.valueOf(wordNumber);
                        wordNumber++;
                    }
                    
                    for (int i = position[0]; j < startWord.length(); i++) {
                        
                        
                        if (crossword[position[1]][i] == null || (crossword[position[1]][i] != null && crossword[position[1]][i].wordNumber.equals("")))
                            crossword[position[1]][i] = startWord.letters.get(j);
                        startWord.setEndIndex(i, position[1]);
                        j++;
                    }
                }
                //All words from the wordsThatDoNotFit list are added back into the potential word list
                //to maximize the amount of words in the crossword 
                for (int k = 0; k < wordsThatDoNotFit.size(); k++)
                    words.add(wordsThatDoNotFit.remove(k));
                
                //keepGoing is set back to true until a new word is found that fits and
                //the new word that was put into the puzzle is added to the used words list
                keepGoing = true;
                usedWords.add(startWord);
            }
        }
    }
    
    public String toString() {
        
        String output = "";
        
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                
                if (crossword[i][j] == null) 
                    output += "- ";
                else 
                    output += crossword[i][j] + " ";
                
                if (j == y-1) output += "\n";
            }
        }

        return output;
    }
    
    public Letter[][] getCrossword() {
        return crossword;
    }
    //Returns the numbers associated with each word used in the crossword and thier orientation
    public String getUsedWords() {
        
        String acrossWords = "Across\n\n";
        String downWords = "Down\n\n";
        
        for (int i = 0; i < usedWords.size(); i++) {
            
            if (usedWords.get(i).orientation == "horizontal")
                acrossWords += usedWords.get(i).letters.get(0).wordNumber + ". " 
                        + usedWords.get(i) + "\n";
            
            else
                downWords += usedWords.get(i).letters.get(0).wordNumber + ". " 
                        + usedWords.get(i) + "\n";
        }
        return acrossWords + "\n\n" + downWords;
    }
}
