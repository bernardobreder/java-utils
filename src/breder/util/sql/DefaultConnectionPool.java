package breder.util.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

import breder.util.log.console.BrederLog;
import breder.util.sql.driver.IDBDriver;

public class DefaultConnectionPool {

  /** Drive para um banco específico */
  private final IDBDriver driver;
  /** Lista de Connection que está livre para ser usado por alguma Thread nova */
  private final LinkedList<ConnectionEntity> list =
    new LinkedList<ConnectionEntity>();
  /** Threads sendo usado por Threads */
  private final ThreadLocal<ConnectionEntity> threads =
    new ThreadLocal<ConnectionEntity>();
  /** Número de Connection aberto em uso ou não */
  private int count;
  /** Número máximo de Threads usando uma Connection */
  private final int maxConnection;
  /** Timeout */
  private int timeout = 60 * 1000;

  /**
   * Construtor
   * 
   * @param driver
   * @param maxConnection
   */
  public DefaultConnectionPool(IDBDriver driver, int maxConnection) {
    this.driver = driver;
    this.maxConnection = maxConnection;
    try {
      Class.forName(driver.getClassDriver());
    }
    catch (ClassNotFoundException e) {
      throw new Error(e);
    }
  }

  /**
   * Recupera uma conexão da thread corrente. Caso ainda não tenha sido criada,
   * será construida uma própria para a thread corrente.
   * 
   * @return conexão sql
   * @throws SQLException
   * @throws InterruptedException
   */
  public Connection get() throws SQLException, InterruptedException {
    ConnectionEntity c = this.threads.get();
    if (c != null) {
      return c.connection;
    }
    // Entidade de conexão
    ConnectionEntity entity = null;
    // Indica se precisa fechar a conexão da entidade
    boolean close = false;
    // Indica se precisa reconectar a conexão da entidade
    boolean reconnect = false;
    // Sincroniza o bloco
    synchronized (this) {
      // Se estiver lotado
      if (count >= this.maxConnection) {
        // Espera ser liberado alguma entidade
        while (this.list.size() == 0) {
          // Espera a notificação
          try {
            this.wait(100);
          }
          catch (InterruptedException e1) {
            // Interrompe
            Thread.currentThread().interrupt();
            // Retorna nulo
            return null;
          }
        }
        // Recupera o que foi liberado
        entity = this.list.removeFirst();
      }
      else if (this.list.size() == 0) {
        // Cria uma nova entidade
        entity = new ConnectionEntity();
        // Indica que precisa reconectar
        reconnect = true;
      }
      else {
        // Recupera o que foi liberado
        entity = this.list.removeFirst();
        // Verfica o tempo de uso
        if (System.currentTimeMillis() - entity.lastUsed > timeout) {
          // Indica para fechar
          close = true;
          // Indica para reconectar
          reconnect = true;
        }
      }
    }
    // Verifica se precisa fechar a conexão
    if (close) {
      // Fecha a conexão existente
      entity.connection.close();
      // Decrementa o numero de conexoes aberta
      this.count--;
    }
    // Se precisa reconectar
    if (reconnect) {
      // Faz a conexão
      entity.connection = this.connect();
      // Incrementa o numero de conexoes aberta
      this.count++;
    }
    // Atribui a thread corrente
    this.threads.set(entity);
    // Atualiza o tempo de uso
    entity.lastUsed = System.currentTimeMillis();
    // Retorna a conection
    return entity.connection;
  }

  /**
   * Libera a conexão para outra thread usar
   * 
   * @throws SQLException
   */
  public synchronized void commit() throws SQLException {
    // Recupera a entidade da thread corrente
    ConnectionEntity entity = this.threads.get();
    // Verifica se foi consultado o banco
    if (entity != null) {
      // Realiza o commit
      entity.connection.commit();
    }
  }

  /**
   * Libera a conexão para outra thread usar
   */
  public synchronized void rollback() {
    // Recupera a entidade da thread corrente
    ConnectionEntity entity = this.threads.get();
    // Verifica se foi consultado o banco
    if (entity != null) {
      // Realiza o commit
      try {
        entity.connection.rollback();
      }
      catch (SQLException e) {
      }
    }
  }

  /**
   * Libera a conexão para outra thread usar
   */
  public synchronized void free() {
    // Recupera a entidade da thread corrente
    ConnectionEntity entity = this.threads.get();
    // Verifica se foi consultado o banco
    if (entity != null) {
      // Remove a entidade da thread corrente
      this.threads.remove();
      // Adiciona na lista de Connection disponivel
      this.list.addLast(entity);
      // Notifica todos
      this.notifyAll();
    }
  }

  /**
   * Conecta ao banco de dados
   * 
   * @return
   * @throws SQLException
   * @throws InterruptedException
   */
  protected Connection connect() throws SQLException, InterruptedException {
    //		String prefix = "jdbc:" + driver.getSgbdName() + ":";
    //		if (driver.getHost() != null) {
    //			prefix += String.format("//%s/%s", driver.getHost(),
    //					driver.getDatabase());
    //		}
    SQLException exception = null;
    for (int n = 0; n < timeout / 1000; n++) {
      try {
        Connection c =
          DriverManager.getConnection(driver.getUrl(), driver.getUsername(),
            driver.getPassword());
        c.setAutoCommit(false);
        return c;
      }
      catch (SQLException e) {
        BrederLog.error(e);
        exception = e;
        try {
          Thread.sleep(1000);
        }
        catch (InterruptedException e1) {
          Thread.currentThread().interrupt();
          throw e1;
        }
      }
    }
    if (exception == null) {
      exception = new SQLException("connection");
    }
    throw exception;
  }

  /**
   * @return the timeout
   */
  public int getTimeout() {
    return timeout;
  }

  /**
   * @param timeout the timeout to set
   */
  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  /**
   * Entidade de conexão
   * 
   * @author bernardobreder
   * 
   */
  private static class ConnectionEntity {
    public Connection connection;
    public long lastUsed;
  }

}
