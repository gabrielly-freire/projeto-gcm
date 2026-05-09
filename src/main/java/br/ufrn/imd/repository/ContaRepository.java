package br.ufrn.imd.repository;
import java.util.Set;

public interface ContaRepository<T> {

    Set<T> findAll();
    T findByNumero(String numero);
    boolean existsByNumero(String numero);
    void save(T conta);
}
