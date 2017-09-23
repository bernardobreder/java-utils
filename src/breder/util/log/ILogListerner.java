package breder.util.log;

/**
 * Listener para log
 * 
 */
public interface ILogListerner {

  /**
   * Log de informa��o
   * 
   * @param text texto
   */
  public void info(String text);

  /**
   * Log de erro
   * 
   * @param text erro
   */
  public void error(String text);

}
