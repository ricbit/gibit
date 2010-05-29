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
import java.util.Set;

import com.ricbit.gibit.shared.SeriesDto;
import com.ricbit.gibit.shared.SeriesNotFoundException;

/**
 * Abstraction to access the Datastore.
 * 
 * @author Ricardo Bittencourt (bluepenguin@gmail.com)
 */
public interface Datastore {
  /**
   * Retrieves the inverted indexes for the list of terms in the query.
   * 
   * @param queryTerms the list of terms in the query
   * @return one set of ids for each term in the query
   * @throws SeriesNotFoundException if some of the terms doesn't have an index
   */
  public List<Set<Long>> getInvertedIndex(List<String> queryTerms) throws SeriesNotFoundException;
  
  /**
   * Get information of series from the direct index, given their ids.
   * 
   * @param seriesId a list of series ids
   * @return a list with information about each series
   */
  public List<SeriesDto> getSeries(Iterable<Long> seriesId);
}
