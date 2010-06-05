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

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.ricbit.gibit.shared.SearchResponse;
import com.ricbit.gibit.shared.SearchService;
import com.ricbit.gibit.shared.SearchServiceAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Gibit extends Composite implements EntryPoint, SeriesPagination.Presenter, 
    ValueChangeHandler<String> {
  private static final int FIRST_PAGE = 0;

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
  FlowPanel outerPanel;

  @UiField
  SeriesPagination seriesPagination;
  
  @UiField
  FlowPanel loadingPanel;

  @UiField
  DebugWidget debug;
  
  @UiField
  FlowPanel noMatchPanel;

  private Duration duration;
  
  private SearchState searchState;
  
  public Gibit() {
    initWidget(BINDER.createAndBindUi(this));
  }

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    RootPanel.get().add(this);
    Image.prefetch("loading.gif");
    seriesPagination.setPresenter(this);
    History.addValueChangeHandler(this);
    searchState = new SearchState("");
    History.fireCurrentHistoryState();
  }
  
  private void resetPage() {
    searchState = new SearchState("");
    queryField.setText("");
    queryField.setFocus(true);
    queryField.selectAll();    
    seriesPagination.setVisible(false);
    loadingPanel.setVisible(false);
    debug.setVisible(false);
    noMatchPanel.setVisible(false);
  }

  @UiHandler("sendButton")
  void handleClick(ClickEvent event) {
    performQuery(queryField.getText(), FIRST_PAGE);
  }

  @UiHandler("queryField")
  void handleKeypress(KeyUpEvent event) {
    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
      performQuery(queryField.getText(), FIRST_PAGE);
    }
  }

  private void performQuery(final String query, final int page) {
    sendButton.setEnabled(false);
    seriesPagination.setVisible(false);
    loadingPanel.setVisible(true);
    debug.setVisible(false);
    noMatchPanel.setVisible(false);
    duration = new Duration();
    searchService.searchServer(query, new AsyncCallback<SearchResponse>() {
      @Override
      public void onFailure(Throwable caught) {
        queryNotFound();
      }

      @Override
      public void onSuccess(SearchResponse results) {
        displayResults(results, query, page);
      }
    });
  }
  
  private void queryNotFound() {
    noMatchPanel.setVisible(true);    
    sendButton.setEnabled(true);
    loadingPanel.setVisible(false);
  }
  
  private void displayResults(SearchResponse results, String query, int page) {
    loadingPanel.setVisible(false);
    seriesPagination.setVisible(true);
    seriesPagination.setSeries(results.getSeriesList(), page);
    sendButton.setEnabled(true);
    searchState = new SearchState(query, page);
    History.newItem(searchState.getUrl());
    if (results.isDebug()) {
      debug.setTimeMeasurements(results.getTimeMeasurements(), duration.elapsedMillis());
    }
  }

  @Override
  public void onPageChange(int newPage) {
    searchState = new SearchState(searchState.getQuery(), newPage);
    History.newItem(searchState.getUrl());
    History.fireCurrentHistoryState();
  }

  @Override
  public void onValueChange(ValueChangeEvent<String> event) {
    // TODO(ricbit): This code really, really needs some tests.
    SearchState newSearchState = new SearchState(event.getValue());

    if (newSearchState.isEmpty()) {
      resetPage();
    } else if (searchState.getQuery().equals(newSearchState.getQuery())) {
      seriesPagination.setPage(newSearchState.getPage());
      searchState = newSearchState;
    } else {
      queryField.setText(newSearchState.getQuery());
      performQuery(newSearchState.getQuery(), newSearchState.getPage());    
    }
  }
}

