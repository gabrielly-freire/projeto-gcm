package br.ufrn.imd.exception;

public class ContaNaoEncontradaException extends RuntimeException {

    public ContaNaoEncontradaException() {
        super("Nenhuma conta com esse número foi encontrada.");
    }
}
