package breder.util.io.storage.standard;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Metadata que irá ficar no diretório para informar as propriedades do storage
 * 
 * 
 * @author Bernardo Breder
 */
public class BStorageMetadata implements Serializable {

  /** Serial ID */
  private static final long serialVersionUID = 0;
  /** Ultimas atualizações de cada arquivo */
  private final Map<File, Long> times = new HashMap<File, Long>();
  /** Diretorio de origem */
  private File sourceDir;
  /** Diretorio de destino */
  private File targetDir;

  /**
   * Retorna
   * 
   * @return sourceDir
   */
  public File getSourceDir() {
    return sourceDir;
  }

  /**
   * @param sourceDir
   */
  public void setSourceDir(File sourceDir) {
    this.sourceDir = sourceDir;
  }

  /**
   * Retorna
   * 
   * @return times
   */
  public Map<File, Long> getTimes() {
    return times;
  }

  /**
   * Retorna
   * 
   * @return targetDir
   */
  public File getTargetDir() {
    return targetDir;
  }

  /**
   * @param targetDir
   */
  public void setTargetDir(File targetDir) {
    this.targetDir = targetDir;
  }

}
