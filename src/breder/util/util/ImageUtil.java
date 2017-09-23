package breder.util.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import breder.util.util.input.InputStreamUtil;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * Utilitario para imagem
 * 
 * @author bbreder
 */
public class ImageUtil {

  /**
   * Redimensiona uma imagem
   * 
   * @param input
   * @param width
   * @param height
   * @return imagem redimensionada
   * @throws Exception
   */
  public static InputStream cutRectImage(InputStream input, int width,
    int height) throws Exception {
    BufferedImage image = load(input);
    // Make sure the aspect ratio is maintained, so the image is not skewed
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    // Draw the scaled image
    BufferedImage thumbImage =
      new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2D = thumbImage.createGraphics();
    BufferedImage subimage;
    if (imageWidth > imageHeight) {
      int x = imageWidth / 2 - imageHeight / 2;
      subimage = image.getSubimage(x, 0, imageHeight, imageHeight);
    }
    else {
      int y = imageHeight / 2 - imageWidth / 2;
      subimage = image.getSubimage(0, y, imageWidth, imageWidth);
    }
    g2D.drawImage(subimage, 0, 0, width, height, null);
    // Write the scaled image to the outputstream
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
    JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
    int quality = 100; // Use between 1 and 100, with 100 being highest
    // quality
    quality = Math.max(0, Math.min(quality, 100));
    param.setQuality(quality / 100.0f, false);
    encoder.setJPEGEncodeParam(param);
    encoder.encode(thumbImage);
    ImageIO.write(thumbImage, "png", out);
    // Read the outputstream into the inputstream for the return value
    ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
    System.gc();
    return bis;
  }

  /**
   * Redimensiona uma imagem
   * 
   * @param input
   * @param width
   * @param height
   * @return imagem redimensionada
   * @throws Exception
   */
  public static InputStream putRectImage(InputStream input, int width,
    int height) throws Exception {
    BufferedImage image = load(input);
    // Make sure the aspect ratio is maintained, so the image is not skewed
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    // Draw the scaled image
    BufferedImage thumbImage =
      new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2D = thumbImage.createGraphics();
    if (imageWidth > imageHeight) {
      int g2dHeight = imageHeight * width / imageWidth;
      int y = height / 2 - g2dHeight / 2;
      g2D.drawImage(image, 0, y, width, g2dHeight, null);
    }
    else {
      int g2dWidth = imageWidth * height / imageHeight;
      int x = width / 2 - g2dWidth / 2;
      g2D.drawImage(image, x, 0, g2dWidth, height, null);
    }
    // Write the scaled image to the outputstream
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ImageIO.write(thumbImage, "png", out);
    // Read the outputstream into the inputstream for the return value
    ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
    System.gc();
    return bis;
  }

  /**
   * Carrega uma imagem
   * 
   * @param input
   * @return imagem
   */
  public static BufferedImage load(InputStream input) {
    try {
      ImageIO.setUseCache(false);
      BufferedImage image = ImageIO.read(input);
      System.gc();
      return image;
    }
    catch (Exception e) {
      e.printStackTrace();
      return new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
    }
  }

  /**
   * Carrega uma imagem
   * 
   * @param resource
   * @return imagem
   */
  public static BufferedImage load(String resource) {
    InputStream input = InputStreamUtil.getResource(resource);
    if (input == null) {
      throw new IllegalArgumentException(resource);
    }
    return load(input);
  }

  /**
   * Aplica um light na imagem
   * 
   * @param image
   * @return imagem
   * @throws IOException
   */
  public static BufferedImage light(BufferedImage image) {
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    BufferedImage result =
      new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2D = result.createGraphics();
    g2D.drawImage(image, 0, 0, imageWidth, imageHeight, null);
    int[] argb = new int[4];
    for (int x = 0; x < imageWidth; x++) {
      for (int y = 0; y < imageHeight; y++) {
        result.getData().getPixel(x, y, argb);
        if (argb[0] > 0) {
          argb[0] = (int) Math.min(argb[0] * 1.3, 255);
          argb[1] = (int) Math.min(argb[1] * 1.3, 255);
          argb[2] = (int) Math.min(argb[2] * 1.3, 255);
          argb[3] = (int) Math.min(argb[3] * 1.3, 255);
          result.getRaster().setPixel(x, y, argb);
        }
      }
    }
    return result;
  }

  /**
   * Aplica um light na imagem
   * 
   * @param image
   * @return imagem
   * @throws IOException
   */
  public static BufferedImage dark(BufferedImage image) {
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    BufferedImage result =
      new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2D = result.createGraphics();
    g2D.drawImage(image, 0, 0, imageWidth, imageHeight, null);
    int[] argb = new int[4];
    for (int x = 0; x < imageWidth; x++) {
      for (int y = 0; y < imageHeight; y++) {
        result.getData().getPixel(x, y, argb);
        if (argb[0] > 0) {
          argb[0] = (int) Math.max(argb[0] * 0.7, 0);
          argb[1] = (int) Math.max(argb[1] * 0.7, 0);
          argb[2] = (int) Math.max(argb[2] * 0.7, 0);
          argb[3] = (int) Math.max(argb[3] * 0.7, 0);
          result.getRaster().setPixel(x, y, argb);
        }
      }
    }
    return result;
  }

  /**
   * Testador
   * 
   * @param args
   * @throws FileNotFoundException
   * @throws IOException
   * @throws Exception
   */
  public static void main(String[] args) throws FileNotFoundException,
    IOException, Exception {
    InputStreamUtil.copyStream(ImageUtil.putRectImage(new FileInputStream(
      "/Users/bernardobreder/Dropbox/breder/Foto.png"), 100, 100),
      new FileOutputStream("/Users/bernardobreder/breder/photo_temp.png"));
  }

}
