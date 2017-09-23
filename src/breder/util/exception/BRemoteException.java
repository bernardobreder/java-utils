package breder.util.exception;

import java.rmi.RemoteException;

public class BRemoteException extends RemoteException {

  private final String message;

  private final String classname;

  private final String stacktraces;

  public BRemoteException(String message) {
    this(message, BRemoteException.class.getName(), build(Thread
      .currentThread().getStackTrace()));
  }

  public BRemoteException(Throwable t) {
    this(t.getMessage(), t.getClass().getName(), build(t.getStackTrace()));
  }

  private static String build(StackTraceElement[] stackTraceElements) {
    StringBuilder sb = new StringBuilder();
    for (StackTraceElement elem : stackTraceElements) {
      sb.append(elem.toString() + "\n");
    }
    return sb.toString();
  }

  public BRemoteException(String message, String classname, String stacktraces) {
    this.message = message;
    this.classname = classname;
    this.stacktraces = stacktraces;
  }

  public String getMessage() {
    return message;
  }

  public String getClassname() {
    return classname;
  }

  public String getStacktraces() {
    return stacktraces;
  }

  @Override
  public String toString() {
    return String.format("%s: %s\r\n%s", getClassname(), getMessage(),
      getStacktraces());
  }

}
