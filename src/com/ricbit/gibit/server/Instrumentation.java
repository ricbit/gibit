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

package com.ricbit.gibit.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ricbit.gibit.shared.SeriesNotFoundException;
import com.ricbit.gibit.shared.TimeMeasurementsDto;

public class Instrumentation {
  private final Provider<Long> timestampProvider;
  private TimeMeasurementsDto measurementDto;

  public interface MeasurableCode<R, S> {
    public R run (S input) throws SeriesNotFoundException;
  }
  
  @Inject
  public Instrumentation(@Timestamp Provider<Long> timestampProvider) {
    this.timestampProvider = timestampProvider;
  }
  
  public <R, S> R measure(int stage, S input, MeasurableCode<R, S> code) 
      throws SeriesNotFoundException {
    long start = timestampProvider.get();
    R result = code.run(input);
    long end = timestampProvider.get();
    measurementDto.setMeasure(stage, (int) (end - start));
    return result;
  }
  
  public void setMeasurementDto(TimeMeasurementsDto dto) {
    measurementDto = dto;    
  }
  
  public TimeMeasurementsDto getMeasurementDto() {
    return measurementDto;
  }
}
