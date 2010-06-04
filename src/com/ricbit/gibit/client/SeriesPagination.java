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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ricbit.gibit.shared.SeriesDto;

public class SeriesPagination extends Composite {
  
  interface Binder extends UiBinder<Widget, SeriesPagination> {}
  private static Binder BINDER = GWT.create(Binder.class);
  
  interface Style extends CssResource {
    String buttonEnabled();
    String buttonDisabled();
  }
  
  public interface Presenter {
    void onPageChange(int newPage);
  }
  
  @UiField
  Style style;
  
  @UiField
  FlowPanel mainPanel;
  
  @UiField
  FlowPanel outerPanel;

  @UiField
  Anchor prevButton;
  
  @UiField
  Anchor nextButton;
  
  @UiField
  FlowPanel controls;
  
  private int currentPage;

  private int seriesPerPage;

  private List<SeriesDto> series;
  
  private List<SeriesWidget> widgetCache;
  
  private Presenter presenter;
  
  public SeriesPagination() {
    initWidget(BINDER.createAndBindUi(this));
  }

  public void setSeries(List<SeriesDto> seriesList, int page) {
    series = seriesList;
    seriesPerPage = (outerPanel.getOffsetWidth()) / 230;
    currentPage = page * seriesPerPage >= seriesList.size() ? 0 : page;
    widgetCache = new ArrayList<SeriesWidget>();
    for (int i = 0; i < series.size(); i++) {
      widgetCache.add(null);
    }
    setPage(currentPage);
  }

  private void setPage(int page) {
    int start = seriesPerPage * currentPage;
    int end = Math.min(series.size(), start + seriesPerPage); 
    mainPanel.setWidth(String.valueOf((end - start) * 230) + "px");
    mainPanel.clear();
    for (int i = start; i < end; i++) {
      SeriesWidget widget = getSeries(i);
      mainPanel.add(widget);
    }
    prevButton.setStyleName(start == 0 ? style.buttonDisabled() : style.buttonEnabled());
    nextButton.setStyleName(end == series.size() ? style.buttonDisabled() : style.buttonEnabled());
    presenter.onPageChange(page);
  }

  private SeriesWidget getSeries(int index) {
    SeriesWidget widget = widgetCache.get(index);
    if (widget != null) {
      return widget;
    }
    widget = new SeriesWidget();
    widget.setSeries(series.get(index));
    widgetCache.set(index, widget);
    return widget;
  }

  public void clear() {
    mainPanel.clear();
  }
  
  @UiHandler("nextButton")
  public void nextButtonClick(ClickEvent event) {
    currentPage++;
    if (currentPage * seriesPerPage >= series.size()) {
      currentPage--;
    }
    setPage(currentPage);
  }

  @UiHandler("prevButton")
  public void prevButtonClick(ClickEvent event) {
    currentPage--;
    if (currentPage < 0) {
      currentPage++;
    }
    setPage(currentPage);
  }

  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }
}
