package breder.util.io.storage.standard;

import breder.util.log.storage.ErrorLog;
import breder.util.log.storage.LogModel;

/**
 * Thread do storage
 * 
 * 
 * @author Bernardo Breder
 */
public class BStorageThread extends Thread {

  /** Sessão do Storage */
  private BStorageSession session;

  /**
   * Construtor
   * 
   * @param session
   */
  public BStorageThread(BStorageSession session) {
    this.session = session;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    while (!Thread.interrupted()) {
      try {

      }
      catch (Throwable e) {
        LogModel.getInstance().add(new ErrorLog(e));
      }
    }
  }

  /**
   * Retorna
   * 
   * @return session
   */
  public BStorageSession getSession() {
    return session;
  }

}
