public class ContaCorrente extends Conta {
    private double taxaManutencao = 2.00; // Taxa por saque

    public ContaCorrente(String titular, int numero) {
        super(titular, numero);
    }

    @Override
    public boolean sacar(double valor) {
        double valorComTaxa = valor + taxaManutencao;
        // Polimorfismo: utiliza a lógica de saque da classe mãe com o valor acrescido da taxa
        return super.sacar(valorComTaxa);
    }
}