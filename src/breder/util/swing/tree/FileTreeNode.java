package breder.util.swing.tree;

/**
 * Nó folha
 * 
 * 
 * @author bbreder
 */
public abstract class FileTreeNode extends AbstractTreeNode {

  /**
   * Construtor
   * 
   * @param parent
   */
  public FileTreeNode(AbstractTreeNode parent) {
    super(parent);
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

}
