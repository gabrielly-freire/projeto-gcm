package br.ufrn.imd.model;

public class ContaBonus extends ContaBancaria {

    private static final int PONTUACAO_INICIAL = 10;
    private static final int DIVISOR_PONTOS_CREDITO = 150;
    private static final int DIVISOR_PONTOS_TRANSFERENCIA = 200;

    private int pontuacao;

    public ContaBonus(String numero) {
        super(numero);
        this.pontuacao = PONTUACAO_INICIAL;
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
        adicionarPontos((int) (valor / DIVISOR_PONTOS_CREDITO));
    }

    @Override
    public void transferirPara(ContaBancaria destino, double valor) {
        this.setSaldo(this.getSaldo() - valor);
        destino.setSaldo(destino.getSaldo() + valor);

        adicionarPontos((int) (valor / DIVISOR_PONTOS_TRANSFERENCIA));
    }
}
