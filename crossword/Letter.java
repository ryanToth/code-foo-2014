package crossword;

/**
 *
 * @author Ryan
 */
public class Letter {
    
    String letter = "";
    boolean inUse = false;
    
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
