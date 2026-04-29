package br.ufrn.imd.service;

import br.ufrn.imd.repository.ContaBancariaRepository;
import br.ufrn.imd.repository.ContaBancariaRepositoryImpl;

public class ContaBancariaService {

    ContaBancariaRepository repository;

    public ContaBancariaService() {
        repository = ContaBancariaRepositoryImpl.getInstance();
    }
}
