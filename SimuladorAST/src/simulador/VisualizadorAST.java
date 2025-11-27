package simulador;

import parser.ast.Expressao;
import parser.ast.ExpressaoBinaria;
import parser.ast.Numero;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class VisualizadorAST {
    
    // A classe  recebe a arvore pronta do Parser, calcula as posições x e y e desenha os nós os círculos e a arvore.

    
    private double raioNo;
    private double espacamentoVertical;
    
    private Expressao raiz;
    private Map<Expressao, Vector2> posicoes;
    private double larguraTela;
    private double alturaTela;

    private List<Expressao> nosVisiveis = new ArrayList<>();
    private double progressoAnimacao = 1.0;
    private Font fonteNo;
    
    public VisualizadorAST(Expressao raiz, double larguraTela, double alturaTela, Font fonteNo) {
        this.raiz = raiz;
        this.larguraTela = larguraTela;
        this.alturaTela = alturaTela;
        this.posicoes = new HashMap<>();
        this.fonteNo = fonteNo;
        
        int profundidade = getProfundidade(raiz);
        if (profundidade <= 0) profundidade = 1;

        double espacoUtil = alturaTela - 140 - 50; 
        this.espacamentoVertical = espacoUtil / (profundidade > 1 ? (profundidade - 1) : 1);

        if (this.espacamentoVertical > 120) this.espacamentoVertical = 120;
        if (this.espacamentoVertical < 50) this.espacamentoVertical = 50;

        this.raioNo = this.espacamentoVertical / 3.0;
        
        if (this.raioNo > 30) this.raioNo = 30;
        if (this.raioNo < 15) this.raioNo = 15;
        
        calcularPosicoes(); 
    }

    public void setAnimacao(List<Expressao> nos, double progresso) {
        this.nosVisiveis = nos;
        this.progressoAnimacao = progresso;
    }

    public int getProfundidade(Expressao expr) {
        if (expr == null) return 0;
        if (expr instanceof Numero) return 1;
        if (expr instanceof ExpressaoBinaria binaria) {
            int profE = getProfundidade(binaria.getOperandoE());
            int profD = getProfundidade(binaria.getOperandoD());
            return 1 + Math.max(profE, profD);
        }
        return 1; 
    }

    public int getContagemNos(Expressao expr) {
        if (expr == null) return 0;
        if (expr instanceof ExpressaoBinaria binaria) {
            return 1 + getContagemNos(binaria.getOperandoE()) + getContagemNos(binaria.getOperandoD());
        }
        return 1; 
    }
    
    private void calcularPosicoes() {
        if (raiz != null) {
            double xInicial = larguraTela / 2;
            double yInicial = 140; 
            calcularPosicoesRecursivo(raiz, xInicial, yInicial, larguraTela / 4); 
        }
    }
    
    private void calcularPosicoesRecursivo(Expressao expr, double x, double y, double espacamento) {
        if (expr == null) return;
        posicoes.put(expr, new Vector2(x, y));
        
        if (expr instanceof ExpressaoBinaria binaria) {
            double novoEspacamento = espacamento / 2;
            double minEspacamento = this.raioNo * 1.5; 
            if (novoEspacamento < minEspacamento) novoEspacamento = minEspacamento;
            
            calcularPosicoesRecursivo(binaria.getOperandoE(), x - novoEspacamento, y + this.espacamentoVertical, novoEspacamento);
            calcularPosicoesRecursivo(binaria.getOperandoD(), x + novoEspacamento, y + this.espacamentoVertical, novoEspacamento);
        }
    }
    
    public void desenhar(EngineFrame engine) {
        if (raiz != null) {
            desenharLinhas(raiz, engine);
            desenharNos(raiz, engine);
        }
    }
    
    private void desenharLinhas(Expressao expr, EngineFrame engine) {
        if (expr == null || !nosVisiveis.contains(expr)) {
            return;
        }

        if (expr instanceof ExpressaoBinaria binaria) {
            Vector2 posAtual = posicoes.get(expr);
         
            if (nosVisiveis.isEmpty()) return;
            Expressao ultimoNo = nosVisiveis.get(nosVisiveis.size() - 1);
            
            Expressao opE = binaria.getOperandoE();
            Vector2 posOpE = posicoes.get(opE);
            if (posOpE != null && nosVisiveis.contains(opE)) { 
                int alpha = 255;
                if (opE == ultimoNo && progressoAnimacao < 1.0) { // Só aplica fade se estiver ativamente animando
                    alpha = (int)(Math.min(1.0, progressoAnimacao) * 255);
                }
                engine.drawLine(posAtual, posOpE, new Color(128, 128, 128, alpha));
                desenharLinhas(opE, engine);
            }
            
            Expressao opD = binaria.getOperandoD();
            Vector2 posOpD = posicoes.get(opD);
            if (posOpD != null && nosVisiveis.contains(opD)) { 
                int alpha = 255;
                if (opD == ultimoNo && progressoAnimacao < 1.0) {
                    alpha = (int)(Math.min(1.0, progressoAnimacao) * 255);
                }
                engine.drawLine(posAtual, posOpD, new Color(128, 128, 128, alpha));
                desenharLinhas(opD, engine);
            }
        }
    }
    
    private void desenharNos(Expressao expr, EngineFrame engine) {
        if (expr == null || !nosVisiveis.contains(expr)) {
            return;
        }

        Vector2 pos = posicoes.get(expr);
        if (pos == null) return; 

        int alpha = 255;
        if (!nosVisiveis.isEmpty()) {
            Expressao ultimoNo = nosVisiveis.get(nosVisiveis.size() - 1);
            if (expr == ultimoNo && progressoAnimacao < 1.0) {
                alpha = (int)(Math.min(1.0, progressoAnimacao) * 255);
            }
        }

        Color corFundo, corBorda, corTexto;
        
        if (expr instanceof Numero) {
            corFundo = new Color(180, 220, 255, alpha);
            corBorda = new Color(50, 100, 200, alpha);
        } else {
            corFundo = new Color(255, 200, 150, alpha);
            corBorda = new Color(200, 100, 50, alpha);
        }
        corTexto = new Color(0, 0, 0, alpha);
        
        engine.fillCircle(pos, this.raioNo, corFundo);
        engine.drawCircle(pos, this.raioNo, corBorda);
        
        if (this.fonteNo != null) {
            engine.setFont(this.fonteNo);
        }
        
        String texto = obterTexto(expr);
        int larguraTexto = engine.measureText(texto);
        Vector2 posTexto = new Vector2(pos.x - larguraTexto / 2, pos.y + 5);
        
        engine.drawText(texto, posTexto, corTexto); 
        
        if (expr instanceof ExpressaoBinaria binaria) {
            desenharNos(binaria.getOperandoE(), engine);
            desenharNos(binaria.getOperandoD(), engine);
        }
    }
    
    private String obterTexto(Expressao expr) {
        if (expr instanceof Numero numero) {
            return numero.getToken().valor();
        } else if (expr instanceof ExpressaoBinaria binaria) {
            return binaria.getOperador().valor();
        }
        return "?";
    }
}