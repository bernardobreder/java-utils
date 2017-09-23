package breder.util.swing.model;

import java.rmi.RemoteException;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import breder.util.lookandfeel.LookAndFeel;
import breder.util.swing.table.BTable;
import breder.util.swing.tree.IListener;

/**
 * Modelo básico
 * 
 * 
 * @author Bernardo Breder
 * @param <E>
 */
public abstract class AbstractObjectModel<E> implements IObjectModel<E> {

  /** Listener */
  protected EventListenerList listeners = new EventListenerList();
  /** Modelo pai */
  private IObjectModel<E> parent;
  /** Modelo proximo */
  private final IObjectModel<E> next;
  /** Selecionado */
  private E selected;
  /** Colunas */
  private String[] columns;

  /**
   * Construtor
   * 
   * @param next
   * @param columns
   */
  public AbstractObjectModel(IObjectModel<E> next, String... columns) {
    super();
    this.next = next;
    if (columns == null || columns.length == 0) {
      columns = new String[] { "" };
    }
    this.columns = columns;
    if (next != null) {
      next.setParent(this);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Comparable<?> getValueAt(E element, int row, int column) {
    return element.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refresh() {
    IObjectModel<E> model = this;
    while (model.getNext() != null) {
      model = model.getNext();
    }
    while (model != null) {
      model.refreshModel();
      model = model.getParent();
    }
    this.refreshParents();
  }

  /**
   * Atualiza o pai
   */
  public void refreshParents() {
    if (this.getParent() == null) {
      this.fireDataModelChanged();
      return;
    }
    IObjectModel<E> model = this.getParent();
    while (true) {
      model.refreshModel();
      if (model.getParent() == null) {
        model.fireDataModelChanged();
        break;
      }
      else {
        model = model.getParent();
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void fireDataModelChanged() {
    if (this.getParent() != null) {
      this.getParent().fireDataModelChanged();
    }
    {
      Object[] listeners = this.listeners.getListenerList();
      for (int i = listeners.length - 2; i >= 0; i -= 2) {
        Object object = listeners[i];
        if (object == ListDataListener.class) {
          ListDataEvent e =
            new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, this
              .getSize());
          ((ListDataListener) listeners[i + 1]).contentsChanged(e);
          ((ListDataListener) listeners[i + 1]).contentsChanged(e =
            new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, this
              .getSize()));
        }
        else if (object == TableModelListener.class) {
          // Foi retirado porque estava fazendo refresh nas colunas
          // dando efeito estranho.
          // ((TableModelListener) listeners[i + 1])
          // .tableChanged(new TableModelEvent(this,
          // TableModelEvent.HEADER_ROW));
          ((TableModelListener) listeners[i + 1])
            .tableChanged(new TableModelEvent(this));
        }
      }
    }
    this.fireListener();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void fireColumnModelChanged() {
    if (this.getParent() != null) {
      this.getParent().fireDataModelChanged();
    }
    {
      Object[] listeners = this.listeners.getListenerList();
      for (int i = listeners.length - 2; i >= 0; i -= 2) {
        Object object = listeners[i];
        if (object == ListDataListener.class) {
          ListDataEvent e =
            new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, this
              .getSize());
          ((ListDataListener) listeners[i + 1]).contentsChanged(e);
          ((ListDataListener) listeners[i + 1]).contentsChanged(e =
            new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, this
              .getSize()));
        }
        else if (object == TableModelListener.class) {
          ((TableModelListener) listeners[i + 1])
            .tableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
          ((TableModelListener) listeners[i + 1])
            .tableChanged(new TableModelEvent(this));
        }
      }
    }
    this.fireListener();
  }

  /**
   * Dispara os listeners
   */
  public void fireListener() {
    Object[] listeners = this.listeners.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      Object object = listeners[i];
      if (object == IListener.class) {
        ((IListener) listeners[i + 1]).run();
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IObjectModel<E> getParent() {
    return parent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setParent(IObjectModel<E> parent) {
    this.parent = parent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IObjectModel<E> getNext() {
    return next;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addListDataListener(ListDataListener l) {
    this.listeners.add(ListDataListener.class, l);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getElementAt(int index) {
    return this.getRow(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeListDataListener(ListDataListener l) {
    this.listeners.remove(ListDataListener.class, l);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getSelectedItem() {
    return this.selected;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void setSelectedItem(Object anItem) {
    this.selected = (E) anItem;
  }

  /**
   * Busca por uma linha
   * 
   * @param row
   * @return indice da linha
   */
  public int indexOf(E row) {
    int rows = this.getRowCount();
    for (int n = 0; n < rows; n++) {
      E r = this.getRow(n);
      if (r.equals(row)) {
        return n;
      }
    }
    return -1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addTableModelListener(TableModelListener l) {
    listeners.add(TableModelListener.class, l);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (this.getSize() > 0) {
      return Object.class;
    }
    else {
      return this.getValueAt(this.getRow(0), 0, columnIndex).getClass();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getColumnCount() {
    return this.columns.length;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getColumnName(int columnIndex) {
    return this.columns[columnIndex];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getRowCount() {
    return this.getSize();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return this.getValueAt(this.getRow(rowIndex), rowIndex, columnIndex);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeTableModelListener(TableModelListener l) {
    listeners.remove(TableModelListener.class, l);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T findNext(Class<T> c) {
    IObjectModel<E> model = this;
    while (model != null) {
      if (c.isInstance(model)) {
        return (T) model;
      }
      model = model.getNext();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addListener(IListener listener) {
    this.listeners.add(IListener.class, listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeListener(IListener listener) {
    this.listeners.remove(IListener.class, listener);
  }

  /**
   * Testador
   * 
   * @param args
   */
  public static void main(String[] args) {
    LookAndFeel.getInstance().installNative();
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    IObjectModel<String> model = new RemoteObjectModel<String>("a") {

      @Override
      public String[] refreshModelRemote() throws RemoteException {
        try {
          Thread.sleep(2000);
        }
        catch (InterruptedException e) {
        }
        return new String[] { "a", "b" };
      }
    };
    IObjectModel<String> model3 = new RemoteObjectModel<String>("a") {

      @Override
      public String[] refreshModelRemote() throws RemoteException {
        try {
          Thread.sleep(2000);
        }
        catch (InterruptedException e) {
        }
        return new String[] { "a", "b" };
      }
    };
    IObjectModel<String> model2 = new StaticObjectModel<String>("b") {

      private String[] values = new String[] { "a", "b", "c" };

      @Override
      public String getRow(int index) {
        return values[index];
      }

      @Override
      public int getSize() {
        return values.length;
      }

    };
    Box box = Box.createVerticalBox();
    box.add(new JComboBox(model));
    box.add(new JScrollPane(new JList(model)));
    box.add(new JScrollPane(new JTable(model2)));
    box.add(new JScrollPane(new BTable<String>(new FilterObjectModel<String>(
      model3) {

      @Override
      public boolean accept(String element) {
        return true;
      }
    })));
    frame.add(box);
    frame.pack();
    frame.setSize(800, frame.getHeight());
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
