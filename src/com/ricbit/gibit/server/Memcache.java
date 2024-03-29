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

import java.util.List;

import com.ricbit.gibit.shared.SeriesDto;

/**
 * Abstraction to access the Memcache.
 * 
 * @author Ricardo Bittencourt (bluepenguin@gmail.com)
 */
public interface Memcache {
  /**
   * Retrieve from the memcache the list of series for a given query.
   * 
   * @param normalizedQuery the query, normalized
   * @return the list of series for this query, or null if not present in the cache
   */
  public List<SeriesDto> getSeries(String normalizedQuery);
  
  /**
   * Inserts a list of series in the memcache.
   * 
   * @param normalizedQuery the query that returned this list of series
   * @param series a list of series to be stored in the cache
   */
  public void setSeries(String normalizedQuery, List<SeriesDto> series);
}
