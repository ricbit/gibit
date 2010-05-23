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
import java.util.List;

public class SearchResponse implements Serializable {
  private static final long serialVersionUID = 1L;

  private List<SeriesDto> seriesList;
  
  private TimeMeasurementsDto timeMeasurements;
  
  private boolean debug;

  public List<SeriesDto> getSeriesList() {
    return seriesList;
  }

  public void setSeriesList(List<SeriesDto> seriesList) {
    this.seriesList = seriesList;
  }

  public TimeMeasurementsDto getTimeMeasurements() {
    return timeMeasurements;
  }

  public void setTimeMeasurements(TimeMeasurementsDto timeMeasurements) {
    this.timeMeasurements = timeMeasurements;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public boolean isDebug() {
    return debug;
  }
}
