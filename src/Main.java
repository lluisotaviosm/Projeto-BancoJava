import java.util.Scanner;

public class Main {
    private static Banco banco = new Banco();
    private static Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao = -1;

        while (opcao != 0) {
            exibirMenu();
            opcao = entrada.nextInt();
            entrada.nextLine(); // Limpeza de buffer para não pular o nome

            switch (opcao) {
                case 1 -> criarConta();
                case 2 -> listarContas();
                case 3 -> depositar();
                case 4 -> sacar();
                case 5 -> transferir();
                case 6 -> consultarSaldo();
                case 7 -> excluirConta(); // BÔNUS
                case 0 -> System.out.println("Sistema encerrado.");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("\n===== BANCO JAVA =====");
        System.out.println("1 - Criar conta");
        System.out.println("2 - Listar contas");
        System.out.println("3 - Depositar");
        System.out.println("4 - Sacar");
        System.out.println("5 - Transferir");
        System.out.println("6 - Consultar saldo / Extrato");
        System.out.println("7 - Excluir conta");
        System.out.println("0 - Sair");
        System.out.print("\nEscolha uma opção: ");
    }

    private static void criarConta() {
        System.out.print("Nome do titular: ");
        String nome = entrada.nextLine();
        System.out.print("Número da conta: ");
        int num = entrada.nextInt();
        System.out.println("Tipo de conta:\n1 - Corrente\n2 - Poupança");
        int tipo = entrada.nextInt();

        if (tipo == 1) banco.adicionarConta(new ContaCorrente(nome, num));
        else banco.adicionarConta(new ContaPoupanca(nome, num));

        System.out.println("Conta criada com sucesso.");
    }

    private static void listarContas() {
        if (banco.listarContas().isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
        } else {
            for (Conta c : banco.listarContas()) System.out.println(c);
        }
    }

    private static void depositar() {
        System.out.print("Número da conta: ");
        Conta c = banco.buscarConta(entrada.nextInt());
        if (c != null) {
            System.out.print("Valor do depósito: ");
            c.depositar(entrada.nextDouble());
            System.out.println("Depósito realizado.");
        } else System.out.println("Conta não encontrada.");
    }

    private static void sacar() {
        System.out.print("Número da conta: ");
        Conta c = banco.buscarConta(entrada.nextInt());
        if (c != null) {
            System.out.print("Valor do saque: ");
            if (c.sacar(entrada.nextDouble())) System.out.println("Saque autorizado.");
            else System.out.println("Erro: Saldo insuficiente.");
        } else System.out.println("Conta não encontrada.");
    }

    private static void transferir() {
        System.out.print("Nº conta origem: ");
        Conta origem = banco.buscarConta(entrada.nextInt());
        System.out.print("Nº conta destino: ");
        Conta destino = banco.buscarConta(entrada.nextInt());

        if (origem != null && destino != null) {
            System.out.print("Valor da transferência: ");
            double v = entrada.nextDouble();
            if (origem.sacar(v)) {
                destino.depositar(v);
                System.out.println("Transferência OK!");
            } else System.out.println("Erro: Saldo insuficiente na origem.");
        } else System.out.println("Uma das contas não existe.");
    }

    private static void consultarSaldo() {
        System.out.print("Número da conta: ");
        Conta c = banco.buscarConta(entrada.nextInt());
        if (c != null) c.exibirExtrato();
        else System.out.println("Conta não encontrada.");
    }

    private static void excluirConta() {
        System.out.print("Nº da conta para excluir: ");
        if (banco.removerConta(entrada.nextInt())) System.out.println("Conta removida.");
        else System.out.println("Erro: Conta não existe.");
    }
}