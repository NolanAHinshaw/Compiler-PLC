import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lex {
    // initializing variables that will be used in order to find out line, pos, and specific character
    private int line;
    private int pos;
    private int position;
    private char chr;
    private File s;

    // creating a hashmap in order to store and lookup the specific keywords in a programming language
    HashMap<String, TokenType> keywords = new HashMap<String, TokenType>();

    Lex(File file) {
        this.line = 1;
        this.pos = 0;
        this.position = 0;
        this.s = file;
        this.chr = ((CharSequence) this.s).charAt(0);
        this.keywords.put("for", TokenType.KEYWORD);
        this.keywords.put("while", TokenType.KEYWORD);
        this.keywords.put("do", TokenType.KEYWORD);
        this.keywords.put("if", TokenType.KEYWORD);
        this.keywords.put("else", TokenType.KEYWORD);
        this.keywords.put("print", TokenType.KEYWORD);
        this.keywords.put("switch", TokenType.KEYWORD);
        this.keywords.put("case", TokenType.KEYWORD);
        this.keywords.put("default", TokenType.KEYWORD);
        this.keywords.put("null", TokenType.KEYWORD);
        
    }

    /* 
    - func that checks if the character that seceeds a certain character is expected or not
    - the parameters represent a ternary operator in that if the expected char is given, 
    it will assign the token type to the ifyes param, if not, itll be assigned to the ifno parameter.
    - ex: < can be followed by = but it doesnt have to be
    */
    Token follow(char expect, TokenType ifyes, TokenType ifno, int line, int pos) {
        if (getNextChar() == expect) {
            getNextChar();
            return new Token(ifyes, "", line, pos);
        }
        if (ifno == TokenType.EOF) {
            error(line, pos, String.format("follow: unrecognized character: (%d) '%c'", (int)this.chr, this.chr));
        }
        return new Token(ifno, "", line, pos);
    }

    // func that gets the next character in a file
    char getNextChar() {
        this.pos++;
        this.position++;
        if (this.position >= this.s.length()) {
            this.chr = '\u0000';
            return this.chr;
        }
        this.chr = ((CharSequence) this.s).charAt(this.position);
        if (this.chr == '\n') {
            this.line++;
            this.pos = 0;
        }
        return this.chr;
    }
    
    // func that puts the tokens into a arraylist and prints them out
    void printTokens() {
        Token t;
        List<Token> ordered_tokens = new ArrayList<Token>();
        while ((t = getToken()).tokentype != TokenType.EOF) {
            ordered_tokens.add(t);
        }
        for (Token token : ordered_tokens) {
            System.out.println(token);
        }
    }

    /* 
    - func that checks the character and gets the token based on the type of charcater it is.
    - implements switch statement and using the follow func that was initialized above in order 
    to get the token of a specific char 
    */
    Token getToken() {
        int line, pos;
        while (Character.isWhitespace(this.chr)) {
            getNextChar();
        }
        line = this.line;
        pos = this.pos;
        
        switch (this.chr) {
            case '(': getNextChar(); return new Token(TokenType.LEFT_PARENTHESIS, "", line, pos);
            case ')': getNextChar(); return new Token(TokenType.RIGHT_PARENTHESIS, "", line, pos);
            case '+': getNextChar(); return new Token(TokenType.PLUS, "", line, pos);
            case '-': getNextChar(); return new Token(TokenType.MINUS, "", line, pos);
            case '*': getNextChar(); return new Token(TokenType.TIMES, "", line, pos);
            case '/': getNextChar(); return new Token(TokenType.DIVIDE, "", line, pos);
            case '%': getNextChar(); return new Token(TokenType.MODULO, "", line, pos);
            case '\u0000': return new Token(TokenType.EOF, "", this.line, this.pos);
            case '<': return follow('=', TokenType.LESS_OR_EQUALS, TokenType.LESS_THAN, line, pos);
            case '>': return follow('=', TokenType.GREATER_OR_EQUALS, TokenType.GREATER_THAN, line, pos);
            case '=': return follow('=', TokenType.EQUAL, TokenType.ASSIGNMENT_OPERATOR, line, pos);
            case '&': return follow('&', TokenType.AND, TokenType.EOF, line, pos);
            case '|': return follow('|', TokenType.OR, TokenType.EOF, line, pos);
            case '"': return string_lit(this.chr, line, pos);
            
            default: return identifier_or_integer(line, pos);
        }
    }

    // func that takes in a string literal and checks if it is correct or not 
    Token string_lit(char start, int line, int pos) {
        String result = "";
        while (getNextChar() != start) {
            if (this.chr == '\u0000') {
                error(line, pos, "EOF while scanning string literal");
            }
            if (this.chr == '\n') {
                error(line, pos, "EOL while scanning string literal");
            }
            result += this.chr;
        }
        getNextChar();
        return new Token(TokenType.STRING, result, line, pos);
    }

    // func that determines if the specific character is a identifier or an integer in order to determine its tokentype
    Token identifier_or_integer(int line, int pos) {
        boolean is_number = true;
        String text = "";
        while (Character.isAlphabetic(this.chr) || Character.isDigit(this.chr) || this.chr == '_') {
            text += this.chr;
            if (!Character.isDigit(this.chr)) {
                is_number = false;
            }
            getNextChar();
        }
        
        if (text.equals("")) {
            error(line, pos, String.format("identifer_or_integer unrecognized character: (%d) %c", (int)this.chr, this.chr));
        }
        
        if (Character.isDigit(text.charAt(0))) {
            if (!is_number) {
                error(line, pos, String.format("invalid number: %s", text));
            }
            return new Token(TokenType.INT_LIT, text, line, pos);
        }
        
        if (this.keywords.containsKey(text)) {
            return new Token(this.keywords.get(text), "", line, pos);
        }
        return new Token(TokenType.IDENTIFIER, text, line, pos);
    }

    public static void error(int line, int pos, String msg) {
        if (line > 0 && pos > 0) {
            System.out.printf("%s in line %d, pos %d\n", msg, line, pos);
        } else {
            System.out.println(msg);
        }
        System.exit(1);
    }
}