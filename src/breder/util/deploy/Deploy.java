package breder.util.deploy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import breder.util.net.BrederFtp;
import breder.util.net.Ftp;
import breder.util.so.SoUtil;
import breder.util.util.FileUtil;
import breder.util.util.input.InputStreamUtil;

public abstract class Deploy {

  protected File tempDir = new File("tmp");

  protected File outDir = new File("out");

  protected File iconfile = new File(
    "../breder.util/src/breder/util/deploy/resource/quebracabeca.ico");

  protected File icnsfile = new File(
    "../breder.util/src/breder/util/deploy//resource/quebracabeca.icns");

  public Deploy() {
    this.remove(this.outDir);
    this.outDir.mkdirs();
    this.remove(this.tempDir);
    this.tempDir.mkdirs();
  }

  public void buildZip(File fileout, File... dirins) throws IOException {
    File tempDir = FileUtil.buildTmp();
    tempDir.mkdirs();
    try {
      for (File dirin : dirins) {
        this.tmpCopy(tempDir, dirin);
      }
      this.tmpZip(fileout, tempDir);
    }
    finally {
      FileUtil.remove(tempDir);
    }
  }

  public void extractZip(ZipInputStream input, File dir) throws IOException {
    InputStreamUtil.extractZip(input, dir);
  }

  public void buildJar(File fileout, String mainclass, File... dirins)
    throws IOException {
    File tempDir = FileUtil.buildTmp();
    tempDir.mkdirs();
    try {
      for (File dirin : dirins) {
        if (dirin.isFile()) {
          this.tmpCopy(tempDir, dirin);
        }
        else {
          File[] files = dirin.listFiles();
          if (files == null) {
            throw new RuntimeException(dirin.getAbsolutePath());
          }
          for (File file : files) {
            this.tmpCopy(tempDir, file);
          }
        }
      }
      this.tmpJar(mainclass, fileout, tempDir);
    }
    finally {
      this.remove(tempDir);
    }
  }

  public void tmpCopy(File destDir, File dirin) throws IOException {
    if (dirin.exists()) {
      if (dirin.isDirectory()) {
        copyDir(dirin, new File(destDir, dirin.getName()));
      }
      else {
        if (dirin.getName().endsWith(".jar")) {
          JarInputStream input = new JarInputStream(new FileInputStream(dirin));
          try {
            JarEntry entry = input.getNextJarEntry();
            while (entry != null) {
              if (entry.isDirectory()) {
                File dir = new File(destDir, entry.getName());
                dir.mkdirs();
              }
              else {
                int index = entry.getName().lastIndexOf('/');
                if (index != -1) {
                  File dir =
                    new File(destDir, entry.getName().substring(0, index));
                  dir.mkdirs();
                }
                copyStream(input, new FileOutputStream(new File(destDir, entry
                  .getName())));
              }
              input.closeEntry();
              entry = input.getNextJarEntry();
            }
          }
          finally {
            input.close();
          }

        }
        else {
          copyFile(dirin, destDir);
        }
      }
    }
  }

  public void tmpZip(File fileout, File tempDir2) throws IOException {
    fileout.getParentFile().mkdirs();
    File destDir = tempDir2;
    FileOutputStream foutput = new FileOutputStream(fileout);
    ZipOutputStream output = new ZipOutputStream(foutput);
    try {
      for (File aux : destDir.listFiles()) {
        add(destDir, aux, output);
      }
    }
    finally {
      output.close();
      foutput.close();
    }
  }

  public void tmpJar(String mainclass, File fileout, File destDir)
    throws IOException {
    Manifest manifest = new Manifest();
    manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
    if (mainclass != null) {
      manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, mainclass);
    }
    // manifest.getMainAttributes().put(Attributes.Name.CLASS_PATH, ".");
    FileOutputStream foutput = new FileOutputStream(fileout);
    JarOutputStream output = new JarOutputStream(foutput, manifest);
    try {
      for (File aux : destDir.listFiles()) {
        add(destDir, aux, output);
      }
    }
    finally {
      output.close();
      foutput.close();
    }
  }

  public static void add(File baseDir, File source, ZipOutputStream target)
    throws IOException {
    if (source.getName().startsWith(".")) {
      return;
    }
    BufferedInputStream in = null;
    try {
      String base = baseDir.getPath().replace("\\", "/");
      String name = source.getPath().replace("\\", "/");
      name = name.substring(base.length());
      if (name.charAt(0) == '/') {
        name = name.substring(1);
      }
      if (source.isDirectory()) {
        if (!name.isEmpty()) {
          if (!name.endsWith("/")) {
            name += "/";
          }
          JarEntry entry = new JarEntry(name);
          entry.setTime(source.lastModified());
          target.putNextEntry(entry);
          target.closeEntry();
        }
        for (File nestedFile : source.listFiles()) {
          add(baseDir, nestedFile, target);
        }
      }
      else {
        JarEntry entry = new JarEntry(name);
        entry.setTime(source.lastModified());
        target.putNextEntry(entry);
        in = new BufferedInputStream(new FileInputStream(source));
        copyStream(in, target);
        target.closeEntry();
      }
    }
    finally {
      if (in != null) {
        in.close();
      }
    }
  }

  public void remove(File file) {
    if (!file.exists()) {
      return;
    }
    if (file.isDirectory()) {
      File[] files = file.listFiles();
      for (File other : files) {
        this.remove(other);
      }
    }
    if (file.exists()) {
      file.delete();
    }
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
    if (!srcDir.getName().startsWith(".")) {
      destDir.mkdirs();
      for (File file : srcDir.listFiles()) {
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
    for (int n = 0, s = 0; n < bytes.length; n += 1024, s++) {
      int len = Math.min(1024, bytes.length - n);
      output.write(bytes, n, len);
      System.out.println(String.format("Step: %d/%d", s + 1, (int) fstep));
    }
  }

  public void publish(final String url, final File filename) throws IOException {
    InputStream input = new FileInputStream(filename);
    OutputStream output = getFtp(url).getOutputStream();
    copyStream(input, output);
    input.close();
    output.close();
  }

  public Ftp getFtp(final String url) throws IOException {
    return new BrederFtp(url);
  }

  public void buildAppSwt(File jarFile, String mainClass, File iconFile,
    File swtLibFile, File appFile) throws IOException {
    if (!jarFile.exists()) {
      throw new IllegalArgumentException("jar not existe : "
        + jarFile.getAbsolutePath());
    }
    if (!iconFile.exists()) {
      throw new IllegalArgumentException("icon not existe : "
        + iconFile.getAbsolutePath());
    }
    if (appFile.exists()) {
      this.remove(appFile);
    }
    appFile.mkdirs();
    File contentDir = new File(appFile, "Contents");
    contentDir.mkdirs();
    {
      String appName =
        appFile.getName().substring(0,
          appFile.getName().length() - ".app".length());
      File infoFile = new File(contentDir, "Info.plist");
      OutputStream output = new FileOutputStream(infoFile);
      output
        .write(String
          .format(
            "<?xml version='1.0' encoding='UTF-8'?>"
              + "<!DOCTYPE plist SYSTEM 'file://localhost/System/Library/DTDs/PropertyList.dtd'>"
              + "<plist version='0.9'>" + "<dict>" + "<key>CFBundleName</key>"
              + "<string>%s</string>" + "<key>CFBundleExecutable</key>"
              + "<string>main</string>" + "<key>CFBundleGetInfoString</key>"
              + "<string>SWT for Mac OS X</string>"
              + "<key>CFBundleIconFile</key>" + "<string>%s</string>"
              + "<key>CFBundleIdentifier</key>"
              + "<string>org.eclipse.swt.swthello</string>"
              + "<key>CFBundleInfoDictionaryVersion</key>"
              + "<string>6.0</string>" + "<key>CFBundlePackageType</key>"
              + "<string>APPL</string>"
              + "<key>CFBundleShortVersionString</key>"
              + "<string>1.0</string>" + "<key>CFBundleSignature</key>"
              + "<string>?????</string>" + "<key>CFBundleVersion</key>"
              + "<string>1.0</string>" + "<key>NSPrincipalClass</key>"
              + "<string>NSApplication</string>" + "</dict>" + "</plist>",
            appName, iconFile.getName()).getBytes());
      output.close();
    }
    {
      File macosDir = new File(contentDir, "MacOS");
      macosDir.mkdirs();
      {
        File mainFile = new File(macosDir, "main");
        OutputStream output = new FileOutputStream(mainFile);
        output
          .write(("#!/bin/sh\n"
            + "BASEDIR=`dirname $0`\n"
            + "exec java -XstartOnFirstThread -classpath $BASEDIR/swt/swt.jar:$BASEDIR/"
            + jarFile.getName() + " -Djava.library.path=$BASEDIR/swt " + mainClass)
            .getBytes());
        output.close();
        new ProcessBuilder("chmod", "+x", mainFile.getAbsolutePath()).start();
      }
      {
        File swtDir = new File(macosDir, "swt");
        swtDir.mkdirs();
        File swtFile = copy(swtLibFile, swtDir);
        swtFile.renameTo(new File(swtDir, "swt.jar"));
      }
      copy(jarFile, macosDir);
    }
    {
      File resourcesDir = new File(contentDir, "Resources");
      resourcesDir.mkdirs();
      copyFile(iconFile, resourcesDir);
    }
  }

  public void buildApp(File jarFile, String mainClass, File iconFile,
    File appFile) throws IOException {
    if (!jarFile.exists()) {
      throw new IllegalArgumentException("jar not existe : "
        + jarFile.getAbsolutePath());
    }
    if (!iconFile.exists()) {
      throw new IllegalArgumentException("icon not existe : "
        + iconFile.getAbsolutePath());
    }
    if (appFile.exists()) {
      this.remove(appFile);
    }
    appFile.mkdirs();
    File contentDir = new File(appFile, "Contents");
    contentDir.mkdirs();
    {
      String appName =
        appFile.getName().substring(0,
          appFile.getName().length() - ".app".length());
      File infoFile = new File(contentDir, "Info.plist");
      OutputStream output = new FileOutputStream(infoFile);
      output
        .write(String
          .format(
            "<?xml version='1.0' encoding='UTF-8'?>"
              + "<!DOCTYPE plist SYSTEM 'file://localhost/System/Library/DTDs/PropertyList.dtd'>"
              + "<plist version='0.9'>" + "<dict>" + "<key>CFBundleName</key>"
              + "<string>%s</string>" + "<key>CFBundleIdentifier</key>"
              + "<string>%s</string>" + "<key>CFBundleVersion</key>"
              + "<string>100.0</string>"
              + "<key>CFBundleAllowMixedLocalizations</key>"
              + "<string>true</string>" + "<key>CFBundleExecutable</key>"
              + "<string>JavaApplicationStub</string>"
              + "<key>CFBundleDevelopmentRegion</key>"
              + "<string>English</string>" + "<key>CFBundlePackageType</key>"
              + "<string>APPL</string>" + "<key>CFBundleSignature</key>"
              + "<string>????</string>"
              + "<key>CFBundleInfoDictionaryVersion</key>"
              + "<string>6.0</string>" + "<key>CFBundleIconFile</key>"
              + "        <string>%s</string>" + "<key>Java</key>" + "<dict>"
              + "        <key>MainClass</key>" + "        <string>%s</string>"
              + "        <key>JVMVersion</key>"
              + "        <string>1.5+</string>"
              + "        <key>ClassPath</key>"
              + "        <string>$JAVAROOT/%s</string>" + "</dict>" + "</dict>"
              + "</plist>", appName, mainClass, iconFile.getName(), mainClass,
            jarFile.getName()).getBytes());
      output.close();
    }
    {
      File macosDir = new File(contentDir, "MacOS");
      File cmdFile =
        new File(
          "/System/Library/Frameworks/JavaVM.framework/Resources/MacOS/JavaApplicationStub");
      macosDir.mkdirs();
      copy(cmdFile, macosDir);
      File file = new File(macosDir, cmdFile.getName());
      if (SoUtil.isUnix()) {
        new ProcessBuilder("chmod", "+x", file.toString()).start();
      }
    }
    {
      File pkginfoFile = new File(contentDir, "PkgInfo");
      OutputStream output = new FileOutputStream(pkginfoFile);
      output.write("APPL????".getBytes());
      output.close();
    }
    {
      File resourcesDir = new File(contentDir, "Resources");
      resourcesDir.mkdirs();
      copyFile(iconFile, resourcesDir);
      {
        File javaDir = new File(resourcesDir, "Java");
        jarFile.mkdirs();
        copyFile(jarFile, javaDir);
      }
    }
  }

  public void buildExe(File jarFile, File iconFile, boolean isConsole,
    File launch4jExe, File exeFile) throws IOException {
    if (!jarFile.exists()) {
      throw new IllegalArgumentException("jar not existe : "
        + jarFile.getAbsolutePath());
    }
    if (!launch4jExe.exists()) {
      throw new IllegalArgumentException("launch4j not existe : "
        + launch4jExe.getAbsolutePath());
    }
    if (exeFile.exists()) {
      this.remove(exeFile);
    }
    File xmlFile = new File(this.outDir, "launch4j.xml");
    {
      String headertype = isConsole ? "console" : "gui";
      String icon = iconFile != null ? iconFile.getAbsolutePath() : "";
      OutputStream output = new FileOutputStream(xmlFile);
      output.write(String.format(
        "<launch4jConfig>" + "<dontWrapJar>false</dontWrapJar>"
          + "<headerType>%s</headerType>" + "<jar>%s</jar>"
          + "<outfile>%s</outfile>" + "<icon>%s</icon>"
          + "<errTitle></errTitle>" + "<cmdLine></cmdLine>" + "<chdir></chdir>"
          + "<priority>normal</priority>"
          + "<downloadUrl>http://java.com/download</downloadUrl>"
          + "<supportUrl></supportUrl>"
          + "<customProcName>false</customProcName>"
          + "<stayAlive>false</stayAlive>" + "<manifest></manifest>" + "<jre>"
          + "<path></path>" + "<minVersion>1.5.0</minVersion>"
          + "<maxVersion></maxVersion>"
          + "<jdkPreference>preferJre</jdkPreference>" + "</jre>"
          + "</launch4jConfig>", headertype, jarFile.getAbsolutePath(),
        exeFile.getAbsolutePath(), icon).getBytes());
      output.close();
    }
    ProcessBuilder builder =
      new ProcessBuilder(Arrays.asList(launch4jExe.getAbsolutePath(), xmlFile
        .getAbsolutePath()));
    Process process = builder.start();
    System.out.println(new String(InputStreamUtil.getBytes(process
      .getErrorStream())));
  }

  public void finish() {
    this.remove(this.tempDir);
    this.remove(this.outDir);
  }

}
