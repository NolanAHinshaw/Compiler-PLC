import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    
    /* 
    main function takes in a user inputted string which is then convertd into a file that can be run 
    through lexical analyzer. it takes each character in the input and adds it into a base string. 
    from there it calls the lex class which can identify tokens and return them.
    */
    public static void main(String[] args) throws FileNotFoundException {

        System.out.println("Enter the file path you want to run through lexical analyzer: ");
        Scanner input = new Scanner(System.in);
        String user_input = input.nextLine();
        File file = new File(user_input);
        String source = " ";
        while (input.hasNext()) {
            source += input.nextLine() + "\n";
        }
        Lex l = new Lex(file);
        l.printTokens();
        input.close();
    }
}
