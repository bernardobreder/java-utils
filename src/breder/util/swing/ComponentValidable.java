package breder.util.swing;

public interface ComponentValidable {

  public void addValidator(Validator validator);

  public boolean fireValidator();

}
