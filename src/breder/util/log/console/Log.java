package breder.util.log.console;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

  private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
    "dd/MM/yy kk:mm:ss");

  public static OutputStream output;

  public void info(String project, int code, String message, Object... args) {
    print("[INFO]", project, code, message, args);
  }

  public void warning(String project, int code, String message, Object... args) {
    print("[WARNING]", project, code, message, args);
  }

  public void severe(String project, int code, String message, Object... args) {
    print("[SEVERE]", project, code, message, args);
  }

  public void error(String project, int code, String message, Object... args) {
    print("[ERROR]", project, code, message, args);
  }

  public void error(String project, int code, Throwable e) {
    StringBuilder sb = new StringBuilder();
    StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
    for (int n = getStackMethodIndex(); n < traceElements.length; n++) {
      sb.append(traceElements[n].toString());
      if (n != traceElements.length - 1) {
        sb.append("\n");
      }
    }
    print("[ERROR]", project, code, sb.toString());
  }

  protected void print(String type, String project, int code, String message,
    Object... args) {
    Thread thread = Thread.currentThread();
    StackTraceElement stack = thread.getStackTrace()[getStackMethodIndex()];
    StringBuilder sb = new StringBuilder();
    sb.append(type);
    sb.append(" ");
    sb.append(FORMAT.format(new Date()));
    sb.append(" ");
    sb.append(project);
    sb.append("(");
    sb.append(code);
    sb.append(") ");
    sb.append(thread.getName());
    sb.append("(");
    sb.append(thread.getId());
    sb.append(") ");
    sb.append(stack.getFileName().subSequence(0,
      stack.getFileName().length() - ".java".length()));
    sb.append(".");
    sb.append(stack.getMethodName());
    sb.append("(");
    sb.append(stack.getLineNumber());
    sb.append("):\n");
    if (args != null && args.length > 0) {
      sb.append(String.format(message, args));
    }
    else {
      sb.append(message);
    }
    try {
      output.write(sb.toString().getBytes("utf-8"));
    }
    catch (IOException e) {
      System.out.println(sb.toString());
    }
  }

  protected int getStackMethodIndex() {
    return 3;
  }

}
