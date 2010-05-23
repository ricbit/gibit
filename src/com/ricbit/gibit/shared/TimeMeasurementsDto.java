/**
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

  public TimeMeasurementsDto() {
    memcacheRead = 0;
    memcacheWrite = 0;
    intersection = 0;
    invertedDatastoreRead = 0;
    directDatastoreRead = 0;
    ranking = 0;
  }
  
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
