package breder.util.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class AbstractConfigurator {

  protected final Properties properties;

  private String filename;

  public AbstractConfigurator(String prefix, String name) {
    this.properties = new Properties();
    this.filename =
      String.format((this.isHidded() ? "." : "") + "%s.%s.properties", prefix,
        name);
    try {
      File file = new File(filename);
      if (file.exists()) {
        this.properties.load(new FileInputStream(file));
      }
    }
    catch (Exception e) {
    }
  }

  public boolean isHidded() {
    return true;
  }

  public void save() throws IOException {
    this.properties.store(new FileOutputStream(filename), null);
  }

  public String check(String key) {
    String value = properties.getProperty(key);
    if (value == null)
      throw new RuntimeException();
    return value;
  }

  public String check(String key, String defaultValue) throws IOException {
    String value = properties.getProperty(key);
    if (value == null) {
      properties.setProperty(key, defaultValue);
      value = defaultValue;
      this.save();
    }
    return value;
  }

  public String get(String key) {
    return properties.getProperty(key);
  }

  public String get(String key, String defaultValue) {
    String value = properties.getProperty(key);
    if (value == null) {
      return defaultValue;
    }
    else {
      return value;
    }
  }

  public void set(String key, String value) {
    properties.setProperty(key, value);
    try {
      this.save();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

}
