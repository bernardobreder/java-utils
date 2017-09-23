package breder.util.swing.model;

/**
 * Modelo estático
 * 
 * 
 * @author Bernardo Breder
 * @param <E>
 */
public abstract class StaticObjectModel<E> extends AbstractObjectModel<E> {

  /**
   * Construtor
   * 
   * @param columns
   */
  public StaticObjectModel(String... columns) {
    this(null, columns);
  }

  /**
   * Construtor
   * 
   * @param next
   * @param columns
   */
  public StaticObjectModel(IObjectModel<E> next, String... columns) {
    super(next, columns);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshModel() {
  }

}
