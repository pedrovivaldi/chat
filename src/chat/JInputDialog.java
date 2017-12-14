/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.awt.Component;
import java.awt.event.*;
import java.util.Locale;
import javax.swing.*;

public class JInputDialog extends JDialog {

    public static final int SENHA = 0, NORMAL = 1;
    private JPasswordField campo1;
    private JTextField campo2;
    private JLabel nome;
    private String texto;
    private JButton ok;
    private int tipo;
    private boolean foi;

    public JInputDialog(int tipo, String titulo) {
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setSize(270, 100);
        this.setTitle(titulo);

        this.tipo = tipo;

        nome = new JLabel();
        nome.setLocation(10, 20);
        nome.setSize(60, 25);
        this.add(nome);
        if (tipo == SENHA) {
            nome.setText("Senha:");
            campo1 = new JPasswordField(15);
            campo1.setLocation(70, 20);
            campo1.setSize(100, 20);
            campo1.addKeyListener(new Teclado());
            this.add(campo1);
        } else if (tipo == NORMAL) {
            nome.setText("IP:");
            campo2 = new JTextField(15);
            campo2.setLocation(70, 20);
            campo2.setSize(100, 20);
            campo2.addKeyListener(new Teclado());
            this.add(campo2);
        }
        
        texto = "";
        
        ok = new JButton("OK");
        ok.addActionListener(new OK());
        ok.setSize(50, 30);
        ok.setLocation(180, 18);
        this.add(ok);
        
        foi = false;
        this.setModal(true);
        this.setVisible(true);
    }

    private class OK implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (tipo == SENHA) {
                texto = campo1.getText();
            } else {
                texto = campo2.getText();
            }
            foi = true;
            JInputDialog.this.dispose();
        }
    }
    

    private class Teclado extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                ok.doClick();
            }
        }
    }
    
    public boolean getOk(){
        return foi;
    }

    public String getText() {
        return texto;
    }
}
