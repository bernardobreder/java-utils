package breder.util.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

public class BFocusTraversalPolicy extends FocusTraversalPolicy {

  private final List<Component> components = new ArrayList<Component>();

  @Override
  public Component getComponentAfter(Container aContainer, Component aComponent) {
    int index = this.components.indexOf(aComponent);
    if (index == this.components.size() - 1) {
      return null;
    }
    else {
      return this.components.get(index + 1);
    }
  }

  @Override
  public Component getComponentBefore(Container aContainer, Component aComponent) {
    int index = this.components.indexOf(aComponent);
    if (index == 0) {
      return null;
    }
    else {
      return this.components.get(index - 1);
    }
  }

  @Override
  public Component getDefaultComponent(Container aContainer) {
    return this.components.get(0);
  }

  @Override
  public Component getFirstComponent(Container aContainer) {
    return this.components.get(0);
  }

  @Override
  public Component getLastComponent(Container aContainer) {
    return this.components.get(this.components.size() - 1);
  }

  @Override
  public Component getInitialComponent(Window window) {
    return this.components.get(0);
  }

  public boolean add(Component e) {
    return components.add(e);
  }

}
