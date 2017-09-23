package breder.util.io;

import java.io.File;

import breder.util.exception.SOSupportedException;
import breder.util.so.SoUtil;

public class HomeFile extends File {

  private static final File DIR;

  static {
    if (SoUtil.isUnix()) {
      String env = null;
      if (new File("~").exists()) {
        env = new File("~/").getAbsolutePath();
      }
      if (env == null) {
        env = System.getenv("HOME");
        if (env == null) {
          env = System.getenv("home");
        }
      }
      if (env == null) {
        DIR = new File("./");
      }
      else {
        DIR = new File(env);
      }
    }
    else if (SoUtil.isWindow()) {
      DIR = new File("c:\\");
    }
    else {
      throw new SOSupportedException();
    }
  }

  public HomeFile() {
    super(DIR.getAbsolutePath());
  }

}
