package breder.util.trayicon;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import breder.util.resource.Resource;
import breder.util.swing.BFrame;

/**
 * Utilitário para TrayIcon
 * 
 * 
 * @author Bernardo Breder
 */
public class BTrayIcon {

  /**
   * Constroi o trayicon
   * 
   * @param tray
   */
  public static void build(final ITrayIcon tray) {
    if (SystemTray.isSupported()) {
      Image icon = tray.getIcon();
      if (icon == null) {
        icon = Resource.getInstance().getTrayIcon();
      }
      PopupMenu popup = new PopupMenu();
      {
        MenuItem[] aux = tray.buildMenu();
        if (aux == null) {
          aux = new MenuItem[0];
        }
        {
          MenuItem item = new MenuItem("Show");
          item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              tray.getMainFrame().setVisible(false);
              tray.getMainFrame().setVisible(true);
              tray.getMainFrame().requestFocus();
            }
          });
          popup.add(item);
        }
        popup.addSeparator();
        for (int n = 0; n < aux.length; n++) {
          popup.add(aux[n]);
        }
        if (aux.length > 0) {
          popup.addSeparator();
        }
        {
          MenuItem item = new MenuItem("Quit");
          item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              JFrame frame = tray.getMainFrame();
              if (frame instanceof BFrame) {
                ((BFrame) frame).close();
              }
              else {
                frame.dispose();
              }
              System.exit(0);
            }
          });
          popup.add(item);
        }
      }
      TrayIcon trayIcon = new TrayIcon(icon, tray.getName(), popup);
      try {
        SystemTray.getSystemTray().add(trayIcon);
      }
      catch (AWTException e) {
      }
    }
  }

  /**
   * Constroi o trayicon
   * 
   * @param menu
   * @param icon
   * @param title
   * @param listener
   * @param frame
   */
  public static void build(final MenuItem[] menu, final Image icon,
    final String title, final ActionListener listener, final BFrame frame) {
    build(new ITrayIcon() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (listener != null) {
          listener.actionPerformed(e);
        }
      }

      @Override
      public String getName() {
        return title;
      }

      @Override
      public Image getIcon() {
        if (icon == null) {
          return Resource.getInstance().getTrayIcon();
        }
        else {
          return icon;
        }
      }

      @Override
      public JFrame getMainFrame() {
        return frame;
      }

      @Override
      public MenuItem[] buildMenu() {
        return menu;
      }

    });
  }

}
