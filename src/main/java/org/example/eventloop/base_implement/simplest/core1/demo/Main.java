package org.example.eventloop.base_implement.simplest.core1.demo;

import org.example.eventloop.base_implement.simplest.core1.HandleChain;

public class Main {

  public static void main(String[] args) {
    HandleChain handleChain = new HandleChain();
    handleChain.addHandle(new CalcHandle());
    handleChain.addHandle(new CalcHandle());
    handleChain.addHandle(new CalcHandle());
    handleChain.addHandle(new CalcHandle());

    handleChain.handle(new Number());
  }
}
