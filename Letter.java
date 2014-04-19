package crossword;

import java.util.LinkedList;

/**
 *
 * @author Ryan
 */
public class Letter {
    
    String letter = "";
    boolean inUse = false;
    String orientation = null;
    
    public Letter(String letter) {
        this.letter = letter;
    }
    
    public void setOrientation(String set) {
        orientation = set;
    }
    
    public String toString() {
        return letter;
    }
    
    public boolean equals(Letter o) {
        
        return letter.equals(o.letter);
    }
}
