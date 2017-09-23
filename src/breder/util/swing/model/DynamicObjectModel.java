package breder.util.swing.model;

public abstract class DynamicObjectModel<E> extends AbstractObjectModel<E> {

  private boolean enable = true;

  public DynamicObjectModel(IObjectModel<E> next) {
    super(next);
  }

  public abstract void refreshModel();

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return this.getNext().getColumnClass(columnIndex);
  }

  @Override
  public int getColumnCount() {
    return this.getNext().getColumnCount();
  }

  @Override
  public String getColumnName(int columnIndex) {
    return this.getNext().getColumnName(columnIndex);
  }

  @Override
  public Comparable<?> getValueAt(E element, int row, int column) {
    return this.getNext().getValueAt(element, row, column);
  }

}
