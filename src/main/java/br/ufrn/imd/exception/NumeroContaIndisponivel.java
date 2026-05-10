package br.ufrn.imd.exception;

public class NumeroContaIndisponivel extends RuntimeException{

    public NumeroContaIndisponivel(String message) {
        super("Número da conta indisponível: " + message + ".");
    }
}
