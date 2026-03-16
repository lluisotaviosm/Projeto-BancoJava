import java.util.ArrayList;

public class Banco {
    // Requisito: Lista de objetos Conta
    private ArrayList<Conta> listaContas = new ArrayList<>();

    public void adicionarConta(Conta c) {
        listaContas.add(c);
    }

    public boolean removerConta(int numero) {
        // BÔNUS: Exclusão de conta
        Conta c = buscarConta(numero);
        if (c != null) {
            listaContas.remove(c);
            return true;
        }
        return false;
    }

    public Conta buscarConta(int numero) {
        for (Conta c : listaContas) {
            if (c.getNumero() == numero) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<Conta> listarContas() {
        return listaContas;
    }
}