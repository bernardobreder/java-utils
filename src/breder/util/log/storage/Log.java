package breder.util.log.storage;

import java.io.Serializable;
import java.util.Date;

/**
 * Item de log
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class Log implements Serializable {

  /** Data */
  private final Date date;
  /** Tipo */
  private final LogType type;
  /** Titulo */
  private final String title;
  /** Texto */
  private final String text;

  /**
   * @param type
   * @param title
   * @param text
   */
  public Log(LogType type, String title, String text) {
    super();
    this.date = new Date();
    this.type = type;
    this.title = title;
    this.text = text;
  }

  /**
   * Retorna
   * 
   * @return date
   */
  public Date getDate() {
    return date;
  }

  /**
   * Retorna
   * 
   * @return type
   */
  public LogType getType() {
    return type;
  }

  /**
   * Retorna
   * 
   * @return title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Retorna
   * 
   * @return text
   */
  public String getText() {
    return text;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "Log [type=" + type + ", text=" + text + "]";
  }

}
