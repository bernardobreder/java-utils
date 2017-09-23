package breder.util.swing.tree;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import breder.util.util.ClassUtil;

/**
 * Nó simples
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class AbstractTreeNode implements ITreeNode {

  /** Nó pai */
  private final AbstractTreeNode parent;
  /** Cache do nó */
  protected AbstractTreeNode[] cache;
  /** Modelo */
  private BTreeModel model;

  /**
   * Construtor
   * 
   * @param parent
   */
  public AbstractTreeNode(AbstractTreeNode parent) {
    super();
    this.parent = parent;
    if (this.parent != null) {
      this.model = parent.getModel();
    }
  }

  /**
   * Mudança no conteudo do nó
   */
  public void fireDataChange() {
    this.getModel().nodeChanged(this);
  }

  /**
   * Mudança no conteudo do nó
   */
  public void fireStructureChange() {
    this.getModel().nodeStructureChanged(this);
  }

  /**
   * Retorna os filhos
   * 
   * @return filhos
   */
  public abstract AbstractTreeNode[] getChildren();

  /**
   * {@inheritDoc}
   */
  @Override
  public Icon getIcon() {
    return null;
  }

  /**
   * Indica se houve mudança
   * 
   * @return mudança ?
   */
  public boolean isChanged() {
    AbstractTreeNode[] elements = this.getChildren();
    if (elements.length != this.cache.length) {
      return true;
    }
    for (int n = 0; n < elements.length; n++) {
      AbstractTreeNode newNode = elements[n];
      AbstractTreeNode oldNode = this.cache[n];
      if (!newNode.toString().equals(oldNode.toString())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Limpa o cache
   */
  public void reset() {
    this.cache = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset(DefaultTreeModel model) {
    if (this.isChanged()) {
      this.reset();
      model.nodeStructureChanged(this);
    }
    for (AbstractTreeNode node : this.getCache()) {
      if (node.cache != null) {
        node.reset(model);
      }
    }
  }

  /**
   * Retorna o nome
   * 
   * @return nome
   */
  public String getName() {
    return this.toString();
  }

  /**
   * Retorna o cache
   * 
   * @return cache
   */
  protected AbstractTreeNode[] getCache() {
    if (cache == null) {
      this.cache = this.getChildren();
    }
    return this.cache;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Enumeration<?> children() {
    return Collections.enumeration(Arrays.asList(this.getCache()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getAllowsChildren() {
    return getChildCount() != 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TreeNode getChildAt(int childIndex) {
    return this.getCache()[childIndex];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getChildCount() {
    return this.getCache().length;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getIndex(TreeNode node) {
    return Arrays.asList(this.getCache()).indexOf(node);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractTreeNode getParent() {
    return parent;
  }

  /**
   * Retorna pai
   * 
   * @param <E>
   * @param c
   * @return pai
   */
  public <E extends AbstractTreeNode> E getParent(Class<E> c) {
    AbstractTreeNode node = this;
    while (node != null) {
      if (ClassUtil.isInstanceof(node.getClass(), c)) {
        return c.cast(node);
      }
      node = node.getParent();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLeaf() {
    return !getAllowsChildren();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setModel(BTreeModel model) {
    this.model = model;
  }

  /**
   * Retorna o menu
   * 
   * @return menu
   */
  public JPopupMenu getPopupMenu() {
    return null;
  }

  /**
   * Retorna a acao
   * 
   * @return menu
   */
  public AbstractAction getOpenAction() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BTreeModel getModel() {
    return model;
  }

  /**
   * Texto a ser apresentado {@inheritDoc}
   */
  @Override
  public abstract String toString();

}
