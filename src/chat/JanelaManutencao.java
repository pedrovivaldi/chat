package chat;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Caio
 */
public class JanelaManutencao extends JFrame {

    // componentes
    private JTabbedPane abas;
    private JPanel aba1, aba2, aba3, aba4;
    private JLabel lblNomeSala, lblQtdeMax, lblSenhaSala, lblLoginUsuario, lblNickUsuario, lblSenhaUsuario,
            lblLoginAdm, lblSenhaAdm, lblNickAdm, imagemDeFundo1, imagemDeFundo2, imagemDeFundo3, imagemDeFundo4, imagemDeFundo5;
    private JTextField txtNomeSala, txtQtdeMax, txtLoginUsuario, txtNickUsuario, txtLoginAdm, txtNickAdm;
    private JPasswordField txtSenhaSala, txtSenhaUsuario, txtSenhaAdm;
    private JButton btnSair1, btnSair3, btnSair4, btnLimparSala, btnLimparUsuario, btnLimparAdm,
            btnExcluirSala, btnExcluirUsuario, btnExcluirAdm, btnInserirSala, btnInserirUsuario,
            btnInserirAdm;
    private JList lsbSalas, lsbUsuarios, lsbAdms;
    private JScrollPane sp1, sp3, sp4;
    // variaveis
    private ObjectInputStream entrada;
    private ObjectOutputStream saida;
    private Receptor receptor;
    private Mensagem msg;
    private Salas salas;
    private Usuario usuario;
    private Usuarios usuarios;
    private Usuarios adms;
    private byte[] imagemPadrao;

    public JanelaManutencao(Usuario user, ObjectOutputStream out, ObjectInputStream in) {
        this.setLayout(null);
        this.setSize(520, 500);

        aba1 = new JPanel();
        aba1.setLayout(null);
        aba2 = new JPanel();
        aba2.setLayout(null);
        aba3 = new JPanel();
        aba3.setLayout(null);
        aba4 = new JPanel();
        aba4.setLayout(null);

        // aba de salas
        lsbSalas = new JList();
        lsbSalas.addMouseListener(new MouseListaSalas());
        sp1 = new JScrollPane(lsbSalas);
        sp1.setLocation(10, 30);
        sp1.setSize(200, 380);
        sp1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        aba1.add(sp1);

        lblNomeSala = new JLabel("Nome:");
        lblNomeSala.setLocation(240, 40);
        lblNomeSala.setSize(40, 40);
        aba1.add(lblNomeSala);

        txtNomeSala = new JTextField();
        txtNomeSala.setLocation(300, 50);
        txtNomeSala.setSize(150, 20);
        aba1.add(txtNomeSala);

        lblQtdeMax = new JLabel("QtdeMax:");
        lblQtdeMax.setLocation(240, 80);
        lblQtdeMax.setSize(70, 40);
        aba1.add(lblQtdeMax);

        txtQtdeMax = new JTextField();
        txtQtdeMax.setLocation(300, 90);
        txtQtdeMax.setSize(90, 20);
        aba1.add(txtQtdeMax);

        lblSenhaSala = new JLabel("Senha:");
        lblSenhaSala.setLocation(240, 120);
        lblSenhaSala.setSize(40, 40);
        aba1.add(lblSenhaSala);

        txtSenhaSala = new JPasswordField();
        txtSenhaSala.setLocation(300, 130);
        txtSenhaSala.setSize(150, 20);
        aba1.add(txtSenhaSala);

        btnInserirSala = new JButton("Inserir");
        btnInserirSala.setLocation(350, 180);
        btnInserirSala.setSize(75, 25);
        aba1.add(btnInserirSala);
        btnInserirSala.addActionListener(new InserirSala());


        btnLimparSala = new JButton("Limpar");
        btnLimparSala.setLocation(260, 180);
        btnLimparSala.setSize(75, 25);
        aba1.add(btnLimparSala);
        btnLimparSala.addActionListener(new LimparSala());

        btnExcluirSala = new JButton("Excluir");
        btnExcluirSala.setLocation(230, 340);
        btnExcluirSala.setSize(75, 25);
        aba1.add(btnExcluirSala);
        btnExcluirSala.addActionListener(new ExcluirSala());

        btnSair1 = new JButton("Sair");
        btnSair1.setLocation(420, 380);
        btnSair1.setSize(60, 25);
        aba1.add(btnSair1);
        btnSair1.addActionListener(new Sair());

        // aba de usuarios
        lsbUsuarios = new JList();
        lsbUsuarios.addMouseListener(new MouseListaUsuarios());
        sp3 = new JScrollPane(lsbUsuarios);
        sp3.setLocation(10, 30);
        sp3.setSize(200, 380);
        sp3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        aba2.add(sp3);

        lblLoginUsuario = new JLabel("Login:");
        lblLoginUsuario.setLocation(240, 40);
        lblLoginUsuario.setSize(40, 40);
        aba2.add(lblLoginUsuario);

        txtLoginUsuario = new JTextField();
        txtLoginUsuario.setLocation(280, 50);
        txtLoginUsuario.setSize(150, 20);
        aba2.add(txtLoginUsuario);

        lblNickUsuario = new JLabel("Nick:");
        lblNickUsuario.setLocation(240, 80);
        lblNickUsuario.setSize(40, 40);
        aba2.add(lblNickUsuario);

        txtNickUsuario = new JTextField();
        txtNickUsuario.setLocation(280, 90);
        txtNickUsuario.setSize(150, 20);
        aba2.add(txtNickUsuario);

        lblSenhaUsuario = new JLabel("Senha:");
        lblSenhaUsuario.setLocation(240, 120);
        lblSenhaUsuario.setSize(40, 40);
        aba2.add(lblSenhaUsuario);

        txtSenhaUsuario = new JPasswordField();
        txtSenhaUsuario.setLocation(280, 130);
        txtSenhaUsuario.setSize(150, 20);
        aba2.add(txtSenhaUsuario);

        btnInserirUsuario = new JButton("Inserir");
        btnInserirUsuario.setLocation(340, 165);
        btnInserirUsuario.setSize(75, 25);
        aba2.add(btnInserirUsuario);
        btnInserirUsuario.addActionListener(new InserirUsuario());

        btnLimparUsuario = new JButton("Limpar");
        btnLimparUsuario.setLocation(260, 165);
        btnLimparUsuario.setSize(75, 25);
        aba2.add(btnLimparUsuario);
        btnLimparUsuario.addActionListener(new LimparUsuario());

        btnExcluirUsuario = new JButton("Excluir");
        btnExcluirUsuario.setLocation(230, 340);
        btnExcluirUsuario.setSize(75, 25);
        aba2.add(btnExcluirUsuario);
        btnExcluirUsuario.addActionListener(new ExcluirUsuario());

        btnSair3 = new JButton("Sair");
        btnSair3.setLocation(420, 380);
        btnSair3.setSize(60, 25);
        aba2.add(btnSair3);
        btnSair3.addActionListener(new Sair());

        // aba de adms
        lsbAdms = new JList();

        sp4 = new JScrollPane(lsbAdms);
        lsbAdms.addMouseListener(new MouseListaAdms());
        sp4.setLocation(10, 30);
        sp4.setSize(200, 380);
        sp4.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp4.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        aba3.add(sp4);

        lblLoginAdm = new JLabel("Login:");
        lblLoginAdm.setLocation(240, 40);
        lblLoginAdm.setSize(40, 40);
        aba3.add(lblLoginAdm);

        txtLoginAdm = new JTextField();
        txtLoginAdm.setLocation(280, 50);
        txtLoginAdm.setSize(150, 20);
        aba3.add(txtLoginAdm);

        lblNickAdm = new JLabel("Nick:");
        lblNickAdm.setLocation(240, 80);
        lblNickAdm.setSize(40, 40);
        aba3.add(lblNickAdm);

        txtNickAdm = new JTextField();
        txtNickAdm.setLocation(280, 90);
        txtNickAdm.setSize(150, 20);
        aba3.add(txtNickAdm);

        lblSenhaAdm = new JLabel("Senha:");
        lblSenhaAdm.setLocation(240, 120);
        lblSenhaAdm.setSize(40, 40);
        aba3.add(lblSenhaAdm);

        txtSenhaAdm = new JPasswordField();
        txtSenhaAdm.setLocation(280, 130);
        txtSenhaAdm.setSize(150, 20);
        aba3.add(txtSenhaAdm);

        btnInserirAdm = new JButton("Inserir");
        btnInserirAdm.setLocation(340, 165);
        btnInserirAdm.setSize(75, 25);
        aba3.add(btnInserirAdm);
        btnInserirAdm.addActionListener(new InserirAdm());

        btnLimparAdm = new JButton("Limpar");
        btnLimparAdm.setLocation(260, 165);
        btnLimparAdm.setSize(75, 25);
        aba3.add(btnLimparAdm);
        btnLimparAdm.addActionListener(new LimparAdm());


        btnExcluirAdm = new JButton("Excluir");
        btnExcluirAdm.setLocation(230, 340);
        btnExcluirAdm.setSize(75, 25);
        aba3.add(btnExcluirAdm);
        btnExcluirAdm.addActionListener(new ExcluirAdm());

        btnSair4 = new JButton("Sair");
        btnSair4.setLocation(420, 380);
        btnSair4.setSize(60, 25);
        aba3.add(btnSair4);
        btnSair4.addActionListener(new Sair());

        abas = new JTabbedPane();
        abas.setSize(510, 470);
        abas.add("Salas", aba1);
        abas.add("Usuários", aba2);
        abas.add("Administradores", aba3);
        this.getContentPane().add(abas);

        imagemDeFundo1 = new JLabel(redimensionaImagem(new ImageIcon("images/fundo3.png"), 510, 470));
        imagemDeFundo1.setSize(530, 500);
        aba1.add(imagemDeFundo1);

        imagemDeFundo2 = new JLabel(redimensionaImagem(new ImageIcon("images/fundo3.png"), 510, 470));
        imagemDeFundo2.setSize(530, 500);
        aba2.add(imagemDeFundo2);

        imagemDeFundo3 = new JLabel(redimensionaImagem(new ImageIcon("images/fundo3.png"), 510, 470));
        imagemDeFundo3.setSize(530, 500);
        aba3.add(imagemDeFundo3);
        
        imagemDeFundo4 = new JLabel(redimensionaImagem(new ImageIcon("images/fundo3.png"), 520, 500));
        imagemDeFundo4.setSize(530, 500);
        this.add(imagemDeFundo4);
        
        imagemDeFundo5 = new JLabel(redimensionaImagem(new ImageIcon("images/fundo3.png"), 520, 500));
        imagemDeFundo5.setSize(530, 500);
        abas.add(imagemDeFundo5);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new Janela());
        this.setVisible(true);
        this.setIconImage(new ImageIcon("images/icon.png").getImage());
        this.setTitle("Manutenção");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        saida = out;
        entrada = in;
        usuario = user;
    }

    private ImageIcon redimensionaImagem(ImageIcon ii, int width, int height) {
        ImageIcon img = new ImageIcon(ii.getImage());
        img.setImage(img.getImage().getScaledInstance(width, height, 100));
        return img;
    }

    private class MouseListaSalas extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (salas.qntasSalas() > 0) {
                txtNomeSala.setText(salas.getSala(lsbSalas.getSelectedIndex()).getNome());
                txtQtdeMax.setText(Integer.toString(salas.getSala(lsbSalas.getSelectedIndex()).getQtdeMax()));
                txtSenhaSala.setText(salas.getSala(lsbSalas.getSelectedIndex()).getSenha());
            }
        }
    }

    private class MouseListaUsuarios extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (adms.qntosUsuarios() > 0) {
                txtLoginUsuario.setText(usuarios.getUsuario(lsbUsuarios.getSelectedIndex()).getLogin());
                txtNickUsuario.setText(usuarios.getUsuario(lsbUsuarios.getSelectedIndex()).getNick());
                txtSenhaUsuario.setText(usuarios.getUsuario(lsbUsuarios.getSelectedIndex()).getSenha());
            }
        }
    }

    private class MouseListaAdms extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (adms.qntosUsuarios() > 0) {
                txtLoginAdm.setText(adms.getUsuario(lsbAdms.getSelectedIndex()).getLogin());
                txtNickAdm.setText(adms.getUsuario(lsbAdms.getSelectedIndex()).getNick());
                txtSenhaAdm.setText(adms.getUsuario(lsbAdms.getSelectedIndex()).getSenha());
            }
        }
    }

    private void atualizarAdms(Mensagem aux) {
        adms = (Usuarios) aux.getConteudo();
        DefaultListModel dlm = new DefaultListModel();
        lsbAdms.setModel(dlm);
        for (int i = 0; i < adms.qntosUsuarios(); i++) {
            Usuario novoAdm = adms.getUsuario(i);
            synchronized (this) {
                dlm.addElement(novoAdm.getLogin());
            }
        }
    }

    private void atualizarUsuarios(Mensagem aux) {
        usuarios = (Usuarios) aux.getConteudo();
        DefaultListModel dlm = new DefaultListModel();
        lsbUsuarios.setModel(dlm);
        for (int i = 0; i < usuarios.qntosUsuarios(); i++) {
            Usuario novoUsuario = usuarios.getUsuario(i);
            synchronized (this) {
                dlm.addElement(novoUsuario.getLogin());
            }
        }
    }

    private void atualizarSalas(Mensagem aux) {
        salas = (Salas) aux.getConteudo();
        DefaultListModel dlm = new DefaultListModel();
        lsbSalas.setModel(dlm);
        for (int i = 0; i < salas.qntasSalas(); i++) {
            Sala novaSala = salas.getSala(i);
            synchronized (this) {
                dlm.addElement(novaSala.getNome());
            }
        }
    }

    private void pedirDados() {
        msg = new Mensagem(Mensagem.PEDIR_SALAS);
        enviar(msg);
        msg = new Mensagem(Mensagem.PEDIR_USUARIOS);
        enviar(msg);
        msg = new Mensagem(Mensagem.PEDIR_ADMS);
        enviar(msg);
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
                        case Mensagem.DEVOLVE_ADMS:
                            atualizarAdms(msgResposta);
                            break;
                        case Mensagem.DEVOLVE_USUARIOS:
                            atualizarUsuarios(msgResposta);
                            break;
                        case Mensagem.DEVOLVE_SALAS:
                            atualizarSalas(msgResposta);
                            break;
                        case Mensagem.INCLUIU:
                            JOptionPane.showMessageDialog(null, msgResposta.getConteudo(), "Manutenção", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        case Mensagem.EXCLUIU:
                            JOptionPane.showMessageDialog(null, msgResposta.getConteudo(), "Manutenção", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        case Mensagem.ALTEROU:
                            JOptionPane.showMessageDialog(null, msgResposta.getConteudo(), "Manutenção", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        case Mensagem.ERRO:
                            JOptionPane.showMessageDialog(null, msgResposta.getConteudo(), "Manutenção", JOptionPane.ERROR_MESSAGE);
                            break;
                        case Mensagem.PAUSAR:
                            parar = true;
                            break;
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(JanelaManutencao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void enviar(Mensagem aux) {
        try {
            saida.writeObject(aux);
            saida.reset();
        } catch (IOException ex) {
            Logger.getLogger(JanelaManutencao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class Janela extends WindowAdapter {

        @Override
        public void windowOpened(WindowEvent e) {
            receptor = new Receptor(entrada);
            receptor.start();
            pedirDados();
            carregarFotoPadrao();
        }
    }

    private class ExcluirSala implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            msg = new Mensagem(lsbSalas.getSelectedValue(), Mensagem.EXCLUIR_SALA);
            enviar(msg);
            btnLimparSala.doClick();
        }
    }

    private class InserirSala implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if ((!txtNomeSala.getText().equals("")) || (!txtQtdeMax.getText().equals(""))) {
                Sala sala;
                if (txtSenhaSala.getText().equals("")) {
                    sala = new Sala(txtNomeSala.getText(), Integer.valueOf(txtQtdeMax.getText()), "");
                } else {
                    sala = new Sala(txtNomeSala.getText(), Integer.valueOf(txtQtdeMax.getText()), txtSenhaSala.getText());
                }
                msg = new Mensagem(sala, Mensagem.INCLUIR_SALA);
                enviar(msg);
                btnLimparSala.doClick();
            } else {
                JOptionPane.showMessageDialog(null, " Preencha todos os campos para efetuar o cadastro.");
                btnLimparSala.doClick();
            }
        }
    }

    private class ExcluirUsuario implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (lsbUsuarios.getSelectedValue() != null) {
                msg = new Mensagem(lsbUsuarios.getSelectedValue(), Mensagem.EXCLUIR_USUARIO);
                enviar(msg);
                btnLimparUsuario.doClick();
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um usuário para remover");
            }
        }
    }

    private void carregarFotoPadrao() {
        try {
            File arq = new File("G:/Chat/images/avatar.gif");
            FileInputStream put = new FileInputStream(arq);
            byte[] image = new byte[(int) arq.length()];
            imagemPadrao = image;
            put.read(image);
        } catch (IOException ex) {
            Logger.getLogger(JanelaManutencao.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    private class InserirUsuario implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if ((!txtLoginUsuario.getText().equals("")) || (!txtSenhaUsuario.getText().equals("")) || (!txtNickUsuario.getText().equals(""))) {
                Usuario usuario = new Usuario(txtLoginUsuario.getText(), txtNickUsuario.getText(), txtSenhaUsuario.getText());
                usuario.setImagem(imagemPadrao);
                msg = new Mensagem(usuario, Mensagem.INCLUIR_USUARIO);
                enviar(msg);
                btnLimparUsuario.doClick();
            } else {
                JOptionPane.showMessageDialog(null, " Preencha todos os campos para efetuar o cadastro.");
                btnLimparUsuario.doClick();
            }
        }
    }

    private class ExcluirAdm implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (lsbAdms.getSelectedValue() != null) {
                msg = new Mensagem(lsbAdms.getSelectedValue(), Mensagem.EXCLUIR_ADM);
                enviar(msg);
                btnLimparAdm.doClick();
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um administrador para remover");
            }
        }
    }

    private class InserirAdm implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if ((!txtLoginAdm.getText().equals("")) || (!txtSenhaAdm.getText().equals(""))) {
                Usuario adm = new Usuario(txtLoginAdm.getText(), txtNickAdm.getText(), txtSenhaAdm.getText());
                adm.setImagem(imagemPadrao);
                msg = new Mensagem(adm, Mensagem.INCLUIR_ADM);
                enviar(msg);
                btnLimparAdm.doClick();
            } else {
                JOptionPane.showMessageDialog(null, " Preencha todos os campos para efetuar o cadastro.");
                btnLimparAdm.doClick();
            }
        }
    }

    private class LimparSala implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            txtNomeSala.setText(null);
            txtQtdeMax.setText(null);
            txtSenhaSala.setText(null);
            txtNomeSala.grabFocus();
        }
    }

    private class LimparUsuario implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            txtLoginUsuario.setText(null);
            txtNickUsuario.setText(null);
            txtSenhaUsuario.setText(null);
            txtLoginUsuario.grabFocus();
        }
    }

    private class LimparAdm implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            txtLoginAdm.setText(null);
            txtNickAdm.setText(null);
            txtSenhaAdm.setText(null);
            txtLoginAdm.grabFocus();
        }
    }

    private class Sair implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            enviar(new Mensagem(Mensagem.PAUSAR));
            JanelaSalas js = new JanelaSalas(usuario, saida, entrada);
            JanelaManutencao.this.dispose();
        }
    }
}
