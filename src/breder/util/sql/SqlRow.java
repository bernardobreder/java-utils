package breder.util.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * Linha de Sql
 * 
 * 
 * @author Bernardo Breder
 */
public class SqlRow {

  /** Colunas */
  private final List<Object> columns = new ArrayList<Object>();
  /** Currente indice */
  private int index;

  /**
   * Adiciona uma coluna
   * 
   * @param e
   */
  protected void add(Object e) {
    columns.add(e);
  }

  /**
   * Retorna o valor de um indice
   * 
   * @param <E>
   * @param index
   * @return valor
   */
  @SuppressWarnings("unchecked")
  public <E> E get(int index) {
    return (E) this.columns.get(index);
  }

  /**
   * Retorna o valor de um indice
   * 
   * @param <E>
   * @return valor
   */
  public <E> E get() {
    return this.get(index++);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    int size = this.columns.size();
    for (int n = 0; n < size; n++) {
      Object data = this.columns.get(n);
      if (data == null) {
        sb.append("null");
      }
      else {
        sb.append(data.toString());
      }
      if (n != size - 1) {
        sb.append(", ");
      }
    }
    sb.append("]");
    return sb.toString();
  }

}
