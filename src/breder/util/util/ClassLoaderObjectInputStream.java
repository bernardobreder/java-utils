package breder.util.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * Classloader
 * 
 * 
 * @author Bernardo Breder
 */
public class ClassLoaderObjectInputStream extends ObjectInputStream {

  /** Classloader */
  private ClassLoader classLoader;

  /**
   * Construtor
   * 
   * @param classLoader
   * @param in
   * @throws IOException
   */
  public ClassLoaderObjectInputStream(ClassLoader classLoader, InputStream in)
    throws IOException {
    super(in);
    this.classLoader = classLoader;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
    ClassNotFoundException {

    try {
      String name = desc.getName();
      return Class.forName(name, false, classLoader);
    }
    catch (ClassNotFoundException e) {
      return super.resolveClass(desc);
    }
  }

}
