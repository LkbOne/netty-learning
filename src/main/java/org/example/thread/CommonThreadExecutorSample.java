package org.example.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CommonThreadExecutorSample implements Executor {


  Thread thread;

  ExecutorService executorService;


  CommonThreadExecutorSample executor;


  CommonThreadExecutorSample() {
    executorService = Executors.newFixedThreadPool(1);
    executor = this;
  }

  private boolean isCommonThread(Thread thread) {
    return thread == this.thread;
  }

  public void execute(final Thread t) {
    if (isCommonThread(t)) {
      execute(new Runnable() {
        @Override
        public void run() {
          System.out.println("old threads");
        }
      });
    } else {
      executorService.execute(new Runnable() {
        @Override
        public void run() {
          thread = Thread.currentThread();
          System.out.println("new thread");
          executor.execute(thread);
        }
      });
    }
  }


  @Override
  public void execute(Runnable command) {
    executorService.execute(command);
  }

  public static void main(String[] args) throws InterruptedException {
    CommonThreadExecutorSample commonThreadExecutor = new CommonThreadExecutorSample();
    commonThreadExecutor.execute(Thread.currentThread());
  }
}
