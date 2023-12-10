package org.example.eventloop.base_implement.simplest.core2;

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
    if(null != head){
      head.execute(o);
    }
  }
}
