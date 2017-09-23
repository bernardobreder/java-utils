package breder.util.swing;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class BPanel extends JPanel {

  public BPanel(String title) {
    this.setLayout(new BorderLayout());
    if (title != null) {
      this.setBorder(new TitledBorder(title));
    }
  }

  public BPanel addComponent(Component c, Object index) {
    this.add(c, index);
    return this;
  }

}
