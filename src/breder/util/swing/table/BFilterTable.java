package breder.util.swing.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

import breder.util.lookandfeel.LookAndFeel;
import breder.util.swing.BComponent;
import breder.util.swing.GBC;
import breder.util.swing.GroupValidator;
import breder.util.swing.Validator;
import breder.util.swing.model.FilterObjectModel;
import breder.util.swing.model.IObjectModel;
import breder.util.swing.model.StaticObjectModel;

public class BFilterTable<E> extends BComponent {

  private IObjectModel<E> model;

  private BTable<E> table;

  private JTextComponent[] texts;

  private GroupValidator groupValidator;

  public BFilterTable(IObjectModel<E> model) {
    this.model = model;
    this.init();
  }

  @Override
  protected Component buildComponent() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(this.buildFilter(), BorderLayout.NORTH);
    panel.add(this.buildTable(), BorderLayout.CENTER);
    return panel;
  }

  private Component buildFilter() {
    JPanel panel = new JPanel(new GridBagLayout());
    int columns = model.getColumnCount();
    this.texts = new JTextField[columns];
    for (int n = 0; n < columns; n++) {
      panel.add(new JLabel(model.getColumnName(n) + " : "), new GBC(0, n)
        .right());
      panel.add(this.texts[n] = new JTextField(), new GBC(1, n).horizontal());
      this.texts[n].addKeyListener(new KeyAdapter() {

        @Override
        public void keyReleased(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (table.getModel().getSize() == 1) {
              int row = 0;
              E cell = table.getModel().getRow(row);
              table.fireOpenCellListener(row, cell);
            }
          }
          else {
            table.getModel(FilterObjectModel.class).filter();
            if (groupValidator != null) {
              groupValidator.fireValidator();
            }
          }
        }
      });
    }
    return panel;
  }

  private Component buildTable() {
    table = new BTable<E>(new MyFilterObjectModel(model));
    this.table.getTable().getSelectionModel().addListSelectionListener(
      new ListSelectionListener() {

        @Override
        public void valueChanged(ListSelectionEvent e) {
          if (groupValidator != null) {
            groupValidator.fireValidator();
          }
        }
      });
    this.table.getModel().refresh();
    return table;
  }

  public IObjectModel<E> getModel() {
    return table.getModel();
  }

  public void addOpenCellListener(IOpenCellListener<E> listener) {
    table.addOpenCellListener(listener);
  }

  public BTable<E> getTable() {
    return table;
  }

  @Override
  public void addValidator(Validator validator) {
    this.fireValidator();
  }

  @Override
  public boolean fireValidator() {
    return this.table.getTable().getSelectedRow() >= 0
      || this.table.getModel().getRowCount() == 1;
  }

  private class MyFilterObjectModel extends FilterObjectModel<E> {

    public MyFilterObjectModel(IObjectModel<E> next) {
      super(next);
    }

    @Override
    public boolean accept(E element) {
      {
        boolean found = true;
        for (JTextComponent text : texts) {
          if (text.getText().trim().length() > 0) {
            found = false;
            break;
          }
        }
        if (found) {
          return true;
        }
      }
      {
        boolean found = true;
        for (int n = 0; n < texts.length; n++) {
          String string = texts[n].getText().trim();
          if (string.length() > 0) {
            Object object = this.getValueAt(element, 0, n);
            if (object.toString().toLowerCase().indexOf(string.toLowerCase()) == -1) {
              found = false;
              break;
            }
          }
        }
        return found;
      }
    }

  }

  public static void main(String[] args) {
    LookAndFeel.getInstance().installNative();
    final JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new BorderLayout());
    final BFilterTable<String[]> table;
    {
      table =
        new BFilterTable<String[]>(new StaticObjectModel<String[]>("a", "b",
          "c") {

          String[][] rows = new String[][] { { "1x1", "1x2", "1x3" },
              { "2x1", "2x2", "2x3" }, { "3x1", "3x2", "3x3" } };

          @Override
          public String[] getRow(int index) {
            return rows[index];
          }

          @Override
          public int getSize() {
            return rows.length;
          }

          @Override
          public Comparable<String> getValueAt(String[] element, int row,
            int column) {
            return element[column];
          }

        });
      frame.add(table.getComponent(), BorderLayout.CENTER);
    }
    {
      JButton c = new JButton();
      c.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
        }
      });
      frame.add(c, BorderLayout.SOUTH);
    }
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

}
