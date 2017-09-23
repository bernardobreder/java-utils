package breder.util.io.storage;

import java.io.File;
import java.io.IOException;

import breder.util.io.storage.standard.BStorage;

/**
 * Interface principal de Storage
 * 
 * 
 * @author Bernardo Breder
 */
public interface IStorage {

  /**
   * Instancia padrão
   */
  public final IStorage STANDARD = new BStorage();

  /**
   * Retona uma sessão de Storage a partir de um diretório storage
   * 
   * @param directory diretório storage
   * @return sessão de Storage
   * @throws IOException
   */
  public IStorageSession getStorage(File directory) throws IOException;

  /**
   * Indica se é um diretório storage
   * 
   * @param directory
   * @return é um diretório storage
   */
  public boolean isStorage(File directory);

}
