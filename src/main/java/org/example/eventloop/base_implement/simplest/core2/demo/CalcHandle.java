package org.example.eventloop.base_implement.simplest.core2.demo;


import org.example.eventloop.base_implement.simplest.core2.AbstractHandle;

public class CalcHandle extends AbstractHandle {
  @Override
  public void handle(Object o) {
    Number number = (Number) o;
    System.out.println(Thread.currentThread() + " " + number.add());
  }
}
