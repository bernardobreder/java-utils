package breder.util.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TomcatLog {

  public static String projectName;

  private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
    "MMM d, yyyy h:mm:ss aaa");

  private static void print(String level, String format, Object... args) {
    System.out.println(String.format("%s %s", FORMAT.format(new Date()), Thread
      .currentThread().getStackTrace()[3].toString()));
    if (projectName == null) {
      System.out.println(String.format("%s: %s", level, String.format(format,
        args)));
    }
    else {
      System.out.println(String.format("INFO: [%s] %s", level, projectName,
        String.format(format, args)));
    }
  }

  public static void info(String format, Object... args) {
    print("INFO", format, args);
  }

  public static void warning(String format, Object... args) {
    print("WARNING", format, args);
  }

  public static void severe(String format, Object... args) {
    print("SEVERE", format, args);
  }

  public static void error(Throwable t) {
    String message = t.getMessage();
    if (message != null) {
      message = message.replace('\n', ' ');
    }
    print("ERROR", String.format("throw %s at %s: %s", t.getClass()
      .getSimpleName(), t.getStackTrace()[0], message));
  }

  public static String getProjectName() {
    return projectName;
  }

  public static void setProjectName(String projectName) {
    TomcatLog.projectName = projectName;
  }

}
