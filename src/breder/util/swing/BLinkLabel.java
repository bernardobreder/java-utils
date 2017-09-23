package breder.util.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import breder.util.task.EventTask;

/**
 * Link
 * 
 * 
 * @author Bernardo Breder
 */
public class BLinkLabel extends JLabel {

  /** Listeners */
  private List<ActionListener> listeners;

  /**
   * Construtor
   * 
   * @param icon
   * @param text
   * @param listener
   */
  public BLinkLabel(Icon icon, String text, ActionListener listener) {
    super(text, icon, JLabel.LEFT);
    this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    this.addMouseListener(new MouseAdapter() {
      /**
       * {@inheritDoc}
       */
      @Override
      public void mouseEntered(MouseEvent e) {
        setForeground(Color.BLUE);
        e.consume();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void mouseExited(MouseEvent e) {
        setForeground(Color.BLACK);
        e.consume();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void mouseClicked(MouseEvent e) {
        fireActionListener();
      }
    });
    this.addActionListener(listener);
    this.setFocusable(true);
  }

  /**
   * Dispara o evento de click
   */
  public void fireActionListener() {
    if (this.listeners != null) {
      for (ActionListener listener : this.listeners) {
        listener.actionPerformed(new ActionEvent(this, -1, ""));
      }
    }
  }

  /**
   * Adiciona um evento
   * 
   * @param listener
   */
  public void addActionListener(ActionListener listener) {
    if (listener != null) {
      if (listeners == null) {
        this.listeners = new ArrayList<ActionListener>();
      }
      this.listeners.add(listener);
    }
  }

  /**
   * Testador
   * 
   * @param args
   */
  public static void main(String[] args) {
    EventTask.invokeLater(new Runnable() {
      @Override
      public void run() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new BLinkLabel(null, "Abrir", new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            StandardDialogs.showInfoDialog(null, "Test", "Teste");
          }
        }));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
      }
    });
  }

}
