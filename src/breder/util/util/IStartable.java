package breder.util.util;

import breder.util.task.ITask;

/**
 * Interface que mostrar tudo que � inicializavel
 * 
 */
public interface IStartable {

  /**
   * Inicializa a estrutura
   */
  public ITask start();

}
