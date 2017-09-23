package breder.util.swing.tree;

import javax.swing.tree.DefaultTreeModel;

public class BTreeModel extends DefaultTreeModel {

  public BTreeModel(ITreeNode root) {
    super(root);
    root.setModel(this);
  }

  @Override
  public ITreeNode getRoot() {
    return (ITreeNode) super.getRoot();
  }

  public void refresh() {
    ITreeNode root = this.getRoot();
    root.reset(this);
  }

}
