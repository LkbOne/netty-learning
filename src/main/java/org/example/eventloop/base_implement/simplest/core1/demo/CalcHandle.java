package org.example.eventloop.base_implement.simplest.core1.demo;

import org.example.eventloop.base_implement.simplest.core1.AbstractHandle;

public class CalcHandle extends AbstractHandle {


  public CalcHandle() {
    super();
  }

  @Override
  public void handle(Object o) {
    Number num = (Number) o;
    System.out.println(Thread.currentThread() + " " + num.add());
  }
}
