package br.ufrn.imd.service;

import br.ufrn.imd.exception.ContaNaoEncontradaException;
import br.ufrn.imd.exception.ContaJaCadastradaException;
import br.ufrn.imd.model.ContaBancaria;
import br.ufrn.imd.model.ContaBonus;
import br.ufrn.imd.model.ContaPoupanca;
import br.ufrn.imd.repository.ContaBancariaRepository;

public class ContaBancariaService {

    private ContaBancariaRepository repository;
    private ContaBonusService bonusService = new ContaBonusService();

    public ContaBancariaService() {
        this.repository = ContaBancariaRepository.getInstance();
    }

    public void cadastrarConta(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("Número da conta é obrigatório.");
        }
        if (repository.existsByNumero(numero)) {
            throw new ContaJaCadastradaException(numero);
        }
        ContaBancaria novaConta = new ContaBancaria(numero);
        repository.save(novaConta);
    }

    public double consultarSaldo(String numeroConta) {
        return verificarContaExistente(numeroConta).getSaldo();
    }

    public void debitar(String numeroConta, double valor) {
        ContaBancaria conta = verificarContaExistente(numeroConta);
        if (valor > conta.getSaldo()) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
        conta.setSaldo(conta.getSaldo() - valor);
        repository.save(conta);
    }

    public void creditar(String numeroConta, double valor) {
        ContaBancaria conta = verificarContaExistente(numeroConta);
        conta.setSaldo(conta.getSaldo() + valor);
        repository.save(conta);
        bonusService.processarBonusDeposito(numeroConta, valor);
    }

    public void transferir(String origem, String destino, double valor) {
        if (valor <= 0) throw new IllegalArgumentException("Valor inválido.");
        
        ContaBancaria contaOrigem = verificarContaExistente(origem);
        ContaBancaria contaDestino = verificarContaExistente(destino);

        if (contaOrigem.getSaldo() < valor) throw new IllegalArgumentException("Saldo insuficiente.");

        contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
        contaDestino.setSaldo(contaDestino.getSaldo() + valor);

        repository.save(contaOrigem);
        repository.save(contaDestino);
        bonusService.processarBonusTransferencia(destino, valor);
    }

    public boolean isContaPoupanca(String numeroConta) {
        ContaBancaria conta = repository.findByNumero(numeroConta);
        return conta instanceof ContaPoupanca;
    }

    public boolean isContaBonus(String numeroConta) {
        ContaBancaria conta = repository.findByNumero(numeroConta);
        return conta instanceof ContaBonus;
    }

    public int getPontuacao(String numeroConta) {
        ContaBancaria conta = repository.findByNumero(numeroConta);
        if (conta instanceof ContaBonus) {
            return ((ContaBonus) conta).getPontuacao();
        }
        throw new IllegalArgumentException("A conta não é do tipo Bonus.");
    }

    private ContaBancaria verificarContaExistente(String numeroConta) {
        ContaBancaria conta = repository.findByNumero(numeroConta);
        if (conta == null) throw new ContaNaoEncontradaException();
        return conta;
    }
}