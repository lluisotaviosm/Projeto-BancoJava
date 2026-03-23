public class Funcionario implements Autenticavel{
    private String nome;
    private String senha;
    private int tentativas = 0;
    private boolean bloqueado = false;

    public Funcionario(String nome, String senha) {
        this.nome = nome;
        this.senha = senha;
    }
    public boolean autenticar(String senhaInformada){
        if (bloqueado) {
            System.out.println("ACESSO NEGADO: Usuário bloqueado.");
            return false;
        }
        if (this.senha.equals(senhaInformada)) {
            tentativas = 0;
            return true;
        } else {
            tentativas++;
            if (tentativas >= 3) bloqueado = true;
            return false;
        }
    }
    public String getNome(){return nome; }
}
