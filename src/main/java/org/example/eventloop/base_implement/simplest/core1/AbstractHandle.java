package org.example.eventloop.base_implement.simplest.core1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractHandle implements Handle {

  ExecutorService executor;

  public AbstractHandle() {
    executor = Executors.newFixedThreadPool(1);
  }

  public void execute(Object o) {
    final Object oo = o;
    executor.submit(new Runnable() {
      @Override
      public void run() {
        handle(oo);
      }
    });
  }
}
