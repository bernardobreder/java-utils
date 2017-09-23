package breder.util.log;

import java.util.Vector;

/**
 * Implementa��o padr�o de log
 * 
 * @author Bernardo Breder
 * 
 */
public class Log implements ILog {

  private static final Log instance = new Log();

  private final Vector<ILogListerner> listeners = new Vector<ILogListerner>();

  private Log() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addListener(ILogListerner listener) {
    listeners.add(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeListener(ILogListerner listener) {
    for (int n = 0; n < listeners.size(); n++) {
      if (this.listeners.get(n) == listener) {
        this.listeners.remove(n);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void printInfo(String text, Object... objects) {
    String msg = String.format(text, objects);
    ILogListerner[] ls = listeners.toArray(new ILogListerner[0]);
    for (ILogListerner l : ls) {
      l.info(msg);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void printError(String text, Object... objects) {
    String msg = String.format(text, objects);
    ILogListerner[] ls = listeners.toArray(new ILogListerner[0]);
    for (ILogListerner l : ls) {
      l.error(msg);
    }
  }

  /**
   * Retorna o objeto singleton
   * 
   * @return
   */
  public static Log getInstance() {
    return instance;
  }

}
