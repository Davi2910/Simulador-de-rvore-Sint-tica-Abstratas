package parser;

/**.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Token {
    
    public static final Token EOF = new Token( "", TipoToken.EOF );
    
    private final String valor;
    private final TipoToken tipo;
    
    public Token( String valor, TipoToken tipo ) {
        this.valor = valor;
        this.tipo = tipo;
    }
    
    public String valor() {
        return valor;
    }
    
    public TipoToken tipo() {
        return tipo;
    }
    
    @Override
    public String toString() {
        return valor;
    }
    
}