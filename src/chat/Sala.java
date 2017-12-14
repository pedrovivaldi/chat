package chat;



import java.io.Serializable;

/**
 * @author Caio CÃ©sar
 */
public class Sala implements Serializable {

    private String nome;
    private String senha;
    private int qtdeMax;
    private String estadoAtual;
    private Usuarios usuarios;

    public Sala(String nome, int qtdeMax, String senha) {
        this.nome = nome;
        this.senha = senha;
        this.qtdeMax = qtdeMax;
        this.estadoAtual = "Normal";
        usuarios = new Usuarios();
    }

    public Sala(String nome, int qtdeMax) {
        this.nome = nome;
        this.senha = "";
        this.qtdeMax = qtdeMax;
        this.estadoAtual = "Normal";
        usuarios = new Usuarios();
    }

    public String getEstadoAtual() {
        return estadoAtual;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQtdeMax() {
        return qtdeMax;
    }

    public void setQtdeMax(int qtdeMax) {
        this.qtdeMax = qtdeMax;
    }
   
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    public int getQtdeOcupados() {
        return usuarios.qntosUsuarios();
    }

    public void addUsuario(Usuario user){
        if (usuarios.qntosUsuarios() < qtdeMax) {
            this.usuarios.addUsuario(user);
        }
        if (usuarios.qntosUsuarios() == qtdeMax){
            estadoAtual = "cheia";
        }
    }
    
    public void removeUsuario(Usuario user){
        usuarios.removeUsuario(user);
    }
    
}
