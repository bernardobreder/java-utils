package breder.util.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Campo de calendário
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class BComponentPopup extends JPanel {

  /** Calendario */
  private Component component;
  /** Texto */
  private JTextField field;
  /** Botão */
  private JButton button;
  /** Dialogo */
  private OpenDialog dialog;

  /**
   * Construtor
   * 
   * @param c
   */
  public BComponentPopup(Component c) {
    this.component = c;
    this.setLayout(new GridBagLayout());
    this.add(this.field = new JTextField(), new GBC(0, 0).horizontal());
    this.add(this.button = new JButton("..."), new GBC(1, 0));
    this.field.setEditable(false);
    this.button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dialog = new OpenDialog();
      }
    });
    this.refresh();
  }

  /**
   * Atualiza a interface
   */
  public abstract void refresh();

  /**
   * Retorna
   * 
   * @return dialog
   */
  public OpenDialog getDialog() {
    return dialog;
  }

  /**
   * Retorna
   * 
   * @return component
   */
  public Component getComponent() {
    return component;
  }

  /**
   * Retorna
   * 
   * @return field
   */
  public JTextField getField() {
    return field;
  }

  /**
   * Retorna
   * 
   * @return button
   */
  public JButton getButton() {
    return button;
  }

  /**
   * Janela de dialogo
   * 
   * 
   * @author Bernardo Breder
   */
  public class OpenDialog extends JFrame {

    /**
     * Construtor
     */
    public OpenDialog() {
      this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      this.add(component);
      this.addWindowFocusListener(new WindowAdapter() {
        @Override
        public void windowLostFocus(WindowEvent e) {
          dispose();
        }
      });
      this.setUndecorated(true);
      this.setSize(component.getPreferredSize().width, component
        .getPreferredSize().height);
      this.setLocation(button.getLocationOnScreen());
      {
        int x = this.getX();
        int w = this.getWidth();
        int y = this.getY();
        int h = this.getHeight();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        if (x + w > screen.width) {
          this.setLocation(screen.width - w, y);
        }
        if (y + h > screen.height) {
          this.setLocation(x, screen.height - h);
        }
      }
      this.setVisible(true);
    }
  }

}
