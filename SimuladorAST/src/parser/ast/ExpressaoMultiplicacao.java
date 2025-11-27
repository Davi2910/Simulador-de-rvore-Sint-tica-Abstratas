package parser.ast;

import parser.Token;

/**
 * Um dó da AST para expressões de multiplicação (operadores * e /)
 * 
 * @author Prof. Dr. David Buzatto
 */
public class ExpressaoMultiplicacao extends ExpressaoBinaria {
    
    public ExpressaoMultiplicacao( Expressao operandoE, Token operador, Expressao operandoD ) {
        super( operandoE, operador, operandoD );
    }
    
}
