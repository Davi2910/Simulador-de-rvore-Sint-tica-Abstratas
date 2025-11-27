package simulador;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import parser.ast.Expressao;
import parser.ast.ExpressaoBinaria;

public class IteradorBFS implements Iterator<Expressao> {
    
    // Implementa o Padrão Iterator, objeto que caminha pela a arvore, indo para os nós de nível a nível.

    private Queue<Expressao> fila;

    public IteradorBFS(Expressao raiz) {
        fila = new LinkedList<>();
        if (raiz != null) {
            fila.add(raiz); 
        }
    }
    
    @Override
    public boolean hasNext() {
        return !fila.isEmpty();
    }

    @Override
    public Expressao next() {
        if (!hasNext()) {
            return null; 
        }

        Expressao atual = fila.poll();
       
        if (atual instanceof ExpressaoBinaria binaria) {
            if (binaria.getOperandoE() != null) {
                fila.add(binaria.getOperandoE());
            }
            if (binaria.getOperandoD() != null) {
                fila.add(binaria.getOperandoD());
            }
        }
        
        return atual;
    }


    @Override
    public void remove() {
        throw new UnsupportedOperationException("A operação rremove nao funciona.");
    }
}