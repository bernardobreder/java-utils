package breder.util.log.storage;

/**
 * Log de info
 * 
 * 
 * @author Bernardo Breder
 */
public class InfoLog extends Log {

  /** Message */
  private final String message;

  /**
   * @param message
   */
  public InfoLog(String message) {
    super(LogType.INFO, message, message);
    this.message = message;
  }

  /**
   * Retorna
   * 
   * @return message
   */
  public String getMessage() {
    return message;
  }

}
