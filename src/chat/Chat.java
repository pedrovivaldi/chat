package chat;


import javax.swing.UIManager;

/**
 * @author u11155
 */
public class Chat {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
        }
        JanelaLogin login = new JanelaLogin();
    }
}
