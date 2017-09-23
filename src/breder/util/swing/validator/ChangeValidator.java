package breder.util.swing.validator;

import breder.util.swing.Validator;

public class ChangeValidator implements Validator {

  private String old;

  public ChangeValidator(String old) {
    super();
    this.setOldValue(old);
  }

  @Override
  public boolean valid(String text) {
    return !old.equals(text);
  }

  public void setOldValue(String old) {
    this.old = old;
  }

}
