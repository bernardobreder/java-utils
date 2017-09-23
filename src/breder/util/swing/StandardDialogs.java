/*
 * Created on Mar 28, 2005
 */
package breder.util.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import breder.util.util.LNG;
import breder.util.util.LNGKeys;

/**
 * A classe <code>StandardDialogs</code> fornece m�todos de conveni�ncia para a
 * cria��o de di�logos com o usu�rio.
 * 
 */
public class StandardDialogs {

  /** Op��o de confirma��o para o SIM */
  public static final int YES_OPTION = JOptionPane.YES_OPTION;
  /** Op��o de confirma��o para o N�O */
  public static final int NO_OPTION = JOptionPane.NO_OPTION;
  /** Op��o de confirma��o para o CANCELA */
  public static final int CANCEL_OPTION = JOptionPane.CANCEL_OPTION;

  /**
   * Mostra um diálogo com uma informação.
   * 
   * @param component O componente que solicitou o diálogo.
   * @param title o título do diálogo.
   * @param msg A mensagem a ser exibida.
   */
  public static void showInfoDialog(Component component, String title,
    Object msg) {
    JOptionPane.showMessageDialog(component, msg, title,
      JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Mostra um diálogo com uma informação.
   * 
   * @param component O componente que solicitou o diálogo.
   * @param title o título do diálogo.
   * @param text
   * @param titles
   * @return campos
   */
  public static String[] showInputDialog(Window component, String title,
    String text, String[] titles) {
    Dialog dialog = new Dialog(component, title, text, titles);
    dialog.pack();
    dialog.setSize(new Dimension(dialog.getWidth() + 100,
      dialog.getHeight() + 20));
    dialog.setLocationRelativeTo(null);
    dialog.setModal(true);
    dialog.setVisible(true);
    return dialog.results;
  }

  /**
   * Mostra um diálogo com uma pergunta e oferece um conjunto de botões com os
   * títulos especificados como alternativas de resposta.
   * 
   * @param component A janela principal que solicitou esse diálogo.
   * @param title O título do diálogo.
   * @param question A pergunta feita pelo diálogo.
   * @param options Um array com os títulos dos botões de opção.
   * 
   * @return O índice da opção escolhida.
   * 
   * @throws IllegalArgumentException se o array for nulo ou vazio.
   */
  public static int showOptionDialog(Component component, String title,
    Object question, Object[] options) {
    if (options == null || options.length == 0) {
      throw new IllegalArgumentException("options == null");
    }
    if (options.length == 0) {
      throw new IllegalArgumentException("options.length == 0");
    }
    return JOptionPane.showOptionDialog(component, question, title,
      JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
      options[0]);
  }

  /**
   * Mostra uma janela de di�logo com os bot�es "Sim", "N�o" e "Cancelar".
   * 
   * @param window O componente pai que solicitou o di�logo.
   * @param title O t�tulo do di�logo.
   * @param question A pergunta feita pelo di�logo.
   * 
   * @return �ndice da resposta (ver {@link JOptionPane}) .
   */
  public static int showYesNoCancelDialog(Component window, String title,
    Object question) {
    Object[] options =
      { LNG.get(LNGKeys.YES), LNG.get(LNGKeys.NO), LNG.get(LNGKeys.CANCEL) };
    return showOptionDialog(window, title, question, options);
  }

  /**
   * Mostra uma janela de di�logo com os bot�es "Sim" e "N�o".
   * 
   * @param component O componente que solicitou o di�logo.
   * @param title O t�tulo do di�logo.
   * @param question A pergunta feita pelo di�logo.
   * 
   * @return o �ndice da op��o selecionada (ver {@link JOptionPane}).
   */
  public static int showYesNoDialog(final Component component,
    final String title, final Object question) {
    final Object[] options = { "Sim", "Não" };
    return showOptionDialog(component, title, question, options);
  }

  /**
   * Mostra uma janela de di�logo com os bot�es "Sim", "Sim para todos", "N�o" e
   * "N�o para todos".
   * 
   * @param component O componente que solicitou o di�logo.
   * @param title O t�tulo do di�logo.
   * @param question A pergunta feita pelo di�logo.
   * 
   * @return o �ndice da op��o selecionada (ver {@link JOptionPane}).
   */
  public static int showYesNoAllDialog(final Component component,
    final String title, final Object question) {
    final Object[] options =
      { LNG.get(LNGKeys.YES), LNG.get(LNGKeys.YES_TO_ALL), LNG.get(LNGKeys.NO),
          LNG.get(LNGKeys.NO_TO_ALL) };
    return showOptionDialog(component, title, question, options);
  }

  /**
   * Mostra um di�logo de aviso de erro.
   * 
   * @param component A janela principal que solicitou esse di�logo.
   * @param title O t�tulo do di�logo.
   * @param msg A mensagem a ser exibida.
   */
  public static void showErrorDialog(Component component, String title,
    String msg) {
    JOptionPane.showMessageDialog(component, msg, title,
      JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Mostra um di�logo de alerta (warning).
   * 
   * @param component A janela principal que solicitou esse di�logo.
   * @param title O t�tulo do di�logo.
   * @param msg A mensagem a ser exibida.
   */
  public static void showWarningDialog(Component component, String title,
    String msg) {
    JOptionPane.showMessageDialog(component, msg, title,
      JOptionPane.WARNING_MESSAGE);
  }

  /**
   * Dialogo para solicitar o nome
   * 
   * 
   * @author Bernardo Breder
   */
  private static class Dialog extends JDialog {

    /** Resultado */
    public String[] results;

    /**
     * Construtor
     * 
     * @param window
     * @param title
     * @param text
     * @param labels
     */
    public Dialog(Window window, String title, String text, String... labels) {
      super(window);
      this.setTitle(title);
      this.add(this.buildCenter(text, labels));
    }

    /**
     * Constroi o painel
     * 
     * @param text
     * @param labels
     * @return painel
     */
    private Component buildCenter(String text, final String... labels) {
      KeyAdapter keyListener = new KeyAdapter() {
        /**
         * {@inheritDoc}
         */
        @Override
        public void keyReleased(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            e.consume();
            close();
          }
        }
      };
      this.addKeyListener(keyListener);
      JPanel panel = new JPanel(new GridBagLayout());
      JTextArea area = new JTextArea(text);
      area.setEditable(false);
      area.setOpaque(false);
      area.setWrapStyleWord(true);
      area.setFocusable(false);
      // JScrollPane areaScroll = new JScrollPane(area);
      // areaScroll.setOpaque(false);
      // areaScroll.setPreferredSize(new Dimension(400,
      // area.getPreferredSize().height
      // +
      // areaScroll.getHorizontalScrollBar().getPreferredSize().height));
      // areaScroll.setBorder(BorderFactory.createEmptyBorder());
      panel.add(area, new GBC(0, 0).gridwh(2, 1).both());
      final JTextField[] fields = new JTextField[labels.length];
      for (int n = 0; n < labels.length; n++) {
        String title = labels[n];
        panel.add(new JLabel(title), new GBC(0, n + 1));
        panel.add(fields[n] = new JTextField(), new GBC(1, n + 1).horizontal());
        fields[n].addKeyListener(keyListener);
      }
      {
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            close();
          }
        });
        cancelButton.addKeyListener(keyListener);
        buttons.add(cancelButton);
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            results = new String[labels.length];
            for (int n = 0; n < labels.length; n++) {
              results[n] = fields[n].getText().trim();
            }
            close();
          }
        });
        okButton.addKeyListener(keyListener);
        buttons.add(okButton);
        this.getRootPane().setDefaultButton(okButton);
        panel.add(buttons, new GBC(0, labels.length + 1).gridwh(2, 1)
          .horizontal());
      }
      return panel;
    }

    /**
     * Fecha a janela
     */
    protected void close() {
      dispose();
    }

  }

  /**
   * Testador
   * 
   * @param args
   */
  public static void main(String[] args) {
    System.out.println(Arrays.toString(StandardDialogs.showInputDialog(null,
      "Titulo", "Textof dadskjhfsadk ",
      new String[] { "Firstname", "Lastname" })));
  }

}
