package breder.util.io.storage;

import java.io.File;

/**
 * Listener de eventos do Storage
 * 
 * 
 * @author Bernardo Breder
 */
public interface IStorageListener {

  /**
   * Evento do Storage sendo iniciado
   */
  public void startStorage();

  /**
   * Evento do Storage sendo iniciado
   */
  public void endStorage();

  /**
   * Publicando o arquivo no destino
   * 
   * @param file
   */
  public void publishing(File file);

  /**
   * Publicação concluida no destino
   * 
   * @param file
   */
  public void published(File file);

  /**
   * Buscando por novas atualizações
   */
  public void searching();

}
