package chat;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author u11155
 */
public class JanelaChat extends JFrame {

    // componentes
    private JLabel lblSala, lblNick, lblImagemUser, lblImagemOutro, imagemDeFundo;
    private JEditorPane epConversa, epMensagem;
    private JScrollPane sp1, sp2, sp3;
    private JButton btnEnviar, btnCor, btnFonte, btnMsgPrivada;
    private JList lsbUsuarios;
    private JMenuBar menubar;
    private JMenu arquivo, menuSalas, deslogar1;
    private JMenuItem mudarImagem, enviarArquivo, alterarDados, sairDaSala, deslogar;
    // variaveis
    private Usuario user;
    private Sala sala;
    private String ultEnvio;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private Receptor receptor;
    private Usuarios usuariosSala;
    private Color corAtual = Color.BLACK;
    private String[] fonteLetra;
    private String textoHTML;
    private HTMLEditorKit kit1;
    private byte[] imagemAtual;

    public JanelaChat(Sala sala, Usuario usuario, ObjectOutputStream out, ObjectInputStream in) {

        this.setLayout(null);
        this.setSize(720, 530);

        menubar = new JMenuBar();
        arquivo = new JMenu("Arquivo");
        menuSalas = new JMenu("Salas");
        deslogar1 = new JMenu("Deslogar");
        deslogar = new JMenuItem("Deslogar");
        mudarImagem = new JMenuItem("Mudar Imagem");
        enviarArquivo = new JMenuItem("Enviar Arquivo");
        alterarDados = new JMenuItem("Minha Conta");
        sairDaSala = new JMenuItem("Sair da sala");

        enviarArquivo.addActionListener(new EnviarArquivo());
        deslogar.addActionListener(new Deslogar());
        sairDaSala.addActionListener(new SairSala());
        alterarDados.addActionListener(new Conta());
        mudarImagem.addActionListener(new MudarImagem());

        arquivo.add(mudarImagem);
        arquivo.add(alterarDados);
        arquivo.add(enviarArquivo);
        menuSalas.add(sairDaSala);
        deslogar1.add(deslogar);
        menubar.add(arquivo);
        menubar.add(menuSalas);
        menubar.add(deslogar1);


        this.setJMenuBar(menubar);

        lblSala = new JLabel("Sala: ");
        lblSala.setLocation(30, 10);
        lblSala.setSize(100, 40);
        this.add(lblSala);

        lblNick = new JLabel("Nick: ");
        lblNick.setLocation(420, 10);
        lblNick.setSize(100, 40);
        this.add(lblNick);

        lblImagemUser = new JLabel(redimensionaImagem(usuario.getImagem()));
        lblImagemUser.setSize(100, 100);
        lblImagemUser.setLocation(585, 75);
        this.add(lblImagemUser);

        lblImagemOutro = new JLabel();
        lblImagemOutro.setSize(100, 100);
        lblImagemOutro.setLocation(585, 225);
        this.add(lblImagemOutro);

        lsbUsuarios = new JList();
        // Scrolling
        sp1 = new JScrollPane(lsbUsuarios);
        sp1.setLocation(420, 50);
        sp1.setSize(150, 320);
        sp1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(sp1);
        lsbUsuarios.addMouseListener(new MostrarUsuario());

        epConversa = new JEditorPane();
        epConversa.setEditable(false);
        // Scrolling
        sp2 = new JScrollPane(epConversa);
        sp2.setAutoscrolls(true);
        sp2.setLocation(30, 50);
        sp2.setSize(370, 320);
        sp2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(sp2);

        epMensagem = new JEditorPane();
        epMensagem.setEditable(true);
        // Scrolling
        sp3 = new JScrollPane(epMensagem);
        sp3.setAutoscrolls(true);
        sp3.setLocation(30, 380);
        sp3.setSize(300, 60);
        sp3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(sp3);
        epMensagem.addKeyListener(new Teclado());

        btnEnviar = new JButton(new ImageIcon("images/send.png"));
        btnEnviar.setToolTipText("Enviar");
        btnEnviar.setBorderPainted(false);
        btnEnviar.setContentAreaFilled(false);
        btnEnviar.setFocusPainted(false);
        btnEnviar.setLocation(335, 378);
        btnEnviar.setSize(65, 25);
        this.add(btnEnviar);
        btnEnviar.addActionListener(new Enviar());
        
        btnMsgPrivada = new JButton("Mensagem Privada");
        btnMsgPrivada.setVisible(false);
        btnMsgPrivada.setLocation(570, 350);
        btnMsgPrivada.setSize(130, 25);
        btnMsgPrivada.addActionListener(new MensagemPrivada());
        this.add(btnMsgPrivada);

        btnCor = new JButton(new ImageIcon("images/color.png"));
        btnCor.setToolTipText("Cor");
        btnCor.setBorderPainted(false);
        btnCor.setContentAreaFilled(false);
        btnCor.setFocusPainted(false);
        btnCor.setLocation(420, 378);
        btnCor.setSize(50, 25);
        this.add(btnCor);
        btnCor.addActionListener(new Cor());

        btnFonte = new JButton(new ImageIcon("images/font.png"));
        btnFonte.setToolTipText("Fonte");
        btnFonte.setBorderPainted(false);
        btnFonte.setContentAreaFilled(false);
        btnFonte.setLocation(512, 378);
        btnFonte.setSize(60, 25);
        btnFonte.setFocusPainted(false);
        this.add(btnFonte);
        btnFonte.addActionListener(new Fonte());

        imagemDeFundo = new JLabel(redimensionaImagem(new ImageIcon("images/fundo3.png")));
        imagemDeFundo.setSize(720, 530);
        this.add(imagemDeFundo);

        this.addWindowListener(new Janela());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Chat");
        this.setIconImage(new ImageIcon("images/icon.png").getImage());
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        lblSala.setText(sala.getNome());
        user = usuario;
        this.sala = sala;
        entrada = in;
        saida = out;
    }

    private ImageIcon redimensionaImagem(byte[] fileBytes) {
        ImageIcon img = new ImageIcon(fileBytes);
        img.setImage(img.getImage().getScaledInstance(100, 100, 100));
        return img;
    }

    private ImageIcon redimensionaImagem(ImageIcon ii) {
        ImageIcon img = new ImageIcon(ii.getImage());
        img.setImage(img.getImage().getScaledInstance(720, 530, 100));
        return img;
    }

    private ImageIcon redimensionaImagem(File arq) {
        ImageIcon img = null;
        try {
            FileInputStream put = new FileInputStream(arq);
            byte[] image = new byte[(int) arq.length()];
            imagemAtual = image;
            put.read(image);
            img = new ImageIcon(image);
            img.setImage(img.getImage().getScaledInstance(100, 100, 100));
        } catch (IOException ex) {
            Logger.getLogger(JanelaCadastro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return img;
    }

    private class Janela extends WindowAdapter {

        @Override
        public void windowOpened(WindowEvent e) {
            lblSala.setText(sala.getNome());
            lblNick.setText(user.getNick());
            epMensagem.grabFocus();
            fonteLetra = new String[4];
            fonteLetra[0] = "Times New Roman";
            fonteLetra[1] = "12";
            fonteLetra[2] = "normal";
            fonteLetra[3] = "normal";
            kit1 = new HTMLEditorKit();
            epConversa.setEditorKit(kit1);
            Document doc1 = kit1.createDefaultDocument();
            epConversa.setDocument(doc1);
            usuariosSala = new Usuarios();
            receptor = new Receptor(entrada);
            receptor.start();
            pedirUsuarios();
            textoHTML = "";
            ultEnvio = "";
        }

        @Override
        public void windowClosing(WindowEvent e) {
            Mensagem msg = new Mensagem(Mensagem.DESLOGAR);
            enviar(msg);
        }
    }

    public void pedirUsuarios() {
        Mensagem aux = new Mensagem(Mensagem.PEDIR_SALAS);
        enviar(aux);
    }

    public void escreverNaTela(Mensagem aux) {
        String texto = ((String) aux.getConteudo());
        String nick = ((String) aux.getConteudo2()).split("!-:")[1];
        String aux2 = "<b>" + nick + ", diz:</b>";
        if (((String) aux.getConteudo2()).split("!-:")[0].equals("$direct")) {
            textoHTML = textoHTML + texto;
            ultEnvio = "";
        } else {
            if (!ultEnvio.equals(nick)) {
                ultEnvio = nick;
                textoHTML = textoHTML + aux2 + texto;
            } else {
                textoHTML = textoHTML + texto;
            }
        }
        epConversa.setText(textoHTML);

    }

    public void atualizarUsuarios(Mensagem aux) {
        usuariosSala = ((Salas) aux.getConteudo()).getSala(sala.getNome()).getUsuarios();
        DefaultListModel dfm = new DefaultListModel();
        lsbUsuarios.setModel(dfm);
        for (int i = 0; i < usuariosSala.qntosUsuarios(); i++) {
            synchronized (this) {
                dfm.addElement(usuariosSala.getUsuario(i).getNick());
            }
        }
    }

    private void alterarImagemUser(Mensagem aux) {
        user = (Usuario) aux.getConteudo2();
        usuariosSala.getUsuario(user.getLogin()).setImagem(user.getImagem());
        lblImagemUser.setIcon(redimensionaImagem(user.getImagem()));
    }

    private class Receptor extends Thread {

        private ObjectInputStream entrada;
        private boolean parar;

        public Receptor(ObjectInputStream in) {
            entrada = in;
            parar = false;
        }

        @Override
        public synchronized void run() {
            while (!parar) {
                try {
                    Mensagem msgResposta = (Mensagem) entrada.readObject();
                    switch (msgResposta.getTipo()) {
                        case Mensagem.ESCREVER_NA_TELA:
                            escreverNaTela(msgResposta);
                            break;
                        case Mensagem.DEVOLVE_SALAS:
                            atualizarUsuarios(msgResposta);
                            break;
                        case Mensagem.PAUSAR:
                            parar = true;
                            break;
                        case Mensagem.ALTEROU:
                            alterarImagemUser(msgResposta);
                            break;
                        case Mensagem.ERRO:
                            JOptionPane.showMessageDialog(null, msgResposta.getConteudo2(), "Erro", JOptionPane.ERROR_MESSAGE);
                            break;
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(JanelaChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private class Teclado extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                btnEnviar.doClick();
            }
        }
    }

    private class MostrarUsuario extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (usuariosSala.qntosUsuarios() > 0) {
                btnMsgPrivada.setVisible(true);
                lblImagemOutro.setIcon(redimensionaImagem(usuariosSala.getUsuario(lsbUsuarios.getSelectedIndex()).getImagem()));
            }
        }
    }

    private void enviar(Mensagem msgEnviar) {
        try {
            saida.writeObject(msgEnviar);
            saida.reset();
        } catch (IOException ex) {
            Logger.getLogger(JanelaChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class Enviar implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (!epMensagem.getText().equals("")) {
                String envio = "";
                String aux = "<b>Você, diz:</b>";
                String comandoNome = "";
                if (ultEnvio.equals(user.getNick())) {
                    try {
                        comandoNome = epMensagem.getText().split("/")[1];
                    } catch (Exception i) {
                    }
                    if (!comandoNome.split(" ")[0].equals("direct")) {
                        envio = "<a style=" + (char) 34 + "font-family:" + fonteLetra[0] + "; font-size:" + fonteLetra[1] + "px; font-weight:" + fonteLetra[2] + "; font-style:" + fonteLetra[3] + "; color:rgb(" + corAtual.getRed() + "," + corAtual.getGreen() + "," + corAtual.getBlue() + ");" + (char) 34 + ">" + epMensagem.getText() + "</a>" + "<br>";
                        textoHTML = textoHTML + envio;
                    } else {
                        envio = "<a style=" + (char) 34 + "font-family:" + fonteLetra[0] + "; font-size:" + fonteLetra[1] + "px; font-weight:" + fonteLetra[2] + "; font-style:" + fonteLetra[3] + "; color:rgb(" + corAtual.getRed() + "," + corAtual.getGreen() + "," + corAtual.getBlue() + ");" + (char) 34 + ">" + epMensagem.getText().split("/")[2] + "</a>" + "<br>";
                        textoHTML = textoHTML + "<br>" + "Sua mensagem direta: <br>" + envio;
                    }
                } else {
                    try {
                        comandoNome = epMensagem.getText().split("/")[1];
                    } catch (Exception i) {
                    }
                    if (!comandoNome.split(" ")[0].equals("direct")) {
                        envio = "<br><a style=" + (char) 34 + "font-family:" + fonteLetra[0] + "; font-size:" + fonteLetra[1] + "px; font-weight:" + fonteLetra[2] + "; font-style:" + fonteLetra[3] + "; color:rgb(" + corAtual.getRed() + "," + corAtual.getGreen() + "," + corAtual.getBlue() + ");" + (char) 34 + ">" + epMensagem.getText() + "</a><br>";
                        textoHTML = textoHTML + aux + envio;
                    } else {
                        envio = "<a style=" + (char) 34 + "font-family:" + fonteLetra[0] + "; font-size:" + fonteLetra[1] + "px; font-weight:" + fonteLetra[2] + "; font-style:" + fonteLetra[3] + "; color:rgb(" + corAtual.getRed() + "," + corAtual.getGreen() + "," + corAtual.getBlue() + ");" + (char) 34 + ">" + epMensagem.getText().split("/")[2] + "</a><br>";
                        textoHTML = textoHTML + aux + "<br>" + "Sua mensagem direta: <br>" + envio;
                    }
                    ultEnvio = user.getNick();
                }
                if (!comandoNome.split(" ")[0].equals("direct")) {
                    envio = "<br><a style=" + (char) 34 + "font-family:" + fonteLetra[0] + "; font-size:" + fonteLetra[1] + "px; font-weight:" + fonteLetra[2] + "; font-style:" + fonteLetra[3] + "; color:rgb(" + corAtual.getRed() + "," + corAtual.getGreen() + "," + corAtual.getBlue() + ");" + (char) 34 + ">" + epMensagem.getText() + "</a><br>";
                } else {
                    envio = "<br><a style=" + (char) 34 + "font-family:" + fonteLetra[0] + "; font-size:" + fonteLetra[1] + "px; font-weight:" + fonteLetra[2] + "; font-style:" + fonteLetra[3] + "; color:rgb(" + corAtual.getRed() + "," + corAtual.getGreen() + "," + corAtual.getBlue() + ");" + (char) 34 + ">" + "<b>Mensagem direta de " + user.getLogin() + ": </b> " + epMensagem.getText().split("/")[2] + "</a><br>";
                }
                Mensagem msgEnviar = new Mensagem(envio, sala.getNome() + "!-:" + user.getNick(), Mensagem.ENVIAR_MSG_CHAT);
                enviar(msgEnviar);
                epConversa.setText(textoHTML);
                epMensagem.setText(null);
                epMensagem.grabFocus();
            }
        }
    }

    private class Cor implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            corAtual = JColorChooser.showDialog(null, "Escolha a cor desejada para suas mensagens", corAtual);
            epMensagem.setForeground(corAtual);
        }
    }

    private class Fonte implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Frame janelaTexto = new Frame();
            FontChooser fc = new FontChooser(janelaTexto);
            fc.setResizable(false);
            fc.setLocationRelativeTo(null);
            fc.setVisible(true);
            if (fc.getOK()) {
                fonteLetra = fc.getNewFont();
                int tipo = Font.PLAIN;
                if (fonteLetra[2].equals("bold")) {
                    tipo = Font.BOLD;
                }
                epMensagem.setFont(new Font(fonteLetra[0], tipo, Integer.parseInt(fonteLetra[1])));
            }
        }
    }

    private class Conta implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            enviar(new Mensagem(Mensagem.PAUSAR));
            JanelaConta conta = new JanelaConta(sala.getNome(), user, saida, entrada);
            usuariosSala.getUsuario(user.getLogin()).setImagem(conta.getUsuario().getImagem());
            lblImagemUser.setIcon(redimensionaImagem(conta.getUsuario().getImagem()));
            conta.dispose();
            receptor = new Receptor(entrada);
            receptor.start();
        }
    }
    
    private class MensagemPrivada implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (!lsbUsuarios.getSelectedValue().equals(user.getNick()))
                epMensagem.setText("/direct " + lsbUsuarios.getSelectedValue() + "/ ");
            epMensagem.grabFocus();
        }
    }

    private class MudarImagem implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String caminhoArquivo;
            JFileChooser selecionarImagem = new JFileChooser();
            FileNameExtensionFilter filter1 = new FileNameExtensionFilter("Arquivo PNG (.png)", "png");
            FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Arquivo JPEG (.jpg)", "jpg");
            selecionarImagem.addChoosableFileFilter(filter1);
            selecionarImagem.addChoosableFileFilter(filter2);
            int selecao = selecionarImagem.showOpenDialog(null);
            if (selecao == JFileChooser.APPROVE_OPTION) {
                String extensao = selecionarImagem.getSelectedFile().getAbsolutePath().substring(selecionarImagem.getSelectedFile().getAbsolutePath().length() - 3);
                if (extensao.equals("png") || extensao.equals("jpg") || extensao.equals("gif")) {
                    caminhoArquivo = selecionarImagem.getSelectedFile().getAbsolutePath();
                    lblImagemUser.setIcon(redimensionaImagem(new File(caminhoArquivo)));
                }
            }
            Usuario newUser = user;
            user.setImagem(imagemAtual);
            enviar(new Mensagem(newUser, sala.getNome(), Mensagem.ALTERAR_DADOS));
        }
    }

    private class EnviarArquivo implements ActionListener {

        public void actionPerformed(ActionEvent e) {
        }
    }

    private class SairSala implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Mensagem nova = new Mensagem(sala.getNome(), Mensagem.SAIR_DA_SALA);
            enviar(nova);
            enviar(new Mensagem(Mensagem.PAUSAR));
            JanelaSalas jSalas = new JanelaSalas(user, saida, entrada);
            JanelaChat.this.dispose();
        }
    }

    private class Deslogar implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Mensagem msg = new Mensagem(Mensagem.DESLOGAR);
            enviar(msg);
            JanelaLogin login = new JanelaLogin();
            JanelaChat.this.dispose();
            receptor.stop();
        }
    }
}
