package br.ufrn.imd.model;

public class ContaBonus extends ContaBancaria {
    
    private int pontuacao;

    public ContaBonus(String numero) {
        super(numero);
        this.pontuacao = 10;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void adicionarPontos(int pontos) {
        this.pontuacao += pontos;
    }
}