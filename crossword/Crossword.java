package crossword;

import java.io.FileNotFoundException;

/**
 *
 * @author Ryan
 */
public class Crossword {

    //The main method to demonstarte functionality
    
    public static void main(String[] args) throws FileNotFoundException {
        
        //WordLayout crossword = new WordLayout();
        new CrosswordGUI(20,20,null,"numbers");
    }

}
