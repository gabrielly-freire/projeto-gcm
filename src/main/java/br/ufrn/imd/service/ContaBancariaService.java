package br.ufrn.imd.service;

import br.ufrn.imd.exception.ContaBancariaNaoEncontradaException;
import br.ufrn.imd.exception.ContaJaCadastradaException;
import br.ufrn.imd.model.ContaBancaria;
import br.ufrn.imd.repository.ContaBancariaRepository;
import br.ufrn.imd.repository.ContaBancariaRepositoryImpl;

public class ContaBancariaService {

    private ContaBancariaRepository repository;

    public ContaBancariaService() {
        repository = ContaBancariaRepositoryImpl.getInstance();
    }

    public void cadastrarConta(String numero){
        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("Número da conta é obrigatório.");
        }

        if (repository.existsByNumero(numero)) {
            throw new ContaJaCadastradaException(numero);
        }

        ContaBancaria novaConta = new ContaBancaria(numero);
        novaConta.setSaldo(0.0);

        repository.save(novaConta);
    }

    public double consultarSaldo(String numeroConta){
        ContaBancaria conta = repository.findByNumero(numeroConta);

        if (conta == null) {
            throw new ContaBancariaNaoEncontradaException();
        }

        return conta.getSaldo();
    }

    public void debitar(String numeroConta, double valor){
        ContaBancaria conta = repository.findByNumero(numeroConta);
        if (conta == null){
            throw new ContaBancariaNaoEncontradaException();
        }
        conta.setSaldo(conta.getSaldo() - valor);
        repository.save(conta);
    }

    public void creditar(String numeroConta, double valor){
        ContaBancaria conta = repository.findByNumero(numeroConta);
        if (conta == null){
            throw new ContaBancariaNaoEncontradaException();
        }
        conta.setSaldo(conta.getSaldo() + valor);
        repository.save(conta);
    }

    public void transferir(String numeroContaOrigem, String numeroContaDestino, Double valor) {
        verificarValidadeValor(valor);

        ContaBancaria contaOrigem = verificarContaExistente(numeroContaOrigem);
        ContaBancaria contaDestino = verificarContaExistente(numeroContaDestino);

        contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
        contaDestino.setSaldo(contaDestino.getSaldo() + valor);
    }

    private void verificarValidadeValor(Double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
    }

    private ContaBancaria verificarContaExistente(String numeroConta) {
        ContaBancaria conta = repository.findByNumero(numeroConta);

        if (conta == null) {
            throw new ContaBancariaNaoEncontradaException();
        }

        return conta;
    }

}
