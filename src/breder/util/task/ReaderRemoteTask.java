package breder.util.task;

import java.util.concurrent.locks.Lock;

public abstract class ReaderRemoteTask extends RemoteTask {

  /**
   * Construtor
   * 
   * @param message
   */
  public ReaderRemoteTask(String message) {
    super(message);
  }

  public abstract void lockPerform() throws Throwable;

  @Override
  public final void perform() throws Throwable {
    Lock lock = RemoteTask.lock.readLock();
    try {
      lock.lock();
      this.lockPerform();
    }
    finally {
      lock.unlock();
    }
  }

}
