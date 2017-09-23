package breder.util.util.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.WeakHashMap;

/**
 * Conjunto Fraco
 * 
 * @author bernardobreder
 * 
 * @param <K>
 */
public class WeakHashSet<K> extends WeakHashMap<K, Object> {

  /** Objeto */
  private static final Object PRESENT = new Object();

  public boolean contains(Object o) {
    return this.containsKey(o);
  }

  public Iterator<K> iterator() {
    return this.keySet().iterator();
  }

  public Object[] toArray() {
    return this.keySet().toArray();
  }

  public <T> T[] toArray(T[] a) {
    return this.keySet().toArray(a);
  }

  public boolean add(K e) {
    return this.put(e, PRESENT) == null;
  }

  public boolean containsAll(Collection<?> c) {
    return this.keySet().containsAll(c);
  }

  public boolean addAll(Collection<? extends K> c) {
    boolean flag = false;
    for (K elem : c) {
      flag |= this.add(elem);
    }
    return flag;
  }

  @SuppressWarnings("unchecked")
  public boolean removeAll(Collection<?> c) {
    boolean flag = false;
    for (Object elem : c) {
      flag |= this.add((K) elem);
    }
    return flag;
  }

}
