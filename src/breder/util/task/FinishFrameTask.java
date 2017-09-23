package breder.util.task;

import java.awt.Window;

import javax.swing.JFrame;

import breder.util.swing.BFrame;

/**
 * Tarefa de fechar a janela
 * 
 * 
 * @author Bernardo Breder
 */
public class FinishFrameTask extends LocalTask {

  /** Janela */
  private BFrame frame;
  /** Janela */
  private JFrame jframe;
  /** Janela */
  private Window window;

  /**
   * Construtor
   * 
   * @param frame
   */
  public FinishFrameTask(BFrame frame) {
    super();
    this.frame = frame;
  }

  /**
   * Construtor
   * 
   * @param window
   */
  public FinishFrameTask(Window window) {
    super();
    this.window = window;
  }

  /**
   * Construtor
   * 
   * @param frame
   */
  public FinishFrameTask(JFrame frame) {
    super();
    this.jframe = frame;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateUI() {
    if (this.jframe != null) {
      this.jframe.dispose();
    }
    else if (window != null) {
      window.dispose();
    }
    else if (frame != null) {
      this.frame.close();
    }
  }

}
