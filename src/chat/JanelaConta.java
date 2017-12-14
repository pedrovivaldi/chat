package chat;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Caio
 */
public class JanelaConta extends JDialog {

    // componentes
    private JLabel lblLogin, lblNick, lblSenha, lblNovaSenha, lblConfirmaSenha, lblImagem, imagemDeFundo;
    private JTextField txtLogin, txtNick;
    private JPasswordField txtSenha, txtNovaSenha, txtConfirmaSenha;
    private JButton btnAlterar, btnLimpar, btnCancelar, btnExcluir, btnProcurar;
    // variaveis
    private Usuario user;
    private Mensagem msg;
    private ObjectInputStream entrada;
    private ObjectOutputStream saida;
    private byte[] imagemAtual;
    private String nome;

    public JanelaConta(String nome, Usuario usuario, ObjectOutputStream out, ObjectInputStream in) {
        user = usuario;
        entrada = in;
        saida = out;
        this.setLayout(null);
        this.setSize(550, 300);

        lblLogin = new JLabel("Login:");
        lblLogin.setLocation(30, 20);
        lblLogin.setSize(40, 25);
        this.add(lblLogin);

        lblNick = new JLabel("Nick:");
        lblNick.setLocation(30, 50);
        lblNick.setSize(40, 25);
        this.add(lblNick);

        lblSenha = new JLabel("Senha antiga:");
        lblSenha.setLocation(30, 80);
        lblSenha.setSize(80, 25);
        this.add(lblSenha);

        lblNovaSenha = new JLabel("Nova senha:");
        lblNovaSenha.setLocation(30, 110);
        lblNovaSenha.setSize(80, 25);
        this.add(lblNovaSenha);

        lblConfirmaSenha = new JLabel("Confirmar senha:");
        lblConfirmaSenha.setLocation(30, 140);
        lblConfirmaSenha.setSize(100, 25);
        this.add(lblConfirmaSenha);

        txtLogin = new JTextField(20);
        txtLogin.setLocation(130, 19);
        txtLogin.setSize(150, 23);
        txtLogin.setEditable(false);
        this.add(txtLogin);


        txtNick = new JTextField(20);
        txtNick.setLocation(130, 49);
        txtNick.setSize(150, 23);
        this.add(txtNick);

        txtSenha = new JPasswordField(20);
        txtSenha.setLocation(130, 79);
        txtSenha.setSize(150, 23);
        this.add(txtSenha);

        txtNovaSenha = new JPasswordField(20);
        txtNovaSenha.setLocation(130, 109);
        txtNovaSenha.setSize(150, 23);
        this.add(txtNovaSenha);

        txtConfirmaSenha = new JPasswordField(20);
        txtConfirmaSenha.setLocation(130, 139);
        txtConfirmaSenha.setSize(150, 23);
        this.add(txtConfirmaSenha);
        txtConfirmaSenha.addKeyListener(new Teclado());

        lblImagem = new JLabel();
        lblImagem.setSize(100, 100);
        lblImagem.setLocation(350, 20);
        this.add(lblImagem);

        btnProcurar = new JButton("Procurar imagem...");
        btnProcurar.setSize(140, 25);
        btnProcurar.setLocation(325, 140);
        this.add(btnProcurar);
        btnProcurar.addActionListener(new ProcurarImagem());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setLocation(40, 180);
        btnCancelar.setSize(74, 25);
        this.add(btnCancelar);
        btnCancelar.addActionListener(new Cancelar());

        btnLimpar = new JButton("Limpar");
        btnLimpar.setLocation(120, 180);
        btnLimpar.setSize(70, 25);
        this.add(btnLimpar);
        btnLimpar.addActionListener(new Limpar());

        btnAlterar = new JButton("Alterar dados");
        btnAlterar.setLocation(200, 180);
        btnAlterar.setSize(98, 25);
        this.add(btnAlterar);
        btnAlterar.addActionListener(new AlterarDados());

        btnExcluir = new JButton("Excluir cadastro");
        btnExcluir.setLocation(115, 220);
        btnExcluir.setSize(110, 25);
        this.add(btnExcluir);
        btnExcluir.addActionListener(new Excluir());

        imagemDeFundo = new JLabel(redimensionaImagem(new ImageIcon("images/fundo.jpg")));
        imagemDeFundo.setSize(550, 300);
        this.add(imagemDeFundo);

        this.nome = nome;
        
        this.setLocationRelativeTo(null);
        this.addWindowListener(new Janela());
        this.setIconImage(new ImageIcon("images/icon.png").getImage());
        this.setTitle("Conta");
        this.setResizable(false);
        this.setModal(true);
        this.setVisible(true);
    }
    

    private ImageIcon redimensionaImagem(byte[] fileBytes) {
        ImageIcon img = new ImageIcon(fileBytes);
        img.setImage(img.getImage().getScaledInstance(100, 100, 100));
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

    private ImageIcon redimensionaImagem(ImageIcon ii) {
        ImageIcon img = new ImageIcon(ii.getImage());
        img.setImage(img.getImage().getScaledInstance(550, 300, 100));
        return img;
    }

    private class Janela extends WindowAdapter {

        public void windowOpened(WindowEvent e) {
            lblImagem.setIcon(redimensionaImagem(user.getImagem()));
            txtLogin.setText(user.getLogin());
            txtNick.setText(user.getNick());
        }
        
    }

    private class Teclado extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                btnAlterar.doClick();
            }
        }
    }

    private class ProcurarImagem implements ActionListener {

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
                if (extensao.equals("png") || extensao.equals("jpg") || extensao.equals("gif")){
                    caminhoArquivo = selecionarImagem.getSelectedFile().getAbsolutePath();
                    lblImagem.setIcon(redimensionaImagem(new File(caminhoArquivo)));
                }
            }
        }
    }

    private class Limpar implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            txtSenha.setText(null);
            txtNick.setText(null);
            txtNovaSenha.setText(null);
            txtConfirmaSenha.setText(null);
            txtNick.grabFocus();
        }
    }


    private class AlterarDados implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (txtSenha.getText().equals(user.getSenha().trim())) {
                if (txtNovaSenha.getText().equals(txtConfirmaSenha.getText())) {
                    Usuario novoUser = user;
                    novoUser.setNick(txtNick.getText());
                    novoUser.setSenha(txtNovaSenha.getText());
                    novoUser.setImagem(imagemAtual);
                    msg = new Mensagem(novoUser,nome, Mensagem.ALTERAR_DADOS);
                    try {
                        saida.writeObject(msg);
                        saida.reset();
                        
                        Mensagem msgResposta = (Mensagem) entrada.readObject();
                            if (msgResposta.getTipo() == Mensagem.ALTEROU) {
                                user = (Usuario)msgResposta.getConteudo2();
                                JOptionPane.showMessageDialog(null, msgResposta.getConteudo());
                                JanelaConta.this.setVisible(false);
                            } else {
                                JOptionPane.showMessageDialog(null, msgResposta.getConteudo(), "Erro", 2);
                                btnLimpar.doClick();
                            }
                        }  catch (ClassNotFoundException ex) {
                        Logger.getLogger(JanelaLogin.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(JanelaLogin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Nova senha e confirmação não conferem");
                    btnLimpar.doClick();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Senha atual incorreta");
                btnLimpar.doClick();
            }
        }
    }
    
    public Usuario getUsuario(){
        return user;
    }

    private class Excluir implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            msg = new Mensagem(user.getLogin(), Mensagem.EXCLUIR_USUARIO);
            try {
                saida.writeObject(msg);
                saida.flush();

                Mensagem msgResposta = (Mensagem) entrada.readObject();
                if (msgResposta.getTipo() == Mensagem.EXCLUIU) {
                    JOptionPane.showMessageDialog(null, msgResposta.getConteudo());
                    JanelaConta.this.dispose();
                    JanelaLogin login = new JanelaLogin();
                } else {
                    JOptionPane.showMessageDialog(null, msgResposta.getConteudo(), "Erro", 2);
                    btnLimpar.doClick();
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(JanelaLogin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(JanelaLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class Cancelar implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JanelaConta.this.dispose();
        }
    }
}
