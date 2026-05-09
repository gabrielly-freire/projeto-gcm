package br.ufrn.imd.model;

public class ContaPoupanca extends ContaBancaria {
    
    public ContaPoupanca(String numero) {
        super(numero);
    }
    
    public void renderJuros(double taxa) {
        double juros = getSaldo() * taxa;
        setSaldo(getSaldo() + juros);
    }
}
