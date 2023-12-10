package org.example.eventloop.base_implement.simplest.core3.demo;

import org.example.eventloop.base_implement.simplest.core3.HandleChain;

public class Main {

  public static void main(String[] args) {
    HandleChain handleChain = new HandleChain();
    for(int i = 0; i < 1000; i++) {
      handleChain.addHandle(new CalcHandle());
    }
    handleChain.handle(new Number());
  }
}
