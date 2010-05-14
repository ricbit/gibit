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
