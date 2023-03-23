public class Token {
    public TokenType tokentype;
    public String value;
    public int line;
    public int pos;
    Token(TokenType token, String value, int line, int pos) {
        this.tokentype = token; this.value = value; this.line = line; this.pos = pos;
    }
}