package breder.util.log;

/**
 * Estrutura de log para notificar situa��es que ocorrem no processamento de
 * algum c�digo
 * 
 */
public interface ILog {

  /**
   * Objeto padr�o
   */
  public static final ILog DEFAULT = Log.getInstance();

  /**
   * Adiciona um Listener
   * 
   * @param listener
   */
  public void addListener(ILogListerner listener);

  /**
   * Remove o Listener
   * 
   * @param listener
   */
  public void removeListener(ILogListerner listener);

  /**
   * Imprime uma informa��o
   * 
   * @param text
   * @param objects
   */
  public void printInfo(String text, Object... objects);

  /**
   * Imprime um erro
   * 
   * @param text
   * @param objects
   */
  public void printError(String text, Object... objects);

}
