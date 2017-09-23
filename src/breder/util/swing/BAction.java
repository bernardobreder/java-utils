package breder.util.swing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import breder.util.task.Task;

/**
 * Ação
 * 
 * 
 * @author Bernardo Breder
 */
public class BAction extends AbstractAction {

  /** Tarefa */
  private Task task;

  /**
   * Construtor
   * 
   * @param title
   * @param task
   */
  public BAction(String title, Task task) {
    this(null, title, task);
  }

  /**
   * Construtor
   * 
   * @param icon
   * @param title
   * @param task
   */
  public BAction(Icon icon, String title, Task task) {
    super(title, icon);
    this.task = task;
  }

  /**
   * Construtor
   * 
   * @param icon
   * @param task
   */
  public BAction(Icon icon, Task task) {
    super("", icon);
    this.task = task;
  }

  /**
   * Construtor
   * 
   * @param icon
   * @param title
   * @param description
   * @param task
   */
  public BAction(Icon icon, String title, String description, Task task) {
    super(title, icon);
    this.task = task;
    this.putValue(SHORT_DESCRIPTION, description);
  }

  /**
   * Ação {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    this.task.start();
  }

}
