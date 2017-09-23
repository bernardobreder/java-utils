package breder.util.task;

import java.util.List;

import breder.util.swing.ICloseable;

public interface IWindow extends ICloseable {

  public void setVisible(boolean flag);

  public List<IWindow> getChildren();

  public void dispose();

  public boolean isClosed();

}
