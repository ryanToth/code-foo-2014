package crossword;

/**
 *
 * @author Ryan
 */
public class Letter {
    
    String letter = "";
    boolean inUse = false; //If this letter cannot have a word attached to it, this will be true
    String wordNumber = ""; //If the letter is the first of a word, the corresponding crossword number will be held here
    
    public Letter(String letter) {
        this.letter = letter;
    }
    
    public String toString() {
        return letter;
    }
    
    public boolean equals(Letter o) {
        
        return letter.equals(o.letter);
    }
}
