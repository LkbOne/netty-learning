package org.example.thread.pools;

import org.example.thread.utils.UseTime;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用于使用池化技术。比较单单实用线程与线程池的耗时比较
 */
public class ThreadAndThreadPoolCompare {

  static class UseThread extends UseTime {
    @Override
    protected void calcUnit() {
      for (int i = 0; i < 1000; i++) {
        final int finalI = i;
        new Thread(new Runnable() {
          @Override
          public void run() {
            System.out.println(finalI);
          }
        }).start();
      }
    }
  }

  static class UseThreadPool extends UseTime {

//    - 组件是：自定义ThreadPoolExecutor。而不是直接使用ExecutorService pool = Executors.newSingleThreadExecutor()因为内部是一个无限大的阻塞队列，会使任务放在队列中，无法使主线程计算较正确的完成时间;
//    - 核心线程与最大线程数相同都是：1
//    - 队列选择了同步队列
//    - 抛弃策略：自定义了抛弃策略，保证不抛弃任务且往同步队列添加，保证线性的执行.

    //     ExecutorService pool = Executors.newSingleThreadExecutor();
    ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS,
        new SynchronousQueue<Runnable>(), new RejectedExecutionHandler() {
      @Override
      public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
          executor.getQueue().put(r);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    });

    @Override
    protected void calcUnit() {
      for (int i = 0; i < 1000; i++) {
        final int finalI = i;
        pool.execute(new Runnable() {
          @Override
          public void run() {
            System.out.println(finalI);
          }
        });
      }
    }
  }

  public static void main(String[] args) {
    UseThread useThread = new UseThread();
    long useThreadTime = useThread.useTime();

    UseThreadPool useThreadPool = new UseThreadPool();
    long useThreadPoolTime = useThreadPool.useTime();
    System.out.println("新建线程的耗时：" + useThreadTime + " ms");
    System.out.println("线程池耗时：" + useThreadPoolTime + " ms");
  }
}
