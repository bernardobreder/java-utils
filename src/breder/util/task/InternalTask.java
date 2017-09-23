package breder.util.task;

import breder.util.swing.BInternalFrame;
import breder.util.swing.DesktopPane;

public abstract class InternalTask extends LocalTask {

  private final Class<? extends BInternalFrame> clazz;

  private final BInternalFrame parent;

  private final Object[] objects;

  public InternalTask(Class<? extends BInternalFrame> clazz,
    BInternalFrame parent, Object... objects) {
    super();
    this.clazz = clazz;
    this.parent = parent;
    this.objects = objects;
  }

  public abstract DesktopPane getDesktop();

  @Override
  public void updateUI() {
    Class[] classes = new Class[1 + this.objects.length];
    Object[] objects = new Object[1 + this.objects.length];
    classes[0] = BInternalFrame.class;
    objects[0] = parent;
    for (int n = 0; n < this.objects.length; n++) {
      classes[n + 1] = this.objects[n].getClass();
      objects[n + 1] = this.objects[n];
    }
    try {
      clazz.getConstructor(classes).newInstance(objects).setVisible(true);
      BInternalFrame win = clazz.getConstructor(classes).newInstance(objects);
      win.setVisible(true);
      this.getDesktop().add(win);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
