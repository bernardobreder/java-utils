package breder.util.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Mapa com tamanho fixo máximo
 * 
 * 
 * @author Bernardo Breder
 * @param <K>
 * @param <V>
 */
public class ThreadCacheMap<K, V> extends HashMap<K, V> {

  /** Tamanho */
  private final int size;

  /**
   * Construtor
   * 
   * @param size
   */
  public ThreadCacheMap(int size) {
    super();
    this.size = size;
    if (this.size <= 0) {
      throw new IllegalArgumentException("size must be higher then zero");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized V put(K key, V value) {
    if (this.size() >= this.size) {
      this.remove(this.keySet().iterator().next());
    }
    return super.put(key, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void putAll(Map<? extends K, ? extends V> m) {
    for (K key : m.keySet()) {
      this.put(key, m.get(key));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized int size() {
    return super.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized boolean isEmpty() {
    return super.isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized V get(Object key) {
    return super.get(key);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized boolean containsKey(Object key) {
    return super.containsKey(key);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized V remove(Object key) {
    return super.remove(key);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void clear() {
    super.clear();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized boolean containsValue(Object value) {
    return super.containsValue(value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized Object clone() {
    return super.clone();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized Set<K> keySet() {
    return super.keySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized Collection<V> values() {
    return super.values();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized Set<java.util.Map.Entry<K, V>> entrySet() {
    return super.entrySet();
  }

}
