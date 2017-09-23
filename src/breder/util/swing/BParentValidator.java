package breder.util.swing;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class BParentValidator {

  private List<Component> rootComponents;

  private List<BComponent> childComponents;

  private List<Component> getRootComponents() {
    if (rootComponents == null) {
      this.rootComponents = new ArrayList<Component>();
    }
    return rootComponents;
  }

  private List<BComponent> getChildComponents() {
    if (childComponents == null) {
      this.childComponents = new ArrayList<BComponent>();
    }
    return childComponents;
  }

  public void addComponent(Component component) {
    this.getRootComponents().add(component);
    this.fireValidator();
  }

  public void addValidable(BComponent component) {
    this.getChildComponents().add(component);
    component.setParentValidator(this);
    this.fireValidator();
  }

  public void fireValidator() {
    boolean result = true;
    for (BComponent validator : this.getChildComponents()) {
      result &= validator.fireValidator();
    }
    for (Component component : this.getRootComponents()) {
      component.setEnabled(result);
    }
  }

}
