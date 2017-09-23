package breder.util.swing.tree;

public class SimpleAbstractTreeNode extends AbstractTreeNode {

  private final Object object;

  public SimpleAbstractTreeNode(AbstractTreeNode parent, Object object) {
    super(parent);
    this.object = object;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractTreeNode[] getChildren() {
    return new AbstractTreeNode[0];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getAllowsChildren() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLeaf() {
    return true;
  }

  @Override
  public String toString() {
    return object.toString();
  }

}
