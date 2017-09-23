package breder.util.sql;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import breder.util.util.TomcatLog;

/**
 * @author bbreder
 */
public class DB {

  /**
   * Instancia unica
   */
  private static final DB instance = new DB();

  /**
   * Construtor padrão
   */
  private DB() {
  }

  /**
   * Query de select nativa
   * 
   * @param query
   * @param objects
   * @return resposta da query
   * @throws SQLException
   */
  private List<List<Object>> select(String query, Object... objects)
    throws SQLException {
    PreparedStatement ps =
      ConnectionPool.getInstance().get().prepareStatement(query);
    try {
      for (int n = 0; n < objects.length; n++) {
        ps.setObject(n + 1, objects[n]);
      }
      boolean result = ps.execute();
      if (result) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        ResultSet rs = ps.getResultSet();
        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
          List<Object> objs = new ArrayList<Object>(cols);
          for (int n = 0; n < cols; n++) {
            objs.add(rs.getObject(n + 1));
          }
          list.add(objs);
        }
        return list;
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
   * Query de select nativa
   * 
   * @param query
   * @param objects
   * @return resposta da query
   * @throws SQLException
   */
  private int insert(String query, Object... objects) throws SQLException {
    PreparedStatement ps =
      ConnectionPool.getInstance().get().prepareStatement(query,
        PreparedStatement.RETURN_GENERATED_KEYS);
    try {
      for (int n = 0; n < objects.length; n++) {
        ps.setObject(n + 1, objects[n]);
      }
      ps.executeUpdate();
      int autoIncKeyFromApi = -1;
      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next()) {
        autoIncKeyFromApi = rs.getInt(1);
      }
      return autoIncKeyFromApi;
    }
    finally {
      ps.close();
    }
  }

  /**
   * Implementação de um select
   * 
   * @param query
   * @param objects
   * @return resultado do select
   * @throws SQLException
   */
  public List<List<Object>> selectImpl(String query, Object... objects)
    throws SQLException {
    long time = System.currentTimeMillis();
    List<List<Object>> list = this.select(query, objects);
    time = System.currentTimeMillis() - time;
    if (list == null) {
      time = System.currentTimeMillis() - time;
      TomcatLog.info(String.format("'%s' in %d miliseg", query, 0, time));
      return null;
    }
    if (list.size() == 0) {
      TomcatLog.info(String.format("'%s' with %d tuplas in %d miliseg", query,
        0, time));
    }
    else {
      TomcatLog.info(String.format(
        "'%s' with %d tuplas and %d columns in total %d object in %d miliseg",
        query, list.size(), list.get(0).size(), list.size()
          * list.get(0).size(), time));
    }
    return list;
  }

  /**
   * Implementação de um select
   * 
   * @param query
   * @param objects
   * @return resultado do select
   * @throws SQLException
   */
  public int insertImpl(String query, Object... objects) {
    for (;;) {
      try {
        long time = System.currentTimeMillis();
        int id = this.insert(query, objects);
        time = System.currentTimeMillis() - time;
        TomcatLog.info(String.format("'%s' in %d miliseg", query, 0, time));
        return id;
      }
      catch (SQLIntegrityConstraintViolationException e) {
        TomcatLog.error(e);
        throw new RuntimeException(e.getMessage(), e);
      }
      catch (SQLException e) {
        TomcatLog.error(e);
        throw new RuntimeException(e.getMessage(), e);
      }
      catch (RuntimeException e) {
        TomcatLog.error(e);
        throw e;
      }
    }
  }

  /**
   * Implementação de um select
   * 
   * @param query
   * @param objects
   * @return resultado do select
   * @throws SQLException
   */
  private List<SqlTupla> selectTuplaImpl(String query, Object... objects)
    throws SQLException {
    List<List<Object>> data = this.selectImpl(query, objects);
    List<SqlTupla> list = new ArrayList<SqlTupla>(data.size());
    for (List<Object> row : data) {
      SqlTupla tupla = new SqlTupla();
      for (Object cell : row) {
        tupla.add(cell);
      }
      list.add(tupla);
    }
    return list;
  }

  /**
   * Realiza a query e retorna os objetos em java
   * 
   * @param query
   * @param objects
   * @return resultado da query
   */
  public List<List<Object>> read(String query, Object... objects) {
    for (;;) {
      try {
        List<List<Object>> result = this.selectImpl(query, objects);
        return result;
      }
      catch (SQLIntegrityConstraintViolationException e) {
        TomcatLog.error(e);
        throw new RuntimeException(e.getMessage(), e);
      }
      catch (SQLException e) {
        TomcatLog.error(e);
        throw new RuntimeException(e.getMessage(), e);
      }
      catch (RuntimeException e) {
        TomcatLog.error(e);
        throw e;
      }
    }
  }

  /**
   * Realiza a query e retorna os objetos em java
   * 
   * @param query
   * @param objects
   * @return resultado da query
   */
  public List<SqlTupla> readTupla(String query, Object... objects) {
    for (;;) {
      try {
        List<SqlTupla> result = this.selectTuplaImpl(query, objects);
        return result;
      }
      catch (SQLIntegrityConstraintViolationException e) {
        TomcatLog.error(e);
        throw new RuntimeException(e.getMessage(), e);
      }
      catch (SQLException e) {
        TomcatLog.error(e);
        throw new RuntimeException(e.getMessage(), e);
      }
      catch (RuntimeException e) {
        TomcatLog.error(e);
        throw e;
      }
    }
  }

  /**
   * Retorna os ids de uma consulta
   * 
   * @param query
   * @param objects
   * @return ids de uma consulta
   */
  public List<Integer> readIds(String query, Object... objects) {
    List<List<Object>> result = this.read(query, objects);
    List<Integer> ids = new ArrayList<Integer>(result.size());
    for (int n = 0; n < result.size(); n++) {
      Object value = result.get(n).get(0);
      if (value instanceof Integer) {
        ids.add((Integer) value);
      }
      else {
        ids.add(new Integer(value.toString()));
      }
    }
    return ids;
  }

  /**
   * Retorna os ids de uma consulta
   * 
   * @param query
   * @param objects
   * @return ids de uma consulta
   */
  public Integer readId(String query, Object... objects) {
    List<Integer> list = this.readIds(query, objects);
    if (list.size() == 0) {
      return null;
    }
    else {
      return list.get(0);
    }
  }

  /**
   * Realiza a query e retorna os objetos em java
   * 
   * @param query
   * @param objects
   */
  public void write(String query, Object... objects) {
    for (;;) {
      try {
        this.selectImpl(query, objects);
        break;
      }
      catch (SQLIntegrityConstraintViolationException e) {
        TomcatLog.error(e);
        throw new RuntimeException(e.getMessage(), e);
      }
      catch (SQLException e) {
        TomcatLog.error(e);
        throw new RuntimeException(e.getMessage(), e);
      }
      catch (RuntimeException e) {
        TomcatLog.error(e);
        throw e;
      }
    }
  }

  /**
   * Realiza a query e retorna os objetos em java
   * 
   * @param query
   * @param objects
   * @return
   */
  public int writeId(String query, Object... objects) {
    return this.insertImpl(query, objects);
  }

  /**
   * Executa um script de db
   * 
   * @param input
   * @return this
   * @throws IOException
   */
  public DB write(InputStream input) throws IOException {
    StringBuilder sb = new StringBuilder();
    for (int n; (n = input.read()) != -1;) {
      if (n == ';') {
        write(sb.toString());
        sb.delete(0, sb.length());
      }
      else {
        sb.append((char) n);
      }
    }
    input.close();
    return this;
  }

  /**
   * Realiza um Commit da conexão
   */
  public void commit() {
    try {
      ConnectionPool.getInstance().get().commit();
    }
    catch (SQLException e) {
      TomcatLog.error(e);
      this.rollback();
    }
    finally {
      ConnectionPool.getInstance().free();
    }
  }

  /**
   * Realiza um Rollback da conexão
   */
  public void rollback() {
    try {
      ConnectionPool.getInstance().get().rollback();
    }
    catch (SQLException e) {
      TomcatLog.error(e);
    }
    finally {
      ConnectionPool.getInstance().free();
    }
  }

  /**
   * Verifica se está conectado
   */
  public synchronized void ping() {
    read(ConnectionPool.getInstance().getDriver().ping());
  }

  /**
   * Funcao que retorna uma sequence unica.
   * 
   * @param table
   * @return sequence
   */
  public int getSequence(String table) {
    return this.readId(ConnectionPool.getInstance().getDriver().lastId(table));
  }

  /**
   * @return the instance
   */
  public static DB getInstance() {
    return instance;
  }

}
