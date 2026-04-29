package br.ufrn.imd.model;

public class ContaBancaria {

    private String numero;
    private Double saldo;

    public ContaBancaria(String numero) {
        this.numero = numero;
        this.saldo = 0.0;
    }

    public String getNumero() {
        return numero;
    }

    public ContaBancaria setNumero(String numero) {
        this.numero = numero;
        return this;
    }

    public Double getSaldo() {
        return saldo;
    }

    public ContaBancaria setSaldo(Double saldo) {
        this.saldo = saldo;
        return this;
    }
}
