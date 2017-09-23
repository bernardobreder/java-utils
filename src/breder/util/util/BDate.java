package breder.util.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BDate extends Date {

  private static String format = "yyyy/MM/dd hh:mm:ss";

  public BDate() {
    this(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(
      Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE), Calendar
      .getInstance().get(Calendar.HOUR), Calendar.getInstance().get(
      Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND));
  }

  public BDate(int year, int month, int day) {
    this(year, month, day, 0, 0, 0);
  }

  public BDate(int year, int month, int day, int hour, int minute, int second) {
    super(new GregorianCalendar(year, month, day, hour, minute, second)
      .getTime().getTime());
  }

  public BDate(Date date) {
    super(date.getTime());
  }

  public BDate(String text) throws ParseException {
    this(text, format);
  }

  public BDate(String text, String format) throws ParseException {
    super(new SimpleDateFormat(format).parse(text).getTime());
  }

  public static void setFormat(String pattern) {
    BDate.format = pattern;
  }

  @Override
  public String toString() {
    return this.toString(BDate.format);
  }

  public String toString(String pattern) {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(this);
    Format format = new SimpleDateFormat(pattern);
    return format.format(this);
  }

  public int getYear() {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(this);
    return calendar.get(Calendar.YEAR);
  }

  public int getMonth() {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(this);
    return calendar.get(Calendar.MONTH);
  }

  public int getDay() {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(this);
    return calendar.get(Calendar.DATE);
  }

  public int getHour() {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(this);
    return calendar.get(Calendar.HOUR);
  }

  public int getMinute() {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(this);
    return calendar.get(Calendar.MINUTE);
  }

  public int getSecond() {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(this);
    return calendar.get(Calendar.SECOND);
  }

  public BDate incYear(int amount) {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(this);
    calendar.add(Calendar.YEAR, amount);
    return new BDate(calendar.getTime());
  }

  public BDate incMonth(int amount) {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(this);
    calendar.add(Calendar.MONTH, amount);
    return new BDate(calendar.getTime());
  }

  public BDate incDay(int amount) {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(this);
    calendar.add(Calendar.DATE, amount);
    return new BDate(calendar.getTime());
  }

  public BDate incHour(int amount) {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(this);
    calendar.add(Calendar.HOUR, amount);
    return new BDate(calendar.getTime());
  }

  public BDate incMinute(int amount) {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(this);
    calendar.add(Calendar.MINUTE, amount);
    return new BDate(calendar.getTime());
  }

  public BDate incSecond(int amount) {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(this);
    calendar.add(Calendar.SECOND, amount);
    return new BDate(calendar.getTime());
  }

}
