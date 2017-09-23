package breder.util.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import breder.util.sql.driver.IDBDriver;
import breder.util.util.TomcatLog;

/**
 * Classe responsável por gerenciar o pool de conexões
 * 
 * 
 * @author bbreder
 */
public class ConnectionPool {

  /** Instancia unica */
  private static final ConnectionPool instance = new ConnectionPool();
  /** Conexão para cada thread */
  private ThreadLocal<Connection> threads;
  /** Lista de conexões disponível para uso */
  private List<Connection> connections;
  /** Número de conexões abertas */
  private int count;
  /** Driver de Banco de Dados */
  private IDBDriver driver;
  /** Número máximo de connection */
  private int maxConnection = 100;
  /** Debug Connection */
  private Connection debugConnection;

  /**
   * Construtor padrão
   */
  private ConnectionPool() {
    this.reset();
    new ReconnectThread().start();
  }

  /**
   * Retorna uma conexão vindo do pool. Operação Thread-Safe
   * 
   * @return conexão disponível e marcada para a thread corrente.
   * @throws SQLException
   */
  public Connection get() throws SQLException {
    if (this.driver.isDebug()) {
      synchronized (this) {
        if (debugConnection == null) {
          debugConnection = connect();
        }
      }
      return debugConnection;
    }
    else {
      Connection c = this.threads.get();
      if (c != null) {
        if (c.isClosed()) {
          this.threads.set(c = this.connect());
        }
      }
      else {
        synchronized (this) {
          if (count >= this.maxConnection) {
            while (this.connections.size() == 0) {
              try {
                Thread.sleep(100);
              }
              catch (InterruptedException e) {
              }
            }
          }
          if (this.connections.size() == 0) {
            c = this.connect();
          }
          else {
            c = this.connections.remove(this.connections.size() - 1);
          }
          this.threads.set(c);
        }
      }
      return c;
    }
  }

  /**
   * Libera uma conexão para ser usado por outra thread.
   */
  public void free() {
    Connection c = this.threads.get();
    if (c != null) {
      this.threads.remove();
      this.connections.add(c);
    }
  }

  /**
   * Realiza a ação de reconectar
   * 
   * @return retorna uma conexão
   */
  private final Connection connect() {
    IDBDriver driver = this.getDriver();
    String classname = driver.getClassDriver();
    return this.load(classname, driver.getUrl(), driver.getUsername(), driver
      .getPassword());
  }

  /**
   * Realiza a carga do connector com o banco de dados
   * 
   * @param driver
   * @param url
   * @param username
   * @param password
   * @return uma conexão
   */
  private Connection load(String driver, String url, String username,
    String password) {
    for (;;) {
      try {
        Class.forName(driver);
        Connection c = DriverManager.getConnection(url, username, password);
        c.setAutoCommit(false);
        this.count++;
        return c;
      }
      catch (Exception e) {
        TomcatLog.error(e);
        try {
          Thread.sleep(1000);
        }
        catch (InterruptedException e1) {
        }
      }
    }
  }

  /**
   * Realiza a carga do connector com o banco de dados
   * 
   * @param driver
   * @param url
   * @param username
   * @param password
   * @return uma conexão
   */
  public boolean tryConnect(String driver, String url, String username,
    String password) {
    try {
      Class.forName(driver);
      DriverManager.getConnection(url, username, password).close();
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * Retorna a instancia unica
   * 
   * @return instancia unica
   */
  public static ConnectionPool getInstance() {
    return instance;
  }

  /**
   * Reseta as conexões
   */
  public void reset() {
    this.connections = new Vector<Connection>();
    this.threads = new ThreadLocal<Connection>();
  }

  /**
   * Reconectar
   */
  public void reconnect() {
    this.connections.clear();
    this.threads.remove();
    if (this.driver.isDebug()) {
      synchronized (this) {
        debugConnection = connect();
      }
    }
  }

  /**
   * Retorna
   * 
   * @return driver
   */
  public IDBDriver getDriver() {
    return driver;
  }

  /**
   * @param driver
   */
  public void setDriver(IDBDriver driver) {
    this.driver = driver;
  }

  /**
   * Retorna
   * 
   * @return maxConnection
   */
  public int getMaxConnection() {
    return maxConnection;
  }

  /**
   * @param maxConnection
   */
  public void setMaxConnection(int maxConnection) {
    this.maxConnection = maxConnection;
  }

  /**
   * Executa o ping em tempos e tempos
   * 
   * 
   * @author Bernardo Breder
   */
  public class ReconnectThread extends Thread {

    /**
     * Construtor
     */
    public ReconnectThread() {
      super("DBThread");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
      for (;;) {
        synchronized (ConnectionPool.this) {
          for (int n = 0; n < connections.size(); n++) {
            Connection c = connections.get(n);
            try {
              c.createStatement().execute(driver.ping());
              c.commit();
            }
            catch (Throwable e) {
              try {
                c.rollback();
              }
              catch (Exception e1) {
              }
            }
          }
        }
        try {
          Thread.sleep(60 * 1000);
        }
        catch (InterruptedException e) {
        }
      }
    }

  }

}
