package chat;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author u11155
 */
public class JanelaLogin extends JFrame {

    // componentes
    private final int porta = 20000;
    private JLabel lblLogin, lblSenha, lblCadastrar;
    private JButton btnLimpar, btnCadastrar, btnLogar;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JLabel imagemDeFundo;
    // variaveis
    private Socket socket;
    private ObjectInputStream entrada;
    private ObjectOutputStream saida;
    private Mensagem msg;
    private Usuario user;

    public JanelaLogin() {
        if (!estabelecerConexao()) {
            JOptionPane.showMessageDialog(null, "Servidor offline, tente + tarde.", "Erro", 2);
            System.exit(0);
        }

        this.setLayout(null);
        this.setSize(350, 210);

        lblLogin = new JLabel("Login:");
        lblLogin.setLocation(30, 10);
        lblLogin.setSize(40, 40);
        this.add(lblLogin);

        lblSenha = new JLabel("Senha:");
        lblSenha.setLocation(30, 50);
        lblSenha.setSize(40, 40);
        this.add(lblSenha);

        lblCadastrar = new JLabel("Ainda não possui uma conta? Cadastre-se:");
        lblCadastrar.setLocation(30, 130);
        lblCadastrar.setSize(220, 20);
        this.add(lblCadastrar);

        btnLimpar = new JButton("Limpar");
        btnLimpar.setLocation(50, 90);
        btnLimpar.setSize(70, 25);
        this.add(btnLimpar);
        btnLimpar.addActionListener(new Limpar());

        btnLogar = new JButton("Logar");
        btnLogar.setLocation(140, 90);
        btnLogar.setSize(70, 25);
        this.add(btnLogar);
        btnLogar.addActionListener(new Logar());

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setLocation(240, 125);
        btnCadastrar.setSize(80, 25);
        this.add(btnCadastrar);
        btnCadastrar.addActionListener(new Cadastrar());

        txtLogin = new JTextField(20);
        txtLogin.setLocation(80, 20);
        txtLogin.setSize(150, 25);
        this.add(txtLogin);

        txtSenha = new JPasswordField(20);
        txtSenha.setLocation(80, 55);
        txtSenha.setSize(150, 25);
        this.add(txtSenha);
        txtSenha.addKeyListener(new Teclado());

        imagemDeFundo = new JLabel(redimensionaImagem(new ImageIcon("images/fundo3.png")));
        imagemDeFundo.setSize(350, 210);
        this.add(imagemDeFundo);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Login");
        this.setIconImage(new ImageIcon("images/icon.png").getImage());
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }

    public JanelaLogin(ObjectOutputStream out, ObjectInputStream in) {
        this.setLayout(null);
        this.setSize(350, 210);

        lblLogin = new JLabel("Login:");
        lblLogin.setLocation(30, 10);
        lblLogin.setSize(40, 40);
        this.add(lblLogin);

        lblSenha = new JLabel("Senha:");
        lblSenha.setLocation(30, 50);
        lblSenha.setSize(40, 40);
        this.add(lblSenha);

        lblCadastrar = new JLabel("Ainda não possui uma conta? Cadastre-se:");
        lblCadastrar.setLocation(30, 130);
        lblCadastrar.setSize(220, 20);
        this.add(lblCadastrar);

        btnLimpar = new JButton("Limpar");
        btnLimpar.setLocation(50, 90);
        btnLimpar.setSize(70, 25);
        this.add(btnLimpar);
        btnLimpar.addActionListener(new Limpar());

        btnLogar = new JButton("Logar");
        btnLogar.setLocation(140, 90);
        btnLogar.setSize(70, 25);
        this.add(btnLogar);
        btnLogar.addActionListener(new Logar());

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setLocation(240, 125);
        btnCadastrar.setSize(80, 25);
        this.add(btnCadastrar);
        btnCadastrar.addActionListener(new Cadastrar());

        txtLogin = new JTextField(20);
        txtLogin.setLocation(80, 20);
        txtLogin.setSize(150, 25);
        this.add(txtLogin);

        txtSenha = new JPasswordField(20);
        txtSenha.setLocation(80, 55);
        txtSenha.setSize(150, 25);
        this.add(txtSenha);
        txtSenha.addKeyListener(new Teclado());

        imagemDeFundo = new JLabel(redimensionaImagem(new ImageIcon("images/fundo3.png")));
        imagemDeFundo.setSize(350, 210);
        this.add(imagemDeFundo);

        this.addWindowListener(new Janela());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Login");
        this.setIconImage(new ImageIcon("icon.png").getImage());
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        entrada = in;
        saida = out;
    }

    private ImageIcon redimensionaImagem(ImageIcon ii) {
        ImageIcon img = new ImageIcon(ii.getImage());
        img.setImage(img.getImage().getScaledInstance(350, 210, 100));
        return img;
    }

    private class Janela extends WindowAdapter {

        @Override
        public void windowClosed(WindowEvent e) {
        }
    }

    private class Teclado extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                btnLogar.doClick();
            }
        }
    }

    private class Limpar implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            limpar();
        }
    }

    private boolean estabelecerConexao() {
        try {
            socket = new Socket("localhost", porta);
            //socket = new Socket("10.0.2.16", porta);
            saida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private class Logar implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (txtLogin.getText().compareTo("") != 0 && txtSenha.getText().compareTo("") != 0) {
                String texto = txtLogin.getText() + "::" + txtSenha.getText();
                msg = new Mensagem(texto, Mensagem.LOGAR);
                try {
                    saida.writeObject(msg);
                    saida.reset();
                } catch (IOException ex) {
                    Logger.getLogger(JanelaLogin.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    Mensagem novaMensagem = (Mensagem) entrada.readObject();
                    if (novaMensagem != null) {
                        if (novaMensagem.getTipo() == Mensagem.LOGOU) {
                            user = (Usuario) novaMensagem.getConteudo();
                            JanelaSalas j = null;
                            j = new JanelaSalas(user, saida, entrada);
                            JanelaLogin.this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, novaMensagem.getConteudo(), "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                        novaMensagem = null;
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    // servidor caiu
                }
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos");
                limpar();
            }
        }
    }

    private void limpar() {
        txtLogin.setText(null);
        txtSenha.setText(null);
        txtLogin.grabFocus();
    }

    private class Cadastrar implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            limpar();
            JanelaCadastro cadastro = new JanelaCadastro(saida, entrada);
        }
    }
}
