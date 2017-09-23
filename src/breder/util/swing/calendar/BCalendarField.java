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
public class BCalendarField extends BComponentPopup {

  /** Formatador */
  private static DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

  /**
   * Construtor
   * 
   * @param date
   */
  public BCalendarField(Date date) {
    super(new BCalendar(date));
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
  public BCalendar getComponent() {
    return (BCalendar) super.getComponent();
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
    LookAndFeel.getInstance().installRandom();
    JFrame frame = new JFrame();
    frame.add(new BCalendarField(null));
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(800, 600);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

}
