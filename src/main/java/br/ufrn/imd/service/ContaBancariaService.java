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

    public void creditar(String numeroConta, double valor){
        ContaBancaria conta = verificarContaExistente(numeroConta);

        verificarValidadeValor(valor);

        conta.setSaldo(conta.getSaldo() + valor);
        repository.save(conta);
    }

    public void transferir(String numeroContaOrigem, String numeroContaDestino, Double valor) {
        verificarValidadeValor(valor);

        ContaBancaria contaOrigem = verificarContaExistente(numeroContaOrigem);
        ContaBancaria contaDestino = verificarContaExistente(numeroContaDestino);

        verificarSaldoBancarioSuficiente(contaOrigem, valor);

        contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
        contaDestino.setSaldo(contaDestino.getSaldo() + valor);
    }

    private void verificarValidadeValor(Double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
    }

    private void verificarSaldoBancarioSuficiente(ContaBancaria contaBancaria, double valor) {
        if (valor > contaBancaria.getSaldo()) {
            throw new SaldoInsuficienteException();
        }
    }

    private ContaBancaria verificarContaExistente(String numeroConta) {
        ContaBancaria conta = repository.findByNumero(numeroConta);

        if (conta == null) {
            throw new ContaBancariaNaoEncontradaException();
        }

        return conta;
    }

}
