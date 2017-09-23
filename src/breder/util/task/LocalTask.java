package breder.util.task;

/**
 * Tarefa que será realizado na Thread do Swing.
 * 
 */
public abstract class LocalTask extends Task implements ILocalTask {

  /**
   * Atributo que indica se a tarefa está parada ou não
   */
  private boolean stoped;

  public boolean preAction() {
    return true;
  };

  public void finalling() {
  }

  public void run() {
    if (this.preAction()) {
      EventTask.invokeLater(new Runnable() {
        @Override
        public void run() {
          try {
            if (!isInterrupted()) {
              updateUI();
            }
          }
          finally {
            finalling();
          }
        }
      });
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void interrupt() {
    stoped = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInterrupted() {
    return stoped;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ITask start() {
    this.run();
    return this;
  }

}
