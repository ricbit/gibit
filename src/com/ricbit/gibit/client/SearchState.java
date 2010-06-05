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

import com.google.gwt.http.client.URL;

public class SearchState {

  private String query;
  private int page;
  
  public SearchState(String query, int page) {
    this.query = query;
    this.page = page;
  }
  
  public SearchState(String token) {
    query = "";
    page = 0;
    String decodedToken = URL.decode(token);
    if (decodedToken.startsWith("search?")) {
      for (String param : decodedToken.substring(7).split(";")) {
         String[] split = param.split("=");
         if (split.length < 2) {
           continue;
         }           
         if ("p".equals(split[0])) {
           try {
             page = Integer.parseInt(split[1]);
           } catch (NumberFormatException e) {
             // Do nothing.
           }
         } else if ("q".equals(split[0])) {
           query = split[1];
         }
      }
    }
  }

  public boolean isEmpty() {
    return query.isEmpty();
  }

  public String getQuery() {
    return query;
  }

  public int getPage() {
    return page;
  }

  public String getUrl() {
    String url = "search?";
    if (page != 0) {
      url += "p=" + page + ";";
    }
    url += "q=" + query; 
    return URL.encode(url);
  }
}
