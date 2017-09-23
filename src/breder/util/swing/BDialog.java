package breder.util.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import breder.util.task.IWindow;

public class BDialog extends JDialog implements IWindow {

  private boolean closed;

  public BDialog(BFrame frame) {
    super(frame);
  }

  public List<IWindow> getChildren() {
    return new ArrayList<IWindow>();
  }

  @Override
  public void close() {
    this.closed = true;
    this.dispose();
  }

  @Override
  public boolean isClosed() {
    return this.closed;
  }

}
