import java.util.ArrayList;
import java.util.List;

public abstract class Conta {
    private String titular;
    private int numero;
    protected double saldo;
    private List<String> historico;// BÔNUS: Histórico de operações
    private double limiteSaque;
    private boolean bloqueada;


    public Conta(String titular, int numero, double limite) {
        this.titular = titular;
        this.numero = numero;
        this.limiteSaque = limite;
        this.saldo = 0.0;
        this.bloqueada = false;
        this.historico = new ArrayList<>();
        this.historico.add("Conta criada com sucesso!!! seu limite é de R$ "+ limite);
    }

    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
            this.historico.add("Depósito: + R$ " + String.format("%.2f", valor));
        }
    }

    public boolean sacar(double valor) {
        if (bloqueada) return false;

        if (valor > 0 && valor <= limiteSaque && this.saldo >= valor){
            this.saldo -= valor;
            this.historico.add("Saque: - R$ " + String.format("%.2f", valor));
            return true;
        }
        return false;
    }

    public void exibirExtrato() {
        System.out.println("\n--- HISTÓRICO DA CONTA: " + numero + " ---");
        for (String acao : historico) {
            System.out.println(acao);
        }
        System.out.println("SALDO ATUAL: R$ " + String.format("%.2f", saldo));
    }

    // Getters para encapsulamento
    public String getTitular() { return titular; }
    public int getNumero() { return numero; }
    public double getSaldo() { return saldo; }
    public List<String> getHistorico() {return historico;}
    @Override
    public String toString() {
        return "Conta: " + numero + " | Titular: " + titular + " | Saldo: R$ " + String.format("%.2f", saldo);
    }
}