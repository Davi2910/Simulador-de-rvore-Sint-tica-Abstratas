package simulador;

import parser.Parser;
import parser.ast.Expressao;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.awt.Color;
import java.awt.Font; 
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class SimuladorAST extends EngineFrame implements KeyListener {
    
    
   // Implementa o Padrão Iterator, objeto que caminha pela a arvore, indo para os nós de nível a nível.

    

    private String expressao;
    private String expressaoDigitada;
    private Parser parser;
    private Expressao raiz;
    private double resultado;
    private VisualizadorAST visualizador;
    private String mensagemErro = null;

    private int contagemNos = 0;
    private int alturaArvore = 0;

    private double btnX = 10;
    private double btnY = 60;
    private double btnLargura = 140;
    private double btnAltura = 30;

    private final double VELOCIDADE_ANIMACAO = 0.4; 
    private Iterator<Expressao> iteradorBFS;
    
    private List<Expressao> nosDesenhados;   
    private double timerAnimacao = 0;
    private boolean estaAnimando = false;

    private Font fonteTitulo, fonteTexto, fonteBotao, fonteResultado, fonteRodape, fonteNo;

    public SimuladorAST() {
        super(800, 600, "Simulador de Árvore Sintática Abstrata", 60, true);
    }
            
    
    @Override
    public void create() {
        expressaoDigitada = ""; 
        nosDesenhados = new ArrayList<>();
        
        fonteTitulo = new Font("Arial", Font.BOLD, 18);
        fonteTexto = new Font("Arial", Font.PLAIN, 16);
        fonteBotao = new Font("Arial", Font.PLAIN, 14);
        fonteResultado = new Font("Arial", Font.BOLD, 16);
        fonteRodape = new Font("Arial", Font.PLAIN, 12);
        fonteNo = new Font("Arial", Font.PLAIN, 12);
        
        System.out.println("=================================");
        System.out.println("Simulador AST ");
     
        
        this.addKeyListener(this);
        setFocusable(true); 
        requestFocusInWindow(); 
    }
    
    private void processarExpressao() {
        
        estaAnimando = false;
        timerAnimacao = 0;
        nosDesenhados = new ArrayList<>();
        
        if (expressaoDigitada == null || expressaoDigitada.trim().isEmpty()) {
            parser = null;
            raiz = null;
            visualizador = null;
            mensagemErro = null;
            contagemNos = 0;
            alturaArvore = 0;
            return;
        }
        
        try{
            expressao = expressaoDigitada;
            System.out.println("Processando expressao: " +  expressao);
            
            parser = Parser.parse(expressao);
            raiz = parser.getExpressaoResultante();
            resultado = parser.getResultado();
            
            visualizador = new VisualizadorAST(raiz, getScreenWidth(), getScreenHeight(), fonteNo);
            
            mensagemErro = null; 
            this.contagemNos = visualizador.getContagemNos(raiz);
            this.alturaArvore = visualizador.getProfundidade(raiz);

    
            iteradorBFS = new IteradorBFS(raiz); 
            estaAnimando = true; 
          
            if (iteradorBFS.hasNext()) {
                nosDesenhados.add(iteradorBFS.next());
            }

            System.out.println("Resultado: " + resultado);
            System.out.println("\nArvore :");
            System.out.println(parser.getAST());
        
        } catch (Exception e) {
            mensagemErro = "Erro: " + e.getMessage();
            System.err.println("ERRO: " + e.getMessage());
            e.printStackTrace();
            
            raiz = null;
            visualizador = null;
            parser = null;
            contagemNos = 0;
            alturaArvore = 0;
            estaAnimando = false;
            iteradorBFS = null; 
        }
    }

    @Override
    public void update(double delta) {
        
        if (!estaAnimando && isMouseButtonPressed(1)) {
            double mX = getMouseX();
            double mY = getMouseY();
            
            if (mX >= btnX && mX <= btnX + btnLargura &&
                mY >= btnY && mY <= btnY + btnAltura) {
                processarExpressao();
            }
        }
        
        if (estaAnimando) {
            
            timerAnimacao += delta; 
            
           
            if (timerAnimacao >= VELOCIDADE_ANIMACAO) {
                timerAnimacao = 0; 
                
              
                if (iteradorBFS.hasNext()) {
                   
                    nosDesenhados.add(iteradorBFS.next());
                } else {
                   
                    estaAnimando = false; 
                    timerAnimacao = 0;
                }
            }
        }
      
    }

    @Override
    public void draw() {
        clearBackground(Color.WHITE);
       
        setFont(fonteTitulo);
        drawText("Simulador de Arvore Sintática Abstrata", new Vector2(10, 10), Color.DARK_GRAY);

        setFont(fonteTexto);
        drawText("Expressao: " + expressaoDigitada, new Vector2(10, 35), Color.BLACK);
        
        int larguraLabel = measureText("Expressao: ");
        int larguraTexto = measureText(expressaoDigitada);
        if (!estaAnimando && (int)(getTime() * 2) % 2 == 0) { 
            drawText("|", new Vector2(10 + larguraLabel + larguraTexto, 35), Color.DARK_GRAY);
        }
        
        double mX = getMouseX();
        double mY = getMouseY();
        boolean mouseSobre = (mX >= btnX && mX <= btnX + btnLargura &&
                              mY >= btnY && mY <= btnY + btnAltura);
        
        if (mouseSobre && !estaAnimando) {
            fillRectangle(btnX, btnY, btnLargura, btnAltura, new Color(220, 220, 220));
        } else {
            fillRectangle(btnX, btnY, btnLargura, btnAltura, new Color(240, 240, 240));
        }
        drawRectangle(btnX, btnY, btnLargura, btnAltura, Color.GRAY);
        
        setFont(fonteBotao);
        String textoBotao = estaAnimando ? "Gerando..." : "Processar";
        int larguraTextoBotao = measureText(textoBotao);
        double textoX = btnX + (btnLargura - larguraTextoBotao) / 2;
        double textoY = btnY + (btnAltura / 2) + 5; 
        drawText(textoBotao, new Vector2(textoX, textoY), estaAnimando ? Color.GRAY : Color.BLACK);
       
        

   
        double yResultado = 105; 
        
        setFont(fonteResultado);
        if (mensagemErro != null) {
            drawText(mensagemErro, new Vector2(10, yResultado), Color.RED);
        } else if (parser != null) {
            
            if (!estaAnimando) {
                String strResultado;
                if (resultado % 1 == 0) {
                    strResultado = String.format("Resultado: %.0f", resultado);
                } else {
                    strResultado = String.format("Resultado: %.2f", resultado);
                }
                drawText(strResultado, new Vector2(10, yResultado), Color.BLUE);
            }
            
            if (visualizador != null) {
                double progresso = estaAnimando ? (timerAnimacao / VELOCIDADE_ANIMACAO) : 1.0;
                visualizador.setAnimacao(nosDesenhados, progresso);
                visualizador.desenhar(this);
            }
        }
        
        setFont(fonteRodape);
        double yFooter = getScreenHeight() - 20; 
        String statusExpressao;
        Color corStatus;
        
        if (mensagemErro != null) {
            statusExpressao = "Expressão Errada";
            corStatus = Color.RED;
        } else if (parser == null) {
            statusExpressao = "Aguardando expressão...";
            corStatus = Color.GRAY;
        } else if (estaAnimando) {
            statusExpressao = "Gerando árvore...";
            corStatus = Color.BLUE;
        } else {
            statusExpressao = "Expressão Ok ";
            corStatus = new Color(0, 150, 0); 
        }
        
        String footerText = String.format("Informações adicionais | Nós: %d | Altura: %d",
                contagemNos, alturaArvore);
        
        drawText(footerText, new Vector2(10, yFooter), Color.DARK_GRAY);
        
        int larguraFooter = measureText(footerText);
        drawText(statusExpressao, new Vector2(10 + larguraFooter + 10, yFooter), corStatus);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        char typed = e.getKeyChar();
        if (estaAnimando) return;
        
        if (typed == '\b' || typed == 8) { 
            if (expressaoDigitada.length() > 0) {
                expressaoDigitada = expressaoDigitada.substring(0, expressaoDigitada.length() - 1);
            }
        } 
        else if (typed == '\n' || typed == 10) { 
            processarExpressao();
        } 
        else if (typed >= ' ' && typed <= '~') { 
            expressaoDigitada += typed;
        }
    }

    @Override public void keyPressed(KeyEvent e) { 
        
    }
    @Override public void keyReleased(KeyEvent e) { 
        
    }
    
    public static void main(String[] args) {
        new SimuladorAST();
    }
}