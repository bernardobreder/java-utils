package breder.util.swing.tree;

/**
 * Nó de diretorio
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class DirectoryTreeNode extends AbstractTreeNode {

  /**
   * Construtor padrão
   * 
   * @param parent
   */
  public DirectoryTreeNode(AbstractTreeNode parent) {
    super(parent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getAllowsChildren() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLeaf() {
    return false;
  }

}
