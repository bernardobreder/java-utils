package breder.util.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Classe de Data implementada atravás de operações aritméticas.
 * 
 * 
 * @author Tecgraf
 */
public class Timer implements Comparable<Timer>, Serializable, Cloneable {

  /** Unidade em Segundos */
  protected static final long SECOND_UNIT = 1000;
  /** Unidade em Minutos */
  protected static final long MINUTE_UNIT = 60 * SECOND_UNIT;
  /** Unidade em Hora */
  protected static final long HOUR_UNIT = 60 * MINUTE_UNIT;
  /** Unidade em Dia */
  protected static final long DAY_UNIT = 24 * HOUR_UNIT;
  /** Unidade em Mês */
  protected static final long MONTH_UNIT = 31 * DAY_UNIT;
  /** Unidade em Ano */
  protected static final long YEAR_UNIT = 12 * MONTH_UNIT;
  /** Timer */
  protected long timer;

  /**
   * Construtor
   * 
   * @param year ano iniciando por 0
   * @param month mes entre 1 e 12
   * @param day dia entre 1 e 31
   * @param hour hora entre 0 e 23
   * @param minute minuto entre 0 e 59
   * @param second segundo entre 0 e 59
   * @param milisecond milisegundo entre 0 e 999
   */
  public Timer(int year, int month, int day, int hour, int minute, int second,
    int milisecond) {
    if (year < 0 || year > 9999) {
      throw new IllegalArgumentException(
        "wrong year value, must be between 0 and 9999");
    }
    if (month < 1 || month > 12) {
      throw new IllegalArgumentException(
        "wrong month value, must be between 1 and 12");
    }
    if (day < 1 || day > 31) {
      throw new IllegalArgumentException(
        "wrong day value, must be between 1 and 31");
    }
    if (hour < 0 || hour > 23) {
      throw new IllegalArgumentException(
        "wrong hour value, must be between 0 and 59");
    }
    if (minute < 0 || minute > 59) {
      throw new IllegalArgumentException(
        "wrong minute value, must be between 0 and 59");
    }
    if (second < 0 || second > 59) {
      throw new IllegalArgumentException(
        "wrong second value, must be between 0 and 59");
    }
    if (milisecond < 0 || milisecond > 999) {
      throw new IllegalArgumentException(
        "wrong milisecond value, must be between 0 and 999");
    }
    this.timer = getTimer(year, month, day, hour, minute, second, milisecond);
  }

  /**
   * Construtor
   * 
   * @param year ano iniciando por 0
   * @param month mes entre 1 e 12
   * @param day dia entre 1 e 31
   * @param hour hora entre 0 e 23
   * @param minute minuto entre 0 e 59
   * @param second segundo entre 0 e 59
   */
  public Timer(int year, int month, int day, int hour, int minute, int second) {
    this(year, month, day, hour, minute, second, 0);
  }

  /**
   * Construtor
   * 
   * @param year ano iniciando por 0
   * @param month mes entre 1 e 12
   * @param day dia entre 1 e 31
   */
  public Timer(int year, int month, int day) {
    this(year, month, day, 0, 0, 0, 0);
  }

  /**
   * Construtor
   * 
   * @param date
   */
  public Timer(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    this.timer =
      this.getTimer(calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
        calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
  }

  /**
   * Construtor
   * 
   * @param calendar
   */
  public Timer(Calendar calendar) {
    this.timer =
      this.getTimer(calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
        calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
  }

  /**
   * Construtor de agora
   */
  public Timer() {
    this(new Date());
  }

  /**
   * Construtor interno
   * 
   * @param timer
   */
  protected Timer(long timer) {
    this.timer = timer;
  }

  /**
   * Recupera o long baseado no tempo
   * 
   * @param year
   * @param month
   * @param day
   * @param hour
   * @param minute
   * @param second
   * @param milisecond
   * @return long do tempo
   */
  protected long getTimer(int year, int month, int day, int hour, int minute,
    int second, int milisecond) {
    return milisecond
      + (SECOND_UNIT * second + (MINUTE_UNIT * minute + (HOUR_UNIT * hour + (DAY_UNIT
        * (day - 1) + (MONTH_UNIT * (month - 1) + (YEAR_UNIT * year))))));
  }

  /**
   * Retorna o ano
   * 
   * @return ano
   */
  public int getYear() {
    return (int) (timer / YEAR_UNIT);
  }

  /**
   * Retorna o mês
   * 
   * @return mês
   */
  public int getMonth() {
    long timer = this.timer;
    timer -= YEAR_UNIT * (int) (this.timer / YEAR_UNIT);
    return (int) (timer / MONTH_UNIT) + 1;
  }

  /**
   * Retorna o dia
   * 
   * @return dia
   */
  public int getDay() {
    long timer = this.timer;
    timer -= YEAR_UNIT * (int) (timer / YEAR_UNIT);
    timer -= MONTH_UNIT * (int) (timer / MONTH_UNIT);
    return (int) (timer / DAY_UNIT) + 1;
  }

  /**
   * Retorna a hora
   * 
   * @return hora
   */
  public int getHour() {
    long timer = this.timer;
    timer -= YEAR_UNIT * (int) (timer / YEAR_UNIT);
    timer -= MONTH_UNIT * (int) (timer / MONTH_UNIT);
    timer -= DAY_UNIT * (int) (timer / DAY_UNIT);
    return (int) (timer / HOUR_UNIT);
  }

  /**
   * Retorna o minuto
   * 
   * @return minuto
   */
  public int getMinute() {
    long timer = this.timer;
    timer -= YEAR_UNIT * (int) (timer / YEAR_UNIT);
    timer -= MONTH_UNIT * (int) (timer / MONTH_UNIT);
    timer -= DAY_UNIT * (int) (timer / DAY_UNIT);
    timer -= HOUR_UNIT * (int) (timer / HOUR_UNIT);
    return (int) (timer / MINUTE_UNIT);
  }

  /**
   * Retorna o segundo
   * 
   * @return segundo
   */
  public int getSecond() {
    long timer = this.timer;
    timer -= YEAR_UNIT * (int) (timer / YEAR_UNIT);
    timer -= MONTH_UNIT * (int) (timer / MONTH_UNIT);
    timer -= DAY_UNIT * (int) (timer / DAY_UNIT);
    timer -= HOUR_UNIT * (int) (timer / HOUR_UNIT);
    timer -= MINUTE_UNIT * (int) (timer / MINUTE_UNIT);
    return (int) (timer / (SECOND_UNIT));
  }

  /**
   * Retorna o milisegundo
   * 
   * @return milisegundo
   */
  public int getMiliSecond() {
    long timer = this.timer;
    timer -= YEAR_UNIT * (int) (timer / YEAR_UNIT);
    timer -= MONTH_UNIT * (int) (timer / MONTH_UNIT);
    timer -= DAY_UNIT * (int) (timer / DAY_UNIT);
    timer -= HOUR_UNIT * (int) (timer / HOUR_UNIT);
    timer -= MINUTE_UNIT * (int) (timer / MINUTE_UNIT);
    timer -= SECOND_UNIT * (int) (timer / SECOND_UNIT);
    return (int) timer;
  }

  /**
   * Retorna uma nova data com os anos incrementados. Se o valor do count for
   * negativo, será decrementado.
   * 
   * @param count
   * @return nova data
   */
  public Timer addYear(int count) {
    Calendar calendar = this.toCalendar();
    calendar.add(Calendar.YEAR, count);
    return new Timer(calendar.get(Calendar.YEAR),
      calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
      calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
      calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
  }

  /**
   * Retorna uma nova data com os meses incrementados. Se o valor do count for
   * negativo, será decrementado.
   * 
   * @param count
   * @return nova data
   */
  public Timer addMonth(int count) {
    Calendar calendar = this.toCalendar();
    calendar.add(Calendar.MONTH, count);
    return new Timer(calendar.get(Calendar.YEAR),
      calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
      calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
      calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
  }

  /**
   * Retorna uma nova data com os dias incrementados. Se o valor do count for
   * negativo, será decrementado.
   * 
   * @param count
   * @return nova data
   */
  public Timer addDay(int count) {
    Calendar calendar = this.toCalendar();
    calendar.add(Calendar.DAY_OF_MONTH, count);
    return new Timer(calendar.get(Calendar.YEAR),
      calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
      calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
      calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
  }

  /**
   * Retorna uma nova data com os horas incrementados. Se o valor do count for
   * negativo, será decrementado.
   * 
   * @param count
   * @return nova data
   */
  public Timer addHour(int count) {
    Calendar calendar = this.toCalendar();
    calendar.add(Calendar.HOUR_OF_DAY, count);
    return new Timer(calendar.get(Calendar.YEAR),
      calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
      calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
      calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
  }

  /**
   * Retorna uma nova data com os minutos incrementados. Se o valor do count for
   * negativo, será decrementado.
   * 
   * @param count
   * @return nova data
   */
  public Timer addMinute(int count) {
    Calendar calendar = this.toCalendar();
    calendar.add(Calendar.MINUTE, count);
    return new Timer(calendar.get(Calendar.YEAR),
      calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
      calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
      calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
  }

  /**
   * Retorna uma nova data com os segundos incrementados. Se o valor do count
   * for negativo, será decrementado.
   * 
   * @param count
   * @return nova data
   */
  public Timer addSecond(int count) {
    Calendar calendar = this.toCalendar();
    calendar.add(Calendar.SECOND, count);
    return new Timer(calendar.get(Calendar.YEAR),
      calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
      calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
      calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
  }

  /**
   * Retorna uma nova data com os milisegundos incrementados. Se o valor do
   * count for negativo, será decrementado.
   * 
   * @param count
   * @return nova data
   */
  public Timer addMiliSecond(int count) {
    Calendar calendar = this.toCalendar();
    calendar.add(Calendar.MILLISECOND, count);
    return new Timer(calendar.get(Calendar.YEAR),
      calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
      calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
      calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
  }

  /**
   * Retorna o timer
   * 
   * @return timer
   */
  public long getTimer() {
    return this.timer;
  }

  /**
   * Realiza a escrita do objeto
   * 
   * @param s
   * @throws IOException
   */
  private void writeObject(ObjectOutputStream s) throws IOException {
    s.writeLong(this.timer);
  }

  /**
   * Realiza a leitura do objeto
   * 
   * @param s
   * @throws IOException
   * @throws ClassNotFoundException
   */
  private void readObject(ObjectInputStream s) throws IOException,
    ClassNotFoundException {
    this.timer = s.readLong();
  }

  /**
   * Cria um calendário com as configurações do objeto
   * 
   * @return calendário configurado
   */
  public Calendar toCalendar() {
    long timer = this.timer;
    int year = (int) (timer / YEAR_UNIT);
    timer -= YEAR_UNIT * year;
    int month = (int) (timer / MONTH_UNIT);
    timer -= MONTH_UNIT * month;
    int day = (int) (timer / DAY_UNIT);
    timer -= DAY_UNIT * day;
    int hour = (int) (timer / HOUR_UNIT);
    timer -= HOUR_UNIT * hour;
    int minute = (int) (timer / MINUTE_UNIT);
    timer -= MINUTE_UNIT * minute;
    int second = (int) (timer / SECOND_UNIT);
    timer -= SECOND_UNIT * second;
    int milisecond = (int) timer;
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day + 1, hour, minute, second);
    calendar.set(Calendar.MILLISECOND, milisecond);
    return calendar;
  }

  /**
   * Retorna o timer
   * 
   * @return timer
   */
  public Date toDate() {
    return this.toCalendar().getTime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return (int) (timer ^ (timer >>> 32));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (obj instanceof Timer == false) {
      return false;
    }
    Timer other = (Timer) obj;
    if (timer != other.timer) {
      return false;
    }
    return true;
  }

  /**
   * Comparador entre mesmo tipo
   * 
   * @param other
   * @return indica se são iguais
   */
  public boolean equals(Timer other) {
    if (other == null) {
      return false;
    }
    return this.timer == other.timer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(Timer o) {
    if (this.timer == o.timer) {
      return 0;
    }
    else if (this.timer < o.timer) {
      return -1;
    }
    else {
      return 1;
    }
  }

  /**
   * Indica se um tempo contem o outro
   * 
   * @param other
   * @return tempo contem o outro
   */
  public boolean contain(Timer other) {
    if (other == null) {
      return false;
    }
    return this.timer == other.timer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    int miliseg = this.getMiliSecond();
    int second = this.getSecond();
    int minute = this.getMinute();
    int hour = this.getHour();
    int day = this.getDay();
    int month = this.getMonth();
    int year = this.getYear();
    StringBuilder sb = new StringBuilder();
    if (day < 10) {
      sb.append('0');
    }
    sb.append(day);
    sb.append('/');
    if (month < 10) {
      sb.append('0');
    }
    sb.append(month);
    sb.append('/');
    if (year < 1000) {
      sb.append('0');
    }
    if (year < 100) {
      sb.append('0');
    }
    if (year < 10) {
      sb.append('0');
    }
    sb.append(year);
    if (hour != 0 || minute != 0 || second != 0 || miliseg != 0) {
      sb.append(' ');
      if (hour < 10) {
        sb.append('0');
      }
      sb.append(hour);
      sb.append(':');
      if (minute < 10) {
        sb.append('0');
      }
      sb.append(minute);
      sb.append(':');
      if (second < 10) {
        sb.append('0');
      }
      sb.append(second);
      if (miliseg != 0) {
        sb.append(':');
        if (miliseg < 100) {
          sb.append('0');
        }
        if (miliseg < 10) {
          sb.append('0');
        }
        sb.append(miliseg);
      }
    }
    return sb.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    return new Timer(this.timer);
  }

}
