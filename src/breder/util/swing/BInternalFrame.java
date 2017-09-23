package breder.util.swing;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import breder.util.lookandfeel.LookAndFeel;
import breder.util.task.IWindow;

public abstract class BInternalFrame extends JInternalFrame implements IWindow {

  private final List<IWindow> childrens = new ArrayList<IWindow>();

  public BInternalFrame(IWindow parent) {
    if (parent != null) {
      parent.getChildren().add(this);
    }
    this.setClosable(true);
    this.setMaximizable(true);
    this.setIconifiable(true);
    this.setResizable(true);
    this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
    BFrameUtil.confEsq(this);
  }

  protected abstract Component build();

  public void close() {
    for (IWindow child : this.childrens) {
      child.close();
    }
    super.dispose();
  }

  @Override
  public List<IWindow> getChildren() {
    return childrens;
  }

  @Override
  public void dispose() {
    this.close();
  }

  public static void main(BInternalFrame iframe) {
    LookAndFeel.getInstance().installNimbus();
    BFrame frame = new BFrame(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    DesktopPane pane = new DesktopPane();
    pane.add(iframe);
    frame.add(pane);
    frame.setSize(iframe.getPreferredSize());
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

}
