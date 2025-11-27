package parser;

/**
 * Driver.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Teste {
    
    public static void main( String[] args ) {
        
        String[] expressoes = {
           "7",
            "42",
            "5 + 3",
            "10 - 2",
            "6 * 4",
            "12 / 3",
            "2 + 3 * 4",
            "15 - 6 / 2",
            "(5 + 3) * 2",
            "20 / (4 - 2)",
            "1 + 2 - 3 * 4",
            "1 + (2 - 3) * 4",
            "10 / 2 + 3 * 5",
            "((7 + 3) / 2) - 1",
            "5 * (8 - 3) + 10 / 2",
            "2 * (3 + (4 - 1))",
            "(10 - 2) / (1 + 3)",
            "1 + (2 * (3 - 1) + 4) / 2",
            "((15 + 5) / (2 * 5)) - (3 + 1)",
            "7 + (8 / 2 - 1) * (3 + 4 / 2) - 5"
        };
        
        for ( String expressao : expressoes ) {
            Parser p = Parser.parse( expressao );
            System.out.println( "Expressao:" );
            System.out.printf( "    %s = %.2f\n\n", expressao, p.getResultado() );
            System.out.println( "AST:" );
            System.out.println( p.getAST() );
            System.out.println( "----------------------------------------\n" );
        }
        
    }
    
}
