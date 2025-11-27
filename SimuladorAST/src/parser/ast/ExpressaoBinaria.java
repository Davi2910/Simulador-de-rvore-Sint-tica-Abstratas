package parser.ast;

import parser.Token;

/**
 * Um dó da AST para expressões binárias.
 * 
 * @author Prof. Dr. David Buzatto
 */
public abstract class ExpressaoBinaria extends Expressao {
    
    protected Expressao operandoE;
    protected Token operador;
    protected Expressao operandoD;
    
    public ExpressaoBinaria( Expressao operandoE, Token operador, Expressao operandoD ) {
        this.operandoE = operandoE;
        this.operador = operador;
        this.operandoD = operandoD;
    }

    public Expressao getOperandoE() {
        return operandoE;
    }

    public Token getOperador() {
        return operador;
    }

    public Expressao getOperandoD() {
        return operandoD;
    }
    
    @Override
    public String toString() {
        return operandoE + " " + operador + " " + operandoD;
    }
    
}
