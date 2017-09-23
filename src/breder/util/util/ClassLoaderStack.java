package breder.util.util;

import java.util.Stack;

/**
 * Pilha de classloader
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class ClassLoaderStack {

  /** Pilha */
  private static Stack<ClassLoader> stack = new Stack<ClassLoader>();

  static {
    push(ClassLoaderStack.class.getClassLoader());
  }

  /**
   * Empilha
   * 
   * @param cl
   */
  public static void push(ClassLoader cl) {
    stack.push(cl);
  }

  /**
   * Dempilha
   */
  public static void pop() {
    stack.pop();
  }

  /**
   * Carrega a classe
   * 
   * @param classname
   * @return classe
   * @throws ClassNotFoundException
   */
  public static Class<?> load(String classname) throws ClassNotFoundException {
    for (int n = stack.size() - 1; n >= 0; n--) {
      try {
        ClassLoader classLoader = stack.get(n);
        return classLoader.loadClass(classname);
      }
      catch (ClassNotFoundException e) {
      }
    }
    throw new ClassNotFoundException(classname);
  }

}
