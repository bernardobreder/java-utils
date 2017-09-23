package breder.util.net.ws.shared;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Requisição http
 * 
 * 
 * @author Bernardo Breder
 */
public class WSRequest implements Serializable {

  /** serialVersionUID */
  private static final long serialVersionUID = 0L;
  /** Nome da classe */
  private final String classname;
  /** Nome da método */
  private final String methodname;
  /** Parametros */
  private final Class<?>[] classParameters;
  /** Parametros */
  private final Object[] objectParameters;
  /** Resultado */
  private Object result;
  /** Hashcode */
  private transient Integer hashCode;

  /**
   * @param classname
   * @param methodname
   * @param classParameters
   * @param objectParameters
   */
  public WSRequest(String classname, String methodname,
    Class<?>[] classParameters, Object[] objectParameters) {
    super();
    this.classname = classname;
    this.methodname = methodname;
    this.classParameters = classParameters;
    this.objectParameters = objectParameters;
  }

  /**
   * Retorna
   * 
   * @return classname
   */
  public String getClassname() {
    return classname;
  }

  /**
   * Retorna
   * 
   * @return methodname
   */
  public String getMethodname() {
    return methodname;
  }

  /**
   * Retorna
   * 
   * @return classParameters
   */
  public Class<?>[] getClassParameters() {
    return classParameters;
  }

  /**
   * Retorna
   * 
   * @return objectParameters
   */
  public Object[] getObjectParameters() {
    return objectParameters;
  }

  /**
   * Retorna
   * 
   * @return result
   */
  public Object getResult() {
    return result;
  }

  /**
   * @param result
   */
  public void setResult(Object result) {
    this.result = result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    if (hashCode == null) {
      final int prime = 31;
      hashCode = 1;
      hashCode = prime * hashCode + Arrays.hashCode(classParameters);
      hashCode =
        prime * hashCode + ((classname == null) ? 0 : classname.hashCode());
      hashCode =
        prime * hashCode + ((methodname == null) ? 0 : methodname.hashCode());
      hashCode = prime * hashCode + Arrays.hashCode(objectParameters);
    }
    return hashCode;
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
    WSRequest other = (WSRequest) obj;
    if (!Arrays.equals(classParameters, other.classParameters)) {
      return false;
    }
    if (classname == null) {
      if (other.classname != null) {
        return false;
      }
    }
    else if (!classname.equals(other.classname)) {
      return false;
    }
    if (methodname == null) {
      if (other.methodname != null) {
        return false;
      }
    }
    else if (!methodname.equals(other.methodname)) {
      return false;
    }
    if (!Arrays.equals(objectParameters, other.objectParameters)) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "WebServiceRequest [classname=" + classname + ", methodname="
      + methodname + "]";
  }

}
