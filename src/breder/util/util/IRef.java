package breder.util.util;

public interface IRef<E> {

  public E getValue() throws Exception;

  public void setValue(E value);

  public void start();

}
