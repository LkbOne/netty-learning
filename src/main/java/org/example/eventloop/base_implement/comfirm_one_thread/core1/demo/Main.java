package org.example.eventloop.base_implement.comfirm_one_thread.core1.demo;


import org.example.eventloop.base_implement.comfirm_one_thread.core1.AbstractExecutor;
import org.example.eventloop.base_implement.comfirm_one_thread.core1.HandleChain;

public class Main {

  public static void main(String[] args) {
    HandleChain handleChain = new HandleChain();
    AbstractExecutor ll = new AbstractExecutor();
    AbstractExecutor tt = new AbstractExecutor();
    for (int i = 0; i < 100; i++) {
      if (i < 10) {
        handleChain.addHandle(new CalcHandle(ll));
      } else {
        handleChain.addHandle(new CalcHandle(tt));
      }
    }

    handleChain.handle(new Number());
  }
}
