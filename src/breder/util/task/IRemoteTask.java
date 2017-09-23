package breder.util.task;

/**
 * Tarefa para operações de grande processamento ou que requer muito tempo
 * 
 */
public interface IRemoteTask extends ILocalTask {

  /**
   * Inicializa uma tarefa numa Thread nova.
   * 
   * @throws Throwable
   */
  public abstract void perform() throws Throwable;

}
