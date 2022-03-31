package jogadores;
import java.util.LinkedList;

public class JogadorSuperPoderosas extends Jogador{
    private int maxLoopHeuristica;

    public JogadorSuperPoderosas(String nome) {
        super(nome);
        maxLoopHeuristica = 9;
    }

    @Override
    public int[] jogar(int[][] tabuleiro) {
        Jogada jogada = melhorJogada(tabuleiro, this.getSimbolo());
        return jogada.get();
    }    

    private Jogada melhorJogada(int[][] tabuleiro, int simbolo){
        LinkedList<Jogada> jogadasPossiveis = jogadasPossiveis(tabuleiro); 
        int melhorValor = -2;
        int valorAtual;
        Jogada melhorJogada = new Jogada(0,0);
        int[][] tab;

        for(Jogada jogada : jogadasPossiveis){
            tab = novoTabuleiro(tabuleiro, jogada, simbolo);
            int novoSimb = (simbolo == 1)? 0 : 1;
            valorAtual = miniMax(tab, simbolo, novoSimb, maxLoopHeuristica);
            if(valorAtual > melhorValor){
                melhorValor = valorAtual;
                melhorJogada = jogada;
            }    
        }
        return melhorJogada;
    }
    private LinkedList<Jogada> jogadasPossiveis(int[][] tabuleiro){
        LinkedList<Jogada> jogadas = new LinkedList<>();
        for(int l = 0; l < tabuleiro.length; l++){
            for(int c = 0; c < tabuleiro.length; c++){
                if(tabuleiro[l][c] == -1)
                    jogadas.add(new Jogada(l,c));
            }
        }
        return jogadas;

    }
    private int[][] novoTabuleiro(int[][] tabuleiro, Jogada jogada, int simbolo){
        int[][] novoTabuleiro = copyArray(tabuleiro);
        novoTabuleiro[jogada.linha][jogada.coluna] = simbolo;
        return novoTabuleiro;
    }
    private int miniMax(int[][] tabuleiro, int simbolo, int simboloDaVez, int maxLoop){
        int estadoJogo = jogadorVenceu(tabuleiro);

            if (estadoJogo == -1) {
                System.out.println("Empatou");
                return 0;
            } else if (estadoJogo == simbolo) {
                System.out.println("Ganhei");
                return 1;
            } else if (estadoJogo == 2) {
                System.out.println("Ainda no jogo");
            } else {
                System.out.println("Perdi");
                return -1;               
            }
            if(maxLoop == 0){
                return heuristica(tabuleiro, simboloDaVez, simbolo);
            }
        if(simboloDaVez == simbolo){
            LinkedList<Jogada> jogadasPossiveis = jogadasPossiveis(tabuleiro); 
            int melhorValor = -2;
            int valorAtual;
            int[][] tab;
    
            for(Jogada jogada : jogadasPossiveis){
                tab = novoTabuleiro(tabuleiro, jogada, simbolo);
                int novoSimb = (simbolo == 1)? 0 : 1;
                valorAtual = miniMax(tab, simbolo, novoSimb, maxLoop-1);
                if(valorAtual > melhorValor){
                    melhorValor = valorAtual;
                }    
            }
            return melhorValor;
        }else{
            LinkedList<Jogada> jogadasPossiveis = jogadasPossiveis(tabuleiro); 
            int melhorValor = 10;
            int valorAtual;
            int[][] tab;
    
            for(Jogada jogada : jogadasPossiveis){
                tab = novoTabuleiro(tabuleiro, jogada, simbolo);
                int novoSimb = (simbolo == 1)? 0 : 1;
                valorAtual = miniMax(tab, simbolo, novoSimb, maxLoop-1);
                if(valorAtual < melhorValor){
                    melhorValor = valorAtual;
                }    
            }
            return melhorValor;

        }
    }
    private int heuristica(int[][] tabuleiro, int simboloDaVez, int simbolo){
        int contLinha = 0;
        int contColuna = 0;
        for(int l = 0; l < tabuleiro.length; l++){
            for(int c = 0; c < tabuleiro.length; c++){
                if(tabuleiro[l][c] == simboloDaVez){
                    contLinha++;
                }else if(tabuleiro[l][c] == -1){

                }else{
                    contLinha--;
                }
            }
        }
        for(int c = 0; c < tabuleiro.length; c++){
            for(int l = 0; l < tabuleiro.length; l++){
                if(tabuleiro[l][c] == simboloDaVez){
                    contColuna++;
                }else if(tabuleiro[l][c] == -1){

                }else{
                    contColuna--;
                }
            }
        }
        if((contLinha+contColuna) > 0){
            return (simbolo == simboloDaVez)? 1 : -1;
        }else if((contLinha+contColuna) < 0){
            return (simbolo == simboloDaVez)? -1 : 1;
        }else{
            return 0;
        }
    }



    class Jogada{
        int linha;
        int coluna;
        public Jogada(int linha, int coluna){
            this.linha = linha;
            this.coluna = coluna;
        }
        public int[] get(){
            int[] posicao = {linha, coluna};
            return posicao;
        }
    }
    public int[][] copyArray(int[][] array) {
        int[][] resolt = new int[array.length][array.length];
        for(int i = 0; i < resolt.length; i ++) {
            for (int j = 0; j < resolt[i].length; j++) {
                resolt[i][j] = array[i][j];
            }
        }
        return resolt;
    }
    private int jogadorVenceu(int[][] tabuleiro){
        int soma1 = 0;        
        int soma0 = 0;        
        
        //Vencedor linha:
        for(int i = 0; i < tabuleiro.length; i++){
            soma1 = 0;
            soma0 = 0;
            for(int j = 0; j < tabuleiro.length; j++){
                if(tabuleiro[i][j] == 1){
                    soma1++;
                }
                if(tabuleiro[i][j] == 0){
                    soma0++;
                }
            }
            
            if(soma1 == tabuleiro.length)
                return 1;
            if(soma0 == tabuleiro.length)
                return 0;
        }
        
        
        //Vencedor coluna:
        for(int i = 0; i < tabuleiro.length; i++){
            soma1 = 0;
            soma0 = 0;
            for(int j = 0; j < tabuleiro.length; j++){
                if(tabuleiro[j][i] == 1){
                    soma1++;
                }
                if(tabuleiro[j][i] == 0){
                    soma0++;
                }
            }
            if(soma1 == tabuleiro.length)
                return 1;
            if(soma0 == tabuleiro.length)
                return 0;    
        }
        
        //Diagonal principal
        soma1 = 0;
        soma0 = 0;
        for(int i = 0; i < tabuleiro.length; i++){
            if(tabuleiro[i][i] == 1){
                soma1++;
            }
            if(tabuleiro[i][i] == 0){
                soma0++;
            }
        }
        if(soma1 == tabuleiro.length)
            return 1;
        if(soma0 == tabuleiro.length)
            return 0;
        
        //Diagonal principal
        soma1 = 0;
        soma0 = 0;
        for(int i = 0; i < tabuleiro.length; i++){
            if(tabuleiro[i][tabuleiro.length-i-1] == 1){
                soma1++;
            }
            if(tabuleiro[i][tabuleiro.length-i-1] == 0){
                soma0++;
            }
        }
        if(soma1 == tabuleiro.length)
            return 1;
        if(soma0 == tabuleiro.length)
            return 0; 

        int jogadas = 0;
        for(int i = 0; i <  tabuleiro.length; i++) {
            for(int j = 0; j < tabuleiro[i].length; j++) {
                if (tabuleiro[i][j] != -1) {
                    jogadas++;
                }
            }
        }

        if(jogadas == tabuleiro.length*tabuleiro.length) {
            return -1;
        } else {
            return 2;
        }
    }
}
