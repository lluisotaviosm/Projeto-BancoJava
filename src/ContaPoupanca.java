public class ContaPoupanca extends Conta {
    private double taxaRendimento = 0.005; // 0.5% de rendimento

    public ContaPoupanca(String titular, int numero) {
        super(titular, numero);
    }

    public void aplicarRendimento() {
        double rendimento = this.getSaldo() * taxaRendimento;
        this.depositar(rendimento);
    }
}