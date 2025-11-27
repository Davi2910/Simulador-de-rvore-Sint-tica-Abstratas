package parser;

/**
 * Um tokenizador para o parser.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Lexer {
    
    private Fluxo fluxo;
    private Token atual;
    
    public Lexer( String str ) {
        fluxo = new Fluxo( str );
        proximo();
    }
    
    public Token getToken() {
        return atual;
    }
    
    public void proximo() {
        
        descartarBrancos();
        
        if ( fluxo.getChar() != Fluxo.EOF && atual != Token.EOF ) {
            
            String valor = "";
            char c = fluxo.getChar();

            switch ( c ) {
                case '+':
                    valor += c;
                    fluxo.proximo();
                    break;
                case '-':
                    valor += c;
                    fluxo.proximo();
                    break;
                case '*':
                    valor += c;
                    fluxo.proximo();
                    break;
                case '/':
                    valor += c;
                    fluxo.proximo();
                    break;
                case '%':
                    valor += c;
                    fluxo.proximo();
                    break;
                case '(':
                    valor += c;
                    fluxo.proximo();
                    break;
                case ')':
                    valor += c;
                    fluxo.proximo();
                    break;
                default:
                    while ( c != Fluxo.EOF && ( ( c >= '0' && c <= '9' ) || c == '.' ) ) {
                        valor += c;
                        fluxo.proximo();
                        c = fluxo.getChar();
                    }
                    break;
            }

            atual = new Token( valor, resolverTipo( valor ) );
            return;
            
        }
        
        atual = Token.EOF;
        
    }
    
    private void descartarBrancos() {
        while ( fluxo.getChar() != Fluxo.EOF && fluxo.getChar() == ' ' ) {
            fluxo.proximo();
        }
    }
    
    private TipoToken resolverTipo( String valor ) {
        
        switch ( valor ) {
            case "+": return TipoToken.ADICAO;
            case "-": return TipoToken.SUBTRACAO;
            case "*": return TipoToken.MULTIPLICACAO;
            case "/": return TipoToken.DIVISAO;
            case "%": return TipoToken.MODULO;
            case "(": return TipoToken.PARENTESES_ESQUERDO;
            case ")": return TipoToken.PARENTESES_DIREITO;
            default: return TipoToken.NUMERO;
        }
        
    }
    
}
