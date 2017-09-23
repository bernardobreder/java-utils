package breder.util.swing.calendar;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import breder.util.lookandfeel.LookAndFeel;
import breder.util.swing.GBC;
import breder.util.util.DateUtil;

/**
 * Painel de calendario Deprecated porque ainda não foi terminado
 * 
 * @author bbreder.
 */
public class BCalendar extends JPanel {

  /** Modelo de dia */
  private SpinnerNumberModel dayModel;
  /** Modelo no mes */
  private DefaultComboBoxModel monthModel;
  /** Modelo de ano */
  private SpinnerNumberModel yearModel;
  /** Lista de componentes */
  private List<List<JToggleButton>> components =
    new ArrayList<List<JToggleButton>>();
  /** Calendario */
  private Calendar calendarSelected = Calendar.getInstance();
  /** Calendario */
  private Calendar calendarViewer = Calendar.getInstance();
  /** Listener */
  private List<BCalendarListener> listeners =
    new ArrayList<BCalendarListener>();

  /**
   * Construtor
   * 
   * @param date
   */
  public BCalendar(Date date) {
    if (date != null) {
      this.calendarSelected.setTime(date);
    }
    this.calendarViewer.setTime(this.calendarSelected.getTime());
    this.setLayout(new GridBagLayout());
    this.add(this.buildDayText(), new GBC(0, 0));
    this.add(this.buildMouthCombo(), new GBC(1, 0).horizontal());
    this.add(this.buildYearCombo(), new GBC(2, 0));
    this.add(this.buildWeekPanel(), new GBC(0, 1).horizontal().gridwh(3, 1));
    this.add(this.buildCalendarPanel(), new GBC(0, 2).both().gridwh(3, 1));
    this.fireRefresh();
  }

  /**
   * Invoca os eventos
   */
  public void fireChangedDate() {
    for (BCalendarListener listener : this.listeners) {
      listener.dateChanged(this.getSelectedDate());
    }
  }

  /**
   * Atualiza os componentes
   */
  public void fireRefresh() {
    Calendar c = this.calendarViewer;
    Calendar firstDay = new GregorianCalendar();
    firstDay.setTime(DateUtil.getFirstDayOfMonth(c.getTime()));
    int index = firstDay.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
    int daySelected = this.getSelectedCalendar().get(Calendar.DAY_OF_MONTH);
    int monthSelected = this.getSelectedCalendar().get(Calendar.MONTH);
    int yearSelected = this.getSelectedCalendar().get(Calendar.YEAR);
    int monthView = this.calendarViewer.get(Calendar.MONTH);
    int yearView = this.calendarViewer.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int daysInMonth = DateUtil.getDaysInMonth(month);
    int count = 1;
    for (int y = 0; y < 6; y++) {
      for (int x = 0; x < 7; x++) {
        JToggleButton button = this.components.get(y).get(x);
        button.setText("");
      }
    }
    for (int y = 0; y < 6; y++) {
      for (int x = y == 0 ? index : 0; x < 7; x++) {
        int value = count++;
        JToggleButton button = this.components.get(y).get(x);
        button.setText("" + value);
        button.setSelected(value == daySelected && monthSelected == monthView
          && yearSelected == yearView);
        if (count > daysInMonth) {
          break;
        }
      }
      if (count > daysInMonth) {
        break;
      }
    }
    for (int y = 0; y < 6; y++) {
      for (int x = 0; x < 7; x++) {
        JToggleButton button = this.components.get(y).get(x);
        button.setEnabled(button.getText().length() > 0);
      }
    }
  }

  /**
   * @param e
   * @see java.util.List#add(java.lang.Object)
   */
  public void add(BCalendarListener e) {
    listeners.add(e);
  }

  /**
   * @param o
   * @see java.util.List#remove(java.lang.Object)
   */
  public void remove(Object o) {
    listeners.remove(o);
  }

  /**
   * Seleciona um dia
   * 
   * @param day
   */
  public void setSelectedDay(int day) {
    this.dayModel.setValue(day);
    calendarSelected.set(Calendar.DAY_OF_MONTH, day);
    calendarViewer.set(Calendar.DAY_OF_MONTH, day);
    this.setViewedDay(day);
    this.fireChangedDate();
  }

  /**
   * Seleciona um dia
   * 
   * @param month
   */
  public void setSelectedMonth(int month) {
    this.monthModel.setSelectedItem(this.monthModel.getElementAt(month));
    calendarSelected.set(Calendar.MONTH, month);
    this.setViewedMonth(month);
    this.fireChangedDate();
  }

  /**
   * Seleciona um dia
   * 
   * @param year
   */
  public void setSelectedYear(int year) {
    this.yearModel.setValue(year);
    calendarSelected.set(Calendar.YEAR, year);
    this.setViewedYear(year);
    this.fireChangedDate();
  }

  /**
   * Seleciona um dia
   * 
   * @param day
   */
  public void setViewedDay(int day) {
    calendarViewer.set(Calendar.DAY_OF_MONTH, day);
  }

  /**
   * Seleciona um dia
   * 
   * @param month
   */
  public void setViewedMonth(int month) {
    calendarViewer.set(Calendar.MONTH, month);
  }

  /**
   * Seleciona um dia
   * 
   * @param year
   */
  public void setViewedYear(int year) {
    calendarViewer.set(Calendar.YEAR, year);
  }

  /**
   * Retorna o dia [1-31] selecionada
   * 
   * @return data
   */
  public int getSelectedDay() {
    return this.calendarSelected.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Retorna o mês [0-11] selecionada
   * 
   * @return data
   */
  public int getSelectedMonth() {
    return this.calendarSelected.get(Calendar.MONTH);
  }

  /**
   * Retorna o ano [1900-2100] selecionada
   * 
   * @return data
   */
  public int getSelectedYear() {
    return this.calendarSelected.get(Calendar.YEAR);
  }

  /**
   * Retorna o dia [1-31] selecionada
   * 
   * @return data
   */
  public int getViewedDay() {
    return this.calendarViewer.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Retorna o mês [0-11] selecionada
   * 
   * @return data
   */
  public int getViewedMonth() {
    return this.calendarViewer.get(Calendar.MONTH);
  }

  /**
   * Retorna o ano [1900-2100] selecionada
   * 
   * @return data
   */
  public int getViewedYear() {
    return this.calendarViewer.get(Calendar.YEAR);
  }

  /**
   * Retorna
   * 
   * @return calendar
   */
  public Calendar getSelectedCalendar() {
    return calendarSelected;
  }

  /**
   * Retorna
   * 
   * @return calendar
   */
  public Calendar getViewedCalendar() {
    return calendarViewer;
  }

  /**
   * Altera a seleção do calendario
   * 
   * @param date
   */
  public void setSelectedCalendar(Date date) {
    this.calendarSelected.setTime(date);
    this.fireChangedDate();
    this.fireRefresh();
  }

  /**
   * Retorna
   * 
   * @return calendar
   */
  public Date getSelectedDate() {
    return calendarSelected.getTime();
  }

  /**
   * Retorna
   * 
   * @return calendar
   */
  public Date getViewedDate() {
    return calendarViewer.getTime();
  }

  /**
   * Campo de dia
   * 
   * @return componente
   */
  private Component buildDayText() {
    int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    this.dayModel = new SpinnerNumberModel(day, 1, 31, 1);
    final JSpinner dayPanel = new JSpinner(dayModel);
    DefaultEditor editor = (DefaultEditor) dayPanel.getEditor();
    editor.getTextField().setEditable(false);
    dayPanel.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        setSelectedDay(Integer.valueOf(dayPanel.getValue().toString()));
        fireRefresh();
      }
    });
    return dayPanel;
  }

  /**
   * Campo de mes
   * 
   * @return componente
   */
  private Component buildMouthCombo() {
    DateFormatSymbols dateFormatSymbols =
      new DateFormatSymbols(Locale.getDefault());
    String[] monthNames = dateFormatSymbols.getMonths();
    String[] names = new String[12];
    for (int n = 0, m = 0; n < monthNames.length; n++) {
      if (monthNames[n].length() > 0) {
        names[m++] = monthNames[n];
      }
    }
    this.monthModel = new DefaultComboBoxModel(names);
    final JComboBox box = new JComboBox(monthModel);
    box.setSelectedIndex(this.getViewedCalendar().get(Calendar.MONTH));
    box.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setViewedMonth(box.getSelectedIndex());
        fireRefresh();
      }
    });
    return box;
  }

  /**
   * Campo de ano
   * 
   * @return componente
   */
  private Component buildYearCombo() {
    int year = Calendar.getInstance().get(Calendar.YEAR);
    this.yearModel = new SpinnerNumberModel(year, 1900, 2100, 1);
    final JSpinner pane = new JSpinner(yearModel);
    DefaultEditor editor = (DefaultEditor) pane.getEditor();
    editor.getTextField().setEditable(false);
    pane.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        setViewedYear(Integer.valueOf(pane.getValue().toString()));
        fireRefresh();
      }
    });
    return pane;
  }

  /**
   * Painel de calendario
   * 
   * @return componente
   */
  private Component buildWeekPanel() {
    JPanel panel = new JPanel(new GridLayout(1, 7, 0, 0));
    DateFormatSymbols dateFormatSymbols =
      new DateFormatSymbols(Locale.getDefault());
    String[] monthNames = dateFormatSymbols.getShortWeekdays();
    String[] names = new String[12];
    for (int n = 0, m = 0; n < monthNames.length; n++) {
      if (monthNames[n].length() > 0) {
        names[m++] = monthNames[n];
      }
    }
    for (int n = 0; n < 7; n++) {
      JLabel label = new JLabel();
      label.setText(names[n]);
      label.setHorizontalAlignment(JLabel.CENTER);
      panel.add(label);
    }
    return panel;
  }

  /**
   * Painel de calendario
   * 
   * @return componente
   */
  private Component buildCalendarPanel() {
    JPanel panel = new JPanel(new GridLayout(6, 7, 0, 0));
    for (int y = 0; y < 6; y++) {
      List<JToggleButton> list = new ArrayList<JToggleButton>();
      for (int x = 0; x < 7; x++) {
        final JToggleButton button = new JToggleButton();
        button.setMargin(new Insets(0, 0, 0, 0));
        button.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            setSelectedDay(Integer.parseInt(button.getText()));
            setSelectedMonth(getViewedMonth());
            setSelectedYear(getViewedYear());
            fireRefresh();
          }
        });
        list.add(button);
        panel.add(button);
      }
      this.components.add(list);
    }
    return panel;
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
    frame.add(new BCalendar(null));
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(800, 600);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

}
