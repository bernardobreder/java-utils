package breder.util.swing.model;

import java.util.Comparator;

public class RowOrderObjectModel<E> extends OrderObjectModel<E> implements
  Comparator<E> {

  private int sortColumnIndex;

  private boolean sortAlphabet;

  public RowOrderObjectModel(IObjectModel<E> next) {
    super(next, null);
    this.setComparator(this);
    this.sortAlphabet = true;
  }

  public void sort(int columnIndex) {
    if (this.sortColumnIndex == columnIndex) {
      this.sortAlphabet = !this.sortAlphabet;
    }
    else {
      this.sortAlphabet = true;
      this.sortColumnIndex = columnIndex;
    }
    this.sort(columnIndex, this.sortAlphabet);
  }

  public void sort(int columnIndex, boolean alphabet) {
    this.sortColumnIndex = columnIndex;
    this.sortAlphabet = alphabet;
    this.refreshModel();
  }

  @Override
  public int compare(E o1, E o2) {
    Comparable c1 = this.getNext().getValueAt(o1, -1, this.sortColumnIndex);
    Comparable c2 = this.getNext().getValueAt(o2, -1, this.sortColumnIndex);
    int value = c1.compareTo(c2);
    if (this.sortAlphabet) {
      return value;
    }
    else {
      return value * -1;
    }
  }

}
