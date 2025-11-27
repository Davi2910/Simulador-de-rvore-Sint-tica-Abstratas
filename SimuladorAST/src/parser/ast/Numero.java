package parser.ast;

import parser.Token;

/**
 * Um nó da AST para números.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Numero extends Expressao {
    
    private Token token;
    
    public Numero( Token token ) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
    
    @Override
    public String toString() {
        return token.toString();
    }
    
}
