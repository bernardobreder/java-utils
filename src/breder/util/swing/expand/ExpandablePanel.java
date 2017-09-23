package breder.util.swing.expand;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import breder.util.lookandfeel.LookAndFeel;
import breder.util.resource.Resource;
import breder.util.swing.BImage;
import breder.util.swing.GBC;
import breder.util.swing.layout.VerticalLayout;

/**
 * Painel que expand algum conteudo
 * 
 * 
 * @author bbreder
 */
public class ExpandablePanel extends JPanel {

  /** Componente filho */
  private Component child;
  /** Imagem */
  private BImage image;
  /** Listener */
  private List<ExpandableListener> listeners =
    new ArrayList<ExpandableListener>();
  /** Painel */
  private JPanel panel;
  /** Imagem */
  private static final BufferedImage plusImage = Resource.getInstance()
    .getPlusImage();
  /** Imagem */
  private static final BufferedImage minusImage = Resource.getInstance()
    .getMinusImage();

  /**
   * Construtor
   * 
   * @param label
   * @param child
   */
  public ExpandablePanel(Component label, Component child) {
    this.child = child;
    this.setLayout(new GridBagLayout());
    this.image = new BImage(plusImage);
    image.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        fireExpand();
      }
    });
    this.panel = new JPanel(new BorderLayout());
    this.panel.add(child);
    this.add(image, new GBC(0, 0));
    this.add(label, new GBC(1, 0).horizontal());
    this.add(panel, new GBC(1, 1).both());
    this.panel.setVisible(false);
  }

  /**
   * Expande ou encolhe o componente
   */
  public void fireExpand() {
    boolean flag = this.image.getImage() == plusImage;
    if (flag) {
      this.image.setImage(minusImage);
    }
    else {
      this.image.setImage(plusImage);
    }
    this.image.repaint();
    this.panel.setVisible(flag);
    this.panel.validate();
    if (flag) {
      for (ExpandableListener listener : this.listeners) {
        listener.expanded(this);
      }
    }
    else {
      for (ExpandableListener listener : this.listeners) {
        listener.collapsed(this);
      }
    }
  }

  /**
   * Retorna o filho
   * 
   * @return child
   */
  public Component getChild() {
    return child;
  }

  /**
   * Altera o filho
   * 
   * @param child
   */
  public void setChild(Component child) {
    this.child = child;
    this.panel.removeAll();
    this.panel.add(child);
    this.panel.validate();
  }

  /**
   * Adiciona um listener
   * 
   * @param listener
   */
  public void addListener(ExpandableListener listener) {
    this.listeners.add(listener);
  }

  /**
   * Remove um listener
   * 
   * @param listener
   */
  public void removeListener(ExpandableListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * Remove os listeners
   */
  public void removeAllListeners() {
    this.listeners.clear();
  }

  /**
   * Testador
   * 
   * @param args
   */
  public static void main(String[] args) {
    LookAndFeel.getInstance().installSeaglass();
    JFrame frame = new JFrame();
    JPanel child = new JPanel(new VerticalLayout());
    child.add(new JLabel("Label1"));
    child.add(new JLabel("Label2"));
    child.add(new JLabel("Label3"));
    frame.setLayout(new VerticalLayout());
    ExpandablePanel panel =
      new ExpandablePanel(new JLabel("Clica aqui"), child);
    frame.add(panel);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(800, 600);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

}
