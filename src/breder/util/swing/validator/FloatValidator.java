package breder.util.swing.validator;

import breder.util.swing.Validator;

public class FloatValidator implements Validator {

  @Override
  public boolean valid(String text) {
    if (text.length() == 0)
      return false;
    try {
      new Float(text);
    }
    catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

}
