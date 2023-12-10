package org.example.eventloop.base_implement.simplest.core1;

import org.example.eventloop.base_implement.simplest.core1.demo.Number;

import java.util.ArrayList;
import java.util.List;

public class HandleChain {
  List<AbstractHandle> handleList;

  public HandleChain() {
    handleList = new ArrayList<>();
  }

  public void addHandle(AbstractHandle handle) {
    handleList.add(handle);
  }

  public void handle(Number num) {
    for (AbstractHandle handle : handleList) {
      handle.execute(num);
    }
  }
}
