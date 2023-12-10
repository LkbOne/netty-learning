package org.example.eventloop.base_implement.simplest.core2;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class AbstractHandle implements Handle {

  AbstractHandle successor;

  Executor executor;

  public AbstractHandle() {
    executor = Executors.newSingleThreadExecutor();
  }

  public void setSuccessor(AbstractHandle abstractHandle) {
    this.successor = abstractHandle;
  }

  public AbstractHandle getSuccessor() {
    return successor;
  }
  private void toNext(Object o) {
    if (successor == null) {
      return;
    }
    successor.execute(o);
  }

  public final void execute(Object o) {
    final Object oo = o;
    executor.execute(new Runnable() {
      @Override
      public void run() {
        handle(oo);
        toNext(oo);
      }
    });
  }
}
