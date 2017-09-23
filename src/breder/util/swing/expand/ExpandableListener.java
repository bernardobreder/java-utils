package breder.util.swing.expand;

import java.util.EventListener;

/**
 * Listener do componente ExpandablePanel
 * 
 * 
 * @author bbreder
 */
public interface ExpandableListener extends EventListener {

  /**
   * Called whenever an item in the tree has been expanded.
   * 
   * @param table
   */
  public void expanded(ExpandablePanel table);

  /**
   * Called whenever an item in the tree has been collapsed.
   * 
   * @param table
   */
  public void collapsed(ExpandablePanel table);

}
