package breder.util.task;

import breder.util.swing.BFrame;

public class FrameTask extends LocalTask {

  private final Class<? extends IWindow> clazz;

  private final BFrame parent;

  private final Object[] objects;

  public FrameTask(Class<? extends IWindow> clazz, BFrame frame,
    Object... objects) {
    super();
    this.clazz = clazz;
    this.parent = frame;
    this.objects = objects;
  }

  @Override
  public void updateUI() {
    Class[] classes = new Class[1 + this.objects.length];
    Object[] objects = new Object[1 + this.objects.length];
    classes[0] = IWindow.class;
    objects[0] = parent;
    for (int n = 0; n < this.objects.length; n++) {
      classes[n + 1] = this.objects[n].getClass();
      objects[n + 1] = this.objects[n];
    }
    try {
      clazz.getConstructor(classes).newInstance(objects).setVisible(true);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}
