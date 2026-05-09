package br.ufrn.imd.service;

import br.ufrn.imd.exception.ContaNaoEncontradaException;
import br.ufrn.imd.exception.ContaJaCadastradaException;
import br.ufrn.imd.model.ContaBancaria;
import br.ufrn.imd.model.ContaPoupanca;
import br.ufrn.imd.repository.ContaBancariaRepository;

public class ContaPoupancaService {
    
    private ContaBancariaRepository repository = ContaBancariaRepository.getInstance();

    public void cadastrarContaPoupanca(String numero) {
        if (repository.existsByNumero(numero)) {
            throw new ContaJaCadastradaException(numero);
        }
        ContaPoupanca novaPoupanca = new ContaPoupanca(numero);
        repository.save(novaPoupanca);
    }

    public void renderJuros(String numeroConta, double taxa) {
        ContaBancaria conta = repository.findByNumero(numeroConta);
        
        if (conta == null) throw new ContaNaoEncontradaException();
        
        if (conta instanceof ContaPoupanca) {
            ((ContaPoupanca) conta).renderJuros(taxa);
            repository.save(conta); 
        } else {
            throw new IllegalArgumentException("A conta não é do tipo poupança.");
        }
    }

    // Métodos de conveniência que chamam o repositório compartilhado
    public double consultarSaldo(String numero) {
        ContaBancaria conta = repository.findByNumero(numero);
        if (conta == null) throw new ContaNaoEncontradaException();
        return conta.getSaldo();
    }
}