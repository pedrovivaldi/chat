package chat;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Caio
 */
public class JanelaCadastro extends JFrame {

    // componentes
    private JLabel lblLogin, lblNick, lblSenha, lblConfirmaSenha, lblImagem, imagemDeFundo;
    private JTextField txtLogin, txtNick;
    private JPasswordField txtSenha, txtConfirmaSenha;
    private JButton btnCadastrar, btnLimpar, btnCancelar, btnProcurar;
    // variaveis
    private ObjectInputStream entrada;
    private ObjectOutputStream saida;
    private String caminhoArquivo;
    private byte[] imagemAtual;
    private Mensagem msg;

    public JanelaCadastro(ObjectOutputStream out, ObjectInputStream in) {
        this.setLayout(null);
        this.setSize(550, 250);

        lblLogin = new JLabel("Login:");
        lblLogin.setLocation(30, 20);
        lblLogin.setSize(40, 25);
        this.add(lblLogin);

        lblNick = new JLabel("Nick:");
        lblNick.setLocation(30, 50);
        lblNick.setSize(40, 25);
        this.add(lblNick);

        lblSenha = new JLabel("Senha:");
        lblSenha.setLocation(30, 80);
        lblSenha.setSize(40, 25);
        this.add(lblSenha);

        lblImagem = new JLabel(redimensionaImagem(new File("images/avatar.gif")));
        lblImagem.setSize(100, 100);
        lblImagem.setLocation(350, 20);
        this.add(lblImagem);

        btnProcurar = new JButton("Procurar imagem...");
        btnProcurar.setSize(140, 25);
        btnProcurar.setLocation(340, 150);
        this.add(btnProcurar);
        btnProcurar.addActionListener(new ProcurarImagem());

        lblConfirmaSenha = new JLabel("Confirmar senha:");
        lblConfirmaSenha.setLocation(30, 110);
        lblConfirmaSenha.setSize(90, 25);
        this.add(lblConfirmaSenha);

        txtLogin = new JTextField(20);
        txtLogin.setLocation(130, 19);
        txtLogin.setSize(150, 23);
        this.add(txtLogin);

        txtNick = new JTextField(20);
        txtNick.setLocation(130, 49);
        txtNick.setSize(150, 23);
        this.add(txtNick);

        txtSenha = new JPasswordField(20);
        txtSenha.setLocation(130, 79);
        txtSenha.setSize(150, 23);
        this.add(txtSenha);

        txtConfirmaSenha = new JPasswordField(20);
        txtConfirmaSenha.setLocation(130, 109);
        txtConfirmaSenha.setSize(150, 23);
        this.add(txtConfirmaSenha);
        txtConfirmaSenha.addKeyListener(new Teclado());

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setLocation(240, 150);
        btnCadastrar.setSize(80, 25);
        this.add(btnCadastrar);
        btnCadastrar.addActionListener(new Cadastrar());

        btnLimpar = new JButton("Limpar");
        btnLimpar.setLocation(150, 150);
        btnLimpar.setSize(70, 25);
        this.add(btnLimpar);
        btnLimpar.addActionListener(new Limpar());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setLocation(50, 150);
        btnCancelar.setSize(80, 25);
        this.add(btnCancelar);
        btnCancelar.addActionListener(new Voltar());

        imagemDeFundo = new JLabel(redimensionaImagem(new ImageIcon("images/fundo.jpg")));
        imagemDeFundo.setSize(550, 250);
        this.add(imagemDeFundo);

        this.addWindowListener(new Janela());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.setIconImage(new ImageIcon("images/icon.png").getImage());
        this.setTitle("Cadastro");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        entrada = in;
        saida = out;
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

    private ImageIcon redimensionaImagem(ImageIcon ii) {
        ImageIcon img = new ImageIcon(ii.getImage());
        img.setImage(img.getImage().getScaledInstance(550, 250, 100));
        return img;
    }

    private class Janela extends WindowAdapter {
    }

    private class Teclado extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                btnCadastrar.doClick();
            }
        }
    }

    private class Limpar implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            limpar();
        }
    }

    private void limpar() {
        txtLogin.setText(null);
        txtSenha.setText(null);
        txtNick.setText(null);
        txtConfirmaSenha.setText(null);
        txtLogin.grabFocus();
        lblImagem.setIcon(redimensionaImagem(new File("images/avatar.gif")));
    }

    private class ProcurarImagem implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            caminhoArquivo = "";
            JFileChooser selecionarImagem = new JFileChooser();
            FileNameExtensionFilter filter1 = new FileNameExtensionFilter("Arquivo PNG (.png)", "png");
            FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Arquivo JPEG (.jpg)", "jpg");
            selecionarImagem.addChoosableFileFilter(filter1);
            selecionarImagem.addChoosableFileFilter(filter2);
            int selecao = selecionarImagem.showOpenDialog(null);
            if (selecao == JFileChooser.APPROVE_OPTION) {
                caminhoArquivo = selecionarImagem.getSelectedFile().getAbsolutePath();
                lblImagem.setIcon(redimensionaImagem(new File(caminhoArquivo)));
            }
        }
    }

    private class Cadastrar implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (txtNick.getText().compareTo("") != 0 && txtLogin.getText().compareTo("") != 0 && txtSenha.getText().compareTo("") != 0 && txtConfirmaSenha.getText().compareTo("") != 0) {
                if (txtSenha.getText().compareTo(txtConfirmaSenha.getText()) == 0) {
                    Usuario novo = new Usuario(txtLogin.getText(), txtNick.getText(), txtSenha.getText(), imagemAtual);
                    msg = new Mensagem(novo, Mensagem.CADASTRAR);
                    try {
                        saida.writeObject(msg);
                        saida.reset();
                        
                        Mensagem msgResposta = (Mensagem) entrada.readObject();
                        if (msgResposta.getTipo() == Mensagem.CADASTROU) {
                            JOptionPane.showMessageDialog(null, msgResposta.getConteudo(), "Cadastro", JOptionPane.INFORMATION_MESSAGE);
                            JanelaCadastro.this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, msgResposta.getConteudo(), "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(JanelaCadastro.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(JanelaCadastro.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Senhas diferentes");
                    btnLimpar.doClick();
                }
            }
        }
    }

    private class Voltar implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JanelaLogin jl = new JanelaLogin();
            JanelaCadastro.this.dispose();
        }
    }
}
