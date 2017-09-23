package breder.util.swing;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import breder.util.resource.Resource;
import breder.util.task.IWindow;

public class BFrame extends JFrame implements IWindow {

  private final List<IWindow> children = new ArrayList<IWindow>();

  private final IWindow parent;

  private boolean closed;

  public BFrame(BFrame parent) {
    this.setIconImage(Resource.getInstance().getTrayIcon());
    {
      if (parent != null) {
        parent.children.add(this);
      }
      this.parent = parent;
    }
    this.confClose();
    if (parent != null) {
      BFrameUtil.confEsq(this);
    }
    else {
      this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
  }

  /**
   * Atribui um tamanho
   * 
   * @param d
   */
  public void setSize(double d) {
    int width = this.getWidth();
    int height = this.getHeight();
    this.setSize((int) (width * d), (int) (height * d));
  }

  private void confClose() {
    this.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        close();
      }

      @Override
      public void windowClosing(WindowEvent e) {
        close();
      }
    });
  }

  @Override
  public List<IWindow> getChildren() {
    return this.children;
  }

  @Override
  public void close() {
    if (!this.isClosed()) {
      this.closed = true;
      this.setVisible(false);
      for (IWindow frame : children) {
        frame.setVisible(false);
      }
      for (IWindow frame : children) {
        frame.dispose();
      }
      this.dispose();
    }
  }

  @Override
  public boolean isClosed() {
    return this.closed;
  }

  @Override
  public void setVisible(boolean b) {
    super.setVisible(b);
    this.closed = !b;
  }

  @Override
  public void setSize(int width, int height) {
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    super.setSize(Math.min(size.width - 100, width), Math.min(
      size.height - 100, height));
  }

}
