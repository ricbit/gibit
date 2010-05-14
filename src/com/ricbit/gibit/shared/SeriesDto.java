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

public class SeriesDto implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int id;
  private int year;
  private int issues;
  private String name;
  private String publisher;
  private boolean coverPresent;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public int getId() {
    return id;
  }
  
  public void setYear(int year) {
    this.year = year;
  }
  
  public int getYear() {
    return year;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }
  
  public void setIssues(int issues) {
    this.issues = issues;
  }
  
  public int getIssues() {
    return issues;
  }
  
  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }
  
  public String getPublisher() {
    return publisher;
  }
  
  public void setCoverPresent(boolean coverPresent) {
    this.coverPresent = coverPresent;
  }
  
  public boolean isCoverPresent() {
    return coverPresent;
  }
}
