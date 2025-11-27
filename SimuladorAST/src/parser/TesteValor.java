package parser;

/**
 * 
 * 
 * @author Prof. Dr. David Buzatto
 */
public class TesteValor {
    
    public static void main( String[] args ) {
        
        String[] expressoes = {
            "7 % 3 * 10.5 - 1 % 2"
        };
        
        for ( String expressao : expressoes ) {
            ParserValor p = ParserValor.parse( expressao );
            System.out.println( "Expressao:" );
            System.out.printf( "    %s = %.2f\n\n", expressao, p.getResultado() );
            System.out.println( "----------------------------------------\n" );
        }
        
    }
    
}
