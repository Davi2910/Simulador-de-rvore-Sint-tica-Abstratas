package parser.ast;

import parser.Token;

/**
 * Um dó da AST para expressões de adição (operadores + e -)
 * 
 * @author Prof. Dr. David Buzatto
 */
public class ExpressaoAdicao extends ExpressaoBinaria {
    
    public ExpressaoAdicao( Expressao operandoE, Token operador, Expressao operandoD ) {
        super( operandoE, operador, operandoD );
    }
    
}
