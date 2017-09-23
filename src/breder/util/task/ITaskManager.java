package breder.util.task;

/**
 * Gerenciador de Tarefas
 * 
 */
public interface ITaskManager {

  /**
   * Objeto padr�o
   */
  public static final ITaskManager DEFAULT = TaskManager.getInstance();

  /**
   * Retorna a tarefa corrente da Thread.
   * 
   * @return
   */
  public ITask getTask();

}
