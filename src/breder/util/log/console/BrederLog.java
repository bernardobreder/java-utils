package breder.util.log.console;

public class BrederLog extends Log {

  private static final Log log = new BrederLog();

  public static void info(BrederCode code, String message, Object... args) {
    log.info("Breder", code.code, message, args);
  }

  public static void warning(BrederCode code, String message, Object... args) {
    log.warning("Breder", code.code, message, args);
  }

  public static void severe(BrederCode code, String message, Object... args) {
    log.severe("Breder", code.code, message, args);
  }

  public static void error(BrederCode code, String message, Object... args) {
    log.error("Breder", code.code, message, args);
  }

  public static void error(BrederCode code, Throwable e) {
    log.error("Breder", code.code, e);
  }

  public static void info(String message, Object... args) {
    log.info("Breder", 0, message, args);
  }

  public static void warning(String message, Object... args) {
    log.warning("Breder", 0, message, args);
  }

  public static void severe(String message, Object... args) {
    log.severe("Breder", 0, message, args);
  }

  public static void error(String message, Object... args) {
    log.error("Breder", 0, message, args);
  }

  public static void error(Throwable e) {
    log.error("Breder", 0, e);
  }

  protected int getStackMethodIndex() {
    return 4;
  }

}
