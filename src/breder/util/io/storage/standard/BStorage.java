package breder.util.io.storage.standard;

import java.io.File;
import java.io.IOException;

import breder.util.io.storage.IStorage;
import breder.util.io.storage.IStorageSession;

/**
 * Implementação padrão de Storage
 * 
 * 
 * @author Bernardo Breder
 */
public class BStorage implements IStorage {

  /**
   * {@inheritDoc}
   */
  @Override
  public IStorageSession getStorage(File directory) throws IOException {
    return new BStorageSession(directory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isStorage(File directory) {
    return new File(directory, BStorageConstant.METADATA_NAME).exists();
  }

}
