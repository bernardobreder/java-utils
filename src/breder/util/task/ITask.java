package breder.util.task;

import java.awt.event.ActionListener;

import breder.util.util.IStartable;

/**
 * Uma tarefa a ser realizado
 * 
 */
public interface ITask extends ActionListener, IStartable {

  /**
   * Interrumpe a tarefa.
   */
  public void interrupt();

  /**
   * Indica se a tarefa foi interrumpida ou não
   * 
   * @return
   */
  public boolean isInterrupted();

  /**
   * Adiciona um evento no finally
   * 
   * @param runnable
   */
  public void addFinally(Runnable runnable);

}
