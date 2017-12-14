package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Caio César
 */
public class Servidor {

    private ServerSocket server;
    private Connection conexao;
    private Statement comando;
    private ResultSet resultado;
    private Mensagem msg;
    private PreparedStatement pstmt;
    private ArrayList<Cliente> clientes;
    private Salas salas;
    private final int porta = 20000;

    public Servidor() {
        clientes = new ArrayList<>();
        fazerConexaoBD();
        carregarDados();

        try {
            server = new ServerSocket(porta);
        } catch (Exception e) {
        }
        run();
    }

    private void run() {
        for (;;) {
            Socket s;
            Cliente novoCliente = null;
            try {
                System.out.println("Esperando conexão");
                s = server.accept();
                novoCliente = new Cliente(s);
                clientes.add(novoCliente);

                System.out.println("Novo cliente conectado." + clientes.size() + "clientes conectados.");
                clientes.get(clientes.size() - 1).start();
            } catch (Exception ex) {
                conexaoPerdida(novoCliente);
                //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void conexaoPerdida(Cliente cl) {
        clientes.remove(cl);
        cl.stop();
        System.out.println("Conexão com o cliente perdida. Há " + clientes.size() + "conectados ainda.");
    }

    private void fazerConexaoBD() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conexao = DriverManager.getConnection("jdbc:sqlserver://regulus:1433;", "BD11183", "BD11183");
            comando = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException E) {
            System.out.println("Erro no Banco de Dados");
        }
    }

    private void carregarDados() {
        salas = new Salas();
        try {
            resultado = comando.executeQuery("Select * from Sala");
            resultado.first();
            do {
                if (resultado.getString("senha") != null) {
                    salas.addSala(new Sala(resultado.getString("nome"), resultado.getInt("qtdeMax"), resultado.getString("senha")));
                } else {
                    salas.addSala(new Sala(resultado.getString("nome"), resultado.getInt("qtdeMax")));
                }
            } while (resultado.next());
        } catch (Exception e) {
        }
    }

    private class Cliente extends Thread {

        private ObjectOutputStream saida;
        private ObjectInputStream entrada;
        private Usuario user;
        private ArrayList<String> nomeSalas;

        public Cliente(Socket s) throws Exception {
            System.out.println("Cliente conectado");
            entrada = new ObjectInputStream(s.getInputStream());
            saida = new ObjectOutputStream(s.getOutputStream());
            nomeSalas = new ArrayList();
            user = null;
        }

        public Usuario getUser() {
            return user;
        }

        public void setUser(Usuario user) {
            this.user = user;
        }

        private void enviar(Mensagem msg) {
            try {
                saida.writeObject(msg);
                saida.reset();
            } catch (IOException ex) {
            }
        }

        @Override
        public void run() {
            Mensagem novaMensagem;
            for (;;) {
                try {
                    novaMensagem = (Mensagem) entrada.readObject();
                    switch (novaMensagem.getTipo()) {
                        case Mensagem.LOGAR:
                            enviar(logar(novaMensagem));
                            break;
                        case Mensagem.CADASTRAR:
                            enviar(cadastrar(novaMensagem));
                            break;
                        case Mensagem.PEDIR_SALAS:
                            enviar(devolveSalas());
                            break;
                        case Mensagem.PEDIR_ADMS:
                            enviar(devolveAdms());
                            break;
                        case Mensagem.PEDIR_USUARIOS:
                            enviar(devolveUsuarios());
                            break;
                        case Mensagem.ENTRAR_SALA:
                            enviar(entrarSala(novaMensagem));
                            if (msg.getTipo() == Mensagem.ENTROU_SALA) {
                                atualizarTodos();
                            }
                            break;
                        case Mensagem.ENVIAR_MSG_CHAT:
                            verificarMensagem(novaMensagem);
                            break;
                        case Mensagem.DESLOGAR:
                            deslogarUsuario();
                            break;
                        case Mensagem.SAIR_DA_SALA:
                            sairDaSala(novaMensagem);
                            break;
                        case Mensagem.ALTERAR_DADOS:
                            enviar(alterarDados(novaMensagem));
                            if (msg.getTipo() == Mensagem.ALTEROU) {
                                atualizarTodos();
                            }
                            break;
                        case Mensagem.EXCLUIR_USUARIO:
                            enviar(excluirUsuario(novaMensagem));
                            if (msg.getTipo() == Mensagem.EXCLUIU) {
                                atualizarTodos();
                            }
                            enviar(devolveUsuarios());
                            break;
                        case Mensagem.INCLUIR_ADM:
                            enviar(incluirAdm(novaMensagem));
                            if (msg.getTipo() == Mensagem.INCLUIU) {
                                atualizarTodos();
                            }
                            enviar(devolveAdms());
                            break;
                        case Mensagem.INCLUIR_USUARIO:
                            enviar(incluirUsuario(novaMensagem));
                            if (msg.getTipo() == Mensagem.INCLUIU) {
                                atualizarTodos();
                            }
                            enviar(devolveUsuarios());
                            break;
                        case Mensagem.INCLUIR_SALA:
                            enviar(incluirSala(novaMensagem));
                            if (msg.getTipo() == Mensagem.INCLUIU) {
                                atualizarTodos();
                            }
                            break;
                        case Mensagem.EXCLUIR_ADM:
                            enviar(excluirAdm(novaMensagem));
                            if (msg.getTipo() == Mensagem.EXCLUIU) {
                                atualizarTodos();
                            }
                            enviar(devolveAdms());
                            break;
                        case Mensagem.EXCLUIR_SALA:
                            enviar(excluirSala(novaMensagem));
                            if (msg.getTipo() == Mensagem.EXCLUIU) {
                                atualizarTodos();
                            }
                            break;
                        case Mensagem.PAUSAR:
                            enviar(novaMensagem);
                            break;
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    //   conexaoPerdida(this);
                }
            }
        }

        private boolean existeSala(String salaAux) {
            boolean aux = false;
            try {
                resultado = comando.executeQuery("select count(*) as qtde from sala where nome = \'" + salaAux + "\'");
                resultado.first();
                aux = resultado.getInt("qtde") != 0;
            } catch (SQLException E) {
                msg.setTipo(Mensagem.ERRO);
                msg.setConteudo("Erro no Banco de Dados");
            }
            return aux;
        }

        private boolean existeUsuario(String login) {
            boolean aux = false;
            try {
                resultado = comando.executeQuery("select count(*) as qtde from UsuarioProjetoAndre where login = \'" + login + "\'");
                resultado.first();
                aux = resultado.getInt("qtde") != 0;
            } catch (SQLException E) {
                msg.setTipo(Mensagem.ERRO);
                msg.setConteudo("Erro no Banco de Dados");
            }

            return aux;
        }

        private boolean confirmaSenha(String nome, String senha) {
            boolean aux = false;
            try {
                resultado = comando.executeQuery("select senha as senhaUser from UsuarioProjetoAndre where login = \'" + nome + "\'");
                resultado.first();
                aux = resultado.getString(1).compareTo(senha) == 0;
            } catch (SQLException E) {
                msg.setTipo(Mensagem.ERRO);
                msg.setConteudo("Erro no Banco de Dados");
            }

            return aux;
        }

        private boolean usuarioLogado(String login) {
            for (int i = 0; i < clientes.size(); i++) {
                if (clientes.get(i).getUser() != null) {
                    if (clientes.get(i).getUser().getLogin().equals(login)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private Mensagem logar(Mensagem aux) {
            String login = ((String) aux.getConteudo()).split("::")[0];
            String senha = ((String) aux.getConteudo()).split("::")[1];
            if (existeUsuario(login)) {
                if (confirmaSenha(login, senha)) {
                    if (!usuarioLogado(login)) {
                        try {
                            resultado = comando.executeQuery("Select * from UsuarioProjetoAndre where login=\'" + login + "\'");
                            resultado.first();
                            user = new Usuario(resultado.getString("login"), resultado.getString("nick"), resultado.getString("senha"), resultado.getString("tipo"), resultado.getBytes("imagem"));
                            msg = new Mensagem(user, Mensagem.LOGOU);
                            System.out.println("Usuario " + user.getLogin() + ", logou.");

                        } catch (SQLException e) {
                            msg = new Mensagem("Erro no banco de dados", Mensagem.ERRO);
                        }
                    } else {
                        msg = new Mensagem("Este usuário já está logado.", Mensagem.ERRO);
                    }
                } else {
                    msg = new Mensagem("Senha incorrreta!", Mensagem.ERRO);
                }
            } else {
                msg = new Mensagem("Login não cadastrado!", Mensagem.ERRO);
            }
            return msg;
        }

        private Mensagem cadastrar(Mensagem aux) {
            String login = ((Usuario) aux.getConteudo()).getLogin();
            String nick = ((Usuario) aux.getConteudo()).getNick();
            String senha = ((Usuario) aux.getConteudo()).getSenha();
            byte[] imagem = ((Usuario) aux.getConteudo()).getImagem();
            if (!existeUsuario(login)) {
                try {
                    if (((byte[]) imagem) != null) {
                        pstmt = conexao.prepareStatement("INSERT INTO usuarioProjetoAndre values(?,?,?,?,?)");
                        pstmt.setString(1, login);
                        pstmt.setString(2, nick);
                        pstmt.setString(3, senha);
                        pstmt.setString(4, "U");
                        pstmt.setBytes(5, (byte[]) imagem);
                        pstmt.executeUpdate();
                        pstmt.close();
                    } else {
                        comando.executeUpdate("Insert into UsuarioProjetoAndre (login, nick, senha, tipo) values ('" + login + "', '" + nick + "', '" + senha + "', 'U')");
                    }
                    msg = new Mensagem("Cadastro efetuado com sucesso", Mensagem.CADASTROU);
                } catch (SQLException e) {
                    msg = new Mensagem("Erro no banco de dados.", Mensagem.ERRO);
                }
            } else {
                msg = new Mensagem("Usuário já cadastrado", Mensagem.ERRO);
            }

            return msg;
        }

        private Mensagem devolveSalas() {
            Salas novaSalas = new Salas();
            novaSalas = salas;
            msg = new Mensagem(novaSalas, Mensagem.DEVOLVE_SALAS);
            System.out.println("Salas enviadas com sucesso para " + user.getLogin() + ".");
            return msg;
        }

        private Mensagem devolveUsuarios() {
            try {
                resultado = comando.executeQuery("Select * from UsuarioProjetoAndre where tipo='U'");
                Usuarios usuarios = new Usuarios();
                resultado.first();
                do {
                    usuarios.addUsuario(new Usuario(resultado.getString("login"), resultado.getString("nick"), resultado.getString("senha")));
                } while (resultado.next());
                msg = new Mensagem(usuarios, Mensagem.DEVOLVE_USUARIOS);
            } catch (SQLException e) {
                msg = new Mensagem("Erro no banco de dados", Mensagem.ERRO);
            }
            return msg;
        }

        private Mensagem devolveAdms() {
            try {
                resultado = comando.executeQuery("Select * from UsuarioProjetoAndre where tipo='A'");
                Usuarios usuarios = new Usuarios();
                resultado.first();
                do {
                    usuarios.addUsuario(new Usuario(resultado.getString("login"), resultado.getString("nick"), resultado.getString("senha")));
                } while (resultado.next());
                msg = new Mensagem(usuarios, Mensagem.DEVOLVE_ADMS);
            } catch (SQLException e) {
                msg = new Mensagem("Erro no banco de dados", Mensagem.ERRO);
            }
            return msg;
        }

        private Mensagem excluirSala(Mensagem aux) {
            String nome = (String) aux.getConteudo();
            try {
                if (existeSala(nome)) {
                    if (salas.getSala(nome).getQtdeOcupados() == 0) {
                        comando.executeUpdate("delete from Sala where nome= \'" + nome + "\'");
                        msg = new Mensagem("Sala excluída com sucesso.", Mensagem.EXCLUIU);
                    } else {
                        msg = new Mensagem("Impossivel excluir a sala, existem usuarios nela.", Mensagem.ERRO);
                    }
                } else {
                    msg = new Mensagem("Sala inexistente.", Mensagem.ERRO);
                }
            } catch (SQLException e) {
                msg = new Mensagem("Erro no banco de dados", Mensagem.ERRO);
            }
            salas.removeSala(salas.getSala(nome));
            return msg;

        }

        private Mensagem incluirSala(Mensagem aux) {
            Sala sala = (Sala) aux.getConteudo();
            try {
                if (!existeSala(sala.getNome())) {
                    if (sala.getSenha().equals("")) {
                        comando.executeUpdate("Insert into Sala (nome, qtdeMax) values (\'" + sala.getNome() + "\',"
                                + sala.getQtdeMax() + ")");
                    } else {
                        comando.executeUpdate("Insert into Sala (nome, qtdeMax, senha) values (\'" + sala.getNome() + "\',"
                                + sala.getQtdeMax() + ",\'" + sala.getSenha() + "\')");
                    }
                    msg = new Mensagem("Sala incluída com sucesso.", Mensagem.INCLUIU);
                } else {
                    msg = new Mensagem("Sala já cadastrada.", Mensagem.ERRO);
                }
            } catch (SQLException e) {
                msg = new Mensagem("Erro no banco de dados", Mensagem.ERRO);
            }
            salas.addSala(sala);
            return msg;
        }

        private Mensagem excluirUsuario(Mensagem aux) {
            String login = (String) aux.getConteudo();
            if (existeUsuario(login)) {
                try {
                    if (!usuarioLogado(login)) {
                        comando.executeUpdate("delete from usuarioProjetoAndre where login= '" + login + "'");
                        msg = new Mensagem("Usuário excluido com sucesso", Mensagem.EXCLUIU);
                    } else {
                        msg = new Mensagem("Impossível excluir um administrador logado.", Mensagem.ERRO);
                    }
                } catch (SQLException ex) {
                    msg = new Mensagem("Erro n banco de dados", Mensagem.ERRO);
                }
                msg = new Mensagem(Mensagem.EXCLUIU);
            } else {
                msg = new Mensagem("Usuário inexistente", Mensagem.ERRO);
            }
            if (user.getTipo().equals("U")) {
                deslogarUsuario();
            }
            return msg;
        }

        private Mensagem incluirUsuario(Mensagem aux) {
            Usuario usuario = (Usuario) aux.getConteudo();
            try {
                if (!existeUsuario(usuario.getLogin())) {
                    pstmt = conexao.prepareStatement("INSERT INTO usuarioProjetoAndre (login, nick, senha, tipo, imagem) values(?,?,?,?,?)");
                    pstmt.setString(1, usuario.getLogin());
                    pstmt.setString(2, usuario.getNick());
                    pstmt.setString(3, usuario.getSenha());
                    pstmt.setString(4, "U");
                    pstmt.setBytes(5, (byte[]) usuario.getImagem());
                    pstmt.executeUpdate();
                    pstmt.close();
                    msg = new Mensagem("Usuário incluído com sucesso.", Mensagem.INCLUIU);
                } else {
                    msg = new Mensagem("Usuário já cadastrada.", Mensagem.ERRO);
                }
            } catch (SQLException e) {
                msg = new Mensagem("Erro no banco de dados", Mensagem.ERRO);
            }
            return msg;
        }

        private Mensagem excluirAdm(Mensagem aux) {
            String login = (String) aux.getConteudo();
            try {
                if (existeUsuario(login)) {
                    if (!usuarioLogado(login)) {
                        comando.executeUpdate("delete from UsuarioProjetoAndre where login= \'" + login + "\'");
                        msg = new Mensagem("Administrador excluído com sucesso.", Mensagem.EXCLUIU);
                    } else {
                        msg = new Mensagem("Impossível excluir um administrador logado.", Mensagem.ERRO);
                    }
                } else {
                    msg = new Mensagem("Administrador inexistente.", Mensagem.ERRO);
                }
            } catch (SQLException e) {
                msg = new Mensagem("Erro no banco de dados", Mensagem.ERRO);
            }
            return msg;

        }

        private Mensagem incluirAdm(Mensagem aux) {
            Usuario adm = (Usuario) aux.getConteudo();
            try {
                if (!existeUsuario(adm.getLogin())) {
                    pstmt = conexao.prepareStatement("INSERT INTO usuarioProjetoAndre (login, nick, senha, tipo, imagem) values(?,?,?,?,?)");
                    pstmt.setString(1, adm.getLogin());
                    pstmt.setString(2, adm.getNick());
                    pstmt.setString(3, adm.getSenha());
                    pstmt.setString(4, "A");
                    pstmt.setBytes(5, (byte[]) adm.getImagem());
                    pstmt.executeUpdate();
                    pstmt.close();
                    msg = new Mensagem("Administrador incluído com sucesso.", Mensagem.INCLUIU);
                } else {
                    msg = new Mensagem("Administrador já cadastrada.", Mensagem.ERRO);
                }
            } catch (SQLException e) {
                msg = new Mensagem("Erro no banco de dados", Mensagem.ERRO);
            }
            return msg;
        }

        private Mensagem entrarSala(Mensagem aux) {
            String nome = ((Sala) aux.getConteudo()).getNome();
            if (salas.getSala(nome).getEstadoAtual().equals("cheia")) {
                msg = new Mensagem("Sala cheia, escolha outra sala.", Mensagem.ERRO);
            } else {
                nomeSalas.add(nome);
                salas.getSala(nome).addUsuario(user);
                msg = new Mensagem(Mensagem.ENTROU_SALA);
                System.out.println("Usuario " + user.getLogin() + " entrou na sala: " + nome + ".");
                System.out.println("Há " + salas.getSala(nome).getQtdeOcupados() + " usuários na sala.");
            }
            return msg;
        }

        private void atualizarTodos() {
            for (int i = 0; i < clientes.size(); i++) {
                if (clientes.get(i) != this)
                    clientes.get(i).enviar(devolveSalas());

                if (clientes.get(i).getUser().getLogin() != null) {
                    System.out.println("Salas enviadas para " + clientes.get(i).getUser().getLogin() + ".");
                }
            }
        }

        public synchronized void verificarMensagem(Mensagem msg) {
            String comandoNome;
            comandoNome = ((String) msg.getConteudo()).split("/")[1];
            if (comandoNome.split(" ")[0].equals("direct")) {
                for (int i = 0; i < clientes.size(); i++) {
                    if (salas.getSala(((String) msg.getConteudo2()).split("!-:")[0]).getUsuarios().getUsuario(comandoNome.split(" ")[1]) != null) {
                        if (comandoNome.split(" ")[1].equals(clientes.get(i).getUser().getNick())) {
                            msg.setConteudo(((String) msg.getConteudo()).split("/")[0] + ((String) msg.getConteudo()).split("/")[2]);
                            String aux = ((String) msg.getConteudo2());
                            msg.setConteudo2("$direta:" + aux.substring(aux.lastIndexOf(":")));
                            msg.setTipo(Mensagem.ESCREVER_NA_TELA);
                            clientes.get(i).enviar(msg);
                        }
                    }
                }
            } else {
                for (int i = 0; i < clientes.size(); i++) {
                    String a = ((String) msg.getConteudo2()).split("!-:")[0];
                    if (salas.getSala(a).getUsuarios().getUsuario(clientes.get(i).getUser().getLogin()) != null) {
                        if (clientes.get(i).getUser() != user) {
                            msg.setTipo(Mensagem.ESCREVER_NA_TELA);
                            clientes.get(i).enviar(msg);
                        }
                    }
                }
            }
        }

        private Mensagem alterarDados(Mensagem aux) {
            Usuario userAux = (Usuario) aux.getConteudo();
            String nome = (String) aux.getConteudo2();
            if (existeUsuario(userAux.getLogin())) {
                try {
                    pstmt = conexao.prepareStatement("UPDATE usuarioProjetoAndre SET nick= ?, senha= ?, imagem = ? WHERE login = ?");
                    pstmt.setString(1, userAux.getNick());
                    pstmt.setString(2, userAux.getSenha());
                    pstmt.setBytes(3, userAux.getImagem());
                    pstmt.setString(4, userAux.getLogin());
                    pstmt.executeUpdate();
                    pstmt.close();
                    salas.getSala(nome).getUsuarios().setUsuario(user, userAux);
                    user = userAux;
                    msg = new Mensagem("Dados alterados com sucesso", Mensagem.ALTEROU);
                    msg.setConteudo2(user);
                } catch (SQLException ex) {
                    msg = new Mensagem("Erro no banco de dados", Mensagem.ERRO);
                }
            } else {
                msg = new Mensagem("Usuário inexistente", Mensagem.ERRO);
            }
            return msg;
        }

        private void deslogarUsuario() {
            for (int i = 0; i < salas.qntasSalas(); i++) {
                salas.getSala(i).getUsuarios().removeUsuario(user);
            }
            System.out.println(user.getLogin() + " deslogou corretamente.");
            this.interrupt();
            clientes.remove(this);

            atualizarTodos();
        }

        private void sairDaSala(Mensagem aux) {
            String nome = (String) aux.getConteudo();
            nomeSalas.remove(nome);
            salas.getSala(nome).getUsuarios().removeUsuario(user);
            System.out.println(user.getLogin() + " saiu da sala " + nome + " corretamente.");
            atualizarTodos();
        }
    }

    public static void main(String[] args) {
        Servidor s = new Servidor();
    }
}