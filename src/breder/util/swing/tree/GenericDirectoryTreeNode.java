package breder.util.swing.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GenericDirectoryTreeNode extends RemoteTreeNode {

  private final File dir;

  public GenericDirectoryTreeNode(AbstractTreeNode parent, File dir) {
    super(parent);
    this.setModel(parent.getModel());
    this.dir = dir;
  }

  @Override
  public AbstractTreeNode[] getRemoteChildren() {
    List<AbstractTreeNode> nodes = new ArrayList<AbstractTreeNode>();
    for (File file : dir.listFiles()) {
      if (!(file.isHidden() || file.getName().startsWith("."))) {
        if (file.isDirectory()) {
          nodes.add(new GenericDirectoryTreeNode(this, file));
        }
        else {
          nodes.add(new GenericFileTreeNode(this, file));
        }
      }
    }
    return nodes.toArray(new AbstractTreeNode[0]);
  }

  @Override
  public String toString() {
    return dir.getName();
  }

  public File getDir() {
    return dir;
  }

}
