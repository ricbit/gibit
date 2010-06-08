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

import static com.ricbit.gibit.shared.TimeMeasurementsDto.PipelineStage.DIRECT_DATASTORE_READ;
import static com.ricbit.gibit.shared.TimeMeasurementsDto.PipelineStage.INTERSECTION;
import static com.ricbit.gibit.shared.TimeMeasurementsDto.PipelineStage.INVERTED_DATASTORE_READ;
import static com.ricbit.gibit.shared.TimeMeasurementsDto.PipelineStage.MEMCACHE_READ;
import static com.ricbit.gibit.shared.TimeMeasurementsDto.PipelineStage.MEMCACHE_WRITE;
import static com.ricbit.gibit.shared.TimeMeasurementsDto.PipelineStage.RANKING;

import java.util.List;
import java.util.Set;

import com.google.appengine.repackaged.com.google.common.collect.Iterables;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.ricbit.gibit.shared.SearchResponse;
import com.ricbit.gibit.shared.SearchService;
import com.ricbit.gibit.shared.SeriesDto;
import com.ricbit.gibit.shared.SeriesNotFoundException;
import com.ricbit.gibit.shared.TimeMeasurementsDto;

/**
 * The server side implementation of the RPC service.
 */
public class SearchServiceImpl extends RemoteServiceServlet implements SearchService {
  private static final long serialVersionUID = 1L;  

  private final transient SetUtils setUtils;
  private final transient RankingEngine rankingEngine;
  private final transient Memcache memcache;
  private final transient Datastore datastore;

  @Inject
  public SearchServiceImpl(
      SetUtils setUtils, 
      RankingEngine rankingEngine,
      Memcache memcache,
      Datastore datastore) {
    this.setUtils = setUtils;
    this.rankingEngine = rankingEngine;
    this.memcache = memcache;
    this.datastore = datastore;
  }

  public SearchResponse searchServer(String input) throws SeriesNotFoundException { 
    SearchResponse response = new SearchResponse();
    TimeMeasurementsDto measureDto = new TimeMeasurementsDto();
    response.setTimeMeasurements(measureDto);
    
    final Query query = new Query(input);
    response.setDebug(query.isDebug());
    
    List<SeriesDto> cacheValue = memcacheRead(query.getNormalizedQuery(), measureDto); 
      
    if (cacheValue != null) {
      response.setSeriesList(cacheValue);
      return response;
    }
    
    List<Set<Long>> invertedIndex = invertedDatastoreRead(query.getQueryTerms(), measureDto);     
    Iterable<Long> seriesIdList = intersection(invertedIndex, measureDto); 

    if (Iterables.isEmpty(seriesIdList)){
      throw new SeriesNotFoundException();
    }

    List<SeriesDto> seriesInfo = directDatastoreRead(seriesIdList, measureDto);
    ranking(seriesInfo, measureDto);
    memcacheWrite(query.getNormalizedQuery(), seriesInfo, measureDto);
    
    response.setSeriesList(seriesInfo);
    return response;
  }
  

  @Measure(MEMCACHE_READ)
  List<SeriesDto> memcacheRead(String normalizedQuery, TimeMeasurementsDto dto) {
    return memcache.getSeries(normalizedQuery);
  }
  
  @Measure(INVERTED_DATASTORE_READ)
  List<Set<Long>> invertedDatastoreRead(List<String> queryTerms, TimeMeasurementsDto dto) 
      throws SeriesNotFoundException {
    return datastore.getInvertedIndex(queryTerms);
  }
  
  @Measure(INTERSECTION)
  Iterable<Long> intersection(List<Set<Long>> invertedIndex, TimeMeasurementsDto measureDto) {
    return setUtils.intersect(invertedIndex);
  }
  
  @Measure(DIRECT_DATASTORE_READ)
  List<SeriesDto> directDatastoreRead(Iterable<Long> seriesIdList, 
      TimeMeasurementsDto measureDto) {
    return datastore.getSeries(seriesIdList);
  }
  
  @Measure(RANKING)
  void ranking(List<SeriesDto> seriesInfo, TimeMeasurementsDto measureDto) {
    rankingEngine.rankSeries(seriesInfo);
  }
  
  @Measure(MEMCACHE_WRITE)
  void memcacheWrite(String normalizedQuery, List<SeriesDto> seriesInfo,
      TimeMeasurementsDto measureDto) {
    memcache.setSeries(normalizedQuery, seriesInfo);    
  }
}
