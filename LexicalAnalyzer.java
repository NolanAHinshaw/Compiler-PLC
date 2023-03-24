import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum TokenType {
    ADDITION, SUBTRACTION, 
    MULTIPLICATION, DIVISION, MODULO, 
    PAREN_OPEN, PAREN_CLOSE, 
    ASSIGNMENT, EQUALS, 
    LESS_THAN, LESS_THAN_EQUAL, GREATER_THAN, GREATER_THAN_EQUAL, 
    AND, OR, 
    INTEGER_LITERAL, FLOATING_POINT_LITERAL, VARIABLE_NAME,
    ERROR,
}

class Token {
    TokenType type;
    String lexeme;

    Token(TokenType type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    public String toString() {
        return "<" + type + ", " + lexeme + ">";
    }
}

public class LexicalAnalyzer {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("testfile.txt"));
        String line = reader.readLine();
        ArrayList<Token> tokens = new ArrayList<Token>();

        while (line != null) {
            String[] words = line.split("\\s+");
            for (String word : words) {
                TokenType type = getTokenType(word);
                tokens.add(new Token(type, word));
            }
            line = reader.readLine();
        }

        for (Token token : tokens) {
            System.out.println(token);
        }
        reader.close();
    }

    public static TokenType getTokenType(String word) {
        TokenType type = null;

        switch (word) {
            case "+":
                type = TokenType.ADDITION;
                break;
            case "-":
                type = TokenType.SUBTRACTION;
                break;
            case "*":
                type = TokenType.MULTIPLICATION;
                break;
            case "/":
                type = TokenType.DIVISION;
                break;
            case "%":
                type = TokenType.MODULO;
                break;
            case "(":
                type = TokenType.PAREN_OPEN;
                break;
            case ")":
                type = TokenType.PAREN_CLOSE;
                break;
            case "=":
                type = TokenType.ASSIGNMENT;
                break;
            case "==":
                type = TokenType.EQUALS;
                break;
            case "<":
                type = TokenType.LESS_THAN;
                break;
            case "<=":
                type = TokenType.LESS_THAN_EQUAL;
                break;
            case ">":
                type = TokenType.GREATER_THAN;
                break;
            case ">=":
                type = TokenType.GREATER_THAN_EQUAL;
                break;
            case "&&":
                type = TokenType.AND;
                break;
            case "||":
                type = TokenType.OR;
                break;
            default:
                if (isInteger(word)) {
                    type = TokenType.INTEGER_LITERAL;
                } else if (isFloatingPoint(word)) {
                    type = TokenType.FLOATING_POINT_LITERAL;
                } else if (isVariableName(word)) {
                    type = TokenType.VARIABLE_NAME;
                } else {
                    type = TokenType.ERROR;
                }
                break;
        }

        return type;
    }

    private static boolean isInteger(String s) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    private static boolean isFloatingPoint(String s) {
        Pattern pattern = Pattern.compile("[0-9]*.[0-9]+[f|d]?");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    private static boolean isVariableName(String s) {
        Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
