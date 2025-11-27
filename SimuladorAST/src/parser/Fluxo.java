package parser;

/**
 * Um fluxo de caracteres.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Fluxo {
    
    public static final char EOF = '\0';
    
    private String str;
    private int atual;
    
    public Fluxo( String str ) {
        this.str = str;
    }
    
    public char getChar() {
        if ( atual < str.length() ) {
            return str.charAt( atual );
        }
        return EOF;
    }
    
    public void proximo() {
        if ( atual < str.length() ) {
            atual++;
        }
    }
    
}
