package breder.util.sql.driver;

/**
 * Driver de MySql
 * 
 * 
 * @author Bernardo Breder
 */
public class SqlLiteMemoryDriver implements IDBDriver {

  /**
   * Construtor
   */
  public SqlLiteMemoryDriver() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUrl() {
    return "jdbc:sqlite:";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getClassDriver() {
    return "org.sqlite.JDBC";
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
  public String ping() {
    return "SELECT 1";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String lastId(String table) {
    return "select last_insert_rowid() from " + table;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDebug() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUsername() {
    return null;
  }

}
