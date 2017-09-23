package breder.util.util;

public class RemoteRef<E> implements IRef<E> {

  private E value;

  private RemoteTypedRunnable<E> task;

  private Thread thread;

  private Exception exception;

  public RemoteRef(final RemoteTypedRunnable<E> task) {
    this.task = task;
  }

  @Override
  public synchronized void start() {
    this.thread = new Thread(this.getClass().getSimpleName()) {
      @Override
      public void run() {
        try {
          value = task.run();
        }
        catch (Exception e) {
          exception = e;
        }
      }
    };
    this.thread.start();
  }

  public synchronized E getValue() throws Exception {
    if (this.value == null) {
      if (this.thread == null) {
        this.start();
      }
      try {
        this.thread.join();
      }
      catch (InterruptedException e) {
      }
      if (this.exception != null) {
        throw this.exception;
      }
    }
    return this.value;
  }

  public synchronized void setValue(E value) {
    if (thread.isAlive()) {
      throw new IllegalStateException("thread alredy running");
    }
    this.value = value;
  }

}
