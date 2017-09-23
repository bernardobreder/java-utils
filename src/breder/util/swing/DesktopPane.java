package breder.util.swing;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

public class DesktopPane extends JDesktopPane {

  public DesktopPane() {
    this.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
  }

  public void add(BInternalFrame frame) {
    super.add(frame);
    frame.add(frame.build());
    frame.pack();
    frame.setMinimumSize(frame.getPreferredSize());
    frame.setPreferredSize(new Dimension(800, Math.max(
      this.getPreferredSize().height, 600)));
    frame.setSize(frame.getPreferredSize());
    frame.setVisible(true);
  }

}
