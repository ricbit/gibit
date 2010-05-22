package com.ricbit.gibit.server;

public class TimeInterval {
  private long startTime;

  public void start() {
    startTime = System.currentTimeMillis();
  }
  
  public int end() {
    return (int)(System.currentTimeMillis() - startTime);
  }
}
