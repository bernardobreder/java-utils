package breder.util.swing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import breder.util.swing.mask.DateMask;
import breder.util.swing.validator.NacionalDateValidator;
import breder.util.util.BDate;

public class BDateTextField extends BTextField {

  public static final String pattern = "dd/MM/yyyy";

  public BDateTextField() {
    this(null);
  }

  public BDateTextField(Date date) {
    super("", true);
    this.addValidator(new NacionalDateValidator(pattern));
    this.setMask(new DateMask());
    if (date != null) {
      this.setText(new SimpleDateFormat(pattern).format(date));
    }
  }

  public BDate getDate() {
    try {
      return new BDate(this.getText(), pattern);
    }
    catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }

}
