package breder.util.swing;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListModel;

import breder.util.swing.table.IOpenCellListener;

public class BList<E> extends JList {

  protected List<IOpenCellListener<E>> listeners;

  public BList(ListModel dataModel) {
    super(dataModel);
  }

  public void addOpenCellListener(IOpenCellListener<E> listener) {
    if (listeners == null) {
      listeners = new ArrayList<IOpenCellListener<E>>();
      this.addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2) {
            fireOpenCellListener();
            e.consume();
          }
        }

      });
      this.addKeyListener(new KeyAdapter() {

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

  public void fireOpenCellListener() {
    int row = this.getSelectedIndex();
    if (row != -1) {
      E cell = (E) getModel().getElementAt(row);
      this.fireOpenCellListener(row, cell);
    }
  }

  public void fireOpenCellListener(int row, E cell) {
    for (IOpenCellListener<E> listener : listeners) {
      listener.actionPerformed(row, cell);
    }
  }

}
