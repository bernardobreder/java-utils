package breder.util.swing.model;

import breder.util.task.RemoteTask;

public abstract class RemoteObjectModel<E> extends StaticObjectModel<E> {

  private E[] elements;

  public RemoteObjectModel(String... columns) {
    super(null, columns);
  }

  public abstract E[] refreshModelRemote() throws Exception;

  @Override
  public E getRow(int index) {
    return this.elements[index];
  }

  public int getSize() {
    if (this.elements == null) {
      return 0;
    }
    return this.elements.length;
  }

  @Override
  public void refreshModel() {
    // this.elements = null;
    new RefreshTask().start();
  }

  public E[] getElements() {
    return elements;
  }

  public void setElements(E[] elements) {
    this.elements = elements;
  }

  private class RefreshTask extends RemoteTask {

    public RefreshTask() {
      super(null);
    }

    private E[] elements;

    @Override
    public void perform() throws Throwable {
      this.elements = refreshModelRemote();
    }

    @Override
    public void updateUI() {
      RemoteObjectModel.this.elements = elements;
      RemoteObjectModel.this.refreshParents();
    }

  }

}
