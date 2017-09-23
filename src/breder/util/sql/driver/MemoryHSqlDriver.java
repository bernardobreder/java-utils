package breder.util.sql.driver;

/**
 * Driver de MySql
 * 
 * 
 * @author Bernardo Breder
 */
public class MemoryHSqlDriver implements IDBDriver {

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUrl() {
    return "jdbc:hsqldb:mem:mymemdb";
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
