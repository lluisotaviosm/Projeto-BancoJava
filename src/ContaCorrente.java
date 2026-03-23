public class ContaCorrente extends Conta implements Tributavel {
    public ContaCorrente(String titular, int numero) {
        super(titular, numero, 1000.0);
    }

    @Override
    public double calcularTributo() {
        return this.saldo * 0.1;
    }
}