package breder.util.swing;

import java.awt.BorderLayout;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BLabelComboBox extends JPanel {

  private JComboBox field;

  public BLabelComboBox(String label, ComboBoxModel model) {
    this.setLayout(new BorderLayout());
    this.add(new JLabel(label), BorderLayout.WEST);
    this.add(this.field = new JComboBox(model), BorderLayout.CENTER);
  }

  public JComboBox getField() {
    return field;
  }

}
