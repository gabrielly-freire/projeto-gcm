package br.ufrn.imd.repository;

import br.ufrn.imd.model.ContaBancaria;

import java.util.Set;

public interface ContaBancariaRepository {

    Set<ContaBancaria> findAll();
    ContaBancaria findByNumero(String numero);
    boolean existsByNumero(String numero);
    void save(ContaBancaria conta);
}
