public class CalculadoraDeImposto {
    private double totalImposto;

    public void registrar(Tributavel t){
        this.totalImposto += t.calcularTributo();
    }
    public double getTotalImposto() {
        return totalImposto;
    }
}
