package breder.util.util;

public class LocalRef<E> implements IRef<E> {

  private E value;

  public LocalRef(E value) {
    this.setValue(value);
  }

  @Override
  public E getValue() throws Exception {
    return value;
  }

  @Override
  public void setValue(E value) {
    this.value = value;
  }

  @Override
  public void start() {
  }

}
