package breder.util.task;

/**
 * Conjunto de Thread que serão usados e criados controladamente.
 * 
 * 
 * @author Tecgraf
 */
public class ThreadPool {

  /** Número de Threads */
  private int numberOfThreads;
  /** Número de Threads */
  private Thread[] threads;

  /**
   * Construtor
   * 
   * @param numberOfThreads
   */
  public ThreadPool(int numberOfThreads) {
    this.numberOfThreads = numberOfThreads;
    this.threads = new Thread[numberOfThreads];
  }

  /**
   * Tenta iniciar uma thread. Caso não consiga, retorna false e não durma.
   * 
   * @param r
   * @return indica se consegui iniciar uma thread
   */
  public synchronized boolean tryStart(final Runnable r) {
    if (numberOfThreads <= 0) {
      return false;
    }
    for (int n = 0; n < threads.length; n++) {
      if (this.threads[n] == null) {
        this.threads[n] = new Thread(r, r.getClass().getSimpleName()) {
          @Override
          public void run() {
            try {
              r.run();
            }
            finally {
              numberOfThreads++;
              synchronized (ThreadPool.this) {
                ThreadPool.this.notifyAll();
              }
            }
          }
        };
        this.threads[n].start();
        this.numberOfThreads--;
        return true;
      }
    }
    return false;
  }

  /**
   * Inicializa uma Thread. Caso não tenha disponível, dorma até conseguir um.
   * 
   * @param r
   */
  public synchronized void start(Runnable r) {
    while (!this.tryStart(r)) {
      synchronized (this) {
        try {
          this.wait(100);
        }
        catch (InterruptedException e) {
        }
      }
    }
  }

}
