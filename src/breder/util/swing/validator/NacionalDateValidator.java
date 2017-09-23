package breder.util.swing.validator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NacionalDateValidator extends DateValidator {

  public NacionalDateValidator(String formatter) {
    super("dd/MM/yyyy");
  }

  @Override
  public boolean valid(String text) {
    boolean flag = super.valid(text);
    if (!flag)
      return flag;
    SimpleDateFormat format = new SimpleDateFormat(this.getFormatter());
    try {
      Date date = format.parse(text);
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      int day = c.get(Calendar.DAY_OF_MONTH);
      String dayStr = text.substring(0, 2);
      if (!new Integer(day).toString().equals(new Integer(dayStr).toString())) {
        return false;
      }
      int month = c.get(Calendar.MONTH) + 1;
      String monthStr = text.substring(3, 5);
      if (!new Integer(month).toString().equals(
        new Integer(monthStr).toString())) {
        return false;
      }
      int year = c.get(Calendar.YEAR);
      String yearStr = text.substring(6, 10);
      if (!new Integer(year).toString().equals(new Integer(yearStr).toString())) {
        return false;
      }
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }
}
