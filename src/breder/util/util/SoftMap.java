package breder.util.util;

import java.lang.ref.SoftReference;
import java.util.TreeSet;

/**
 * Mapa na qual seus elementos sao referencias do tipo SoftReference
 * 
 * @author bernardobreder
 * 
 * @param <K>
 * @param <V>
 */
public class SoftMap<K extends Comparable<K>, V> {

  /**
   * Estrutura de mapa na forma de arvore
   */
  protected TreeSet<MySoftReference<K, V>> map =
    new TreeSet<MySoftReference<K, V>>();

  /**
   * Realiza uma consulta no mapa
   * 
   * @param key
   * @return valor da consulta ou nulo caso não ache
   */
  public V get(K key) {
    MySoftReference<K, V> ref = new MySoftReference<K, V>(key, null);
    MySoftReference<K, V> floor = map.floor(ref);
    if (floor != null && floor.equals(ref)) {
      V value = floor.get();
      if (value == null) {
        map.remove(ref);
      }
      return value;
    }
    return null;
  }

  /**
   * Adiciona um elemento no mapa
   * 
   * @param key
   * @param value
   */
  public void put(K key, V value) {
    map.add(new MySoftReference<K, V>(key, value));
  }

  /**
   * Remove um elemento do mapa
   * 
   * @param key
   * @return valor do elemento encontrado ou nulo caso não ache
   */
  public V remove(K key) {
    MySoftReference<K, V> ref = new MySoftReference<K, V>(key, null);
    MySoftReference<K, V> floor = map.floor(ref);
    if (floor != null && floor.equals(ref)) {
      V value = floor.get();
      floor.clear();
      map.remove(ref);
      return value;
    }
    return null;
  }

  /**
   * Limpa o mapa
   */
  public void clear() {
    for (MySoftReference<K, V> ref : map) {
      ref.clear();
    }
    map.clear();
  }

  /**
   * Tamanho do mapa
   * 
   * @return tamanho do mapa
   */
  public int size() {
    return this.map.size();
  }

  /**
   * Indica se o mapa está vazio
   * 
   * @return mapa está vazio
   */
  public boolean isEmpty() {
    return this.map.isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.map.toString();
  }

  /**
   * Um SoftReference que usa o equals e hashcode da referencia
   * 
   * @author bernardobreder
   * @param <K>
   * @param <V>
   */
  private static class MySoftReference<K extends Comparable<K>, V> extends
    SoftReference<V> implements Comparable<MySoftReference<K, V>> {

    /** Hash */
    private K key;
    /** Hash */
    private int hash;

    /**
     * Construtor
     * 
     * @param key
     * @param value
     */
    public MySoftReference(K key, V value) {
      super(value);
      this.key = key;
      this.hash = key.hashCode();
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public int hashCode() {
      return this.hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof MySoftReference)) {
        return false;
      }
      @SuppressWarnings("unchecked")
      MySoftReference<K, V> o = (MySoftReference<K, V>) obj;
      if (this.hash != o.hash) {
        return false;
      }
      return this.key.equals(obj);
    }

    /**
     * @param o
     * @return igualdade
     */
    public boolean equals(MySoftReference<K, V> o) {
      return o != null
        && (this.key == o.key || (this.hash == o.hash && this.key.equals(o)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(MySoftReference<K, V> o) {
      if (this.hash != o.hash) {
        return this.hash - o.hash;
      }
      return this.key.compareTo(o.key);
    }

  }

}
