package breder.util.util;

/**
 * Classe utilitaria
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class ExceptionUtil {

  /**
   * Cria o texto da menssagem de error.
   * 
   * @param t
   * @return messagem
   */
  public static String buildMessage(Throwable t) {
    StringBuilder sb = new StringBuilder();
    sb.append("Exception in thread \"" + Thread.currentThread().getName()
      + "\" ");
    sb.append(t.toString() + "\n");
    for (StackTraceElement e : t.getStackTrace()) {
      sb.append("\tat " + e.toString() + "\n");
    }
    while ((t = t.getCause()) != null) {
      sb.append("Caused by: ");
      sb.append(t.toString() + "\n");
      for (StackTraceElement e : t.getStackTrace()) {
        sb.append("\tat " + e.toString() + "\n");
      }
    }
    return sb.toString().trim();
  }

  /**
   * Testador
   * 
   * @param args
   */
  public static void main(String[] args) {
    System.out.println(ExceptionUtil.buildMessage(new Exception(
      new IllegalArgumentException(new IllegalAccessError()))));
  }

}
