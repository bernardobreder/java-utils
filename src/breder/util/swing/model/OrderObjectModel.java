package breder.util.swing.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrderObjectModel<E> extends DynamicObjectModel<E> {

  private Comparator<E> comparator;

  private final List<Integer> list = new ArrayList<Integer>();

  public OrderObjectModel(IObjectModel<E> next, Comparator<E> comparator) {
    super(next);
    this.comparator = comparator;
  }

  @Override
  public void refreshModel() {
    this.list.clear();
    int size = this.getSize();
    List<Entity> entitys = new ArrayList<Entity>(size);
    for (int n = 0; n < size; n++) {
      Entity entity = new Entity();
      entity.index = n;
      entity.value = this.getNext().getRow(n);
      entitys.add(entity);
    }
    Collections.sort(entitys, new Comparator<Entity>() {
      @Override
      public int compare(Entity o1, Entity o2) {
        return comparator.compare(o1.value, o2.value);
      }
    });
    for (Entity entity : entitys) {
      this.list.add(entity.index);
    }
  }

  @Override
  public E getRow(int index) {
    return this.getNext().getRow(list.get(index));
  }

  @Override
  public int getSize() {
    return this.getNext().getSize();
  }

  public Comparator<E> getComparator() {
    return comparator;
  }

  public void setComparator(Comparator<E> comparator) {
    this.comparator = comparator;
  }

  private class Entity {
    private int index;
    private E value;
  }

}
