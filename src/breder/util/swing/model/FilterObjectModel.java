package breder.util.swing.model;

import java.util.ArrayList;
import java.util.List;

public abstract class FilterObjectModel<E> extends DynamicObjectModel<E> {

  private List<Integer> list = new ArrayList<Integer>();

  public FilterObjectModel(IObjectModel<E> next) {
    super(next);
    this.filter();
  }

  public abstract boolean accept(E element);

  public boolean filter() {
    List<Integer> old = new ArrayList<Integer>(list);
    list.clear();
    this.getNext().refreshModel();
    int size = this.getNext().getRowCount();
    for (int n = 0; n < size; n++) {
      if (this.isEnable() && this.accept(this.getNext().getRow(n))) {
        list.add(n);
      }
    }
    this.fireDataModelChanged();
    return !list.equals(old);
  }

  @Override
  public E getRow(int row) {
    return this.getNext().getRow(list.get(row));
  }

  @Override
  public int getSize() {
    return list.size();
  }

  @Override
  public void refreshModel() {
    this.filter();
  }

}
