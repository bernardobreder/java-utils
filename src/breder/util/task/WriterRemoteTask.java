package breder.util.task;

import java.util.concurrent.locks.Lock;

public abstract class WriterRemoteTask extends RemoteTask {

  /**
   * Carregando
   * 
   * @param message
   */
  public WriterRemoteTask(String message) {
    super(message);
  }

  public abstract void lockPerform() throws Throwable;

  @Override
  public final void perform() throws Throwable {
    Lock lock = RemoteTask.lock.writeLock();
    try {
      lock.lock();
      this.lockPerform();
    }
    finally {
      lock.unlock();
    }
  }

}
