package breder.util.net.ws.client;

/**
 * Configura o webservice
 * 
 * 
 * @author Bernardo Breder
 */
public class WSClientConfig {

  /** Armazena Offline */
  private boolean storage = false;

  /**
   * Retorna
   * 
   * @return storage
   */
  public boolean isStorage() {
    return storage;
  }

  /**
   * @param storage
   */
  public void setStorage(boolean storage) {
    this.storage = storage;
  }

}
