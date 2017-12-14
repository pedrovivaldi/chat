package chat;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author u11155
 */
public class JanelaSalas extends JFrame {

    // componentes
    private JLabel lblUsuario, imagemDeFundo, lblUsuarios;
    private JComboBox cbxCategorias;
    private JScrollPane sp, sp2;
    private JButton btnVoltar, btnAtualizar, btnEntrar, btnManutencao;
    private JList lsbSalas, lsbUsuarios;
    //variaveis
    private Salas salas;
    private Usuario user;
    private Mensagem msg;
    private ObjectInputStream entrada;
    private ObjectOutputStream saida;
    private Receptor receptor;

    public JanelaSalas(Usuario usuario, ObjectOutputStream out, ObjectInputStream in) {
        this.setLayout(null);
        this.setSize(475, 560);

        lblUsuario = new JLabel("Bem Vindo, ");
        lblUsuario.setLocation(30, 10);
        lblUsuario.setSize(100, 40);
        this.add(lblUsuario);
        
        lblUsuarios = new JLabel("Usuários");
        lblUsuarios.setLocation(230, 80);
        lblUsuarios.setSize(70, 40);
        this.add(lblUsuarios);

        String a[] = {"Todas"};
        cbxCategorias = new JComboBox(a);
        cbxCategorias.setLocation(30, 60);
        cbxCategorias.setSize(100, 20);
        this.add(cbxCategorias);
        cbxCategorias.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                btnAtualizar.doClick();
            }
        });

        lsbSalas = new JList();
        lsbSalas.addMouseListener(new Mouse());
        lsbSalas.addKeyListener(new Teclado());
        // Scrolling
        sp = new JScrollPane(lsbSalas);
        sp.setLocation(30, 110);
        sp.setSize(180, 350);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(sp);
        
        lsbUsuarios = new JList();
        // Scrolling
        sp2 = new JScrollPane(lsbUsuarios);
        sp2.setLocation(230, 110);
        sp2.setSize(180, 350);
        sp2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(sp2);

        btnManutencao = new JButton("Manutenção");
        btnManutencao.setLocation(230, 20);
        btnManutencao.setSize(100, 25);
        this.add(btnManutencao);
        btnManutencao.setVisible(false);
        btnManutencao.addActionListener(new Manutencao());

        btnVoltar = new JButton("Voltar");
        btnVoltar.setLocation(50, 480);
        btnVoltar.setSize(70, 25);
        this.add(btnVoltar);
        btnVoltar.addActionListener(new Voltar());

        btnEntrar = new JButton("Entrar");
        btnEntrar.setLocation(260, 480);
        btnEntrar.setSize(70, 25);
        this.add(btnEntrar);
        btnEntrar.addActionListener(new Entrar());


        imagemDeFundo = new JLabel(redimensionaImagem(new ImageIcon("images/fundo3.png")));
        imagemDeFundo.setSize(475, 560);
        this.add(imagemDeFundo);

        this.addWindowListener(new Janela());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Escolher sala");
        this.setIconImage(new ImageIcon("images/icon.png").getImage());
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        user = usuario;
        entrada = in;
        saida = out;
    }

    private ImageIcon redimensionaImagem(ImageIcon ii) {
        ImageIcon img = new ImageIcon(ii.getImage());
        img.setImage(img.getImage().getScaledInstance(475, 560, 100));
        return img;
    }

    private void pedirSalas() {
        try {
            Thread.sleep(150);
        } catch (InterruptedException ex) {
            Logger.getLogger(JanelaSalas.class.getName()).log(Level.SEVERE, null, ex);
        }
        msg = new Mensagem(Mensagem.PEDIR_SALAS);
        try {
            saida.writeObject(msg);
            saida.reset();
        } catch (IOException ex) {
            Logger.getLogger(JanelaLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class Janela extends WindowAdapter {

        @Override
        public void windowOpened(WindowEvent e) {
            pedirSalas();
            receptor = new Receptor(entrada);
            receptor.start();
            lblUsuario.setText("Bem Vindo, " + user.getLogin() + " !");
            switch (user.getTipo()) {
                case "U":
                    btnManutencao.setVisible(false);
                    break;
                case "A":
                    btnManutencao.setVisible(true);
                    break;
            }
        }
    }

    private void atualizarSalas(Mensagem aux) {
        salas = (Salas) aux.getConteudo();
        DefaultListModel dlm = new DefaultListModel();
        for (int i = 0; i < salas.qntasSalas(); i++) {
            Sala novaSala = salas.getSala(i);
            synchronized (this) {
                dlm.addElement(novaSala.getNome() + " (" + novaSala.getQtdeOcupados() + " / " + novaSala.getQtdeMax() + ")");
            }
        }
        lsbSalas.setModel(dlm);
    }

    private class Receptor extends Thread {

        private ObjectInputStream entrada;
        private boolean parar;

        public Receptor(ObjectInputStream in) {
            entrada = in;
            parar = false;
        }

        @Override
        public void run() {
            while (!parar) {
                try {
                        Mensagem novaMensagem = (Mensagem) entrada.readObject();
                        switch (novaMensagem.getTipo()) {
                            case Mensagem.DEVOLVE_SALAS:
                                atualizarSalas(novaMensagem);
                                break;
                            case Mensagem.ERRO:
                                JOptionPane.showMessageDialog(null, novaMensagem.getConteudo(), "Erro", JOptionPane.ERROR_MESSAGE);
                                break;
                            case Mensagem.ENTROU_SALA:
                                JanelaChat janela = new JanelaChat(salas.getSala(lsbSalas.getSelectedIndex()), user, saida, entrada);
                                JanelaSalas.this.dispose();
                                receptor.stop();
                                break;
                            case Mensagem.PAUSAR:
                                parar = true;
                                break;
                        }
                } catch (IOException ex) {
                    Logger.getLogger(JanelaSalas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException e) {
                }

            }
        }
    }

    private class Teclado extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                btnEntrar.doClick();
            }
        }
    }
    
    private class Mouse extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            DefaultListModel dfm = new DefaultListModel();
            lsbUsuarios.setModel(dfm);
            if (lsbSalas.getSelectedIndex() != - 1) {
                for (int i = 0; i < salas.getSala(lsbSalas.getSelectedIndex()).getQtdeOcupados(); i++) {
                    dfm.addElement(salas.getSala(lsbSalas.getSelectedIndex()).getUsuarios().getUsuario(i).getNick());
                }
            }
        }
    }

    private class Voltar implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JanelaLogin login = new JanelaLogin();
            JanelaSalas.this.dispose();
            receptor.stop();
        }
    }

    private class Manutencao implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                saida.writeObject(new Mensagem(Mensagem.PAUSAR));
                saida.reset();
            } catch (IOException ex) {
            Logger.getLogger(JanelaLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
            synchronized (entrada) {
                JanelaManutencao manut = new JanelaManutencao(user, saida, entrada);
            }
            JanelaSalas.this.dispose();
            

        }
    }

    private void entrarSala() {
        msg = new Mensagem(salas.getSala(lsbSalas.getSelectedIndex()), Mensagem.ENTRAR_SALA);
        try {
            saida.writeObject(msg);
            saida.reset();
        } catch (IOException ex) {
            Logger.getLogger(JanelaLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class Entrar implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (lsbSalas.getSelectedValue() != null) {
                String senhaSala = salas.getSala(lsbSalas.getSelectedIndex()).getSenha();
                if (!senhaSala.equals("")) {
                    String senha = "";
                    JInputDialog id = new JInputDialog(0, "Digite a senha da sala:");
                    senha = id.getText();
                    //senha = JOptionPane.showInputDialog(null, "Digite a senha da sala:");
                    if (senha.equals(senhaSala)) {
                        entrarSala();
                    } else {
                        JOptionPane.showMessageDialog(null, "Senha incorreta");
                    }
                } else {
                    entrarSala();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione uma sala para entrar ");
            }
        }
    }
}
