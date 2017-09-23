package breder.util.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Métodos utilitários para a classe System
 * 
 * 
 * @author Bernardo Breder
 */
public class SystemUtil {

  /**
   * Carrega a biblioteca
   * 
   * @param filename
   * @throws IOException
   */
  public static void loadLibrary(String filename) throws IOException {
    String osarch = System.getProperty("os.arch");
    if (osarch.endsWith("64")) {
      filename += "64";
    }
    filename = System.mapLibraryName(filename);
    try {
      System.loadLibrary(filename);
      return;
    }
    catch (Throwable e) {
    }
    InputStream input =
      SystemUtil.class.getClassLoader().getResourceAsStream(filename);
    if (input == null) {
      input = SystemUtil.class.getResourceAsStream(filename);
    }
    if (input == null) {
      throw new FileNotFoundException("libname: " + filename + " not found");
    }
    File tmplib = File.createTempFile("lib" + filename, ".lib");
    try {
      OutputStream out = new FileOutputStream(tmplib);
      byte[] buf = new byte[1024];
      for (int len; (len = input.read(buf)) != -1;) {
        out.write(buf, 0, len);
      }
      input.close();
      out.close();
      System.load(tmplib.getAbsolutePath());
    }
    finally {
      if (!tmplib.delete()) {
        tmplib.deleteOnExit();
      }
    }
  }

  /**
   * Adiciona um diretório para carga de native library
   * 
   * @param resource
   * @throws NoSuchFieldException
   * @throws SecurityException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws IOException
   */
  public static void registryDirectory(String filename)
    throws SecurityException, NoSuchFieldException, IllegalArgumentException,
    IllegalAccessException, IOException {
    File file = File.createTempFile("native", "library");
    file.delete();
    file.deleteOnExit();
    file = new File(file.getParentFile(), filename);
    if (!file.exists()) {
      InputStream input =
        SystemUtil.class.getClassLoader().getResourceAsStream(filename);
      if (input == null) {
        throw new FileNotFoundException(filename);
      }
      file.createNewFile();
      FileOutputStream output = new FileOutputStream(file);
      for (int n; (n = input.read()) != -1;) {
        output.write((char) n);
      }
      output.close();
    }
    Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
    fieldSysPath.setAccessible(true);
    String[] values = (String[]) fieldSysPath.get(null);
    HashSet<String> set = new HashSet<String>(Arrays.asList(values));
    set.add(file.getParentFile().getAbsolutePath());
    fieldSysPath.set(null, set.toArray(new String[set.size()]));
  }

}
