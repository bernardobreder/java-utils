package breder.util.sql.driver;

import java.io.File;

/**
 * Driver de MySql
 * 
 * 
 * @author Bernardo Breder
 */
public class FileHSqlDriver implements IDBDriver {

  /** Caminho do arquivo */
  private final File file;

  /**
   * Construtor
   * 
   * @param file
   */
  public FileHSqlDriver(File file) {
    this.file = file;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUrl() {
    return "jdbc:hsqldb:file:" + file.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getClassDriver() {
    return "org.hsqldb.jdbcDriver";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String ping() {
    return "SELECT 1 FROM dual";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String lastId(String table) {
    return "SELECT LAST_INSERT_ID()";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUsername() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPassword() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDebug() {
    return false;
  }

}
