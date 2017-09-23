package breder.util.swing;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

public class BFrameUtil {

  public static <E extends Component> E getParent(Component component,
    Class<E> clazz) {
    for (Component p = component; p != null; p = p.getParent()) {
      if (clazz.isInstance(p)) {
        return (E) p;
      }
    }
    return null;
  }

  public static void confEsq(final RootPaneContainer c) {
    c.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
    c.getRootPane().getActionMap().put("Cancel", new AbstractAction() { //$NON-NLS-1$

        public void actionPerformed(ActionEvent e) {
          if (c instanceof ICloseable) {
            ((ICloseable) c).close();
          }
        }
      });
  }

  public static void showAsModal(final JFrame frame, final Component owner) {
    showAsModal(frame, (JFrame) SwingUtilities.getRoot(owner));
  }

  public static void showAsModal(final JFrame frame, final JFrame owner) {
    frame.addWindowListener(new WindowAdapter() {

      public void windowOpened(WindowEvent e) {
        owner.setEnabled(false);
      }

      public void windowClosing(WindowEvent e) {
        owner.setEnabled(true);
        frame.removeWindowListener(this);
      }

      public void windowClosed(WindowEvent e) {
        owner.setEnabled(true);
        frame.removeWindowListener(this);
      }
    });

    owner.addWindowListener(new WindowAdapter() {

      public void windowActivated(WindowEvent e) {
        if (frame.isShowing()) {
          frame.toFront();
        }
        else {
          owner.removeWindowListener(this);
        }
      }
    });

    try {
      new EventPump(frame).start();
    }
    catch (Throwable throwable) {
      throw new RuntimeException(throwable);
    }
  }

  static class EventPump implements InvocationHandler {

    Frame frame;

    public EventPump(Frame frame) {
      this.frame = frame;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {
      return frame.isShowing();
    }

    public void start() throws Exception {
      Class<?> clazz = Class.forName("java.awt.Conditional");
      Object conditional =
        Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz },
          this);
      Method pumpMethod =
        Class.forName("java.awt.EventDispatchThread").getDeclaredMethod(
          "pumpEvents", new Class[] { clazz });
      pumpMethod.setAccessible(true);
      pumpMethod.invoke(Thread.currentThread(), new Object[] { conditional });
    }
  }

}
