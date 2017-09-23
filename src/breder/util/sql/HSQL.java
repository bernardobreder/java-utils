package breder.util.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import breder.util.util.TomcatLog;

/**
 * Classe de ajuda para HSql
 * 
 * @author bernardobreder
 * 
 */
public class HSQL {

  /** ThreadLocal */
  private static final ThreadLocal<Connection> connections =
    new ThreadLocal<Connection>();
  /** Url da Connection */
  private static String url = "jdbc:hsqldb:mem:memdb";
  /** Contador */
  private static int connectionUsing = 0;
  /** Contador */
  private static int maxConnection = 100;
  /** Autocommit */
  private static boolean autocommit = false;

  /**
   * Realiza uma query
   * 
   * @param query
   * @param objects
   * @return
   * @throws SQLException
   */
  public static int selectId(String query, Object... objects)
    throws SQLException {
    PreparedStatement ps = ps(query);
    try {
      for (int n = 0; n < objects.length; n++) {
        ps.setObject(n + 1, objects[n]);
      }
      ps.execute();
      ResultSet rs = ps.getResultSet();
      if (rs.next()) {
        return rs.getInt(1);
      }
      else {
        return -1;
      }
    }
    finally {
      ps.close();
    }
  }

  /**
   * Realiza uma query
   * 
   * @param query
   * @param objects
   * @return
   * @throws SQLException
   */
  public static List<SqlTupla> select(String query, Object... objects)
    throws SQLException {
    PreparedStatement ps = ps(query);
    try {
      for (int n = 0; n < objects.length; n++) {
        ps.setObject(n + 1, objects[n]);
      }
      ps.execute();
      ResultSet rs = ps.getResultSet();
      int columns = rs.getMetaData().getColumnCount();
      List<SqlTupla> rows = new ArrayList<SqlTupla>();
      while (rs.next()) {
        SqlTupla row = new SqlTupla(columns);
        for (int n = 0; n < columns; n++) {
          row.add(rs.getObject(n + 1));
        }
        rows.add(row);
      }
      return rows;
    }
    finally {
      ps.close();
    }
  }

  /**
   * Realiza uma query
   * 
   * @param query
   * @param objects
   * @return resultado da query
   * @throws SQLException
   */
  public static SqlTupla selectUnique(String query, Object... objects)
    throws SQLException {
    PreparedStatement ps = ps(query);
    try {
      for (int n = 0; n < objects.length; n++) {
        ps.setObject(n + 1, objects[n]);
      }
      ps.execute();
      ResultSet rs = ps.getResultSet();
      int columns = rs.getMetaData().getColumnCount();
      if (rs.next()) {
        SqlTupla row = new SqlTupla(columns);
        for (int n = 0; n < columns; n++) {
          row.add(rs.getObject(n + 1));
        }
        return row;
      }
      else {
        return null;
      }
    }
    finally {
      ps.close();
    }
  }

  /**
   * Realiza uma query
   * 
   * @param query
   * @param objects
   * @return
   * @throws SQLException
   */
  public static int insert(String query, Object... objects) throws SQLException {
    PreparedStatement ps = ps(query);
    try {
      for (int n = 0; n < objects.length; n++) {
        ps.setObject(n + 1, objects[n]);
      }
      ps.executeUpdate();
      return getIdentify();
    }
    finally {
      ps.close();
    }
  }

  /**
   * Realiza um update
   * 
   * @param query
   * @param objects
   * @throws SQLException
   */
  public static void update(String query, Object... objects)
    throws SQLException {
    PreparedStatement ps = ps(query);
    try {
      for (int n = 0; n < objects.length; n++) {
        ps.setObject(n + 1, objects[n]);
      }
      ps.executeUpdate();
    }
    finally {
      ps.close();
    }
  }

  /**
   * Realiza um update
   * 
   * @param query
   * @param objects
   * @throws SQLException
   */
  public static void delete(String query, Object... objects)
    throws SQLException {
    PreparedStatement ps = ps(query);
    try {
      for (int n = 0; n < objects.length; n++) {
        ps.setObject(n + 1, objects[n]);
      }
      ps.executeUpdate();
    }
    finally {
      ps.close();
    }
  }

  /**
   * Realiza um update
   * 
   * @param query
   * @param objects
   * @throws SQLException
   */
  public static void write(String query, Object... objects) throws SQLException {
    PreparedStatement ps = ps(query);
    try {
      for (int n = 0; n < objects.length; n++) {
        ps.setObject(n + 1, objects[n]);
      }
      ps.executeUpdate();
    }
    finally {
      ps.close();
    }
  }

  /**
   * Realiza um ping
   * 
   * @return ping
   */
  public static boolean ping() {
    try {
      selectUnique("values 1");
      return true;
    }
    catch (SQLException e) {
      return false;
    }
  }

  /**
   * Realiza um commit
   * 
   * @throws SQLException
   */
  public static void commit() {
    try {
      Connection connection = connections.get();
      if (connection != null) {
        try {
          connection.commit();
        }
        catch (SQLException e) {
        }
        try {
          connection.close();
        }
        catch (SQLException e) {
        }
      }
    }
    finally {
      connections.remove();
      synchronized (HSQL.class) {
        connectionUsing--;
        HSQL.class.notifyAll();
      }
    }
  }

  /**
   * Realiza um rollback
   * 
   * @throws SQLException
   */
  public static void rollback() {
    try {
      Connection connection = connections.get();
      if (connection != null) {
        try {
          connection.rollback();
        }
        catch (SQLException e) {
        }
        try {
          connection.close();
        }
        catch (SQLException e) {
        }
      }
    }
    finally {
      connections.remove();
      synchronized (HSQL.class) {
        connectionUsing--;
        HSQL.class.notifyAll();
      }
    }
  }

  /**
   * @return the counter
   */
  public static int getConnectionUsing() {
    return connectionUsing;
  }

  /**
   * @param url the url to set
   */
  public static void setUrl(String url) {
    HSQL.url = url;
  }

  /**
   * @param max the mAX to set
   */
  public static void setMaxConnection(int max) {
    maxConnection = max;
  }

  /**
   * Inicia uma requisição
   * 
   * @param query
   * @return
   * @throws SQLException
   */
  private static PreparedStatement ps(String query) throws SQLException {
    TomcatLog.info(query);
    return getConnection().prepareStatement(query);
  }

  /**
   * Inicia uma requisição
   * 
   * @param query
   * @return
   * @throws SQLException
   */
  private static Statement stm(String query) throws SQLException {
    TomcatLog.info(query);
    Statement stm = getConnection().createStatement();
    stm.execute(query);
    return stm;
  }

  /**
   * Realiza uma query
   * 
   * @param query
   * @param objects
   * @return
   * @throws SQLException
   */
  private static int getIdentify() throws SQLException {
    Statement ps = stm("CALL IDENTITY()");
    try {
      ResultSet rs = ps.getResultSet();
      rs.next();
      return rs.getInt(1);
    }
    finally {
      ps.close();
    }
  }

  /**
   * Retorna o connection
   * 
   * @return connection
   * @throws SQLException
   */
  private static Connection getConnection() throws SQLException {
    Connection connection = connections.get();
    if (connection == null) {
      connection = buildConnection();
      connections.set(connection);
    }
    return connection;
  }

  /**
   * Retorna o connection
   * 
   * @return connection
   * @throws SQLException
   */
  private static Connection buildConnection() throws SQLException {
    try {
      Class.forName("org.hsqldb.jdbcDriver");
    }
    catch (ClassNotFoundException e) {
      throw new Error(e);
    }
    synchronized (HSQL.class) {
      while (connectionUsing >= maxConnection) {
        try {
          HSQL.class.wait();
        }
        catch (InterruptedException e) {
        }
      }
      connectionUsing++;
    }
    Connection c = DriverManager.getConnection(url);
    c.setAutoCommit(autocommit);
    return c;
  }

  /**
   * @param autocommit the autocommit to set
   */
  public static void setAutocommit(boolean autocommit) {
    HSQL.autocommit = autocommit;
  }

}
