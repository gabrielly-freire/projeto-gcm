package br.ufrn.imd.exception;

public class SaldoInsuficienteException extends RuntimeException {

    public SaldoInsuficienteException() {
        super("O saldo da sua conta é insuficiente para realizar a transação.");
    }
}
