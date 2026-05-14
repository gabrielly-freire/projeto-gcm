package br.ufrn.imd.service;

import br.ufrn.imd.exception.ContaBancariaNaoEncontradaException;
import br.ufrn.imd.exception.ContaJaCadastradaException;
import br.ufrn.imd.exception.SaldoInsuficienteException;
import br.ufrn.imd.model.ContaBancaria;
import br.ufrn.imd.model.ContaBonus;
import br.ufrn.imd.model.ContaPoupanca;
import br.ufrn.imd.repository.ContaBancariaRepository;
import br.ufrn.imd.repository.ContaBancariaRepositoryImpl;

public class ContaBancariaService {

    private ContaBancariaRepository repository;

    public ContaBancariaService() {
        this.repository = ContaBancariaRepositoryImpl.getInstance();
    }

    public void cadastrarConta(String numero, double saldo, int tipo) {
        String numeroNormalizado = normalizarNumeroConta(numero);
        
        if (repository.existsByNumero(numeroNormalizado)) {
            throw new ContaJaCadastradaException(numeroNormalizado);
        }

        ContaBancaria novaConta;
        if (tipo == 1) {
            novaConta = new ContaBancaria(numeroNormalizado);
        } else if (tipo == 2) {
            novaConta = new ContaPoupanca(numeroNormalizado, saldo);
        } else if (tipo == 3) {
            novaConta = new ContaBonus(numeroNormalizado);
        } else {
            throw new IllegalArgumentException("Tipo de conta inválido.");
        }

        repository.save(novaConta);
    }

    public double consultarSaldo(String numeroConta) {
        return verificarContaExistente(numeroConta).getSaldo();
    }

    public void debitar(String numeroConta, double valor) {
        ContaBancaria conta = verificarContaExistente(numeroConta);

        verificarValidadeValor(valor);
        verificarSaldoBancarioSuficiente(conta, valor);

        conta.debitar(valor);
        repository.save(conta);
    }

    public void creditar(String numeroConta, double valor) {
        ContaBancaria conta = verificarContaExistente(numeroConta);

        verificarValidadeValor(valor);

        conta.creditar(valor);
        repository.save(conta);
    }

    public void transferir(String numeroContaOrigem, String numeroContaDestino, Double valor) {
        verificarValidadeValor(valor);

        if (numeroContaOrigem.equals(numeroContaDestino)) {
            throw new IllegalArgumentException("Conta de origem e destino não podem ser a mesma.");
        }

        ContaBancaria contaOrigem = verificarContaExistente(numeroContaOrigem);
        ContaBancaria contaDestino = verificarContaExistente(numeroContaDestino);

        verificarSaldoBancarioSuficiente(contaOrigem, valor);

        contaOrigem.transferirPara(contaDestino, valor);

        repository.save(contaOrigem);
        repository.save(contaDestino);
    }

    public void renderJuros(String numeroConta, double taxa) {
        ContaBancaria conta = verificarContaExistente(numeroConta);

        if (conta instanceof ContaPoupanca) {
            ((ContaPoupanca) conta).renderJuros(taxa);
            repository.save(conta);
        } else {
            throw new IllegalArgumentException("A conta não é do tipo poupança.");
        }
    }

    public int getPontuacao(String numeroConta) {
        ContaBancaria conta = verificarContaExistente(numeroConta);
        if (!(conta instanceof ContaBonus bonus)) {
            throw new IllegalArgumentException("A conta não é do tipo bônus.");
        }
        return bonus.getPontuacao();
    }

    public boolean isContaPoupanca(String numeroConta) {
        return verificarContaExistente(numeroConta) instanceof ContaPoupanca;
    }

    public boolean isContaBonus(String numeroConta) {
        return verificarContaExistente(numeroConta) instanceof ContaBonus;
    }

    public boolean isContaBancaria(String numeroConta) {
        ContaBancaria conta = verificarContaExistente(numeroConta);
        return conta.getClass().equals(ContaBancaria.class);
    }

    private void verificarValidadeValor(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
    }

    private void verificarSaldoBancarioSuficiente(ContaBancaria contaBancaria, double valor) {
        double saldoAposOperacao = contaBancaria.getSaldo() - valor;
        if (saldoAposOperacao < saldoMinimoPermitido(contaBancaria)) {
            throw new SaldoInsuficienteException();
        }
    }

    private double saldoMinimoPermitido(ContaBancaria contaBancaria) {
        if (contaBancaria instanceof ContaPoupanca) {
            return 0.0;
        }
        if (contaBancaria instanceof ContaBonus || contaBancaria.getClass().equals(ContaBancaria.class)) {
            return -1000.0;
        }
        return 0.0;
    }

    private ContaBancaria verificarContaExistente(String numeroConta) {
        ContaBancaria conta = repository.findByNumero(numeroConta);

        if (conta == null) {
            throw new ContaBancariaNaoEncontradaException();
        }

        return conta;
    }

    private String normalizarNumeroConta(String numeroConta) {
        if (numeroConta == null || numeroConta.trim().isEmpty()) {
            throw new IllegalArgumentException("Número da conta é obrigatório.");
        }
        return numeroConta.trim();
    }

}
