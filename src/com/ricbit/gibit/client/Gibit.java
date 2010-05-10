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

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.ricbit.gibit.shared.SeriesDto;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Gibit extends Composite implements EntryPoint {
  /**
   * Create a remote service proxy to talk to the server-side search service.
   */
  private final SearchServiceAsync searchService = GWT.create(SearchService.class);

  interface Binder extends UiBinder<Widget, Gibit> {}
  private static Binder BINDER = GWT.create(Binder.class);

  @UiField
  TextBox queryField;

  @UiField
  Button sendButton;

  @UiField
  FlowPanel answerPanel;

  public Gibit() {
    initWidget(BINDER.createAndBindUi(this));
  }

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    RootPanel.get().add(this);
    queryField.setFocus(true);
    queryField.selectAll();
  }

  @UiHandler("sendButton")
  void handleClick(ClickEvent event) {
    performQuery();
  }

  @UiHandler("queryField")
  void handleKeypress(KeyUpEvent event) {
    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
      performQuery();
    }
  }

  private void performQuery() {
    sendButton.setEnabled(false);
    answerPanel.clear();
    searchService.searchServer(queryField.getText(), new AsyncCallback<List<SeriesDto>>() {
      @Override
      public void onFailure(Throwable caught) {
        queryNotFound();
      }

      @Override
      public void onSuccess(List<SeriesDto> results) {
        displayResults(results);
      }
    });
  }
  
  private void queryNotFound() {
    answerPanel.add(new HTML("<center>Series not found.</center>"));
    sendButton.setEnabled(true);
  }
  
  private void displayResults(List<SeriesDto> results) {
    int seriesPerPage = answerPanel.getOffsetWidth() / 230;
    int amountToDisplay = Math.min(results.size(), seriesPerPage);
    for (int i = 0; i < amountToDisplay; i++) {
      SeriesWidget widget = new SeriesWidget();
      widget.setSeries(results.get(i));
      answerPanel.add(widget);
    }
    sendButton.setEnabled(true);
  }
}

