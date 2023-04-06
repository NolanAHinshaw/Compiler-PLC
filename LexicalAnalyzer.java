import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum TokenType {
    ADDITION, SUBTRACTION, 
    MULTIPLICATION, DIVISION, MODULO, 
    PAREN_OPEN, PAREN_CLOSE, CURLY_OPEN, CURLY_CLOSE,
    ASSIGNMENT, EQUALS, NOT_EQUAL, 
    LESS_THAN, LESS_THAN_EQUAL, GREATER_THAN, GREATER_THAN_EQUAL, 
    AND, OR, 
    INTEGER_LITERAL, FLOATING_POINT_LITERAL, VARIABLE_NAME,
    ERROR,
    WHILE_CODE, IF_CODE, INT_LIT, FLOAT_LIT, ID, SEMICOLON,
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
        
// <IF_STMT> --> `if` `(` <BOOL_EXPR> `)` ( <STMT> `;` | <BLOCK> ) [ `else` ( <STMT> `;` | <BLOCK> )] 
    public static void if_stmt(String nextToken){
        if(nextToken != "if")
            System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
        else {
            getTokenType(nextToken);
            if(nextToken != "(")
                System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
            else {
                getTokenType(nextToken);
                bool_expr(nextToken);
                if(nextToken != ")")
                    System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");   
                if(nextToken == "{")
                    block();
                else {
                    stmt();
                    if(nextToken != ";")
                        System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
                } 
                if(nextToken == "else")
                    getTokenType(nextToken);
                    if(nextToken == "{")
                    block();
                else {
                    stmt();
                    if(nextToken != ";")
                        System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
                } 
            }
        }
    }
    
    // <STMT> --> <IF_STMT> | <BLOCK> | <EXPR> | <WHILE_LOOP>
    public static void stmt(String nextToken){
        if(nextToken == TokenType.WHILE_CODE)
            while_loop(nextToken);
        else if(nextToken == TokenType.IF_CODE)
            if_stmt(nextToken);
        else if(nextToken == TokenType.CURLY_OPEN)
            block(nextToken);
        else if(nextToken == TokenType.PAREN_OPEN || nextToken == TokenType.ID || nextToken == TokenType.INT_LIT)
            expr(nextToken);
        else {
            System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
        }
    }
    
    // <STMT_LIST> --> { <STMT> `;` }
    public static void stmt_list(String nextToken){
        while(nextToken != TokenType.ID || nextToken != TokenType.IF_CODE || nextToken != TokenType.WHILE_CODE || nextToken != TokenType.INT_LIT || nextToken != TokenType.WHILE_CODE || nextToken != TokenType.PAREN_OPEN){
            // we will process the statement and check if the  nextToken is a semicolon
            // and continue processing until its not one of those 
            stmt(nextToken);
            System.out.println("<" + TokenType.SEMICOLON + ", " + nextToken + ">");
        }

    }

    // <WHILE_LOOP> --> `while` `(` <BOOL_EXPR> `)` ( <STMT> `;` | <BLOCK>)
    public static void while_loop(String nextToken){
        if(nextToken != TokenType.WHILE_CODE)
            System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
        else {
            System.out.println("<" + TokenType.WHILE_CODE + ", " + nextToken + ">");
            getTokenType(nextToken);
            if(nextToken != TokenType.PAREN_OPEN)
                System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
            else {
                System.out.println("<" + TokenType.PAREN_OPEN + ", " + nextToken + ">");
                getTokenType(nextToken);
                bool_expr(nextToken);
                if(nextToken != TokenType.PAREN_CLOSE)
                    System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");   
                else {
                    System.out.println("<" + TokenType.PAREN_CLOSE + ", " + nextToken + ">");
                }
                if(nextToken == TokenType.CURLY_OPEN)
                    block();
                else {
                    stmt();
                    if(nextToken != TokenType.SEMICOLON)
                        System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
                    else {
                        System.out.println("<" + TokenType.SEMICOLON + ", " + nextToken + ">");
                    }
                } 
            }
        }
    }

    // <BLOCK> --> `{` <STMT_LIST> `}`
    public static void block(String nextToken){
        if(nextToken == TokenType.CURLY_OPEN) {
            System.out.println("<" + TokenType.CURLY_OPEN + ", " + nextToken + ">");
            stmt_list(nextToken);
            System.out.println("<" + TokenType.CURLY_CLOSE + ", " + nextToken + ">");
        }
        else {
            System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
        }
    }

    // <EXPR> --> <TERM> {(`+`|`-`) <TERM>}
    public static void expr(String nextToken){
        term(nextToken);

        while(nextToken == TokenType.ADDITION || nextToken == TokenType.SUBTRACTION){
            getTokenType(nextToken);
            term();
        }
    }

    // <TERM> --> <FACT> {(`*`|`/`|`%`) <FACT>}
    public static void term(String nextToken){
        if(nextToken == TokenType.ID || nextToken == TokenType.INT_LIT)
            getTokenType(nextToken);
        else {
            if(nextToken == TokenType.PAREN_OPEN){
                getTokenType(nextToken);;
                expr(nextToken);
            }
            else {
                System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
            }
        }
    }

    // <FACT> --> ID | INT_LIT | FLOAT_LIT | `(` <EXPR> `)`
    public static void factor(String nextToken){
        if (nextToken == TokenType.ID || nextToken == TokenType.INT_LIT)
            getTokenType(nextToken);
        /* If the RHS is ( <expr> ), call lex to pass over the
        left parenthesis, call expr, and check for the right
        parenthesis */
        else { 
            if (nextToken == TokenType.PAREN_OPEN){
                getTokenType(nextToken);
                expr(nextToken);
                if (nextToken == TokenType.PAREN_CLOSE)
                    getTokenType(nextToken);
                else {
                    System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
                }
            }
            /* It was not an id, an integer literal, or a left
            parenthesis */
            else{
                System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
            }
        }
    }

    // <BOOL_EXPR> --> <BTERM> {(`>`|`<`|`>=`|`<=`) <BTERM>}
    public static void bool_expr(String nextToken){
        b_term();

        if(nextToken == TokenType.LESS_THAN || nextToken == TokenType.LESS_THAN_EQUAL || nextToken == TokenType.GREATER_THAN || nextToken == TokenType.GREATER_THAN_EQUAL){
            switch(nextToken){
                case TokenType.LESS_THAN:
                    System.out.println("<" + TokenType.LESS_THAN + ", " + nextToken + ">");
                    break;
                case TokenType.GREATER_THAN:
                    System.out.println("<" + TokenType.GREATER_THAN + ", " + nextToken + ">");
                    break;
                case TokenType.LESS_THAN_EQUAL:
                    System.out.println("<" + TokenType.LESS_THAN_EQUAL + ", " + nextToken + ">");
                    break;
                case TokenType.GREATER_THAN_EQUAL:
                    System.out.println("<" + TokenType.GREATER_THAN_EQUAL + ", " + nextToken + ">");
                    break;
            }
            b_term();
        }
        else {
            System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
        }
    }

    // <BTERM> --> <BAND> {(`==`|`!=`) <BAND>}
    public static void b_term(String nextToken){
        b_and(nextToken);
        
        if(nextToken == TokenType.EQUALS || nextToken == TokenType.NOT_EQUAL){
            switch(nextToken){
                case "==": 
                    System.out.println("<" + TokenType.EQUALS + ", " + nextToken + ">");
                    break;
                case "!=": 
                    System.out.println("<" + TokenType.NOT_EQUAL + ", " + nextToken + ">");
                    break;
            }
            b_and(nextToken);
        }
        else {
            System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
        }
    }
    
    // <BAND> --> <BOR> {`&&` <BOR>}
    public static void b_and(String nextToken){
        b_or(nextToken);
        
        if(nextToken == TokenType.AND || nextToken == TokenType.OR){
            System.out.println("<" + TokenType.AND + ", " + nextToken + ">");
            b_and(nextToken);
        }
        else {
            System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
        }
    }
    
    // <BOR> --> <EXPR> {`||` <EXPR>}
    public static void b_or(String nextToken){
        expr(nextToken);
        
        if(nextToken == TokenType.OR){
            System.out.println("<" + TokenType.OR + ", " + nextToken + ">");
            expr(nextToken);
        }
        else {
            System.out.println("<" + TokenType.ERROR + ", " + nextToken + ">");
        }
    }
}