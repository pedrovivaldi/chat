package chat;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooser extends JDialog{

  private JComboBox fontName;
  private JCheckBox fontBold, fontItalic;
  private JTextField fontSize;
  private SimpleAttributeSet attributes;
  private Font newFont;
  private String fontList[];
  private boolean OK;
  private Font f;

  public FontChooser(Frame parent) {
    super(parent, "Font Chooser", true);
    setSize(500, 100);
    attributes = new SimpleAttributeSet();

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        closeAndCancel();
      }
    });

    Container c = getContentPane();

    JPanel fontPanel = new JPanel();
    fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    fontName = new JComboBox(fontList);
    fontName.setSelectedIndex(20);
    fontSize = new JTextField("12", 4);
    fontSize.setHorizontalAlignment(SwingConstants.RIGHT);
    fontBold = new JCheckBox("Negrito");
    fontBold.setSelected(false);
    fontItalic = new JCheckBox("Itálico");

    fontPanel.add(fontName);
    fontPanel.add(new JLabel("Size: "));
    fontPanel.add(fontSize);
    fontPanel.add(fontBold);
    fontPanel.add(fontItalic);

    c.add(fontPanel, BorderLayout.NORTH);


    JPanel previewPanel = new JPanel(new BorderLayout());

    JButton okButton = new JButton("Confirmar");
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        closeAndSave();
      }
    });
    JButton cancelButton = new JButton("Cancelar");
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        closeAndCancel();
      }
    });

    JPanel controlPanel = new JPanel();
    controlPanel.add(okButton);
    controlPanel.add(cancelButton);
    previewPanel.add(controlPanel, BorderLayout.NORTH);

    c.add(previewPanel, BorderLayout.CENTER);
  }

  public String[] getNewFont() {
	  String[] aux = new String[4];
	  aux[0] = (String)fontName.getSelectedItem();
	  aux[1] = fontSize.getText();
	  if (fontBold.isSelected())
	  	aux[2] = "bold";
	  else
	  	aux[2] = "normal";
	  if (fontItalic.isSelected())
	  	aux[3] = "italic";
	  else
	  	aux[3] = "normal";
	  return aux;
	  }
  
  public AttributeSet getAttributes() { return attributes; }

  public void closeAndSave() {
    newFont = f;
    OK = true;

    setVisible(false);
  }

  public void closeAndCancel() {
    newFont = null;
    OK = false;
    setVisible(false);
  }

  public boolean getOK()
  {
	  return OK;
  }
}
