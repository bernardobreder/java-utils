package breder.util.log.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Modelo de log
 * 
 * 
 * @author Bernardo Breder
 */
public class LogModel {

  /** Caminho */
  private static String PATHNAME = "./";
  /** Nome */
  private static String FILENAME = "log.data";
  /** Construtor */
  private static final LogModel instance = new LogModel();
  /** Logs */
  private List<Log> list = new Vector<Log>();
  /** Log Size */
  private static final int SIZE = 8 * 1024;

  /**
   * Construtor
   */
  @SuppressWarnings("unchecked")
  private LogModel() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        try {
          checkpoint();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
    File file = new File(PATHNAME + FILENAME);
    if (file.exists()) {
      try {
        ObjectInputStream input =
          new ObjectInputStream(new FileInputStream(file));
        list = (List<Log>) input.readObject();
        input.close();
      }
      catch (Exception e) {
        list = new ArrayList<Log>(SIZE);
      }
    }
    else {
      list = new ArrayList<Log>(SIZE);
    }
  }

  /**
   * Log
   * 
   * @param log
   */
  public void add(Log log) {
    this.list.add(0, log);
    while (this.list.size() > SIZE) {
      this.list.remove(list.size() - 1);
    }
  }

  /**
   * Adiciona um log
   * 
   * @param log
   * @return erro
   * @throws E
   */
  public <E extends Throwable> E addAndThrow(E log) throws E {
    this.add(new ErrorLog(log));
    throw log;
  }

  /**
   * Adiciona um log
   * 
   * @param log
   * @return erro
   * @throws E
   */
  public <E extends Throwable> void add(E log) {
    this.add(new ErrorLog(log));
  }

  /**
   * Retorna
   * 
   * @return list
   */
  public List<Log> getList() {
    return list;
  }

  /**
   * Escreve no arquivo
   * 
   * @throws IOException
   */
  private synchronized void checkpoint() throws IOException {
    ObjectOutputStream output =
      new ObjectOutputStream(new FileOutputStream(PATHNAME + FILENAME));
    output.writeObject(list);
    output.close();
  }

  /**
   * Retorna
   * 
   * @return instance
   */
  public static LogModel getInstance() {
    return instance;
  }

}
