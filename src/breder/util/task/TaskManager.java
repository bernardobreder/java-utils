package breder.util.task;

/**
 * Gerenciador de tarefas
 * 
 */
public class TaskManager implements ITaskManager {

  private static final TaskManager instance = new TaskManager();

  private ThreadLocal<ITask> tasks = new ThreadLocal<ITask>();

  private TaskManager() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ITask getTask() {
    return tasks.get();
  }

  /**
   * Adiciona uma tarefa
   * 
   * @param task
   */
  public void addTask(ITask task) {
    tasks.set(task);
  }

  /**
   * Remove uma tarefa
   * 
   * @param task
   */
  public void removeTask(ITask task) {
    tasks.remove();
  }

  /**
   * Retorna o singleton
   * 
   * @return
   */
  public static TaskManager getInstance() {
    return instance;
  }

}
