package breder.util.swing;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class GroupValidator {

  private final List<ComponentValidable> validables =
    new ArrayList<ComponentValidable>();

  private final List<Component> components = new ArrayList<Component>();

  public void addValidator(ComponentValidable validable) {
    this.validables.add(validable);
    this.fireValidator();
  }

  public void addComponent(Component e) {
    components.add(e);
    this.fireValidator();
  }

  public void fireValidator() {
    boolean result = true;
    for (ComponentValidable validator : this.validables) {
      result &= validator.fireValidator();
    }
    for (Component component : components) {
      component.setEnabled(result);
    }
  }

}
