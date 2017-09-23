package breder.util.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import breder.util.util.ExceptionUtil;

/**
 * Dialogo de Erro
 * 
 * 
 * @author Bernardo Breder
 */
public class BExceptonFrame extends JDialog {

  /**
   * Construtor
   * 
   * @param e
   */
  public BExceptonFrame(Throwable e) {
    this.setTitle(e.getClass().getSimpleName());
    this.setModal(true);
    this.add(this.build(e));
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setSize(320, 240);
    this.setLocationRelativeTo(null);
  }

  /**
   * Controi os componentes
   * 
   * @param e
   * 
   * @return componentes
   */
  private Component build(Throwable e) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(this.buildMessage(e), BorderLayout.NORTH);
    panel.add(this.buildDetail(e), BorderLayout.CENTER);
    panel.add(this.buildButtons(), BorderLayout.SOUTH);
    return panel;
  }

  /**
   * Constroi os botões
   * 
   * @return painel
   */
  private Component buildButtons() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    {
      JButton button = new JButton("Ok");
      button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          BExceptonFrame.this.dispose();
        }
      });
      panel.add(button);
    }
    return panel;
  }

  /**
   * Constroi o detalhe
   * 
   * @param e
   * 
   * @return painel
   */
  private Component buildDetail(Throwable e) {
    String text = ExceptionUtil.buildMessage(e);
    JTextArea area = new JTextArea(text);
    area.setEditable(false);
    JScrollPane scroll = new JScrollPane(area);
    scroll.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    return scroll;
  }

  /**
   * Constroi a mensagem
   * 
   * @param e
   * 
   * @return painel
   */
  private Component buildMessage(Throwable e) {
    JLabel c = new JLabel(e.getMessage());
    c.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    return c;
  }

}
