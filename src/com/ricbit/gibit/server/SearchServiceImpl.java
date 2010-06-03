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
  private final transient Instrumentation instrumentation;

  @Inject
  public SearchServiceImpl(
      SetUtils setUtils, 
      RankingEngine rankingEngine,
      Memcache memcache,
      Datastore datastore,
      Instrumentation instrumentation) {
    this.setUtils = setUtils;
    this.rankingEngine = rankingEngine;
    this.memcache = memcache;
    this.datastore = datastore;
    this.instrumentation = instrumentation;
  }

  public SearchResponse searchServer(String input) throws SeriesNotFoundException { 
    SearchResponse response = new SearchResponse();
    TimeMeasurementsDto timeMeasurementsDto = new TimeMeasurementsDto();
    response.setTimeMeasurements(timeMeasurementsDto);
    instrumentation.setMeasurementDto(timeMeasurementsDto);
    
    final Query query = new Query(input);
    response.setDebug(query.isDebug());
    
    List<SeriesDto> cacheValue = instrumentation.measure(
        MEMCACHE_READ, query.getNormalizedQuery(), 
        new Instrumentation.MeasurableCode<List<SeriesDto>, String>() {
          @Override
          public List<SeriesDto> run(String normalizedQuery) {
            return memcache.getSeries(normalizedQuery);
          }         
        });
    
    if (cacheValue != null) {
      response.setSeriesList(cacheValue);
      return response;
    }
    
    List<Set<Long>> invertedIndex = instrumentation.measure(
        INVERTED_DATASTORE_READ, query.getQueryTerms(), 
        new Instrumentation.MeasurableCode<List<Set<Long>>, List<String>>() {
          @Override
          public List<Set<Long>> run(List<String> queryTerms) throws SeriesNotFoundException {
            return datastore.getInvertedIndex(queryTerms);
          }
        });
    
    Iterable<Long> seriesIdList = instrumentation.measure(
        INTERSECTION, invertedIndex, 
        new Instrumentation.MeasurableCode<Iterable<Long>, List<Set<Long>>>() {
          @Override
          public Iterable<Long> run(List<Set<Long>> invertedIndex) {
            return setUtils.intersect(invertedIndex);
          }
        }); 

    if (Iterables.isEmpty(seriesIdList)){
      throw new SeriesNotFoundException();
    }

    List<SeriesDto> seriesInfo = instrumentation.measure(
        DIRECT_DATASTORE_READ, seriesIdList, 
        new Instrumentation.MeasurableCode<List<SeriesDto>, Iterable<Long>>() {
          @Override
          public List<SeriesDto> run(Iterable<Long> seriesIdList) {
            return datastore.getSeries(seriesIdList);
          }
        }); 
    
    instrumentation.measure(
        RANKING, seriesInfo, 
        new Instrumentation.MeasurableCode<Void, List<SeriesDto>>() {
          @Override
          public Void run(List<SeriesDto> seriesInfo) {
            rankingEngine.rankSeries(seriesInfo);
            return null;
          }
        }); 

    instrumentation.measure(
        MEMCACHE_WRITE, seriesInfo, 
        new Instrumentation.MeasurableCode<Void, List<SeriesDto>>() {
          @Override
          public Void run(List<SeriesDto> seriesInfo) {
            memcache.setSeries(query.getNormalizedQuery(), seriesInfo);
            return null;
          }
        });
    
    response.setSeriesList(seriesInfo);
    return response;
  }
}
