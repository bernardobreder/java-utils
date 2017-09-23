package breder.util.swing.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import breder.util.lookandfeel.LookAndFeel;
import breder.util.swing.BMenuItem;
import breder.util.swing.BMenuItemValidator;
import breder.util.swing.model.IObjectModel;
import breder.util.swing.model.RowOrderObjectModel;
import breder.util.swing.model.StaticObjectModel;

public class BTable<E> extends JScrollPane {

  private JTable table;

  protected List<IOpenCellListener<E>> listeners;

  private JPopupMenu popupMenu;

  private IObjectModel<E> model;

  private int[] columnSizes;

  /**
   * Construtor
   * 
   * @param model
   */
  public BTable(IObjectModel<E> model) {
    super(new JTable(model));
    this.table = (JTable) getViewport().getView();
    this.model = (IObjectModel<E>) this.table.getModel();
    this.getModel().refreshModel();
    this.confPopup();
    this.confOrder();
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {
        getTable().requestFocus();
      }
    });
  }

  public void refresh() {
    int[] rows = this.getTable().getSelectedRows();
    this.getModel().refresh();
    for (int row : rows) {
      this.getTable().getSelectionModel().setSelectionInterval(row, row);
    }
    //		if (this.columnSizes != null) {
    //			this.resizeColumn(this.columnSizes);
    //		}
  }

  public void resizeColumn(int... indexs) {
    Arrays.sort(indexs);
    this.columnSizes = indexs;
    int size = this.getTable().getColumnCount();
    int dsize = this.getModel().getRowCount();
    for (int n = 0; n < size; n++) {
      TableColumn column = this.getTable().getColumnModel().getColumn(n);
      if (Arrays.binarySearch(indexs, n) >= 0) {

      }
      else {
        int width = 0;
        for (int m = 0; m < dsize; m++) {
          TableCellRenderer renderer =
            this.getTable().getDefaultRenderer(
              this.getTable().getModel().getColumnClass(n));
          Component c =
            renderer.getTableCellRendererComponent(this.getTable(), this
              .getTable().getModel().getValueAt(m, n), true, true, m, n);
          width = Math.max(width, c.getPreferredSize().width);
        }
        column.setPreferredWidth(width);
        column.setWidth(width);
        column.setMaxWidth(width);
      }
    }
    this.fireColumnChanged();
  }

  private void fireColumnChanged() {
    this.getModel().fireColumnModelChanged();
  }

  private void confPopup() {
    this.popupMenu = new JPopupMenu();
    {
      BMenuItem item = new BMenuItem(null, "Atualizar", new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          getModel().refresh();
        }

      }, null, new BMenuItemValidator<BMenuItem>() {

        @Override
        public boolean isValid(BMenuItem element) {
          return true;
        }

      });
      this.addPopupMenu(item);
    }
    MouseAdapter listener = new MouseAdapter() {

      @Override
      public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
          popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
        }
      }

    };
    this.addMouseListener(listener);
    this.table.addMouseListener(listener);
  }

  public void addPopupMenu(JMenuItem item) {
    this.popupMenu.add(item);
  }

  private void confOrder() {
    RowOrderObjectModel<E> model = getModel(RowOrderObjectModel.class);
    if (model != null) {
      this.table.getTableHeader().addMouseListener(new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
          if (e.getButton() == MouseEvent.BUTTON1) {
            RowOrderObjectModel<E> model = getModel(RowOrderObjectModel.class);
            int columnIndex =
              table.getColumnModel().getColumnIndexAtX(e.getX());
            model.sort(columnIndex);
            e.consume();
          }
        }

      });
    }
  }

  public void addOpenCellListener(IOpenCellListener<E> listener) {
    if (listeners == null) {
      listeners = new ArrayList<IOpenCellListener<E>>();
      this.getTable().addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2) {
            fireOpenCellListener();
            e.consume();
          }
        }

      });
      this.getTable().addKeyListener(new KeyAdapter() {

        @Override
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            fireOpenCellListener();
            e.consume();
          }
        }

      });
    }
    listeners.add(listener);
  }

  public void removeOpenCellListener(IOpenCellListener<E> listener) {
    listeners.remove(listener);
  }

  public void fireOpenCellListener() {
    int row = table.getSelectedRow();
    if (row != -1) {
      E cell = getModel().getRow(row);
      this.fireOpenCellListener(row, cell);
    }
  }

  public void fireOpenCellListener(int row, E cell) {
    for (IOpenCellListener<E> listener : listeners) {
      listener.actionPerformed(row, cell);
    }
  }

  @SuppressWarnings("unchecked")
  public IObjectModel<E> getModel() {
    return (IObjectModel<E>) table.getModel();
  }

  public <E extends IObjectModel> E getModel(Class<E> c) {
    IObjectModel<?> model = this.getModel();
    while (model != null) {
      if (c.isInstance(model)) {
        return c.cast(model);
      }
      model = model.getNext();
    }
    return null;
  }

  public JTable getTable() {
    return table;
  }

  @Override
  public Dimension getPreferredSize() {
    if (true) {
      return super.getPreferredSize();
    }
    else {
      int width = 400;
      int height = 0;
      {
        height += 2 * (this.getInsets().top + this.getInsets().bottom) + 1;
        height += this.getTable().getTableHeader().getPreferredSize().height;
        height += this.getModel().getSize() * this.getTable().getRowHeight();
      }
      return new Dimension(Math.min(400, width), 200);
    }
  }

  public static void main(String[] args) {
    LookAndFeel.getInstance().installSeaglass();
    final JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new BorderLayout());
    final BTable<String[]> table;
    {
      final String[][] rows =
        new String[][] { { "1x1", "1x2", "1x3" }, { "2x1", "2x2", "2x3" },
            { "3x1", "3x2", "3x3" } };
      final RowOrderObjectModel<String[]> model =
        new RowOrderObjectModel<String[]>(new StaticObjectModel<String[]>("a",
          "b", "c") {

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
      table = new BTable<String[]>(model);
      frame.add(table, BorderLayout.CENTER);
      new Thread() {
        @Override
        public void run() {
          while (true) {
            try {
              Thread.sleep(1000);
            }
            catch (InterruptedException e) {
            }
            if (rows[1][1].equals("2x2")) {
              rows[1][1] = "10x10";
            }
            else {
              rows[1][1] = "2x2";
            }
            table.refresh();
          }
        }
      }.start();
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

  public E getSelectedRow() {
    int index = this.table.getSelectedRow();
    if (index == -1) {
      return null;
    }
    else {
      return this.model.getRow(index);
    }
  }

  public E[] getSelectedRows(Class<E> c) {
    int[] indexs = this.table.getSelectedRows();
    E[] elements = (E[]) Array.newInstance(c, indexs.length);
    for (int n = 0; n < indexs.length; n++) {
      elements[n] = this.getModel().getRow(indexs[n]);
    }
    return elements;
  }

}
