package com.ricbit.gibit.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.ricbit.gibit.shared.SeriesDto;

public class SeriesWidget extends Composite {

  interface Binder extends UiBinder<Widget, SeriesWidget> {}
  private static Binder BINDER = GWT.create(Binder.class);

  @UiField
  Image seriesImage;
  
  @UiField
  HTML seriesName;
  
  public SeriesWidget() {
    initWidget(BINDER.createAndBindUi(this));
  }

  public void setSeries(SeriesDto series) {
    seriesName.setHTML(
        "<b>"+ series.getName()+"</b> ("+series.getPublisher()+", " + series.getYear() + ")");
    if (series.isCoverPresent()) {
      seriesImage.setUrl("http://www.ricbit.com/gibit/" + series.getId() + ".jpg");
    } else {      
      seriesImage.setUrl("http://www.ricbit.com/gibit/missing.jpg");
    }
  }
}
