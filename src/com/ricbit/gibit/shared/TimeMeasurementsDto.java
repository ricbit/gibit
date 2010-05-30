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
import java.util.ArrayList;
import java.util.List;

public class TimeMeasurementsDto implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public interface PipelineStage {
    public static final int MEMCACHE_READ = 0;
    public static final int INVERTED_DATASTORE_READ = 1;
    public static final int INTERSECTION = 2;
    public static final int DIRECT_DATASTORE_READ = 3;
    public static final int RANKING = 4;
    public static final int MEMCACHE_WRITE = 5;
    public static final int TOTAL = 6;
  }
  
  private List<Integer> measure;

  public TimeMeasurementsDto() {
    measure = new ArrayList<Integer>();
    for (int i = 0; i < PipelineStage.TOTAL; i++) {
      measure.add(0);
    }
  }  
  
  public int getMeasure(int stage) {
    return measure.get(stage);
  }
  
  public void setMeasure(int stage, int value) {
    measure.set(stage, value);
  }
}
