package breder.util.swing.tree;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import breder.util.swing.BFrame;

public class BTestTree {

  public static void main(String[] args) {
    final BFrame frame = new BFrame(null);
    final TestTree tree = new TestTree();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        frame.setSize(800, 600);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(tree), BorderLayout.CENTER);
        frame.setVisible(true);
      }
    });
    new Thread() {
      public void run() {
        while (!frame.isClosed()) {
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              tree.getModel().refresh();
            }
          });
          try {
            Thread.sleep(200);
          }
          catch (InterruptedException e) {
          }
        }
      }
    }.start();
  }

  public static class TestTree extends BTree {

    public TestTree() {
      super(new BTreeModel(new TestTreeNode(null, null)));
    }

  }

  public static class TestTreeNode extends RemoteTreeNode {

    private File file;

    public TestTreeNode(AbstractTreeNode parent, File file) {
      super(parent);
      this.file = file;
    }

    @Override
    public AbstractTreeNode[] getRemoteChildren() throws Exception {
      List<AbstractTreeNode> list = new ArrayList<AbstractTreeNode>();
      File[] files;
      if (file == null) {
        files = File.listRoots();
      }
      else {
        files = file.listFiles();
      }
      for (File file : files) {
        if (file.isFile()) {
          list.add(new GenericFileTreeNode(this, file));
        }
        else {
          list.add(new GenericDirectoryTreeNode(this, file));
        }
      }
      return list.toArray(new AbstractTreeNode[0]);
    }

    @Override
    public String toString() {
      return "" + this.hashCode();
    }

  }

}
