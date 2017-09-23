package breder.util.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.swing.JOptionPane;

public abstract class AbstractModel implements Serializable {

  private static final long serialVersionUID = 1L;

  private static GenericConfigurator CONFIGURATION;

  protected AbstractModel(GenericConfigurator conf) {
    try {
      String filename = conf.getFilename();
      if (filename == null) {
        filename = "data.b";
      }
      conf.setFilename(filename);
      AbstractModel.CONFIGURATION = conf;
      this.load(new File(filename));
    }
    catch (Exception e) {
    }
  }

  public void save() {
    try {
      ObjectOutputStream output =
        new ObjectOutputStream(
          new FileOutputStream(CONFIGURATION.getFilename()));
      output.writeObject(this);
      output.close();
    }
    catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Can not save the modification");
    }
  }

  public void load(File file) throws Exception {
    String filename = file.getAbsolutePath();
    if (new File(filename).exists()) {
      ObjectInputStream input =
        new ObjectInputStream(new FileInputStream(filename));
      Object object = input.readObject();
      Field[] fields = FieldUtil.getNotFields(this.getClass(), Modifier.STATIC);
      for (Field field : fields) {
        field.set(this, field.get(object));
      }
      input.close();
    }
  }

}
