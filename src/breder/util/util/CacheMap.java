package breder.util.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapa com tamanho fixo máximo
 * 
 * 
 * @author Bernardo Breder
 * @param <K>
 * @param <V>
 */
public class CacheMap<K, V> extends HashMap<K, V> {

  /** Tamanho */
  private final int size;

  /**
   * Construtor
   * 
   * @param size
   */
  public CacheMap(int size) {
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
  public V put(K key, V value) {
    if (this.size() >= this.size) {
      this.remove(this.keySet().iterator().next());
    }
    return super.put(key, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    for (K key : m.keySet()) {
      this.put(key, m.get(key));
    }
  }

}
