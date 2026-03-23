import java.util.Scanner;

public class Main {
    private static Banco banco = new Banco();
    private static Scanner ler = new Scanner(System.in);
    private static Gerente gerenteChefe = new Gerente("Admin", "1313");

    public static void main(String[] args) {
        int opcao = -1;
        while (opcao != 0) {
            exibirMenu();
            opcao = ler.nextInt();
            ler.nextLine(); // Limpeza de buffer para não pular o nome

            switch (opcao) {
                case 1 -> criarConta();
                case 2 -> listarContas();
                case 3 -> depositar();
                case 4 -> sacar();
                case 5 -> transferir();
                case 6 -> consultarSaldo();
                case 7 -> impostos();
                case 8 -> login();
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
        System.out.println("7 - Tributos");
        System.out.println("8 - Auth Gerente");
        System.out.println("0 - Sair");
        System.out.print("\nEscolha uma opção: ");
    }

    private static void criarConta() {
        System.out.print("Nome do titular: ");
        String nome = ler.nextLine();
        System.out.print("Número da conta: ");
        int num = ler.nextInt();
        System.out.println("Tipo de conta:\n1 - Corrente\n2 - Poupança");
        int tipo = ler.nextInt();

        if (tipo == 1) banco.adicionarConta(new ContaCorrente(nome, num));
        else banco.adicionarConta(new ContaPoupanca(nome, num));
        System.out.println("Conta criada com sucesso.");
    }

    private static void listarContas() {
        for  (Conta c : banco.getTodas()){
            System.out.println(c.getNumero()+" - " + c.getTitular());
        }
    }

    private static void depositar() {
        System.out.print("Número da conta: ");
        Conta c = banco.buscarConta(ler.nextInt());
        if (c != null) {
            System.out.print("Valor do depósito: ");
            c.depositar(ler.nextDouble());
            System.out.println("Depósito realizado.");
        } else System.out.println("Conta não encontrada.");
    }

    private static void sacar() {
        System.out.print("Número da conta: ");
        Conta c = banco.buscarConta(ler.nextInt());
        if (c != null) {
            System.out.print("Valor do saque: ");
            if (c.sacar(ler.nextDouble())) System.out.println("Saque autorizado.");
            else System.out.println("Erro: Saldo insuficiente.");
        } else System.out.println("Conta não encontrada.");
    }

    private static void transferir() {
        System.out.print("Nº conta origem: ");
        Conta origem = banco.buscarConta(ler.nextInt());
        System.out.print("Nº conta destino: ");
        Conta destino = banco.buscarConta(ler.nextInt());

        if (origem != null && destino != null) {
            System.out.print("Valor da transferência: ");
            double v = ler.nextDouble();
            if (origem.sacar(v)) {
                destino.depositar(v);
                System.out.println("Transferência OK!");
            } else System.out.println("Erro: Saldo insuficiente na origem.");
        } else System.out.println("Uma das contas não existe.");
    }

    private static void consultarSaldo() {
        System.out.print("Número da conta: ");
        Conta c = banco.buscarConta(ler.nextInt());
        if (c != null){
            System.out.println("saldo: " + c.getSaldo());
            System.out.println("historico: "+ c.getHistorico());
    }else System.out.println("Conta nao encontrada");
    }

    private static void impostos() {
     CalculadoraDeImposto calc = new CalculadoraDeImposto();
     for(Conta c : banco.getTodas()) {
         if (c instanceof Tributavel) {
             calc.registrar((Tributavel) c);
         }
     }
        System.out.println("total de tributos do banco: R$ "+ calc.getTotalImposto());
    }
    private static void login(){
        System.out.println("Senha do Gerente: ");
        if(gerenteChefe.autenticar(ler.nextLine())){
            System.out.println("Gerente"+ gerenteChefe.getNome()+ " autenticado");
        }else{
            System.out.println("Senha invalida");
        }
    }
}