package org.example.eventloop.base_implement.simplest.core3;

import org.example.eventloop.base_implement.simplest.core3.AbstractHandle;

public class HandleChain {
  AbstractHandle head, tail;


  private boolean isEmpty() {
    return head == null && tail == null;
  }

  public void addHandle(AbstractHandle abstractHandle) {
    if(null == abstractHandle){
      return;
    }
    abstractHandle.setSuccessor(null);
    if(isEmpty()){
      head = tail = abstractHandle;
    }else {
      tail.setSuccessor(abstractHandle);
      tail = tail.getSuccessor();
    }
  }

  public void handle(Object o){
    AbstractHandle node = head;
    while(null != node){
      node.execute(o);
      node = node.getSuccessor();
    }
  }
}
