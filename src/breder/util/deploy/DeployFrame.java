package breder.util.deploy;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import breder.util.swing.GBC;

/**
 * Janela de Deploy
 * 
 * @author bernardobreder
 * 
 */
public class DeployFrame extends JFrame {

  /** Senha */
  private JPasswordField password;
  /** Progresso */
  private JProgressBar progress;
  /** Texto */
  private JTextArea area;

  /**
   * Construtor
   */
  public DeployFrame() {
    this.add(this.build());
    this.setTitle("Deploy");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(640, 480);
    this.setLocationRelativeTo(null);
  }

  /**
   * Constroi a janela
   * 
   * @return
   */
  private Component build() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.add(this.buildTextArea(), new GBC(0, 0).both());
    panel.add(this.buildProgress(), new GBC(0, 1).horizontal());
    panel.add(this.buildPassword(), new GBC(0, 2).horizontal());
    panel.add(this.buildButtons(), new GBC(0, 3).horizontal());
    return panel;
  }

  /**
   * Janela Texto
   * 
   * @return componente
   */
  private Component buildTextArea() {
    this.area = new JTextArea();
    JScrollPane scroll = new JScrollPane(area);
    scroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    return scroll;
  }

  /**
   * Janela Texto
   * 
   * @return componente
   */
  private Component buildProgress() {
    this.progress = new JProgressBar(0, 100);
    return progress;
  }

  /**
   * Janela Texto
   * 
   * @return componente
   */
  private Component buildPassword() {
    JLabel label = new JLabel("Password :");
    this.password = new JPasswordField();
    JPanel panel = new JPanel(new GridBagLayout());
    panel.add(label, new GBC(0, 0));
    panel.add(password, new GBC(1, 0).horizontal());
    return panel;
  }

  /**
   * Janela Texto
   * 
   * @return componente
   */
  private Component buildButtons() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    {
      JButton c = new JButton("Ok");
      c.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          onOkAction();
        }
      });
      this.getRootPane().setDefaultButton(c);
      panel.add(c);
    }
    {
      JButton c = new JButton("Cancel");
      c.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          onCancelAction();
        }
      });
      panel.add(c);
    }
    return panel;
  }

  /**
   * Ação de cancelar
   */
  protected void onCancelAction() {
    this.dispose();
  }

  /**
   * Ação de cancelar
   */
  protected void onOkAction() {
  }

  /**
   * @return the password
   */
  public JPasswordField getPassword() {
    return password;
  }

  /**
   * @param password the password to set
   */
  public void setPassword(JPasswordField password) {
    this.password = password;
  }

  /**
   * @return the progress
   */
  public JProgressBar getProgress() {
    return progress;
  }

  /**
   * @param progress the progress to set
   */
  public void setProgress(JProgressBar progress) {
    this.progress = progress;
  }

  /**
   * @return the area
   */
  public JTextArea getArea() {
    return area;
  }

  /**
   * @param area the area to set
   */
  public void setArea(JTextArea area) {
    this.area = area;
  }

}
