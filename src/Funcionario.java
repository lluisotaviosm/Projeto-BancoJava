public class Funcionario implements Autenticavel{
    private String nome;
    private String senha;

    public Funcionario(String nome, String senha) {
        this.nome = nome;
        this.senha = senha;
    }
    public boolean autenticar(String senhaInformada){
        return this.senha.equals(senhaInformada);
    }
    public String getNome(){return nome; }
}
