package breder.util.util;

import java.util.ArrayList;

/**
 * Pilha implementada com array
 * 
 * @author bernardobreder
 * @param <E>
 */
public class ArrayStack<E> extends ArrayList<E> implements IStack<E> {

  /**
   * {@inheritDoc}
   */
  public void push(E value) {
    this.add(value);
  }

  /**
   * {@inheritDoc}
   */
  public E peek() {
    return this.get(this.size() - 1);
  }

  /**
   * {@inheritDoc}
   */
  public E pop() {
    return this.remove(this.size() - 1);
  }

}
