package breder.util.swing.model;

import javax.swing.ComboBoxModel;
import javax.swing.ListModel;
import javax.swing.table.TableModel;

/**
 * Interface de modelo de tabela
 * 
 * 
 * @author Bernardo Breder
 * @param <E>
 */
public interface IObjectModel<E> extends IModel, ComboBoxModel, ListModel,
  TableModel {

  /**
   * Retorna a estrutura da linnha
   * 
   * @param index
   * @return estrutura
   */
  public E getRow(int index);

  /**
   * Atualiza o modelo
   */
  public void refresh();

  /**
   * {@inheritDoc}
   */
  @Override
  public int getSize();

  /**
   * Retorna o modelo pai
   * 
   * @return modelo pai
   */
  public IObjectModel<E> getParent();

  /**
   * Atribui um modelo pai
   * 
   * @param parent
   */
  public void setParent(IObjectModel<E> parent);

  /**
   * Retorna o valor
   * 
   * @param element
   * @param row
   * @param column
   * @return valor
   */
  public Comparable<?> getValueAt(E element, int row, int column);

  /**
   * Proximo modelo
   * 
   * @return modelo
   */
  public IObjectModel<E> getNext();

  /**
   * Encontra um modelo
   * 
   * @param <T>
   * @param c
   * @return modelo
   */
  public <T> T findNext(Class<T> c);

}
