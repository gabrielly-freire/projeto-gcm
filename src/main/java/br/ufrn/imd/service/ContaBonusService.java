package br.ufrn.imd.service;

import br.ufrn.imd.model.ContaBancaria;
import br.ufrn.imd.model.ContaBonus;
import br.ufrn.imd.repository.ContaBancariaRepository;

public class ContaBonusService {
    
    private ContaBancariaRepository repository = ContaBancariaRepository.getInstance();

    public void cadastrarContaBonus(String numero) {
        ContaBonus novaConta = new ContaBonus(numero);
        repository.save(novaConta);
    }

    public void processarBonusDeposito(String numero, double valor) {
        ContaBancaria conta = repository.findByNumero(numero);
        if (conta instanceof ContaBonus) {
            int pontosGanhos = (int) (valor / 100);
            ((ContaBonus) conta).adicionarPontos(pontosGanhos);
            repository.save(conta);
        }
    }

    public void processarBonusTransferencia(String numero, double valor) {
        ContaBancaria conta = repository.findByNumero(numero);
        if (conta instanceof ContaBonus) {
            int pontosGanhos = (int) (valor / 200);
            ((ContaBonus) conta).adicionarPontos(pontosGanhos);
            repository.save(conta);
        }
    }
}