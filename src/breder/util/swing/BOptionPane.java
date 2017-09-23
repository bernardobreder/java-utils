package breder.util.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import breder.util.lookandfeel.ILookAndFeel;
import breder.util.task.EventTask;

/**
 * Option pane
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class BOptionPane {

  /**
   * Solicita uma senha
   * 
   * @param window
   * @param title
   * @param message
   * @return senha
   */
  public static char[] showPasswordDialog(JWindow window, String title,
    String message) {
    PasswordFrame frame = new PasswordFrame(window, title, message);
    frame.setVisible(true);
    return frame.getResult();
  }

  /**
   * Solicita uma senha
   * 
   * @param window
   * @param title
   * @param message
   * @param time
   * @return {@link JOptionPane}
   */
  public static int showConfirmDialog(JWindow window, String title,
    String message, int time) {
    ConfirmFrame frame = new ConfirmFrame(window, title, message, time);
    frame.setVisible(true);
    return frame.getResult();
  }

  /**
   * Janela de senha
   * 
   * 
   * @author Bernardo Breder
   */
  private static class PasswordFrame extends JDialog {

    /** Botão Ok */
    private JButton okButton;
    /** Botão Cancelar */
    private JButton cancelButton;
    /** Botão Resultado */
    private char[] result;
    /** Botão Senha */
    private JPasswordField passwordField;

    /**
     * Construtor
     * 
     * @param window
     * @param title
     * @param message
     */
    public PasswordFrame(JWindow window, String title, String message) {
      super(window);
      this.setModal(true);
      this.setTitle(title);
      this.setLayout(new BorderLayout(10, 0));
      this.getInsets().set(10, 10, 10, 10);
      this.add(new JLabel(message, SwingConstants.CENTER), BorderLayout.NORTH);
      this.add(this.passwordField = new JPasswordField(), BorderLayout.CENTER);
      {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(this.okButton = new JButton("Ok"));
        panel.add(this.cancelButton = new JButton("Cancel"));
        this.getRootPane().setDefaultButton(okButton);
        okButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            result = passwordField.getPassword();
            dispose();
          }
        });
        cancelButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            dispose();
          }
        });
        this.add(panel, BorderLayout.SOUTH);
      }
      this.setResizable(false);
      this.pack();
      this.setSize(320, this.getHeight() + 100);
      this.setLocationRelativeTo(window);
      new Thread() {
        @Override
        public void run() {

        }
      }.start();
    }

    /**
     * Resultado
     * 
     * @return resultado
     */
    public char[] getResult() {
      return result;
    }

  }

  /**
   * Janela de senha
   * 
   * 
   * @author Bernardo Breder
   */
  private static class ConfirmFrame extends JDialog {

    /** Botão Ok */
    private JButton yesButton;
    /** Botão Cancelar */
    private JButton noButton;
    /** Botão Resultado */
    private int result;
    /** Thread */
    private Thread thread;

    /**
     * Construtor
     * 
     * @param window
     * @param title
     * @param message
     * @param time
     */
    public ConfirmFrame(JWindow window, String title, String message,
      final int time) {
      super(window);
      this.setModal(true);
      this.setTitle(title);
      this.setLayout(new BorderLayout(10, 0));
      this.add(new JLabel(message, SwingConstants.CENTER), BorderLayout.NORTH);
      {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(this.yesButton = new JButton("" + time));
        panel.add(this.noButton = new JButton("Não"));
        this.getRootPane().setDefaultButton(yesButton);
        yesButton.setEnabled(false);
        yesButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            result = JOptionPane.YES_OPTION;
            thread.interrupt();
            dispose();
          }
        });
        noButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            result = JOptionPane.NO_OPTION;
            thread.interrupt();
            dispose();
          }
        });
        this.add(panel, BorderLayout.SOUTH);
      }
      this.setResizable(false);
      this.pack();
      this.setSize(320, this.getHeight());
      this.setLocationRelativeTo(window);
      thread = new Thread(BOptionPane.class.getSimpleName()) {
        @Override
        public void run() {
          try {
            for (int n = time - 1; n >= 0; n--) {
              Thread.sleep(1000);
              final int m = n;
              EventTask.invokeLater(new Runnable() {
                @Override
                public void run() {
                  yesButton.setText("" + m);
                }
              });
            }
            EventTask.invokeLater(new Runnable() {
              @Override
              public void run() {
                yesButton.setText("Sim");
                yesButton.setEnabled(true);
              }
            });
          }
          catch (InterruptedException e) {
          }
        }
      };
      thread.start();
    }

    /**
     * Resultado
     * 
     * @return resultado
     */
    public int getResult() {
      return result;
    }

  }

  /**
   * Testador
   * 
   * @param args
   */
  public static void main(String[] args) {
    ILookAndFeel.DEFAULT.installRandom();
    // BOptionPane.showPasswordDialog(null, "Senha", "Me da uma senha");
    BOptionPane.showConfirmDialog(null, "Senha",
      "Deseja deletar todas as Entidades ?", 10);
  }

}
