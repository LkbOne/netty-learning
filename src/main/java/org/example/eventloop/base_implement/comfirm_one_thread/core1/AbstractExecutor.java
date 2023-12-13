package org.example.eventloop.base_implement.comfirm_one_thread.core1;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AbstractExecutor implements Executor {

  private Thread thread;

  private final Executor executor;

  public AbstractExecutor() {
    executor = Executors.newSingleThreadExecutor();
  }

  private boolean isCommonThread(Thread t) {
    return t == thread;
  }

  @Override
  public void execute(final Runnable command) {

    if (isCommonThread(Thread.currentThread())) {
      System.out.println("old thread");
      this.execute0(command);
    } else {
      this.execute0(new Runnable() {
        @Override
        public void run() {
          System.out.println("new thread");
          thread = Thread.currentThread();
          execute(command);
        }
      });
    }
  }

  private void execute0(Runnable command) {
    executor.execute(command);
  }
}
