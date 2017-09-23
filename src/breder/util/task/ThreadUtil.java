package breder.util.task;

/**
 * Utilitarios de Thread
 * 
 * @author Bernardo Breder
 * 
 */
public class ThreadUtil {

  /**
   * Sleep que n�o considera a interrup��o
   * 
   * @param timer
   */
  public static void sleep(long timer) {
    try {
      Thread.sleep(timer);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public static void join(ThreadGroup group) throws InterruptedException {
    while (group.activeCount() > 0) {
      Thread.sleep(100);
    }
  }

}
