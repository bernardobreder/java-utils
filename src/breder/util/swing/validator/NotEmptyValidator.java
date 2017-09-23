package breder.util.swing.validator;

import breder.util.swing.Validator;

public class NotEmptyValidator implements Validator {

  @Override
  public boolean valid(String text) {
    return text.trim().length() != 0;
  }

}
