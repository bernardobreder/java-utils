package breder.util.swing.tree;

import javax.swing.Icon;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public interface ITreeNode extends TreeNode {

  public void reset(DefaultTreeModel model);

  public void setModel(BTreeModel model);

  public Icon getIcon();

  public BTreeModel getModel();

}
