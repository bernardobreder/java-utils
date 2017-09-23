package breder.util.net.ws.server;

import java.util.HashMap;
import java.util.Map;

/**
 * Configura o webservice
 * 
 * 
 * @author Bernardo Breder
 */
public class WSServerConfig {

  /** Serviços */
  private final Map<String, Object> services = new HashMap<String, Object>();
  /** Timeout */
  private long timeout = 1 * 60 * 60 * 1000;

  /**
   * Adiciona um serviço
   * 
   * @param serviceClass
   * @param service
   */
  public <E> void addService(Class<E> serviceClass, E service) {
    if (!serviceClass.isInterface()) {
      throw new IllegalArgumentException("the service need be a interface");
    }
    this.services.put(serviceClass.getSimpleName(), service);
  }

  /**
   * Retorna o serviço
   * 
   * @param classname
   * @return serviço
   */
  public Object getService(String classname) {
    return this.services.get(classname);
  }

  /**
   * Retorna a classe de contexto
   * 
   * @return classe de contexto
   */
  public Class<? extends WSServerContext> getContextClass() {
    return WSServerContext.class;
  }

  /**
   * Retorna o timeout
   * 
   * @return timeout
   */
  public long getTimeoutSession() {
    return timeout;
  }

  /**
   * @param timeout
   */
  public void setTimeoutSession(long timeout) {
    this.timeout = timeout;
  }

}
