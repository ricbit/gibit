package com.ricbit.gibit.shared;

import java.io.Serializable;

public class TimeMeasurementsDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer memcacheRead;
  
  private Integer invertedDatastoreRead;

  private Integer intersection;
  
  private Integer directDatastoreRead;
  
  private Integer ranking;
  
  private Integer memcacheWrite;

  public Integer getMemcacheRead() {
    return memcacheRead;
  }

  public void setMemcacheRead(Integer memcacheRead) {
    this.memcacheRead = memcacheRead;
  }

  public Integer getInvertedDatastoreRead() {
    return invertedDatastoreRead;
  }

  public void setInvertedDatastoreRead(Integer invertedDatastoreRead) {
    this.invertedDatastoreRead = invertedDatastoreRead;
  }

  public Integer getIntersection() {
    return intersection;
  }

  public void setIntersection(Integer intersection) {
    this.intersection = intersection;
  }

  public Integer getDirectDatastoreRead() {
    return directDatastoreRead;
  }

  public void setDirectDatastoreRead(Integer directDatastoreRead) {
    this.directDatastoreRead = directDatastoreRead;
  }

  public Integer getRanking() {
    return ranking;
  }

  public void setRanking(Integer ranking) {
    this.ranking = ranking;
  }

  public Integer getMemcacheWrite() {
    return memcacheWrite;
  }

  public void setMemcacheWrite(Integer memcacheWrite) {
    this.memcacheWrite = memcacheWrite;
  }
}
