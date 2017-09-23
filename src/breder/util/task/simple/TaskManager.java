package breder.util.task.simple;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Gerente de tarefas
 * 
 * 
 * @author bbreder
 */
public class TaskManager {

  /** Instancia unica */
  private static final TaskManager instance = new TaskManager();
  /** Armazenador de locks */
  private ReadWriteLock rwlock = new ReentrantReadWriteLock();
  /** Lock de leitura */
  private Lock rlock = rwlock.readLock();
  /** Lock de leitura */
  private Lock wlock = rwlock.writeLock();

  /**
   * Construtor padrão
   */
  private TaskManager() {
  }

  /**
   * Inicia uma tarefa como leitura
   * 
   * @param task
   */
  public void startRead(SimpleTask task) {
    rlock.lock();
    try {
      task.start();
    }
    finally {
      rlock.unlock();
    }
  }

  /**
   * Inicia uma tarefa como escrita
   * 
   * @param task
   */
  public void startWrite(SimpleTask task) {
    wlock.lock();
    try {
      task.start();
    }
    finally {
      wlock.unlock();
    }
  }

  /**
   * Retorna
   * 
   * @return instance
   */
  public static TaskManager getInstance() {
    return instance;
  }

}
