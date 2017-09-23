package breder.util.task.simple;

/**
 * Implementação de uma tarefa de grande processamento.
 */
public abstract class SimpleWriterRemoteTask extends SimpleRemoteTask {

  /**
   * Processamento em Thread separada
   * 
   * @throws Throwable
   */
  public abstract void syncPerform() throws Throwable;

  /**
   * Processamento em Thread separada
   * 
   * @throws Throwable
   */
  public final void perform() throws Throwable {
    synchronized (SimpleRemoteTask.class) {
      this.syncPerform();
    }
  }

}
