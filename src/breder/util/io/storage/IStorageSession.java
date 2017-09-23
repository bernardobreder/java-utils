package breder.util.io.storage;

import java.io.File;

/**
 * Sessão de Storage
 * 
 * 
 * @author Bernardo Breder
 */
public interface IStorageSession {

  /**
   * Retorna o diretório root de origem
   * 
   * @return diretório root
   */
  public File getSourceDirectory();

  /**
   * Retorna o diretório root de destino
   * 
   * @return diretório root
   */
  public File getTargetDirectory();

  /**
   * Adiciona um diretório root de origem
   * 
   * @param directory
   * @return owner
   */
  public IStorageSession setSourceDirectory(File directory);

  /**
   * Adiciona um diretório root de destino
   * 
   * @param directory
   * @return owner
   */
  public IStorageSession setTargetDirectory(File directory);

  /**
   * Adiciona um listener
   * 
   * @param listener
   * @return listener
   */
  public IStorageSession addListener(IStorageListener listener);

  /**
   * Adiciona um listener
   * 
   * @param listener
   * @return listener
   */
  public IStorageSession removeListener(IStorageListener listener);

  /**
   * Indica o intervalo de verificação de atualização
   * 
   * @param miliseg
   * @return owner
   */
  public IStorageSession setIntervalRefresh(long miliseg);

  /**
   * Retorna o intervalo de verificação de atualização
   * 
   * @return intervalo de verificação
   */
  public long getIntervalRefresh();

  /**
   * Inicia a atualização dos storages. Somente uma instancia de processamento
   * será executado, independente da quantidade de vezes que esse método for
   * executado.
   * 
   * @return owner
   */
  public IStorageSession start();

  /**
   * Inicia a atualização dos storages
   * 
   * @return owner
   */
  public IStorageSession stop();

}
