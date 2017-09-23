package breder.util.swing.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JFrame;

import breder.util.lookandfeel.LookAndFeel;
import breder.util.swing.BComponentPopup;

/**
 * Campo de calendário
 * 
 * 
 * @author Bernardo Breder
 */
public class BTimeCalendarField extends BComponentPopup {

  /** Formatador */
  private static DateFormat format = new SimpleDateFormat("dd/MM/yyyy kk:mm");

  /**
   * Construtor
   * 
   * @param date
   */
  public BTimeCalendarField(Date date) {
    super(new BTimeCalendar(date));
    this.getComponent().add(new BCalendarListener() {
      @Override
      public void dateChanged(Date date) {
        refresh();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BTimeCalendar getComponent() {
    return (BTimeCalendar) super.getComponent();
  }

  /**
   * Atualiza a interface
   */
  @Override
  public void refresh() {
    this.getField().setText(
      format.format(this.getComponent().getSelectedDate()));
    if (this.getDialog() != null) {
      this.getDialog().dispose();
    }
  }

  /**
   * Inicializador
   * 
   * @param args
   */
  public static void main(String[] args) {
    Locale.setDefault(new Locale("pt"));
    LookAndFeel.getInstance().installNimbus();
    JFrame frame = new JFrame();
    frame.add(new BTimeCalendarField(null));
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(800, 600);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

}
