package com.ricbit.gibit.shared;

import java.io.Serializable;
import java.util.List;

public class SearchResponse implements Serializable {
  private static final long serialVersionUID = 1L;

  private List<SeriesDto> seriesList;
  
  private TimeMeasurementsDto timeMeasurements;

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
}
