package breder.util.swing.table;

import javax.swing.JPopupMenu;

/**
 * Listener de abertura de uma linha de uma tabela
 * 
 * 
 * @author Bernardo Breder
 */
public interface IOpenTableListener {

  /**
   * Executa ação
   * 
   * @param row
   */
  public void actionPerformed(int row);

  /**
   * Retorna o popup
   * 
   * @param row
   * @return popup
   */
  public JPopupMenu getPopupMenu(int row);

}
