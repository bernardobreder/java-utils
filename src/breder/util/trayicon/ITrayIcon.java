package breder.util.trayicon;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

/**
 * Configura o trayicon
 * 
 * 
 * @author Bernardo Breder
 */
public interface ITrayIcon extends ActionListener {

  /**
   * Retorna a janela
   * 
   * @return janela
   */
  public JFrame getMainFrame();

  /**
   * Retorna os menus
   * 
   * @return menus
   */
  public MenuItem[] buildMenu();

  /**
   * Retorna o nome do projeto
   * 
   * @return projeto
   */
  public String getName();

  /**
   * Retorna o icone
   * 
   * @return retorna o icone
   */
  public Image getIcon();

}
