package breder.util.sql.driver;

/**
 * Driver de banco de dados
 * 
 * 
 * @author Bernardo Breder
 */
public interface IDBDriver {

  /**
   * Indica a url
   * 
   * @return url
   */
  public String getUrl();

  /**
   * Retorna a senha
   * 
   * @return senha
   */
  public String getUsername();

  /**
   * Retorna a senha
   * 
   * @return senha
   */
  public String getPassword();

  /**
   * Retorna o nome da classe do driver
   * 
   * @return nome da classe do driver
   */
  public String getClassDriver();

  /**
   * Retorna uma query de ping
   * 
   * @return query
   */
  public String ping();

  /**
   * Retorna a query de indica qual foi o último id carregado
   * 
   * @param table
   * @return id
   */
  public String lastId(String table);

  /**
   * Indica se é Debug
   * 
   * @return debug
   */
  public boolean isDebug();

}
