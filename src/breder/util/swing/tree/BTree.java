package breder.util.swing.tree;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import breder.util.swing.table.IOpenCellListener;

public class BTree extends JTree {

  private List<IOpenCellListener<AbstractTreeNode>> listeners =
    new ArrayList<IOpenCellListener<AbstractTreeNode>>();

  public BTree(DirectoryTreeNode root) {
    this(new BTreeModel(root));
  }

  public BTree(BTreeModel model) {
    super(model);
    this.setRootVisible(false);
    this.addKeyListener(new Key());
    this.addMouseListener(new Mouse());
    this.setCellRenderer(new CellRenderer());
    this.add(new OpenCellListener() {
    });
  }

  /**
   * Atualiza a interface grafica
   */
  public void refresh() {
    this.getModel().refresh();
    this.validate();
    this.invalidate();
  }

  /**
   * Retorna o modelo {@inheritDoc}
   */
  @Override
  public BTreeModel getModel() {
    return (BTreeModel) super.getModel();
  }

  public boolean add(IOpenCellListener<AbstractTreeNode> e) {
    return listeners.add(e);
  }

  public DirectoryTreeNode getNode() {
    return (DirectoryTreeNode) this.getModel().getRoot();
  }

  public AbstractTreeNode getSelectNode() {
    AbstractTreeNode[] rows = this.getSelectNodes();
    if (rows.length != 0) {
      return rows[0];
    }
    else {
      return null;
    }
  }

  public AbstractTreeNode[] getSelectNodes() {
    List<AbstractTreeNode> list = new ArrayList<AbstractTreeNode>();
    if (this.getSelectionPaths() == null) {
      return new AbstractTreeNode[0];
    }
    for (TreePath path : this.getSelectionPaths()) {
      list.add((AbstractTreeNode) path.getLastPathComponent());
    }
    return list.toArray(new AbstractTreeNode[0]);
  }

  public void fireOpenCellListener() {
    for (AbstractTreeNode node : this.getSelectNodes()) {
      for (IOpenCellListener<AbstractTreeNode> listener : this.listeners) {
        listener.actionPerformed(-1, node);
      }
    }
  }

  public void firePopupCellListener(int x, int y) {
    for (AbstractTreeNode node : this.getSelectNodes()) {
      for (IOpenCellListener<AbstractTreeNode> listener : this.listeners) {
        JPopupMenu menu = listener.getPopupMenu(-1, node);
        if (menu != null) {
          menu.show(this, x, y);
        }
      }
    }
  }

  private class Key extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        fireOpenCellListener();
      }
      else if (e.getKeyCode() == KeyEvent.VK_F5) {
        getModel().refresh();
      }
    }

  }

  private class Mouse extends MouseAdapter {

    @Override
    public void mousePressed(MouseEvent e) {
      if (e.getButton() == MouseEvent.BUTTON1) {
        if (e.getClickCount() > 1) {
          fireOpenCellListener();
        }
      }
      else if (e.getButton() == MouseEvent.BUTTON3) {
        firePopupCellListener(e.getX(), e.getY());
      }
    }
  }

  /**
   * Classe de renderizador
   * 
   * 
   * @author bbreder
   */
  private static class CellRenderer extends DefaultTreeCellRenderer {

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
      boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      JLabel label =
        (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded,
          leaf, row, hasFocus);
      AbstractTreeNode node = (AbstractTreeNode) value;
      label.setIcon(node.getIcon());
      return label;
    }

  }

  /**
   * Acao de clicar
   * 
   * 
   * @author bbreder
   */
  private static class OpenCellListener implements
    IOpenCellListener<AbstractTreeNode> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(int row, AbstractTreeNode cell) {
      AbstractAction action = cell.getOpenAction();
      if (action != null) {
        action.actionPerformed(null);
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JPopupMenu getPopupMenu(int row, AbstractTreeNode cell) {
      return cell.getPopupMenu();
    }

  }

}
