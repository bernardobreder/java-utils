package breder.util.deploy;

import java.io.File;
import java.io.IOException;

public abstract class TomcatDeploy extends Deploy {

  protected File outputDir = new File("out");

  protected File tempDir = new File("tmp");

  protected abstract File getTomcatProject();

  protected abstract File[] getServerDirs();

  protected abstract String getProjectName();

  /**
   * Inicializa o deploy
   * 
   * @throws Exception
   */
  public void init() throws Exception {
    this.remove(outputDir);
    this.remove(tempDir);
    this.outputDir.mkdirs();
    this.buildWar(new File(outputDir, "server.war"), this.getTomcatProject(),
      this.getServerDirs());
    this.publish(String.format("webapps/%s.war", this.getProjectName()),
      new File(super.outDir, "server.war"));
    this.remove(outputDir);
  }

  protected void buildWar(File fileout, File tomcat, File... others)
    throws IOException {
    File destDir = new File("tmp");
    try {
      this.remove(destDir);
      destDir.mkdirs();
      {
        File file = new File(outputDir, "client.jar");
        if (file.exists()) {
          copy(new File(outputDir, "client.jar"), tempDir);
        }
      }
      File webinfDir = new File(destDir, "WEB-INF");
      webinfDir.mkdir();
      File classDir = new File(webinfDir, "classes");
      classDir.mkdir();
      File libDir = new File(webinfDir, "lib");
      libDir.mkdir();
      for (File other : others) {
        copyDir(new File(other, "bin"), classDir);
        {
          File libother = new File(other, "lib");
          if (libother.exists()) {
            copyDir(libother, libDir);
          }
        }
      }
      copyDir(new File(tomcat, "WEB-INF/classes"), classDir);
      for (File file : tomcat.listFiles()) {
        String name = file.getName();
        if (!(name.startsWith("."))) {
          if (file.isDirectory()) {
            if (!(name.equals("out") || name.equals("tmp")
              || name.equals("WEB-INF") || name.equals("work"))) {
              copy(file, new File(destDir, file.getName()));
            }
          }
          else {
            copy(file, destDir);
          }
        }
      }
      for (File file : new File(tomcat, "WEB-INF").listFiles()) {
        String name = file.getName();
        if (!(name.equals("classes") || name.equals("src"))) {
          if (file.isDirectory()) {
            copy(file, new File(webinfDir, file.getName()));
          }
          else {
            copy(file, webinfDir);
          }
        }
      }
      this.tmpJar(null, new File(outputDir, "server.war"), destDir);
    }
    finally {
      this.remove(destDir);
    }
  }

}
