package breder.util.net.ws.shared;

import java.io.Serializable;

/**
 * Erro de WebService
 * 
 * 
 * @author Bernardo Breder
 */
public class WSException extends Exception implements Serializable {

  /**
   * 
   */
  public WSException() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @param arg0
   * @param arg1
   */
  public WSException(String arg0, Throwable arg1) {
    super(arg0, arg1);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param arg0
   */
  public WSException(String arg0) {
    super(arg0);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param arg0
   */
  public WSException(Throwable arg0) {
    super(arg0);
    // TODO Auto-generated constructor stub
  }

}
