package parser;

import parser.ast.Expressao;
import parser.ast.ExpressaoAdicao;
import parser.ast.ExpressaoBinaria;
import parser.ast.ExpressaoMultiplicacao;
import parser.ast.Numero;

/**
 * Parser para expressões aritméticas.
 * 
 * Gramática (EBNF):
 *   expressao -> termo ( opAd termo )* .
 *   termo     -> fator ( opMu fator )* .
 *   fator     -> inteiro | '(' expressao ')' .
 *   opAd      -> '+' | '-' .
 *   opMu      -> '*' | '/' .
 *   inteiro   -> [0..9]+ .
 * 
 * @author Prof. Dr. David Buzatto
 */
public class ParserValor {

    private Lexer lexer;
    private double resultado;
    
    public static ParserValor parse( String expressao ) {
        return new ParserValor( expressao );
    }
    
    public double getResultado() {
        return resultado;
    }
    
    private ParserValor( String expressao ) {
        lexer = new Lexer( expressao );
        resultado = parseExpressao();
    }
    
    // expressao -> termo ( opAd termo )* .
    private double parseExpressao() {
        
        double termoE = parseTermo();
        
        while ( lexer.getToken().tipo() != TipoToken.EOF && 
                ( lexer.getToken().tipo() == TipoToken.ADICAO || 
                  lexer.getToken().tipo() == TipoToken.SUBTRACAO ) ) {
            
            Token operador = lexer.getToken();
            lexer.proximo();
            
            double termoD = parseTermo();
            
            switch ( operador.tipo() ) {
                case ADICAO:
                    termoE += termoD;
                    break;
                case SUBTRACAO:
                    termoE -= termoD;
                    break;
            }
            
        }
        
        return termoE;
        
    }
    
    // termo -> fator ( opMu fator )* .
    private double parseTermo() {
        
        double fatorE = parseFator();
        
        while ( lexer.getToken().tipo() != TipoToken.EOF && 
                ( lexer.getToken().tipo() == TipoToken.MULTIPLICACAO || 
                  lexer.getToken().tipo() == TipoToken.DIVISAO || 
                  lexer.getToken().tipo() == TipoToken.MODULO ) ) {
            
            Token operador = lexer.getToken();
            lexer.proximo();
            
            double fatorD = parseFator();
            
            switch ( operador.tipo() ) {
                case MULTIPLICACAO:
                    fatorE *= fatorD;
                    break;
                case DIVISAO:
                    fatorE /= fatorD;
                    break;
                case MODULO:
                    fatorE %= fatorD;
                    break;
            }
            
        }
        
        return fatorE;
        
    }
    
    // fator -> inteiro | '(' expressao ')' .
    private double parseFator() {
        
        double expressao = 0;
        
        if ( lexer.getToken().tipo() == TipoToken.NUMERO ) {
            Token numero = lexer.getToken();
            lexer.proximo();
            expressao = Double.parseDouble( numero.valor() );
        } else if ( lexer.getToken().tipo() == TipoToken.PARENTESES_ESQUERDO ){
            lexer.proximo();
            expressao = parseExpressao();
            lexer.proximo(); // depois do parênteses da direita
        }
        
        return expressao;
        
    }
    
    // visitor para cálculo
    private double calcular( Expressao e ) {
        
        if ( e instanceof Numero numero ) {
            return Double.parseDouble( numero.getToken().valor() );
        } else if ( e instanceof ExpressaoAdicao adicao ) {
            double valorE = calcular( adicao.getOperandoE() );
            double valorD = calcular( adicao.getOperandoD() );
            if ( adicao.getOperador().tipo() == TipoToken.ADICAO ) {
                return valorE + valorD;
            } else {
                return valorE - valorD;
            }
        } else if ( e instanceof ExpressaoMultiplicacao multiplicacao ) {
            double valorE = calcular( multiplicacao.getOperandoE() );
            double valorD = calcular( multiplicacao.getOperandoD() );
            if ( multiplicacao.getOperador().tipo() == TipoToken.MULTIPLICACAO ) {
                return valorE * valorD;
            } else if ( multiplicacao.getOperador().tipo() == TipoToken.DIVISAO ) {
                return valorE / valorD;
            } else {
                return valorE % valorD;
            }
        }
        
        return 0;
        
    }
    
}
