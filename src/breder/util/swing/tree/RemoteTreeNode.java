package breder.util.swing.tree;

import javax.swing.SwingUtilities;

/**
 * Nó que realiza uma consulta remota
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class RemoteTreeNode extends DirectoryTreeNode {

  /** Resultado da consulta */
  private AbstractTreeNode[] result;
  /** Thread de consulta */
  private MyThread thread;

  /**
   * Construtor
   * 
   * @param parent
   */
  public RemoteTreeNode(AbstractTreeNode parent) {
    super(parent);
  }

  /**
   * Realiza a consulta
   * 
   * @return consulta
   * @throws Exception
   */
  public abstract AbstractTreeNode[] getRemoteChildren() throws Exception;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isChanged() {
    AbstractTreeNode[] elements;
    try {
      elements = this.getRemoteChildren();
    }
    catch (Exception e) {
      return false;
    }
    if (this.result == null) {
      return true;
    }
    if (elements.length != this.result.length) {
      return true;
    }
    for (int n = 0; n < elements.length; n++) {
      AbstractTreeNode newNode = elements[n];
      AbstractTreeNode oldNode = this.result[n];
      if (!newNode.toString().equals(oldNode.toString())) {
        return true;
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset() {
    this.result = null;
    super.reset();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractTreeNode[] getChildren() {
    if (this.result == null) {
      if (this.thread == null) {
        this.thread = new MyThread();
        this.thread.start();
      }
      return new AbstractTreeNode[0];
    }
    else {
      return this.result;
    }
  }

  /**
   * Erro de consulta
   * 
   * @param e
   */
  public void handler(Exception e) {
    e.printStackTrace();
  }

  /**
   * Thread de consulta
   * 
   * 
   * @author Bernardo Breder
   */
  private class MyThread extends Thread {
    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
      try {
        result = getRemoteChildren();
      }
      catch (Exception e) {
        handler(e);
      }
      cache = result;
      thread = null;
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          getModel().nodeStructureChanged(RemoteTreeNode.this);
        }
      });
    }
  }

}
