package breder.util.net.ws.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import breder.util.log.storage.ErrorLog;
import breder.util.log.storage.LogModel;
import breder.util.net.ws.shared.WSRequest;

/**
 * Armazenador de resultado de serviço
 * 
 * 
 * @author Bernardo Breder
 */
public class WSStorage {

  /** Caminho */
  private static String PATHNAME = "./";
  /** Nome */
  private static String FILENAME = "ws.data";
  /** Instancia unica */
  private static WSStorage instance;
  /** Tamanho máximo do buffer */
  private static final int SIZE = 8 * 1024;
  /** Cache de requisições */
  private List<WSRequest> list;
  /** Contador */
  private int count;

  /**
   * Construtor
   */
  @SuppressWarnings("unchecked")
  private WSStorage() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        try {
          checkpoint();
        }
        catch (IOException e) {
          LogModel.getInstance().add(new ErrorLog(e));
          e.printStackTrace();
        }
      }
    });
    File file = new File(PATHNAME + FILENAME);
    if (file.exists()) {
      try {
        ObjectInputStream input =
          new ObjectInputStream(new FileInputStream(file));
        list = (List<WSRequest>) input.readObject();
        input.close();
      }
      catch (Exception e) {
        list = new ArrayList<WSRequest>(SIZE);
      }
    }
    else {
      list = new ArrayList<WSRequest>(SIZE);
    }
  }

  /**
   * @param key
   * @return retorna a resposta
   */
  public synchronized Object get(WSRequest key) {
    for (WSRequest request : this.list) {
      if (request.hashCode() == key.hashCode() && request.equals(key)) {
        return request.getResult();
      }
    }
    return null;
  }

  /**
   * @param key
   * @return retorna a resposta
   */
  public synchronized boolean contain(WSRequest key) {
    for (WSRequest request : this.list) {
      if (request.hashCode() == key.hashCode() && request.equals(key)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @param key
   * @return retorna a resposta
   */
  public synchronized int indexOf(WSRequest key) {
    for (int n = 0; n < list.size(); n++) {
      WSRequest request = this.list.get(n);
      if (request.hashCode() == key.hashCode() && request.equals(key)) {
        return n;
      }
    }
    return -1;
  }

  /**
   * @param request
   * @param value
   */
  public synchronized void put(WSRequest request, Object value) {
    while (list.size() >= SIZE) {
      this.list.remove(list.size() - 1);
    }
    request.setResult(value);
    int index = indexOf(request);
    if (index >= 0) {
      list.remove(index);
    }
    else {
      while (list.size() + 1 >= SIZE) {
        this.list.remove(list.size() - 1);
      }
    }
    this.list.add(0, request);
    if (++count > 128) {
      try {
        checkpoint();
        count = 0;
      }
      catch (IOException e) {
        LogModel.getInstance().add(new ErrorLog(e));
      }
    }
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
  public synchronized static WSStorage getInstance() {
    if (instance == null) {
      instance = new WSStorage();
    }
    return instance;
  }

}
