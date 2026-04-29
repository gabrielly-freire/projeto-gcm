package br.ufrn.imd.exception;

public class ContaBancariaNaoEncontradaException extends RuntimeException {

    public ContaBancariaNaoEncontradaException() {
        super("Nenhuma conta com esse número foi encontrada.");
    }
}
