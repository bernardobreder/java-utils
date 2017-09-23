package breder.util.net.ws.server;

import java.util.Properties;

/**
 * Contexto de execução de um serviço no servidor
 * 
 * 
 * @author Bernardo Breder
 */
public class WSServerContext {

  /** Contador de tempo */
  private long lastUpdate;
  /** Propriedade */
  private final Properties prop = new Properties();

  /**
   * Construtor
   */
  public WSServerContext() {
    this.refreshUpdate();
  }

  /**
   * Retorna o contexto
   * 
   * @return contexto
   */
  public static WSServerContext get() {
    return WSServer.getContext();
  }

  /**
   * Retorna o contexto
   * 
   * @param session
   * @return contexto
   */
  public static WSServerContext get(String session) {
    return WSServer.getContext(session);
  }

  /**
   * Retorna o contexto
   */
  public static void remove() {
    WSServer.removeContext();
  }

  /**
   * Retorna o contexto
   * 
   * @param session
   */
  public static void remove(String session) {
    WSServer.removeContext(session);
  }

  /**
   * Atualiza o contador de tempo
   */
  public void refreshUpdate() {
    this.lastUpdate = System.currentTimeMillis();
  }

  /**
   * Retorno o tempo da ultima atualização
   * 
   * @return tempo
   */
  public long getUpdateTime() {
    return System.currentTimeMillis() - this.lastUpdate;
  }

  /**
   * Retorna
   * 
   * @return lastUpdate
   */
  public long getLastUpdate() {
    return lastUpdate;
  }

  /**
   * Retorna
   * 
   * @return prop
   */
  public Properties getProp() {
    return prop;
  }

}
