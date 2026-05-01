package br.ufrn.imd.ui;

import br.ufrn.imd.exception.ContaBancariaNaoEncontradaException;
import br.ufrn.imd.exception.NumeroContaIndisponivel;
import br.ufrn.imd.service.ContaBancariaService;

import java.util.Scanner;

public class ContaBancariaUI {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ContaBancariaService contaBancariaService = new ContaBancariaService();

    public static void main(String[] args) {
        int opcao;

        do {
            printMenu();
            opcao = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (opcao) {
                    case 1:
                        sacar();
                        break;

                    case 2:
                        depositar();
                        break;

                    case 3:
                        consultarSaldo();
                        break;

                    case 4:
                        transferir();
                        break;

                    case 5:
                        criarConta();
                        break;

                    case 0:
                        System.out.println("Saindo...");
                        break;

                    default:
                        System.out.println("Opção inválida!");
                }

            } catch (ContaBancariaNaoEncontradaException e) {
                System.out.println("Conta bancária não encontrada.");
            } catch (NumeroContaIndisponivel e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }

        } while (opcao != 0);
    }

    private static void sacar() {
        System.out.println("\n=== SACAR ===");

        System.out.print("Número da conta: ");
        String numeroConta = scanner.nextLine();

        System.out.print("Valor do saque: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        contaBancariaService.debitar(numeroConta, valor);

        System.out.println("Saque realizado com sucesso!");
    }

    private static void depositar() {
        System.out.println("\n=== DEPOSITAR ===");

        System.out.print("Número da conta: ");
        String numeroConta = scanner.nextLine();

        System.out.print("Valor do depósito: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        contaBancariaService.creditar(numeroConta, valor);

        System.out.println("Depósito realizado com sucesso!");
    }

    private static void consultarSaldo() {

    }

    private static void transferir() {
        System.out.println("\n=== TRANSFERÊNCIA BANCÁRIA ===");

        System.out.print("Conta origem: ");
        String contaOrigem = scanner.nextLine();

        System.out.print("Conta destino: ");
        String contaDestino = scanner.nextLine();

        System.out.print("Valor da transferência: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        contaBancariaService.transferir(contaOrigem, contaDestino, valor);

        System.out.println("Transferência realizada com sucesso!");
    }

    private static void criarConta() {

    }

    public static void printMenu() {
        System.out.println("\n============================");
        System.out.println("      SISTEMA BANCÁRIO");
        System.out.println("============================");
        System.out.println("1. Sacar");
        System.out.println("2. Depositar");
        System.out.println("3. Consultar saldo");
        System.out.println("4. Transferir");
        System.out.println("5. Criar conta");
        System.out.println("0. Sair");
        System.out.print("\nDigite uma opção: ");
    }
}