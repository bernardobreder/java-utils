package breder.util.swing;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BLabelTextField extends JPanel {

  private JTextField field;

  public BLabelTextField(String label) {
    this(label, "");
  }

  public BLabelTextField(String label, String content) {
    this.setLayout(new BorderLayout());
    this.add(new JLabel(label), BorderLayout.WEST);
    this.add(this.field = new JTextField(content), BorderLayout.CENTER);
  }

  public JTextField getField() {
    return field;
  }

}
