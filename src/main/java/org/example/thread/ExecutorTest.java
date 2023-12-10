package org.example.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ExecutorTest implements Executor {

  ReentrantLock lock = new ReentrantLock();

  Condition notFull = lock.newCondition();
  Condition notEmpty = lock.newCondition();

  class Task {
    int i = 0;

    Task(int i) {
      this.i = i;
    }

    public void print() {
      System.out.println(this.i);
    }
  }

  BlockingQueue<Task> tasks = new ArrayBlockingQueue<>(10);

  class TestRunnable implements Runnable {

    @Override
    public void run() {
      try {
        while(true) {
          lock.lock();
          while(tasks.size() == 0){
            notEmpty.await();
          }
//          if(tasks.size() != 0) {
//            Task take = tasks.take();
//            take.print();
//          }
          Task take = tasks.take();
          take.print();
          lock.unlock();
        }
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  List<Thread> pools = new ArrayList<>();
  private void init() {
    for(int i = 0; i < 100; i++) {
      pools.add(new Thread(new TestRunnable()));
    }
  }

  int i = 0;
  private void init2(int max){
    for(; i < max; i++){
      lock.lock();
      tasks.add(new Task(i));
      notEmpty.signal();
      lock.unlock();
    }
  }

  @Override
  public void execute(Runnable command) {
    new Thread(command).start();
  }

    public void tmpExecute() {
      execute(new Runnable() {
        @Override
        public void run() {
          for (Thread pool : pools) {
            pool.start();
          }
        }
      });
    }

  public static void main(String[] args) throws InterruptedException {
    ExecutorTest executorTest = new ExecutorTest();
    executorTest.init();
    executorTest.tmpExecute();
    executorTest.init2(5);
    Thread.sleep(1000);
    executorTest.init2(10);
    Thread.sleep(1000);
    executorTest.init2(15);
  }
}
