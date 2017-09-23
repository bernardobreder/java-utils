package breder.util.swing.layout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import breder.util.swing.GBC;

/**
 * Layout vertical
 * 
 * 
 * @author Bernardo Breder
 */
public class VerticalLayout implements LayoutManager {

  /**
   * The horizontal alignment constant that designates centering. Also used to
   * designate center anchoring.
   */
  public final static int CENTER = 0;
  /**
   * The horizontal alignment constant that designates right justification.
   */
  public final static int RIGHT = 1;
  /**
   * The horizontal alignment constant that designates left justification.
   */
  public final static int LEFT = 2;
  /**
   * The horizontal alignment constant that designates stretching the component
   * horizontally.
   */
  public final static int BOTH = 3;
  /**
   * The anchoring constant that designates anchoring to the top of the display
   * area
   */
  public final static int TOP = 1;
  /**
   * The anchoring constant that designates anchoring to the bottom of the
   * display area
   */
  public final static int BOTTOM = 2;
  /** Espacamento vertical */
  private int vgap;
  /** Alinhamento */
  private int alignment; // LEFT, RIGHT, CENTER or BOTH...how the components
  /** Alinhamento vertical */
  private int anchor; // TOP, BOTTOM or CENTER ...where are the components

  /**
   * Constructs an instance of VerticalLayout with a vertical vgap of 5 pixels,
   * horizontal centering and anchored to the top of the display area.
   */
  public VerticalLayout() {
    this(5, BOTH, TOP);
  }

  /**
   * Constructs a VerticalLayout instance with horizontal centering, anchored to
   * the top with the specified vgap
   * 
   * @param vgap An int value indicating the vertical seperation of the
   *        components
   */
  public VerticalLayout(int vgap) {
    this(vgap, BOTH, TOP);
  }

  /**
   * Constructs a VerticalLayout instance anchored to the top with the specified
   * vgap and horizontal alignment
   * 
   * @param vgap An int value indicating the vertical seperation of the
   *        components
   * @param alignment An int value which is one of
   *        <code>RIGHT, LEFT, CENTER, BOTH</code> for the horizontal alignment.
   */
  public VerticalLayout(int vgap, int alignment) {
    this(vgap, alignment, TOP);
  }

  /**
   * Constructs a VerticalLayout instance with the specified vgap, horizontal
   * alignment and anchoring
   * 
   * @param vgap An int value indicating the vertical seperation of the
   *        components
   * @param alignment An int value which is one of
   *        <code>RIGHT, LEFT, CENTER, BOTH</code> for the horizontal alignment.
   * @param anchor An int value which is one of <code>TOP, BOTTOM, CENTER</code>
   *        indicating where the components are to appear if the display area
   *        exceeds the minimum necessary.
   */
  public VerticalLayout(int vgap, int alignment, int anchor) {
    this.vgap = vgap;
    this.alignment = alignment;
    this.anchor = anchor;
  }

  /**
   * Calcula o tamanho
   * 
   * @param parent
   * @param minimum
   * @return tamanho
   */
  private Dimension layoutSize(Container parent, boolean minimum) {
    Dimension dim = new Dimension(0, 0);
    Dimension d;
    synchronized (parent.getTreeLock()) {
      int n = parent.getComponentCount();
      for (int i = 0; i < n; i++) {
        Component c = parent.getComponent(i);
        if (c.isVisible()) {
          d = minimum ? c.getMinimumSize() : c.getPreferredSize();
          dim.width = Math.max(dim.width, d.width);
          dim.height += d.height;
          if (i > 0) {
            dim.height += vgap;
          }
        }
      }
    }
    Insets insets = parent.getInsets();
    dim.width += insets.left + insets.right;
    dim.height += insets.top + insets.bottom + vgap + vgap;
    return dim;
  }

  /**
   * Lays out the container.
   * 
   * @param parent
   */
  @Override
  public void layoutContainer(Container parent) {
    Insets insets = parent.getInsets();
    synchronized (parent.getTreeLock()) {
      int n = parent.getComponentCount();
      Dimension pd = parent.getSize();
      int y = 0;
      // work out the total size
      for (int i = 0; i < n; i++) {
        Component c = parent.getComponent(i);
        if (c.isVisible()) {
          Dimension d = c.getPreferredSize();
          y += d.height + vgap;
        }
      }
      y -= vgap; // otherwise there's a vgap too many
      // Work out the anchor paint
      if (anchor == TOP) {
        y = insets.top;
      }
      else if (anchor == CENTER) {
        y = (pd.height - y) / 2;
      }
      else {
        y = pd.height - y - insets.bottom;
      }
      // do layout
      for (int i = 0; i < n; i++) {
        Component c = parent.getComponent(i);
        if (c.isVisible()) {
          Dimension d = c.getPreferredSize();
          int x = insets.left;
          int wid = d.width;
          if (alignment == CENTER) {
            x = (pd.width - d.width) / 2;
          }
          else if (alignment == RIGHT) {
            x = pd.width - d.width - insets.right;
          }
          else if (alignment == BOTH) {
            wid = pd.width - insets.left - insets.right;
          }
          c.setBounds(x, y, wid, d.height);
          y += d.height + vgap;
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension minimumLayoutSize(Container parent) {
    return layoutSize(parent, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension preferredLayoutSize(Container parent) {
    return layoutSize(parent, false);
  }

  /**
   * Not used by this class
   */
  @Override
  public void addLayoutComponent(String name, Component comp) {
  }

  /**
   * Not used by this class
   */
  @Override
  public void removeLayoutComponent(Component comp) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return getClass().getName() + "[vgap=" + vgap + " align=" + alignment
      + " anchor=" + anchor + "]";
  }

  /**
   * Testador
   * 
   * @param args
   */
  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new BorderLayout());
    JPanel panel = new JPanel(new VerticalLayout());
    panel.add(new JButton("TESTANDO..............................."));
    panel.add(new JButton());
    panel.add(new JButton());
    panel.add(new JButton());
    panel.add(new JScrollPane(new JTextArea()));
    frame.add(new JScrollPane(panel), BorderLayout.CENTER);
    {
      JPanel pane = new JPanel(new GridBagLayout());
      pane.add(new JLabel("Testando"), new GBC(0, 0));
      pane.add(new JTextField(), new GBC(1, 0).horizontal());
      panel.add(pane);
    }
    frame.pack();
    frame.setVisible(true);
  }

}