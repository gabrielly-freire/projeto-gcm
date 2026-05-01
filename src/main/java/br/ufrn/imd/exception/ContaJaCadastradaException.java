package br.ufrn.imd.exception;

public class ContaJaCadastradaException extends RuntimeException {
    public ContaJaCadastradaException(String numero) {
        super("Já existe uma conta cadastrada com o número: " + numero + ".");
    }
    
}
