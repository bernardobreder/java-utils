package breder.util.swing.tree;

import java.io.File;

public class GenericFileTreeNode extends FileTreeNode {

  private final File file;

  public GenericFileTreeNode(AbstractTreeNode parent, File file) {
    super(parent);
    this.file = file;
  }

  public File getFile() {
    return file;
  }

  @Override
  public String toString() {
    return this.file.getName();
  }

}
