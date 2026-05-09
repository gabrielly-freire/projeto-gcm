package br.ufrn.imd.repository;

import br.ufrn.imd.model.ContaBancaria;

import java.util.HashSet;
import java.util.Set;

public class ContaBancariaRepository implements ContaRepository<ContaBancaria> {

    private static ContaBancariaRepository instance;

    private ContaBancariaRepository() {}

    public static ContaBancariaRepository getInstance() {
        if (instance == null) {
            instance = new ContaBancariaRepository();
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
        if (contas.contains(conta)) {
        contas.remove(conta);
    }
        contas.add(conta);
    }

    public boolean existsByNumero(String numero) {
        return contas.stream().anyMatch(c -> c.getNumero().equals(numero));
    }
}
