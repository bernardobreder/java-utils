package breder.util.io.storage.standard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import breder.util.io.storage.IStorageListener;
import breder.util.io.storage.IStorageSession;

/**
 * Implementação de uma sessão de Storage
 * 
 * 
 * @author Bernardo Breder
 */
public class BStorageSession implements IStorageSession {

  /** Metadata */
  private final BStorageMetadata metadata;
  /** Listeners */
  private List<IStorageListener> listeners = new ArrayList<IStorageListener>();
  /** Intervalo */
  private long inverval = 10 * 60 * 1000;
  /** Thread */
  private BStorageThread thread;

  /**
   * Construtor
   * 
   * @param directory
   * @throws IOException
   */
  public BStorageSession(File directory) throws IOException {
    File metadataFile = new File(directory, BStorageConstant.METADATA_NAME);
    if (metadataFile.exists()) {
      ObjectInputStream input =
        new ObjectInputStream(new FileInputStream(metadataFile));
      try {
        metadata = (BStorageMetadata) input.readObject();
      }
      catch (ClassNotFoundException e) {
        throw new IOException(e);
      }
      input.close();
    }
    else {
      metadata = new BStorageMetadata();
      metadata.setSourceDir(directory);
      ObjectOutputStream output =
        new ObjectOutputStream(new FileOutputStream(metadataFile));
      output.writeObject(metadata);
      output.close();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getSourceDirectory() {
    return this.metadata.getSourceDir();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getTargetDirectory() {
    return this.metadata.getTargetDir();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IStorageSession setSourceDirectory(File directory) {
    this.metadata.setSourceDir(directory);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IStorageSession setTargetDirectory(File directory) {
    this.metadata.setTargetDir(directory);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IStorageSession addListener(IStorageListener listener) {
    this.listeners.add(listener);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IStorageSession removeListener(IStorageListener listener) {
    this.listeners.remove(listener);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IStorageSession setIntervalRefresh(long miliseg) {
    this.inverval = miliseg;
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getIntervalRefresh() {
    return this.inverval;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IStorageSession start() {
    if (this.thread == null) {
      this.thread = new BStorageThread(this);
      this.thread.start();
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IStorageSession stop() {
    this.thread.interrupt();
    return this;
  }

}
