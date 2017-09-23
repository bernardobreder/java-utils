package breder.util.log.storage;

import breder.util.util.ExceptionUtil;

/**
 * Log de info
 * 
 * 
 * @author Bernardo Breder
 */
public class ErrorLog extends Log {

  /** Message */
  private final Throwable exception;

  /**
   * @param error
   */
  public ErrorLog(Throwable error) {
    super(LogType.ERROR, error.getMessage(), ExceptionUtil.buildMessage(error));
    this.exception = error;
  }

  /**
   * Retorna
   * 
   * @return message
   */
  public Throwable getException() {
    return exception;
  }

}
