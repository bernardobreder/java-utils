package breder.util.model;

import java.io.Serializable;

/**
 * Código para todos os modelos
 * 
 * 
 * @author Bernardo Breder
 */
public class Code implements Serializable {

  /** Id */
  private String id;
  /** Id */
  private Integer number;

  /**
   * Construtor
   * 
   * @param prefix
   * @param code
   */
  public Code(String prefix, int code) {
    this.id = prefix + code;
  }

  /**
   * Construtor
   * 
   * @param id
   */
  public Code(String id) {
    this.id = id;
  }

  /**
   * Construtor
   * 
   * @param id
   */
  public Code(int id) {
    this.id = "" + id;
    this.number = id;
  }

  /**
   * Retorna
   * 
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * Retorna
   * 
   * @return id
   */
  public int getPosfix() {
    if (number == null) {
      int index = -1;
      for (int n = 0; n < this.id.length(); n++) {
        char c = this.id.charAt(n);
        if (c >= '0' && c <= '9') {
          index = n;
          break;
        }
      }
      if (index == -1) {
        number = -1;
      }
      else {
        number = new Integer(this.id.substring(index));
      }
    }
    return number;
  }

  /**
   * Retorna
   * 
   * @return id
   */
  public String getPrefix() {
    return this.id.substring(0, this.id.length()
      - Integer.valueOf(this.getPosfix()).toString().length());
  }

  /**
   * @param id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Code other = (Code) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    }
    else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return id;
  }

}
