package breder.util.swing.table;

import javax.swing.JPopupMenu;

public class AbstractOpenCellListener<E> implements IOpenCellListener<E> {

  @Override
  public void actionPerformed(int row, E cell) {
  }

  @Override
  public JPopupMenu getPopupMenu(int row, E cell) {
    return null;
  }

}
