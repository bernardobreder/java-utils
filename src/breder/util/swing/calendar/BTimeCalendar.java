package breder.util.swing.calendar;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import breder.util.lookandfeel.LookAndFeel;
import breder.util.swing.GBC;
import breder.util.swing.layout.VerticalLayout;
import breder.util.swing.model.StaticObjectModel;
import breder.util.swing.table.BTable;
import breder.util.swing.table.IOpenCellListener;

/**
 * Calendário com tempo
 * 
 * 
 * @author Bernardo Breder
 */
public class BTimeCalendar extends JPanel {

  /** Calendário */
  private BCalendar calendar;
  /** Horário minimo */
  private int minimumHour = 6;
  /** Horário minimo */
  private int maximumHour = 22;
  /** Tabela */
  private BTable<Object> table;
  /** Listener */
  private List<BCalendarListener> listeners =
    new ArrayList<BCalendarListener>();

  /**
   * Construtor
   * 
   * @param date
   */
  public BTimeCalendar(Date date) {
    this.setLayout(new GridBagLayout());
    this.add(this.calendar = new BCalendar(date), new GBC(0, 0).horizontal());
    this.add(this.buildTimeTable(), new GBC(0, 1).both());
  }

  /**
   * @return data
   * @see breder.util.swing.calendar.BCalendar#getSelectedDate()
   */
  public Date getSelectedDate() {
    return calendar.getSelectedDate();
  }

  /**
   * Invoca os eventos
   */
  public void fireChangedDate() {
    Date date = this.getCalendar().getSelectedDate();
    Calendar c = new GregorianCalendar();
    c.setTime(date);
    int index = table.getTable().getSelectedRow();
    int hour = this.getMinimumHour() + index / 2;
    int min = 0;
    if (index / 2 != ((float) index) / 2) {
      min = 30;
    }
    c.set(Calendar.HOUR_OF_DAY, hour);
    c.set(Calendar.MINUTE, min);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    this.calendar.setSelectedCalendar(c.getTime());
    for (BCalendarListener listener : this.listeners) {
      listener.dateChanged(c.getTime());
    }
  }

  /**
   * @param e
   * @see java.util.List#add(java.lang.Object)
   */
  public void add(BCalendarListener e) {
    listeners.add(e);
    this.table.addOpenCellListener(new IOpenCellListener<Object>() {
      @Override
      public void actionPerformed(int row, Object cell) {
        fireChangedDate();
      }

      @Override
      public JPopupMenu getPopupMenu(int row, Object cell) {
        return null;
      }
    });
  }

  /**
   * @param o
   * @see java.util.List#remove(java.lang.Object)
   */
  public void remove(Object o) {
    listeners.remove(o);
  }

  /**
   * Retorna
   * 
   * @return minimumHour
   */
  public int getMinimumHour() {
    return minimumHour;
  }

  /**
   * @param minimumHour
   */
  public void setMinimumHour(int minimumHour) {
    this.minimumHour = minimumHour;
  }

  /**
   * Retorna
   * 
   * @return maximumHour
   */
  public int getMaximumHour() {
    return maximumHour;
  }

  /**
   * @param maximumHour
   */
  public void setMaximumHour(int maximumHour) {
    this.maximumHour = maximumHour;
  }

  /**
   * Retorna
   * 
   * @return calendar
   */
  public BCalendar getCalendar() {
    return calendar;
  }

  /**
   * Constroi a tabela de tempo
   * 
   * @return tabela
   */
  private Component buildTimeTable() {
    this.table = new BTable<Object>(new MyModel());
    table.setRowHeaderView(this.buildTimePanel());
    table.setBorder(BorderFactory.createEmptyBorder());
    table.setPreferredSize(calendar.getPreferredSize());
    return table;
  }

  /**
   * Constroi o painel de tempo
   * 
   * @return painel
   */
  private Component buildTimePanel() {
    JPanel panel =
      new JPanel(new VerticalLayout(0, VerticalLayout.BOTH, VerticalLayout.TOP));
    int h = table.getTable().getRowHeight();
    for (int n = 0; n < 34; n++) {
      JLabel label = new JLabel();
      if (n / 2 == ((float) n) / 2) {
        label.setText("" + (getMinimumHour() + n / 2) + ":00");
      }
      else {
        label.setText("" + (getMinimumHour() + n / 2) + ":30");
      }
      if (label.getText().length() == 4) {
        label.setText("0" + label.getText());
      }
      label.setPreferredSize(new Dimension(label.getPreferredSize().width, h));
      panel.add(label);
    }
    return panel;
  }

  /**
   * Modelo da tabela
   * 
   * 
   * @author Bernardo Breder
   */
  private class MyModel extends StaticObjectModel<Object> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getRow(int index) {
      return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
      return (getMaximumHour() - getMinimumHour() + 1) * 2;
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
    final JFrame frame = new JFrame();
    BTimeCalendar c = new BTimeCalendar(null);
    c.add(new BCalendarListener() {
      @Override
      public void dateChanged(Date date) {
        JOptionPane.showMessageDialog(frame, new SimpleDateFormat(
          "dd/MM/yyyy hh:mm:ss").format(date));
      }
    });
    frame.add(c);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(800, 600);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

}
