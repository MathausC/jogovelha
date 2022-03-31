/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogadores;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author aluno
 */
public class JogadorVencedor extends Jogador{
    private Minmax minmax;
    
    public JogadorVencedor(String nome) {
        super(nome);
        minmax = new Minmax(this.getSimbolo());
    }

    @Override
    public int[] jogar(int[][] tabuleiro) {
        minmax.setTabuleiro(tabuleiro);
        return minmax.getJogada();
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

    private class Minmax {
        private int[][] tabuleiro;
        private int simbolo;
        private int numJogadas;
        private Jogada jogadaDaVez;
        private ArrayList<Jogada> primeirasJogadas;

        Minmax(int simbolo) {
            this.simbolo = simbolo;
            if(simbolo == 0) {
                numJogadas = 1;
            } else {
                numJogadas = 0;
            }
        }

        private ArrayList<Jogada> criaPrimeirasJogadas() {//pai de familia
            ArrayList<Jogada> jogadas = new ArrayList<Jogada>();
            for (int i = 0; i < this.tabuleiro.length; i++) {
                for(int j = 0; j < this.tabuleiro[i].length; j++) {
                    int [][] proximoTabuleiro = copyArray(this.tabuleiro);
                    System.out.print(proximoTabuleiro[i][j] + ", ");
                    if(proximoTabuleiro[i][j] == -1) {
                            proximoTabuleiro[i][j] = this.simbolo;
                            int [] ij = {i, j};
                            jogadas.add(new Jogada(proximoTabuleiro, simbolo, ij));
                    }
                }
            }
            return jogadas;
        }

        public void setTabuleiro(int[][] tabuleiro) {
                this.tabuleiro = tabuleiro;
        }

        private  Jogada jogadaInicio() {
            this.primeirasJogadas = criaPrimeirasJogadas();
            Jogada joga =  primeirasJogadas.get(0);
            for(Jogada j : primeirasJogadas) {            
                if(j.peso > joga.peso) {
                    joga = j;
                }                
            }
            return joga;
        }

        private void getJogadaInimiga() {
            boolean flag = false;
            for (Jogada jg : jogadaDaVez.filhos) {
                flag = false;
                for (int i = 0; i < jg.tabuleiro.length; i++) {
                    for (int j = 0; j < jg.tabuleiro[i].length; j++) {
                        if (jg.tabuleiro[i][j] != tabuleiro[i][i]) {
                            flag = true;
                            break;
                        }
                    }
                    if(flag) {
                        break;
                    }
                }
                if(!flag) {
                    jogadaDaVez = jg;
                    break;
                }
            }
        }

        public int[] getJogada() {
            for(int i = 0; i < tabuleiro.length;i++){
                for(int c = 0; c < tabuleiro.length; c++){
                    System.out.print(tabuleiro[i][c] + ", ");
                }
            }
            if(simbolo == 1 && numJogadas == 0) {
                this.jogadaDaVez = jogadaInicio();
                numJogadas++;
                return jogadaDaVez.jogada;
            } else {
                if(simbolo == 0 && numJogadas == 1) {
                    this.jogadaDaVez = jogadaInicio();  
                } else {
                    getJogadaInimiga();
                }
                Jogada jg = jogadaDaVez.filhos.get(0);
                for (Jogada j : jogadaDaVez.filhos) {
                    if(jg.peso < j.peso) {
                        jg = j;
                    }
                }
                jogadaDaVez = jg;
                numJogadas++;
                return jogadaDaVez.jogada;
            }
        }
    }

    private class Jogada {
        Jogada pai;
        int peso;
        int[] jogada = new int[2];
        int simbolloInicio;
        int simboloDaVez;
        int [][] tabuleiro;
        ArrayList<Jogada> filhos = new ArrayList<Jogada>();

        public Jogada (int[][] tabuleiro, int simbolloInicio, int[] jogada) {
            this.tabuleiro = tabuleiro;
            this.simbolloInicio = simbolloInicio;
            if(this.pai == null) {
                this.simboloDaVez = simbolloInicio;
            } else if(pai.simboloDaVez == 1) {
                simboloDaVez = 0;
            } else {
                simboloDaVez = 1;
            }

            int estadoJogo = jogadorVenceu();

            if (estadoJogo == -1) {
                System.out.println("Empatou");
                peso = 0;
            } else if (estadoJogo == simbolloInicio) {
                System.out.println("Ganhei");
                peso = 1;
            } else if (estadoJogo == 2) {
                System.out.println("Ainda no jogo");
                filhos  = criaFilhos();
                peso = calculaPesoComFilhos();
            } else {
                System.out.println("Perdi");
                peso = -1;               
            }
        }

        private int calculaPesoComFilhos() {
            int pesoTemp = 0;
            for(Jogada j : filhos) {
                if(simboloDaVez == simbolloInicio) {
                    if(j.peso >= pesoTemp) {
                        pesoTemp = j.peso;
                    }
                } else {
                    if(j.peso <= pesoTemp) {
                        pesoTemp = j.peso;
                    }
                }
            }
            return pesoTemp;
        }

        private ArrayList<Jogada> criaFilhos() {//pai de familia
            ArrayList<Jogada> js = new ArrayList<Jogada>();
            for (int i = 0; i < this.tabuleiro.length; i++) {
                for(int j = 0; j < this.tabuleiro[i].length; j++) {
                    int [][] proximoTabuleiro = copyArray(this.tabuleiro);
                    System.out.println(proximoTabuleiro[i][j]);
                    if(proximoTabuleiro[i][j] == -1) {
                        proximoTabuleiro[i][j] = this.simboloDaVez;
                        int [] ij = {i, j};
                        Jogada jg = new Jogada(proximoTabuleiro, this.simbolloInicio, ij);
                        jg.pai = this;
                        js.add(jg);
                    }
                }
            }
            return js;
        }

        private int jogadorVenceu(){
            int soma1 = 0;        
            int soma0 = 0;        
            
            //Vencedor linha:
            for(int i = 0; i < this.tabuleiro.length; i++){
                soma1 = 0;
                soma0 = 0;
                for(int j = 0; j < this.tabuleiro.length; j++){
                    if(tabuleiro[i][j] == 1){
                        soma1++;
                    }
                    if(tabuleiro[i][j] == 0){
                        soma0++;
                    }
                }
                
                if(soma1 == this.tabuleiro.length)
                    return 1;
                if(soma0 == this.tabuleiro.length)
                    return 0;
            }
            
            
            //Vencedor coluna:
            for(int i = 0; i < this.tabuleiro.length; i++){
                soma1 = 0;
                soma0 = 0;
                for(int j = 0; j < this.tabuleiro.length; j++){
                    if(this.tabuleiro[j][i] == 1){
                        soma1++;
                    }
                    if(this.tabuleiro[j][i] == 0){
                        soma0++;
                    }
                }
                if(soma1 == this.tabuleiro.length)
                    return 1;
                if(soma0 == this.tabuleiro.length)
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
            if(soma1 == this.tabuleiro.length)
                return 1;
            if(soma0 == this.tabuleiro.length)
                return 0;
            
            //Diagonal principal
            soma1 = 0;
            soma0 = 0;
            for(int i = 0; i < this.tabuleiro.length; i++){
                if(this.tabuleiro[i][this.tabuleiro.length-i-1] == 1){
                    soma1++;
                }
                if(this.tabuleiro[i][this.tabuleiro.length-i-1] == 0){
                    soma0++;
                }
            }
            if(soma1 == this.tabuleiro.length)
                return 1;
            if(soma0 == this.tabuleiro.length)
                return 0; 

            int jogadas = 0;
            for(int i = 0; i <  this.tabuleiro.length; i++) {
                for(int j = 0; j < this.tabuleiro[i].length; j++) {
                    if (this.tabuleiro[i][j] != -1) {
                        jogadas++;
                    }
                }
            }

            if(jogadas == this.tabuleiro.length*this.tabuleiro.length) {
                return -1;
            } else {
                return 2;
            }
        }
    }
}