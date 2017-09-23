package breder.util.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import breder.util.lookandfeel.LookAndFeel;
import breder.util.swing.layout.VerticalLayout;

/**
 * Panel que resume ou não um conteúdo
 * 
 * 
 * @author Bernardo Breder
 */
public class BResumePanel extends JPanel {

  /** Component */
  private Component shortComp;
  /** Component */
  private Component longComp;
  /** Component */
  private JToggleButton shortButton;
  /** Component */
  private JToggleButton longButton;

  /**
   * Construtor
   * 
   * @param shortTitle
   * @param longTitle
   * @param shortComponent
   * @param longComponent
   */
  public BResumePanel(String shortTitle, String longTitle,
    Component shortComponent, Component longComponent) {
    if (shortTitle == null) {
      shortTitle = "Resumo";
    }
    if (longTitle == null) {
      longTitle = "Completo";
    }
    this.setLayout(new VerticalLayout(0));
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panel.add(buildShortButton(shortTitle));
    panel.add(buildLongButton(longTitle));
    this.add(panel);
    this.add(new JSeparator());
    shortComp = this.add(shortComponent);
    longComp = this.add(longComponent);
    this.firePanel(true);
  }

  /**
   * Dispara o aumento
   * 
   * @param isShort
   */
  protected void firePanel(boolean isShort) {
    this.longComp.setVisible(!isShort);
    this.shortComp.setVisible(isShort);
    this.longButton.setSelected(!isShort);
    this.shortButton.setSelected(isShort);
    this.validate();
    this.invalidate();
    this.repaint();
    if (this.getParent() != null) {
      Container c = this.getParent();
      while (c.getParent() != null) {
        c = c.getParent();
      }
      c.validate();
    }
  }

  /**
   * Constroi o botão
   * 
   * @param text
   * 
   * @return botão
   */
  private JToggleButton buildShortButton(String text) {
    shortButton = new JToggleButton(text);
    shortButton.setFocusable(false);
    shortButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        firePanel(true);
      }
    });
    return shortButton;
  }

  /**
   * Constroi o botão
   * 
   * @param text
   * 
   * @return botão
   */
  private JToggleButton buildLongButton(String text) {
    longButton = new JToggleButton(text);
    longButton.setFocusable(false);
    longButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        firePanel(false);
      }
    });
    return longButton;
  }

  /**
   * Retorna
   * 
   * @return shortComp
   */
  public Component getShortComp() {
    return shortComp;
  }

  /**
   * Retorna
   * 
   * @return longComp
   */
  public Component getLongComp() {
    return longComp;
  }

  /**
   * Retorna
   * 
   * @return shortButton
   */
  public JToggleButton getShortButton() {
    return shortButton;
  }

  /**
   * Retorna
   * 
   * @return longButton
   */
  public JToggleButton getLongButton() {
    return longButton;
  }

  /**
   * Inicializador
   * 
   * @param args
   */
  public static void main(String[] args) {
    Locale.setDefault(new Locale("pt"));
    LookAndFeel.getInstance().installRandom();
    JFrame frame = new JFrame();
    frame.add(new BResumePanel(null, null, new JTextField(), new JComboBox()));
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(800, 600);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
