package breder.util.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class BImage extends JComponent {

  private BufferedImage image;

  public BImage(BufferedImage image) {
    super();
    this.image = image;
  }

  @Override
  public void paint(Graphics g) {
    int w = this.getSize().width;
    int h = this.getSize().height;
    int iw = this.image.getWidth();
    int ih = this.image.getHeight();
    g.drawImage(image, w / 2 - iw / 2, h / 2 - ih / 2, null);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(image.getWidth(), image.getHeight());
  }

  public BufferedImage getImage() {
    return image;
  }

  public void setImage(BufferedImage image) {
    this.image = image;
  }

}
