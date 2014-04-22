package crossword;

import java.util.LinkedList;

/**
 *
 * @author Ryan
 */
public class Word implements Comparable {
    
    String orientation = null; //either "vertical" or "horizontal" to show how the word is oriented in the crossword
    String word = null;
    String clue = null;
    LinkedList<Letter> letters = new LinkedList<>(); //A list of the letters that are in this word in the correct order
    int[] startIndex = new int[2]; //The coordinates in the crossword array where the word begins, where the first letter exists
    int[] endIndex = new int[2]; //The coordinates in the crossword arraw where the word ends, where tha last letter exists
    
    public Word(String word, String clue) {
        
        this.word = word;
        this.clue = clue;
        
        //adds the words letters to the list and makes them upper case
        for(int i = 1; i < word.length() + 1; i++) {
            letters.add(new Letter(word.split("")[i].toUpperCase()));
        }
    }
 
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    
    public void  setStartIndex(int x, int y) {
        startIndex[0] = x;
        startIndex[1] = y;
    }
    
    public void  setEndIndex(int x, int y) {
        endIndex[0] = x;
        endIndex[1] = y;
    }
    
    public int length() {
        return word.length();
    }
    
    public String toString() {
        return word;
    }
    
    /* This function decides whether a given word, o, can be placed into the corssword array based on this word
    *  and the words that already are placed into the crossword
    */
    
    public int[] canAddToCrossword(Word o, Letter[][] crossword, WordLayout info) {
        
        int[] position = null;
        
        for (int i = 0; i < word.length(); i++) {
            for (int j = 0; j < o.length(); j++) {
                //This checks if the two words being compared have a common letter
                if (letters.get(i).equals(o.letters.get(j)) && !letters.get(i).inUse) { 

                    int[] coordinates = new int[2];
                    
                    //Here the word that "o" is being compared against is horizontal in the crossword
                    // and when the word is placed in this location attached to this specific letter it will fit onto
                    //the crossword array
                    if (orientation.equals("horizontal") && startIndex[1] - j >= 0 && startIndex[1] + o.length()-j < 20) {

                        coordinates[0] = startIndex[0] + i;
                        coordinates[1] = startIndex[1] - j;
                        
                        //Turns false if a condition where a condition is not met that checks if a word can be
                        // placed in this specific spot in the crossword
                        boolean stillValid = true;
                        
                        int k = 0;

                        for (int p = coordinates[1]; k < o.length(); p++) {
                            
                            //Checks if the new word will overlap with an existing word in the crossword and stillValid will 
                            //stay false unless the new word's letter matches the existing word's letter
                            if (crossword[p][coordinates[0]] != null && !crossword[p][coordinates[0]].equals(o.letters.get(k))) {
                                stillValid = false;
                            }
                                //Check to the left, making sure there must be one space between words
                                if (coordinates[0] != 0 && crossword[p][coordinates[0]] == null) {    
                                    if (crossword[p][coordinates[0]-1] != null)
                                        stillValid = false;
                                }
                                
                                //Checks to the right of the word, making sure there must be one space between words
                                if (coordinates[0] != 19 && crossword[p][coordinates[0]] == null) {
                                    if (crossword[p][coordinates[0]+1] != null)
                                        stillValid = false;
                                }
                            
                            //Check if there is any letters above only for first letter of a vertical word, 
                                //making sure there must be one space between words
                            if (p != 0 && k == 0) {
                                if (crossword[p-1][coordinates[0]] != null)
                                    stillValid = false;
                            }
                            
                            //Check underneath the last letter, making sure there must be one space between words
                            if (p != 19 && k == o.length()-1) {
                                if (crossword[p+1][coordinates[0]] != null)
                                    stillValid = false;
                            }
                            
                            k++;
                        }
                        
                        if (stillValid) {
                            //Sets "o"'s orientation to vertical because it is being chained onto a horizontal word
                            o.setOrientation("vertical");
                        
                            letters.get(i).inUse = true;
                        
                            if (i != word.length()-1)
                                letters.get(i+1).inUse = true;
                            if(i != 0)
                                letters.get(i-1).inUse = true;
                        
                            o.letters.get(j).inUse = true;
                        
                            if (j != 0)
                                o.letters.get(j-1).inUse = true;
                            if (j != o.length()-1)
                                o.letters.get(j+1).inUse = true;                          
                            
                            return coordinates;
                        }
                        //Returns null if the word cannot be placed onto any letter in this specific word
                        //that is being checked
                        else coordinates = null;
                    }
                    //Everything that is checked for the horizontal existing word happens hear except for an existing vertical word
                    else if (orientation.equals("vertical") && startIndex[0] - j >= 0 && startIndex[0] + o.length()-j < 20) {
                        
                        coordinates[0] = startIndex[0] - j;
                        coordinates[1] = startIndex[1] + i;
                        
                        boolean stillValid = true;
                        int k = 0;
            
                        for (int p = coordinates[0]; k < o.length(); p++) {
                            
                            if (crossword[coordinates[1]][p] != null && !crossword[coordinates[1]][p].equals(o.letters.get(k))) {
                                stillValid = false;
                            }
                                //Check above the letter
                                if (coordinates[1] != 0 && crossword[coordinates[1]][p] == null) {    
                                    if (crossword[coordinates[1]-1][p] != null)
                                        stillValid = false;
                                }
                                
                                //Checks below the letter
                                if (coordinates[1] != 19 && crossword[coordinates[1]][p] == null) {
                                    if (crossword[coordinates[1]+1][p] != null)
                                        stillValid = false;
                                }
                            
                            
                            //Check to the left of the letter
                            if (p != 0 && k == 0) {
                                if (crossword[coordinates[1]][p-1] != null)
                                    stillValid = false;
                            }
                            
                            //Checks to the right of the letter
                            if (p != 19 && k == o.length()-1) {
                                if (crossword[coordinates[1]][p+1] != null)
                                    stillValid = false;
                            }
                            
                            k++;
                        }
                        
                        if (stillValid) {
                            
                            o.setOrientation("horizontal");
                        
                            letters.get(i).inUse = true;
                        
                            if (i != word.length()-1)
                                letters.get(i+1).inUse = true;
                            if(i != 0)
                                letters.get(i-1).inUse = true;
                        
                            o.letters.get(j).inUse = true;
                        
                            if (j != 0)
                                o.letters.get(j-1).inUse = true;
                            if (j != o.length()-1)
                                o.letters.get(j+1).inUse = true;
                            
                            return coordinates;
                        }
                        
                        else coordinates = null;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int compareTo(Object o) {
        
        Word other = (Word)o;
        
        if (Integer.parseInt(letters.get(0).wordNumber) < Integer.parseInt(other.letters.get(0).wordNumber))
            return -1;
        else if (letters.get(0).wordNumber.equals(other.letters.get(0).wordNumber))
            return 0;
        else return 1;
        
    }
}
