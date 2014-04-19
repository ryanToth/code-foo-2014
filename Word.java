package crossword;

import java.util.LinkedList;

/**
 *
 * @author Ryan
 */
public class Word {
    
    String orientation = null;
    String word = null;
    LinkedList<Letter> letters = new LinkedList<>();
    int[] startIndex = new int[2];
    int[] endIndex = new int[2];
    
    
    public Word(String word) {
        
        this.word = word;
        
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
    
    public int[] canAddToCrossword(Word o, Letter[][] crossword) {
        
        int[] position = null;
        
        for (int i = 0; i < word.length(); i++) {
            for (int j = 0; j < o.length(); j++) {
                if (letters.get(i).equals(o.letters.get(j)) && !letters.get(i).inUse) {

                    int[] coordinates = new int[2];
                    
                    if (orientation.equals("horizontal") && startIndex[1] - j >= 0 && startIndex[1] + o.length()-j < 20) {

                        coordinates[0] = startIndex[0] + i;
                        coordinates[1] = startIndex[1] - j;
                        
                        boolean stillValid = true;
                        
                        int k = 0;

                        for (int p = coordinates[1]; k < o.length(); p++) {
                            
                            if (crossword[p][coordinates[0]] != null && !crossword[p][coordinates[0]].equals(o.letters.get(k))) {
                                stillValid = false;
                            }
                                //Check to the left
                                if (coordinates[0] != 0 && crossword[p][coordinates[0]] == null) {    
                                    if (crossword[p][coordinates[0]-1] != null)
                                        stillValid = false;
                                }
                                
                                //Checks to the right of the word
                                if (coordinates[0] != 19 && crossword[p][coordinates[0]] == null) {
                                    if (crossword[p][coordinates[0]+1] != null)
                                        stillValid = false;
                                }
                            
                            
                            //Check if there is any letters above only for first letter of a vertical word
                            if (p != 0 && k == 0) {
                                if (crossword[p-1][coordinates[0]] != null)
                                    stillValid = false;
                            }
                            
                            //Check underneath the last letter
                            if (p != 19 && k == o.length()-1) {
                                if (crossword[p+1][coordinates[0]] != null)
                                    stillValid = false;
                            }
                            
                            k++;
                        }
                        
                        if (stillValid) {
                            
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
                        }
                        
                        else coordinates = null;
                        
                        return coordinates;
                    }
                    
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
                        }
                        
                        else coordinates = null;
                        
                        return coordinates;
                    }
                }
            }
        }
        
        return null;
    }
    
}
