package br.ufrn.imd.ui;

import br.ufrn.imd.exception.ContaBancariaNaoEncontradaException;
import br.ufrn.imd.exception.ContaJaCadastradaException;
import br.ufrn.imd.exception.SaldoInsuficienteException;
import br.ufrn.imd.service.ContaBancariaService;

import java.util.Scanner;

public class ContaBancariaUI {

    private static Scanner scanner = new Scanner(System.in);
    private static ContaBancariaService bancarioService = new ContaBancariaService();

    public static void main(String[] args) {
        int opcao;

        do {
            printMenu();
            opcao = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (opcao) {
                    case 1 -> sacar();
                    case 2 -> depositar();
                    case 3 -> consultarSaldo();
                    case 4 -> transferir();
                    case 5 -> renderJuros();
                    case 6 -> criarConta();
                    case 0 -> System.out.println("Saindo do sistema...");
                    default -> System.out.println("Opção inválida! Tente novamente");
                }
            } catch (ContaBancariaNaoEncontradaException e) {
                System.out.println("Erro: Conta não encontrada no sistema.");
            } catch (SaldoInsuficienteException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (ContaJaCadastradaException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Erro de validação: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro inesperado. Tente novamente mais tarde");
            }

        } while (opcao != 0);
    }

    private static void sacar() {
        System.out.println("\n=== SACAR ===");
        System.out.print("Número da conta: ");
        String numero = scanner.nextLine();
        System.out.print("Valor do saque: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        bancarioService.debitar(numero, valor);
        System.out.println("Saque realizado com sucesso!");
    }

    private static void depositar() {
        System.out.println("\n=== DEPOSITAR ===");
        System.out.print("Número da conta: ");
        String numero = scanner.nextLine();
        System.out.print("Valor do depósito: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        bancarioService.creditar(numero, valor);
        System.out.println("Depósito realizado com sucesso!");
    }

    private static void consultarSaldo() {
        System.out.println("\n=== CONSULTAR SALDO ===");
        System.out.print("Número da conta: ");
        String numero = scanner.nextLine();

        double saldo = bancarioService.consultarSaldo(numero);
        System.out.println("Saldo atual: R$ " + saldo);

        if (bancarioService.isContaBonus(numero)) {
        int pontos = bancarioService.getPontuacao(numero); 
        System.out.println("Pontuação atual: " + pontos + " pontos");
        }
    }

    private static void transferir() {
        System.out.println("\n=== TRANSFERÊNCIA BANCÁRIA ===");
        System.out.print("Conta de Origem: ");
        String origem = scanner.nextLine();
        System.out.print("Conta de Destino: ");
        String destino = scanner.nextLine();
        System.out.print("Valor da transferência: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        bancarioService.transferir(origem, destino, valor);
        System.out.println("Transferência realizada com sucesso!");
    }

    private static void criarConta() {
        System.out.println("\n=== CRIAR CONTA ===");
        System.out.print("Número da nova conta: ");
        String numero = scanner.nextLine();
        
        System.out.print("Tipo da conta (1 - Bancaria, 2 - Poupança, 3 - Bonus): ");
        int tipo = scanner.nextInt();
        scanner.nextLine();

        double saldo = 0.0;
        if (tipo == 2) {
            System.out.print("Saldo inicial: ");
            saldo = scanner.nextDouble();
            scanner.nextLine();
        }

        bancarioService.cadastrarConta(numero, saldo, tipo);
        if (tipo == 1) {
            System.out.println("Conta Corrente criada com sucesso!");
        } else if (tipo == 2) {
            System.out.println("Conta Poupança criada com sucesso!");
        } else if (tipo == 3) {
            System.out.println("Conta Bônus criada com sucesso!");
        } else {
            System.out.println("Tipo de conta inválido. Operação cancelada.");
        }
    }

    private static void renderJuros() {
        System.out.println("\n=== RENDER JUROS (POUPANÇA) ===");
        System.out.print("Número da conta: ");
        String numero = scanner.nextLine();

        if (bancarioService.isContaPoupanca(numero)) {
            System.out.print("Taxa de juros (ex: 0.01 para 1%): ");
            double taxa = scanner.nextDouble();
            scanner.nextLine();

            bancarioService.renderJuros(numero, taxa);
            System.out.println("Rendimento aplicado com sucesso!");
        } else {
            System.out.println("Operação negada: Esta conta não é do tipo Poupança.");
        }
    }

    private static void printMenu() {
        System.out.println("\n============================");
        System.out.println("      SISTEMA BANCÁRIO");
        System.out.println("============================");
        System.out.println("1. Sacar");
        System.out.println("2. Depositar");
        System.out.println("3. Consultar Saldo");
        System.out.println("4. Transferir");
        System.out.println("5. Render Juros (Poupança)");
        System.out.println("6. Criar Conta");
        System.out.println("0. Sair");
        System.out.print("\nDigite uma opção: ");
    }
}
