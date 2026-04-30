package br.ufrn.imd.service;

import br.ufrn.imd.exception.ContaBancariaNaoEncontradaException;
import br.ufrn.imd.model.ContaBancaria;
import br.ufrn.imd.repository.ContaBancariaRepository;
import br.ufrn.imd.repository.ContaBancariaRepositoryImpl;

public class ContaBancariaService {

    ContaBancariaRepository repository;

    public ContaBancariaService() {
        repository = ContaBancariaRepositoryImpl.getInstance();
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
}
