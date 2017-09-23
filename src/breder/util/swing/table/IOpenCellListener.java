package breder.util.swing.table;

import javax.swing.JPopupMenu;

public interface IOpenCellListener<E> {

  /**
   * Executa ação
   * 
   * @param row
   * @param cell
   */
  public void actionPerformed(int row, E cell);

  /**
   * Retorna o popup
   * 
   * @param row
   * @param cell
   * @return popup
   */
  public JPopupMenu getPopupMenu(int row, E cell);

}
