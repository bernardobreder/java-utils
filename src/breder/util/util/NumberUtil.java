package breder.util.util;

/**
 * Métodos utilitarios
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class NumberUtil {

  /**
   * Indica se é um número
   * 
   * @param value
   * @return numero
   */
  public static boolean isDouble(String value) {
    try {
      new Double(value);
      return true;
    }
    catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Indica se é um número
   * 
   * @param value
   * @return numero
   */
  public static boolean isFloat(String value) {
    try {
      new Float(value);
      return true;
    }
    catch (NumberFormatException e) {
      return false;
    }
  }

}
