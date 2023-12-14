package org.example.thread.utils;

public abstract class UseTime {

  protected abstract void calcUnit();

  public final long useTime() {
    long startTime = System.currentTimeMillis();
    calcUnit();
    long interval = System.currentTimeMillis() - startTime;
    System.out.println("useTime:" + interval + " ms");
    return interval;
  }
}
