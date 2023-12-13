package org.example.eventloop.base_implement.comfirm_one_thread.core1.demo;


import org.example.eventloop.base_implement.comfirm_one_thread.core1.AbstractExecutor;
import org.example.eventloop.base_implement.comfirm_one_thread.core1.AbstractHandle;

import java.util.concurrent.Executor;

public class CalcHandle extends AbstractHandle {
  public CalcHandle(Executor exe) {
    super(exe);
  }

  @Override
  public void handle(Object o) {
    Number number = (Number) o;
    System.out.println(Thread.currentThread() + " " + number.add());
  }
}
