package org.example.eventloop.base_implement.comfirm_one_thread.core1;


import java.util.concurrent.Executor;

public abstract class AbstractHandle implements Handle {

  AbstractHandle successor;

  Executor executor;

  public AbstractHandle(Executor exe) {
    executor = exe;
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
