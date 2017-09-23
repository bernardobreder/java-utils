package breder.util.swing.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import breder.util.swing.Validator;

public class DateValidator implements Validator {

  private final String formatter;

  public DateValidator(String formatter) {
    super();
    this.formatter = formatter;
  }

  @Override
  public boolean valid(String text) {
    if (text.length() != formatter.length())
      return false;
    SimpleDateFormat format = new SimpleDateFormat(formatter);
    try {
      format.parse(text);
      return true;
    }
    catch (ParseException e) {
      return false;
    }
  }

  public String getFormatter() {
    return formatter;
  }

}
