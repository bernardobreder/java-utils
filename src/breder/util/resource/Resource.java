package breder.util.resource;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import breder.util.util.ImageUtil;

/**
 * Armazena todas as imagens
 * 
 * 
 * @author bbreder
 */
public class Resource {

  /** Instancia unica */
  private static final Resource instance = new Resource();

  /** Mapa de imagens */
  private static final Map<String, BufferedImage> images =
    new HashMap<String, BufferedImage>();

  /**
   * Construtor
   */
  private Resource() {
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getValidatorTrueImage() {
    return getImage("bot_help.gif");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getValidatorFalseImage() {
    return getImage("bot_help_over.gif");
  }

  /**
   * Imagem de trayicon
   * 
   * @return image
   */
  public BufferedImage getTrayIcon() {
    return getImage("trayicon.gif");
  }

  /**
   * Imagem de suma
   * 
   * @return image
   */
  public BufferedImage getPlusImage() {
    return getImage("plus.png");
  }

  /**
   * Imagem de menus
   * 
   * @return image
   */
  public BufferedImage getMinusImage() {
    return getImage("minus.png");
  }

  /**
   * Imagem de menus
   * 
   * @return image
   */
  public BufferedImage getConsulta32Image() {
    return getImage("consulta32.png");
  }

  /**
   * Imagem de menus
   * 
   * @return image
   */
  public BufferedImage getComment32Image() {
    return getImage("comment.png");
  }

  /**
   * Imagem de menus
   * 
   * @return image
   */
  public BufferedImage getChat32Image() {
    return getImage("chat32.png");
  }

  /**
   * Retorna a imagem
   * 
   * @param name
   * @return imagem
   */
  public static BufferedImage getImage(String name) {
    BufferedImage image = images.get(name);
    if (image == null) {
      image = ImageUtil.load("breder/util/resource/" + name);
      images.put(name, image);
    }
    return image;
  }

  /**
   * Retorna a imagem
   * 
   * @param name
   * @return imagem
   */
  public static BufferedImage getImagePathed(String name) {
    BufferedImage image = images.get(name);
    if (image == null) {
      try {
        image = ImageIO.read(Resource.class.getResourceAsStream(name));
        images.put(name, image);
      }
      catch (IOException e) {
        throw new Error(e);
      }
    }
    return image;
  }

  /**
   * Instancia unica
   * 
   * @return owner
   */
  public static Resource getInstance() {
    return instance;
  }

}
