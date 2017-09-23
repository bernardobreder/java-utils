package breder.util.swing;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public abstract class BComponent {

  private List<Validator> validators;

  private BParentValidator parentValidator;

  private Component component;

  public void init() {
    this.setComponent(this.buildComponent());
  }

  protected abstract Component buildComponent();

  public Component getComponent() {
    return component;
  }

  public void setComponent(Component component) {
    this.component = component;
  }

  public BParentValidator getParentValidator() {
    return parentValidator;
  }

  public void setParentValidator(BParentValidator parentValidator) {
    this.parentValidator = parentValidator;
  }

  public void addValidator(Validator validator) {
    this.getValidators().add(validator);
  }

  public abstract boolean fireValidator();

  public void fireParentValidator() {
    if (this.parentValidator != null) {
      this.parentValidator.fireValidator();
    }
  }

  public List<Validator> getValidators() {
    if (validators == null) {
      this.validators = new ArrayList<Validator>();
    }
    return this.validators;
  }

}
