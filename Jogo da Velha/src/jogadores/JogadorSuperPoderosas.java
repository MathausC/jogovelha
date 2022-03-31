package jogadores;
import java.util.LinkedList;

public class JogadorSuperPoderosas extends Jogador{

    public JogadorSuperPoderosas(String nome) {
        super(nome);
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
            valorAtual = miniMax(tab, simbolo, novoSimb);
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
    private int miniMax(int[][] tabuleiro, int simbolo, int simboloDaVez){
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
        if(simboloDaVez == simbolo){
            LinkedList<Jogada> jogadasPossiveis = jogadasPossiveis(tabuleiro); 
            int melhorValor = -2;
            int valorAtual;
            int[][] tab;
    
            for(Jogada jogada : jogadasPossiveis){
                tab = novoTabuleiro(tabuleiro, jogada, simbolo);
                int novoSimb = (simbolo == 1)? 0 : 1;
                valorAtual = miniMax(tab, simbolo, novoSimb);
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
                valorAtual = miniMax(tab, simbolo, novoSimb);
                if(valorAtual < melhorValor){
                    melhorValor = valorAtual;
                }    
            }
            return melhorValor;

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
