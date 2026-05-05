package br.ufrn.imd.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ContaBancaria that = (ContaBancaria) o;
        return Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numero);
    }

    @Override
    public String toString() {
        return "ContaBancaria{" +
                "numero='" + numero + '\'' +
                ", saldo=" + saldo +
                '}';
    }
}
