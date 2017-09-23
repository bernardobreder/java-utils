package breder.util.swing.model;

import breder.util.swing.tree.IListener;

public interface IModel {

  public void addListener(IListener listener);

  public void removeListener(IListener listener);

  public void fireDataModelChanged();

  public void fireColumnModelChanged();

  public void refreshModel();

}
