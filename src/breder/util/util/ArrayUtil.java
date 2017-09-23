package breder.util.util;

import java.util.Collection;
import java.util.List;

/**
 * Classe utilitária para Array
 * 
 * @author bernardobreder
 */
public class ArrayUtil {

  /**
   * ToString
   * 
   * @param c
   * @param separator
   * @return String
   */
  public static <E extends Object> String toString(Collection<E> c,
    String separator) {
    if (c.size() == 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (E object : c) {
      sb.append(object.toString());
      sb.append(separator);
    }
    sb.delete(sb.length() - separator.length(), sb.length());
    return sb.toString();
  }

  /**
   * Cria uma lista através de uma String
   * 
   * @param toString
   * @return Lista
   */
  public List<String> build(String toString) {
    return null;
  }

}
