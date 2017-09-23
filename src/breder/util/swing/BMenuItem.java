package breder.util.swing;

import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import breder.util.so.SoUtil;

public class BMenuItem extends JMenuItem {

  private BMenuItemValidator<BMenuItem> validator;

  public BMenuItem(Action action, KeyStroke accelerator) {
    super(action);
    this.setAccelerator(accelerator);
  }

  public BMenuItem(Icon icon, String text, ActionListener action) {
    super();
    this.addActionListener(action);
    this.setText(text);
    this.setIcon(icon);
  }

  public BMenuItem(Action action) {
    this(action, null);
  }

  public BMenuItem(String name, ActionListener action) {
    this(name, action, (KeyStroke) null);
  }

  public BMenuItem(String name, ActionListener action, KeyStroke accelerator) {
    this(null, name, action, accelerator, null);
  }

  public BMenuItem(String name, ActionListener action, String accelerator) {
    this(null, name, action, SoUtil.getKeyStroke(accelerator), null);
  }

  public BMenuItem(Icon icon, String name, ActionListener action,
    String accelerator) {
    this(icon, name, action, SoUtil.getKeyStroke(accelerator), null);
  }

  public BMenuItem(Icon icon, String name, ActionListener action,
    KeyStroke accelerator, BMenuItemValidator<BMenuItem> validator) {
    super(name, icon);
    this.setAccelerator(accelerator);
    this.addActionListener(action);
    this.validator = validator;
  }

  @Override
  public boolean isEnabled() {
    if (validator == null) {
      return true;
    }
    return validator.isValid(this);
  }

}
