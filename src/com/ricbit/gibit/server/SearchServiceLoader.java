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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ricbit.gibit.shared.SearchResponse;
import com.ricbit.gibit.shared.SearchService;
import com.ricbit.gibit.shared.SeriesNotFoundException;

/**
 * Handler for the search service, builds the servlet using Guice.
 *
 * @author Ricardo Bittencourt (bluepenguin@gmail.com)
 */
public class SearchServiceLoader extends RemoteServiceServlet implements SearchService {
  private static final long serialVersionUID = 1L;
  private static final Injector injector = Guice.createInjector(new ServerModule());
  private SearchService service;
  
  public SearchServiceLoader() {
    service = injector.getInstance(SearchService.class);  
  }
  
  @Override
  public SearchResponse searchServer(String name) throws SeriesNotFoundException {    
    return service.searchServer(name);
  } 
}
