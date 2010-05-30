package com.ricbit.gibit.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ricbit.gibit.shared.SeriesNotFoundException;
import com.ricbit.gibit.shared.TimeMeasurementsDto;

public class Instrumentation {
  private final Provider<Long> timestampProvider;
  private TimeMeasurementsDto measurementDto;

  public interface MeasureableCode<R, S> {
    public R run (S input) throws SeriesNotFoundException;
  }
  
  @Inject
  public Instrumentation(@Timestamp Provider<Long> timestampProvider) {
    this.timestampProvider = timestampProvider;
  }
  
  public <R, S> R measure(int stage, S input, MeasureableCode<R, S> code) throws SeriesNotFoundException {
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
