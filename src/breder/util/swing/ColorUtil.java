package breder.util.swing;

import java.awt.Color;

/**
 * Classe utilitária para cor
 * 
 * @author bernardobreder
 */
public class ColorUtil {

  /**
   * Aumenta o brilho
   * 
   * @param c
   * @param percent
   * @return cor
   */
  public static Color light(Color c, float percent) {
    return new Color((int) (c.getRed() + Math.abs(255 - c.getRed()) * percent),
      (int) (c.getGreen() + Math.abs(255 - c.getGreen()) * percent), (int) (c
        .getBlue() + Math.abs(255 - c.getBlue()) * percent));
  }

  /**
   * Escurece
   * 
   * @param c
   * @param percent
   * @return cor
   */
  public static Color dark(Color c, float percent) {
    return new Color((int) (c.getRed() * percent),
      (int) (c.getGreen() * percent), (int) (c.getBlue() * percent));
  }

}
