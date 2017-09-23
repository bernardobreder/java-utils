package breder.util.task;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;

public abstract class Task implements ITask {

  protected List<Runnable> finallys = new Vector<Runnable>();

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    this.start();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addFinally(Runnable runnable) {
    this.finallys.add(0, runnable);
  }

}
