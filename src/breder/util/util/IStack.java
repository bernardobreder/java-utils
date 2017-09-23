package breder.util.util;

/**
 * Pilha de Objetos
 * 
 * @author bernardobreder
 * @param <E>
 */
public interface IStack<E> {

  /**
   * Empilha
   * 
   * @param value
   */
  public void push(E value);

  /**
   * Retorna o do topo da lista
   * 
   * @return topo da lista
   */
  public E peek();

  /**
   * Empilha
   * 
   * @param value
   * @return objeto
   */
  public E pop();

  /**
   * Retorna o tamanho da pilha
   * 
   * @return tamanho
   */
  public int size();

}
