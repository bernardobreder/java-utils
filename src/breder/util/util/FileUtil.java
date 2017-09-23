package breder.util.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import breder.util.util.input.InputStreamUtil;

public abstract class FileUtil {

  static {
    init();
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

      @Override
      public void run() {
        init();
      }
    }));
  }

  private static void init() {
    File[] files = new File(".").listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory() && !file.isHidden()
          && file.getName().startsWith("tmp-")) {
          remove(file);
        }
      }
    }
  }

  public static String getSharedLibraryExtension() {
    return ".so";
  }

  public static String getExecutableExtension() {
    return "";
  }

  public static void copy(InputStream input, OutputStream output)
    throws IOException {
    byte[] bytes = new byte[1024];
    while (true) {
      int n = input.read(bytes);
      if (n == -1)
        break;
      output.write(bytes, 0, n);
    }
  }

  public static void remove(File file) {
    if (file.isDirectory()) {
      File[] files = file.listFiles();
      for (File other : files) {
        remove(other);
      }
    }
    if (file.exists()) {
      file.delete();
    }
  }

  public static File[] list(File dir, String ext) {
    List<File> list = new ArrayList<File>();
    list(dir, ext, list);
    return list.toArray(new File[list.size()]);
  }

  private static void list(File dir, String ext, List<File> list) {
    File[] files = dir.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          list(file, ext, list);
        }
        else if (file.isFile()) {
          if (file.getName().endsWith("." + ext)) {
            list.add(file);
          }
        }
      }
    }
  }

  public static File[] listContain(File dir, String format) {
    List<File> list = new ArrayList<File>();
    listContain(dir, format, list);
    return list.toArray(new File[list.size()]);
  }

  private static void listContain(File dir, String format, List<File> list) {
    File[] files = dir.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          list(file, format, list);
        }
        else if (file.isFile()) {
          if (file.getName().contains(format)) {
            list.add(file);
          }
        }
      }
    }
  }

  public static File build(String... strings) {
    if (strings.length == 0) {
      return new File("");
    }
    File file = new File(strings[0]);
    for (int n = 1; n < strings.length; n++) {
      file = new File(file, strings[n]);
    }
    return file;
  }

  public static File build(File dir, String... strings) {
    if (strings.length == 0) {
      return dir;
    }
    File file = dir;
    for (int n = 0; n < strings.length; n++) {
      file = new File(file, strings[n]);
    }
    return file;
  }

  public static File buildTmp() {
    Object o = new Object();
    File file = new File("tmp-");
    file = new File("tmp-" + o.hashCode());
    return file;
  }

  public static boolean existSameName(File file) {
    File aux = null;
    for (File f : file.getParentFile().listFiles()) {
      if (f.getName().equals(file.getName())) {
        aux = f;
        break;
      }
    }
    return aux != null;
  }

  public static File copy(File srcFile, File destDir) throws IOException {
    if (srcFile.isDirectory()) {
      copyDir(srcFile, destDir);
    }
    else if (srcFile.isFile()) {
      copyFile(srcFile, destDir);
    }
    return new File(destDir, srcFile.getName());
  }

  public static void copyDir(File srcDir, File destDir) throws IOException {
    if (!srcDir.getName().startsWith(".") && !srcDir.isHidden()) {
      destDir.mkdirs();
      File[] files = srcDir.listFiles();
      if (files != null) {
        for (File file : files) {
          if (!file.isHidden()) {
            if (file.isFile()) {
              copyFile(file, destDir);
            }
            else {
              File dir = new File(destDir, file.getName());
              copyDir(file, dir);
            }
          }
        }
      }
    }
  }

  public static void copyFile(File srcFile, File destDir) throws IOException {
    if (!destDir.exists()) {
      destDir.mkdirs();
    }
    if (!destDir.isDirectory()) {
      throw new RuntimeException("dest directory is not a directory");
    }
    if (!srcFile.exists() || !srcFile.isFile()) {
      throw new RuntimeException("src file is not a file");
    }
    File outputFile = new File(destDir, srcFile.getName());
    if (outputFile.exists()) {
      outputFile.delete();
    }
    outputFile.createNewFile();
    FileOutputStream output = new FileOutputStream(outputFile);
    FileInputStream input = new FileInputStream(srcFile);
    copyStream(input, output);
    output.close();
    input.close();
  }

  public static void copyStream(InputStream input, OutputStream output)
    throws IOException {
    byte[] bytes = InputStreamUtil.getBytes(input);
    float fstep = (float) bytes.length / 1024;
    if ((int) fstep != fstep) {
      fstep = (int) fstep + 1;
    }
    for (int n = 0; n < bytes.length; n += 1024) {
      int len = Math.min(1024, bytes.length - n);
      output.write(bytes, n, len);
    }
  }

}
