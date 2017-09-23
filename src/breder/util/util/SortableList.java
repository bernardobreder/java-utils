package breder.util.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

public class SortableList<E> extends LinkedList<E> {

  private Comparator<E> compare;

  public SortableList(Comparator<E> c) {
    this.compare = c;
  }

  @Override
  public void addFirst(E e) {
    this.add(e);
  }

  @Override
  public void addLast(E e) {
    this.add(e);
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean add(E e) {
    int size = this.size();
    Object[] objects = this.toArray();
    Object[] array = new Object[size + 1];
    System.arraycopy(objects, 0, array, 0, size);
    array[size] = e;
    Object[] clone = array.clone();
    mergeSort(array, clone, 0, size, 0);
    this.clear();
    for (Object object : clone) {
      super.add((E) object);
    }
    return true;
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    boolean flag = true;
    for (E e : c) {
      flag = flag && this.add(e);
    }
    return flag;
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    return this.addAll(c);
  }

  @Override
  public void add(int index, E element) {
    super.add(element);
  }

  @Override
  public void push(E e) {
    this.add(e);
  }

  @SuppressWarnings("unchecked")
  private void mergeSort(Object[] src, Object[] dest, int low, int high, int off) {
    int length = high - low;
    if (length < 7) {
      for (int i = low; i < high; i++)
        for (int j = i; j > low
          && this.compare.compare((E) dest[j - 1], (E) dest[j]) > 0; j--)
          swap(dest, j, j - 1);
      return;
    }
    int destLow = low;
    int destHigh = high;
    low += off;
    high += off;
    int mid = (low + high) >>> 1;
    mergeSort(dest, src, low, mid, -off);
    mergeSort(dest, src, mid, high, -off);
    if (this.compare.compare((E) src[mid - 1], (E) src[mid]) <= 0) {
      System.arraycopy(src, low, dest, destLow, length);
      return;
    }
    for (int i = destLow, p = low, q = mid; i < destHigh; i++) {
      if (q >= high || p < mid
        && this.compare.compare((E) src[p], (E) src[q]) <= 0)
        dest[i] = src[p++];
      else
        dest[i] = src[q++];
    }
  }

  private static void swap(Object[] x, int a, int b) {
    Object t = x[a];
    x[a] = x[b];
    x[b] = t;
  }

}
