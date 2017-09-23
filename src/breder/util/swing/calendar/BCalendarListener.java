package breder.util.swing.calendar;

import java.util.Date;

/**
 * Interface de listener
 */
public interface BCalendarListener {

  /**
   * Evento de mudança de data
   * 
   * @param date
   */
  public void dateChanged(Date date);

}
