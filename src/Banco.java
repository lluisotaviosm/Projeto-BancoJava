import java.util.ArrayList;
import java.io.*;
public class Banco {
    private ArrayList<Conta> contas = new ArrayList<>();

    public void adicionarConta(Conta c) {
        contas.add(c);
    }

    public Conta buscarConta(int numero) {
        for (Conta c : contas) {
            if (c.getNumero() == numero) {
                return c;
            }
        }
        return null;
    }
    public void salvarDados() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("contas.txt"))) {
            for (Conta c : contas) {
                writer.println(c.getNumero() + ";" + c.getTitular() + ";" + c.getSaldo());
            }
            System.out.println("Dados salvos em contas.txt");
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados.");
        }
    }
    public ArrayList<Conta> getTodas() {
        return contas;
    }
}