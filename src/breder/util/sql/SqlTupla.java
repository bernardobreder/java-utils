package breder.util.sql;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Estrutura de tupla de banco de dados
 * 
 * @author Bernardo Breder
 */
public class SqlTupla extends ArrayList<Object> {

  /**
   * Indice da tupla
   */
  private int index;

  /**
   * Construtor
   */
  public SqlTupla() {
    super();
  }

  /**
   * Construtor
   * 
   * @param initialCapacity
   */
  public SqlTupla(int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * Retorna a data
   * 
   * @return data
   */
  public Date getDate() {
    Object object = this.get(index++);
    if (object == null) {
      return null;
    }
    if (object instanceof Long) {
      Long timer = (Long) object;
      object = new Date(timer);
    }
    if (object instanceof String) {
      Pattern pattern = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})");
      Matcher matcher = pattern.matcher(object.toString());
      if (matcher.find()) {
        int year = new Integer(matcher.group(1));
        int mouth = new Integer(matcher.group(2)) - 1;
        int day = new Integer(matcher.group(3));
        object = new GregorianCalendar(year, mouth, day).getTime();
      }
    }
    return (Date) object;
  }

  /**
   * Retorna uma String
   * 
   * @return string
   */
  public String getString() {
    Object object = this.get(index++);
    if (object instanceof byte[]) {
      return new String((byte[]) object);
    }
    return (String) object;
  }

  /**
   * Retorna uma String
   * 
   * @return string
   */
  public byte[] getStream() {
    Object object = this.get(index++);
    return (byte[]) object;
  }

  /**
   * Retorna uma String
   * 
   * @return string
   */
  public String readString() {
    String value = this.getString();
    if (value == null) {
      throw new IllegalArgumentException();
    }
    return value;
  }

  /**
   * Retorna um Inteiro
   * 
   * @return inteiro
   */
  public int getInt() {
    Object object = this.get(index++);
    return (Integer) object;
  }

  /**
   * Retorna um Inteiro
   * 
   * @return inteiro
   */
  public Integer getInteger() {
    Object object = this.get(index++);
    return (Integer) object;
  }

  /**
   * Retorna um Inteiro
   * 
   * @return inteiro
   */
  public Float getFloat() {
    Object object = this.get(index++);
    if (object == null) {
      return null;
    }
    return new Float(object.toString());
  }

  /**
   * Retorna um valor logico
   * 
   * @return valor logico
   */
  public Boolean getBoolean() {
    Object object = this.get(index++);
    if (object == null) {
      return null;
    }
    if (object instanceof Number) {
      return Integer.parseInt(object.toString()) > 0;
    }
    else {
      return (Boolean) object;
    }
  }

}
