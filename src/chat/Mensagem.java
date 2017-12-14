package chat;



import java.io.Serializable;

/**
 * @author Caio CÃ©sar
 */
public class  Mensagem implements Serializable{
    private Object conteudo;
    private Object conteudo2;
    private int tipo;
    public static final int ERRO = 0, LOGAR = 1, LOGOU = 2, CADASTRAR = 3, CADASTROU = 4, PEDIR_SALAS = 5, DEVOLVE_SALAS = 6,
                            PEDIR_CATEGORIAS = 7, DEVOLVE_CATEGORIAS = 8, ENTRAR_SALA = 9, ENTROU_SALA = 10, PEDIR_USUARIOS = 11, DEVOLVE_USUARIOS = 12,
                            PEDIR_ADMS = 13, DEVOLVE_ADMS = 14, EXCLUIR_SALA = 15, EXCLUIR_CATEGORIA = 16, 
                            EXCLUIR_USUARIO = 17,  EXCLUIR_ADM = 18, INCLUIR_SALA = 19, 
                            INCLUIR_CATEGORIA = 20, INCLUIR_USUARIO = 21,  INCLUIR_ADM = 22, EXCLUIU = 23, INCLUIU = 24,
                            ALTEROU = 25, ALTERAR_DADOS = 26, ENVIAR_MSG_CHAT = 27, ESCREVER_NA_TELA = 28,
                            ATUALIZAR_USUARIOS_SALA = 29, PEDIR_USUARIOS_SALA = 30, SAIR_DA_SALA = 31, DESLOGAR = 32, PAUSAR = 33;

    public Mensagem(Object conteudo, int tipo) {
        this.conteudo = conteudo;
        this.tipo = tipo;
    }
    
    public Mensagem(Object conteudo, Object conteudo2, int tipo) {
        this.conteudo = conteudo;
        this.conteudo2 = conteudo2;
        this.tipo = tipo;
    }

    public Mensagem(Object conteudo, Object conteudo2) {
        this.conteudo = conteudo;
        this.conteudo2 = conteudo2;
    }
    
    public Mensagem(int tipo) {
        this.tipo = tipo;
    }

    public Object getConteudo() {
        return conteudo;
    }

    public void setConteudo(Object conteudo) {
        this.conteudo = conteudo;
    }
    
    public Object getConteudo2() {
        return conteudo2;
    }

    public void setConteudo2(Object conteudo2) {
        this.conteudo2 = conteudo2;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
}
