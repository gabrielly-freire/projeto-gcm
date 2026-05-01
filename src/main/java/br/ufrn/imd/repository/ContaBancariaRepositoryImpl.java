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
            new ContaBancaria("123456789"),
            new ContaBancaria("987654321"),
            new ContaBancaria("111111111")
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

    public void save(ContaBancaria conta) {
        contas.add(conta);
    }

    public boolean existsByNumero(String numero) {
        return contas.stream().anyMatch(c -> c.getNumero().equals(numero));
    }
}
