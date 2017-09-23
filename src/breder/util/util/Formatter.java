package breder.util.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Classe com um conjunto de m�todos �teis para toda a aplicac�o tais como
 * formatac�o de n�meros e tratamento de datas.
 */
public class Formatter {

  /** Formata��o de data e hora */
  private static SimpleDateFormat dateHourFormat = null;

  /**
   * Cria um formatador com data e hora.
   * 
   * @return formatador com data e hora.
   */
  public static DateFormat getDateHourFormat() {
    return getDateFormat("dd/MM/yyyy HH:mm:ss");
  }

  /**
   * Retorna formatador
   * 
   * @param formatter
   * @return
   */
  public static DateFormat getDateFormat(String formatter) {
    if (dateHourFormat != null) {
      return dateHourFormat;
    }
    dateHourFormat = new SimpleDateFormat(formatter);
    dateHourFormat.setLenient(false);
    return dateHourFormat;
  }

}