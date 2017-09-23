package breder.util.task;

import java.awt.GridBagLayout;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import breder.util.log.storage.ErrorLog;
import breder.util.log.storage.LogModel;
import breder.util.swing.GBC;
import breder.util.task.EventTask.EventTaskReturn;
import breder.util.util.ExceptionUtil;

/**
 * Implementação de uma tarefa de grande processamento.
 * 
 */
public abstract class RemoteTask extends LocalTask implements IRemoteTask {

  /** Tempo de espera */
  private static final long TIMEOUT_FOR_PROCESS_BAR = 100;
  /** Locks */
  protected static final ReadWriteLock lock = new ReentrantReadWriteLock();
  /** Thread a ser criado */
  private Thread thread;
  /** Janela de Progresso */
  private JFrame processFrame;
  /** Mensagem */
  private String message;

  /**
   * Construtor padrão
   * 
   * @param message
   * @param components
   */
  public RemoteTask(String message) {
    this.message = message;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public abstract void perform() throws Throwable;

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    try {
      Boolean flag = EventTask.invokeAndWait(new EventTaskReturn<Boolean>() {
        @Override
        public Boolean run() {
          return preAction();
        }
      });
      if (flag) {
        try {
          try {
            this.perform();
          }
          finally {
            disposeFrame();
          }
          EventTask.invokeLater(new Runnable() {
            @Override
            public void run() {
              updateUI();
            }
          });
        }
        catch (final Throwable e) {
          EventTask.invokeLater(new Runnable() {
            @Override
            public void run() {
              handler(e);
            }
          });
        }
      }
    }
    finally {
      EventTask.invokeLater(new Runnable() {
        @Override
        public void run() {
          finalling();
        }
      });
    }
  }

  private synchronized void tryOpenFrame() {
    if (this.isShowProcess() && this.thread.isAlive()) {
      EventTask.invokeLater(new Runnable() {
        @Override
        public void run() {
          JFrame frame = new JFrame();
          frame.setTitle("");
          JProgressBar bar = new JProgressBar();
          bar.setIndeterminate(true);
          JPanel panel = new JPanel(new GridBagLayout());
          panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));
          panel.add(new JLabel(message, JLabel.CENTER), new GBC(0, 0).both()
            .top());
          panel.add(bar, new GBC(0, 1).horizontal());
          frame.add(panel);
          frame.setResizable(false);
          frame.pack();
          frame.setLocationRelativeTo(null);
          frame.setVisible(true);
          frame.requestFocusInWindow();
          processFrame = frame;
        }
      });
    }
  }

  private synchronized void disposeFrame() {
    if (this.isShowProcess() && processFrame != null) {
      EventTask.invokeLater(new Runnable() {
        @Override
        public void run() {
          if (processFrame != null) {
            processFrame.dispose();
          }
        }
      });
    }
  }

  /**
   * Acao de erro ocorrido na executacao da tarefa. Nesse m�todo � necess�rio
   * mostrar para o usu�rio o que aconteceu.
   * 
   * @param t erro ocorrido
   */
  public void handler(Throwable t) {
    t.printStackTrace();
    LogModel.getInstance().add(new ErrorLog(t));
    JOptionPane.showMessageDialog(null, ExceptionUtil.buildMessage(t));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void interrupt() {
    thread.interrupt();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInterrupted() {
    return thread.isInterrupted();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ITask start() {
    thread = new Thread(new Runnable() {
      @Override
      public void run() {
        RemoteTask.this.run();
      }
    }, this.getClass().getSimpleName());
    thread.start();
    if (this.isShowProcess()) {
      try {
        thread.join(TIMEOUT_FOR_PROCESS_BAR);
      }
      catch (InterruptedException e) {
      }
      this.tryOpenFrame();
    }
    return this;
  }

  public boolean isShowProcess() {
    return this.message != null;
  }

  /**
   * Testador
   * 
   * @param args
   */
  public static void main(String[] args) {
    new RemoteTask("Carregando...") {

      @Override
      public void updateUI() {
      }

      @Override
      public void perform() throws Throwable {
        Thread.sleep(10000);
      }

    }.start();
  }

}
