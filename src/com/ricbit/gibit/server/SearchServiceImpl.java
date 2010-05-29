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

import com.google.appengine.repackaged.com.google.common.collect.Iterables;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.ricbit.gibit.client.SearchService;
import com.ricbit.gibit.shared.SearchResponse;
import com.ricbit.gibit.shared.SeriesDto;
import com.ricbit.gibit.shared.SeriesNotFoundException;
import com.ricbit.gibit.shared.TimeMeasurementsDto;

/**
 * The server side implementation of the RPC service.
 */
public class SearchServiceImpl extends RemoteServiceServlet implements SearchService {
  private static final long serialVersionUID = 1L;  

  private final SetUtils setUtils;
  private final RankingEngine rankingEngine;
  private final TimeInterval timeInterval;
  private final Memcache memcache;
  private final Datastore datastore;

  @Inject
  public SearchServiceImpl(
      SetUtils setUtils, 
      RankingEngine rankingEngine,
      TimeInterval timeInterval,
      Memcache memcache,
      Datastore datastore) {
    this.setUtils = setUtils;
    this.rankingEngine = rankingEngine;
    this.timeInterval = timeInterval;
    this.memcache = memcache;
    this.datastore = datastore;
  }

  public SearchResponse searchServer(String input) throws SeriesNotFoundException { 
    SearchResponse response = new SearchResponse();
    TimeMeasurementsDto timeMeasurementsDto = new TimeMeasurementsDto();
    response.setTimeMeasurements(timeMeasurementsDto);
    
    Query query = new Query(input);
    response.setDebug(query.isDebug());
    
    timeInterval.start();
    List<SeriesDto> cacheValue = memcache.getSeries(query.getNormalizedQuery());
    timeMeasurementsDto.setMemcacheRead(timeInterval.end());
    
    if (cacheValue != null) {
      response.setSeriesList(cacheValue);
      return response;
    }
    
    timeInterval.start();
    List<Set<Long>> invertedIndex = datastore.getInvertedIndex(query.getQueryTerms());
    timeMeasurementsDto.setInvertedDatastoreRead(timeInterval.end());

    timeInterval.start();
    Iterable<Long> series = setUtils.intersect(invertedIndex);
    timeMeasurementsDto.setIntersection(timeInterval.end());

    if (Iterables.isEmpty(series)){
      throw new SeriesNotFoundException();
    }

    timeInterval.start();
    List<SeriesDto> seriesList = datastore.getSeries(series);
    timeMeasurementsDto.setDirectDatastoreRead(timeInterval.end());
    
    timeInterval.start();
    rankingEngine.rankSeries(seriesList);
    timeMeasurementsDto.setRanking(timeInterval.end());
    
    timeInterval.start();
    memcache.setSeries(query.getNormalizedQuery(), seriesList);
    timeMeasurementsDto.setMemcacheWrite(timeInterval.end());
    
    response.setSeriesList(seriesList);
    return response;
  }
}
