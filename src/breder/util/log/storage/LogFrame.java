package breder.util.log.storage;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import breder.util.lookandfeel.ILookAndFeel;
import breder.util.swing.BFrame;
import breder.util.swing.model.StaticObjectModel;
import breder.util.swing.table.BTable;
import breder.util.task.EventTask;
import breder.util.util.StringUtil;

/**
 * Janela de Log
 * 
 * 
 * @author Bernardo Breder
 */
public class LogFrame extends BFrame {

  /**
   * @param parent
   */
  public LogFrame(BFrame parent) {
    super(parent);
    this.setTitle("Log");
    this.setLayout(new BorderLayout(10, 10));
    this.add(this.buildCenter(), BorderLayout.CENTER);
    this.add(this.buildSouth(), BorderLayout.SOUTH);
    this.pack();
    this.setLocationRelativeTo(null);
  }

  /**
   * Constroi o componente do meio
   * 
   * @return componente
   */
  private Component buildCenter() {
    final BTable<Log> table = new BTable<Log>(new MyModel());
    table.getTable().getColumnModel().getColumn(0).setMaxWidth(150);
    table.getTable().getColumnModel().getColumn(0).setMinWidth(150);
    table.getTable().getColumnModel().getColumn(1).setMaxWidth(50);
    table.getTable().getColumnModel().getColumn(1).setMinWidth(50);
    table.getTable().setFocusable(false);
    table.getTable().addMouseMotionListener(new MouseAdapter() {
      /**
       * {@inheritDoc}
       */
      @Override
      public void mouseMoved(MouseEvent e) {
        int y = e.getY();
        for (int n = 0; n < table.getTable().getRowCount(); n++) {
          y -= table.getTable().getRowHeight(n);
          if (y < 0) {
            Log row = table.getModel().getRow(n);
            table.getTable()
              .setToolTipText(StringUtil.text2html(row.getText()));
            break;
          }
        }
      }
    });
    table.setBorder(BorderFactory.createEmptyBorder());
    return table;
  }

  /**
   * Constroi o componente do meio
   * 
   * @return componente
   */
  private Component buildSouth() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panel.add(this.buildOkButton());
    return panel;
  }

  /**
   * Constroi o botão Ok
   * 
   * @return comp
   */
  private Component buildOkButton() {
    JButton c = new JButton("Ok");
    c.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        LogFrame.this.close();
      }
    });
    this.getRootPane().setDefaultButton(c);
    return c;
  }

  /**
   * Modelo de tabela
   * 
   * 
   * @author Bernardo Breder
   */
  private static class MyModel extends StaticObjectModel<Log> {

    /** Formatador */
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
      "dd/MM/yyyy kk:mm");
    /** Lista de Log */
    private static final List<Log> LIST = LogModel.getInstance().getList();

    /**
     * Construtor
     */
    public MyModel() {
      super("Date", "Type", "Title");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Log getRow(int index) {
      return LIST.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
      return LIST.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Comparable<?> getValueAt(Log element, int row, int column) {
      new SimpleDateFormat("dd/MM/yyyy kk:mm");
      switch (column) {
        case 0:
          return FORMAT.format(element.getDate());
        case 1:
          return element.getType();
        case 2:
          return element.getTitle();
        default:
          return "";
      }
    }

  }

  /**
   * Testador
   * 
   * @param args
   */
  public static void main(String[] args) {
    LogModel.getInstance().add(new InfoLog("Teste"));
    try {
      throw new Exception("Teste");
    }
    catch (Exception e) {
      LogModel.getInstance().add(new ErrorLog(e));
    }
    EventTask.invokeLater(new Runnable() {
      @Override
      public void run() {
        ILookAndFeel.DEFAULT.installRandom();
        new LogFrame(null).setVisible(true);
      }
    });
  }

}
