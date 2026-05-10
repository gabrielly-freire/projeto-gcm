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
        if (pontos <= 0) {
            return;
        }
        this.pontuacao += pontos;
    }

    @Override
    public void creditar(double valor) {
        super.creditar(valor);
        adicionarPontos((int) (valor / 100));
    }

    @Override
    public void transferirPara(ContaBancaria destino, double valor) {
        super.transferirPara(destino, valor);
        adicionarPontos((int) (valor / 200));
    }
}
