package breder.util.log.storage;

import breder.util.swing.BFrame;
import breder.util.task.LocalTask;

/**
 * Finaliza a janela
 * 
 * 
 * @author bbreder
 */
public class LogFrameTask extends LocalTask {

  /** Frame */
  private BFrame frame;

  /**
   * Construtor
   * 
   * @param frame
   */
  public LogFrameTask(BFrame frame) {
    this.frame = frame;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateUI() {
    new LogFrame(frame).setVisible(true);
  }

}
