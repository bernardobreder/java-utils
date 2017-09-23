package breder.util.swing.table;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * Tabela simples
 * 
 * 
 * @author Bernardo Breder
 */
public class SimpleTable extends JTable {

  /** Listener */
  protected List<IOpenTableListener> listeners;

  /**
   * @param dm
   */
  public SimpleTable(TableModel dm) {
    super(dm);
  }

  /**
   * Construtor
   */
  public SimpleTable() {
  }

  /**
   * Dispara evento de click
   */
  public void fireOpenCellListener() {
    int row = this.getSelectedRow();
    if (row != -1) {
      this.fireOpenCellListener(row);
    }
  }

  /**
   * Dispara evento de click
   * 
   * @param row
   */
  public void fireOpenCellListener(int row) {
    if (listeners != null) {
      for (IOpenTableListener listener : listeners) {
        listener.actionPerformed(row);
      }
    }
  }

  /**
   * Cadastra Listener
   * 
   * @param listener
   */
  public void addOpenTableListener(IOpenTableListener listener) {
    if (this.listeners == null) {
      this.listeners = new ArrayList<IOpenTableListener>();
      this.getParent().getParent().addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2) {
            fireOpenCellListener();
            e.consume();
          }
        }
      });
      this.getParent().getParent().addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            fireOpenCellListener();
            e.consume();
          }
        }
      });
    }
    listeners.add(listener);
  }

}
