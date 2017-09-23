package breder.util.task.simple;

import breder.util.log.storage.ErrorLog;
import breder.util.log.storage.LogModel;
import breder.util.task.EventTask;
import breder.util.task.EventTask.EventTaskReturn;

/**
 * Implementação de uma tarefa de grande processamento.
 * 
 */
public abstract class SimpleRemoteTask extends SimpleLocalTask {

  private Thread thread;

  /**
   * Indica se a thread será realizada
   * 
   * @return thread realizada
   */
  public boolean accept() {
    return true;
  }

  /**
   * Processamento em Thread separada
   * 
   * @throws Throwable
   */
  public abstract void perform() throws Throwable;

  /**
   * Processamento de erro
   * 
   * @param t
   */
  public void handler(Throwable t) {
    t.printStackTrace();
    LogModel.getInstance().add(new ErrorLog(t));
  }

  /**
   * Final da tarefa
   */
  public void end() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void start() {
    boolean accept = EventTask.invokeAndWait(new EventTaskReturn<Boolean>() {
      @Override
      public Boolean run() {
        return accept();
      }
    });
    if (accept) {
      thread = new Thread(this.getClass().getSimpleName()) {
        @Override
        public void run() {
          try {
            perform();
            if (!isStop()) {
              EventTask.invokeLater(new Runnable() {
                @Override
                public void run() {
                  updateUI();
                }
              });
            }
          }
          catch (final Throwable t) {
            EventTask.invokeLater(new Runnable() {
              @Override
              public void run() {
                handler(t);
              }
            });
          }
          finally {
            EventTask.invokeLater(new Runnable() {
              @Override
              public void run() {
                end();
              }
            });
          }
        }
      };
      thread.start();
    }
    else {
      EventTask.invokeLater(new Runnable() {
        @Override
        public void run() {
          end();
        }
      });
    }
  }

  public void join() throws InterruptedException {
    while (thread.isAlive()) {
      Thread.sleep(10);
    }
  }

}
