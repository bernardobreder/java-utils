package breder.util.task.simple;

import breder.util.task.EventTask;

/**
 * Tarefa local
 * 
 * 
 * @author bbreder
 */
public abstract class SimpleLocalTask extends SimpleTask {

  /** Stop */
  private boolean stop;

  /**
   * Evento realizado na Thread do Swing
   */
  public abstract void updateUI();

  /**
   * {@inheritDoc}
   */
  @Override
  public void start() {
    EventTask.invokeLater(new Runnable() {
      @Override
      public void run() {
        if (!isStop()) {
          updateUI();
        }
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop() {
    this.stop = true;
  }

  /**
   * Retorna
   * 
   * @return stop
   */
  public boolean isStop() {
    return stop;
  }

}
