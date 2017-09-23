package breder.util.sql.driver;

/**
 * Driver de MySql
 * 
 * 
 * @author Bernardo Breder
 */
public class MySqlDriver implements IDBDriver {

  /** Host */
  private final String host;
  /** Base da Dados */
  private final String database;
  /** Usuario */
  private final String username;
  /** Senha */
  private final String password;

  /**
   * Construtor
   * 
   * @param host
   * @param database
   * @param username
   * @param password
   */
  public MySqlDriver(String host, String database, String username,
    String password) {
    this.host = host;
    this.database = database;
    this.username = username;
    this.password = password;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUrl() {
    return "jdbc:mysql://" + host + "/" + database;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getClassDriver() {
    return "com.mysql.jdbc.Driver";
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
    return username;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDebug() {
    return false;
  }

}
