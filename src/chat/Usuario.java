package chat;



import java.io.*;

/**
 * @author Caio CÃ©sar
 */
public class Usuario implements Serializable {

    private String nick;
    private String login;
    private String senha;
    private String tipo;
    private byte[] imagem;

    public Usuario(String login, String nick, String senha, String tipo) {
        this.nick = nick;
        this.senha = senha;
        this.login = login;
        this.tipo = tipo;
        this.imagem = null;
    }
    
    public Usuario(String login, String senha){
        this.login = login;
        this.senha = senha;
        this.nick = "";
        this.imagem = null;
    }
    
    public Usuario(String login, String nick, String senha){
        this.login = login;
        this.senha = senha;
        this.nick = nick;
        this.imagem = null;
    }
    
    public Usuario(String login, String nick, String senha, byte[] img) {
        this.login = login;
        this.senha = senha;
        this.tipo = "";
        this.nick = nick;
        this.imagem = img;
    }

    public Usuario(String login, String nick, String senha, String tipo,  byte[] img) {
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
        this.nick = nick;
        this.imagem = img;
    }

    public Usuario(String login) {
        this.login = login;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }
}
