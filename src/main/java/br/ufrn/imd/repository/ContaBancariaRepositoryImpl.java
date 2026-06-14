package br.ufrn.imd.repository;

import br.ufrn.imd.model.ContaBancaria;

import java.util.HashSet;
import java.util.Set;

public class ContaBancariaRepositoryImpl implements ContaBancariaRepository {

    private static ContaBancariaRepositoryImpl instance;

    private ContaBancariaRepositoryImpl() {}

    public static ContaBancariaRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new ContaBancariaRepositoryImpl();
        }
        return instance;
    }

    private Set<ContaBancaria> contas = new HashSet<>(Set.of(
            new ContaBancaria("123"),
            new ContaBancaria("456"),
            new ContaBancaria("678")
    ));

    public Set<ContaBancaria> findAll() {
        return contas;
    }

    public ContaBancaria findByNumero(String numero) {
        for (ContaBancaria conta : contas) {
            if (conta.getNumero().equals(numero)) {
                return conta;
            }
        }
        return null;
    }

    public ContaBancaria save(ContaBancaria conta) {
        if (contas.contains(conta)) {
        contas.remove(conta);
    }
        contas.add(conta);
        return conta;
    }

    public boolean existsByNumero(String numero) {
        return contas.stream().anyMatch(c -> c.getNumero().equals(numero));
    }
}
