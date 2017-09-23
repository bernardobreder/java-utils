package breder.util.task;

/**
 * Tarefa na Thread do Swing.
 * 
 */
public interface ILocalTask extends ITask {

  /**
   * Evento que ocorre na Thread do Swing para atualizar a interface gráfica
   */
  public void updateUI();

}
