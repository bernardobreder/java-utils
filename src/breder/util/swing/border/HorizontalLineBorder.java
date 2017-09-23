package breder.util.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

/**
 * Borda horizontal de linha
 * 
 * 
 * @author bbreder
 */
public class HorizontalLineBorder extends AbstractBorder {

  /** Espessura da linha */
  protected int thickness;
  /** Cor da linha */
  protected Color lineColor;

  /**
   * Construtor vazio
   */
  public HorizontalLineBorder() {
    this(1, Color.BLACK);
  }

  /**
   * Construtor para cor
   * 
   * @param color
   */
  public HorizontalLineBorder(Color color) {
    this(1, color);
  }

  /**
   * Construtor padrão
   * 
   * @param thickness
   * @param lineColor
   */
  public HorizontalLineBorder(int thickness, Color lineColor) {
    super();
    this.thickness = thickness;
    this.lineColor = lineColor;
  }

  /**
   * Paints the border for the specified component with the specified position
   * and size.
   * 
   * @param c the component for which this border is being painted
   * @param g the paint graphics
   * @param x the x position of the painted border
   * @param y the y position of the painted border
   * @param width the width of the painted border
   * @param height the height of the painted border
   */
  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int width,
    int height) {
    Color oldColor = g.getColor();
    g.setColor(lineColor);
    for (int i = 0; i < thickness; i++) {
      g.drawLine(x, y + i, width, y + i);
      g.drawLine(x, height - i - 1, width, height - i - 1);
    }
    g.setColor(oldColor);
  }

  /**
   * Returns the insets of the border.
   * 
   * @param c the component for which this border insets value applies
   */
  @Override
  public Insets getBorderInsets(Component c) {
    return new Insets(0, thickness, 0, thickness);
  }

  /**
   * Reinitialize the insets parameter with this Border's current Insets.
   * 
   * @param c the component for which this border insets value applies
   * @param insets the object to be reinitialized
   */
  @Override
  public Insets getBorderInsets(Component c, Insets insets) {
    insets.left = insets.right = insets.bottom = thickness;
    insets.top = insets.bottom = 0;
    return insets;
  }

}
