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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jsr107cache.Cache;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.common.collect.Iterables;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.ricbit.gibit.client.SearchService;
import com.ricbit.gibit.shared.SeriesDto;
import com.ricbit.gibit.shared.SeriesNotFoundException;

/**
 * The server side implementation of the RPC service.
 */
public class SearchServiceImpl extends RemoteServiceServlet implements SearchService {
  private static final int MAX_KEYS_IN_A_QUERY = 1000;

  private static final long serialVersionUID = 1L;  

  private WordSplitter wordSplitter;
  private SetUtils setUtils;
  private KeyGenerator keyGenerator;
  private final DatastoreService datastoreService;

  private final RankingEngine rankingEngine;

  private final CacheKeyGenerator cacheKeyGenerator;

  private final Cache cache;

  @Inject
  public SearchServiceImpl(
      WordSplitter wordSplitter, 
      SetUtils setUtils, 
      KeyGenerator keyGenerator,
      DatastoreService datastoreService,
      RankingEngine rankingEngine,
      CacheKeyGenerator cacheKeyGenerator,
      Cache cache) {
    this.wordSplitter = wordSplitter;
    this.setUtils = setUtils;
    this.keyGenerator = keyGenerator;
    this.datastoreService = datastoreService;
    this.rankingEngine = rankingEngine;
    this.cacheKeyGenerator = cacheKeyGenerator;
    this.cache = cache;
  }

  public List<SeriesDto> searchServer(String input) throws SeriesNotFoundException {   
    List<String> words = wordSplitter.split(input);
    String cacheKey = cacheKeyGenerator.generateCacheKey(words);
    ArrayList<SeriesDto> cacheValue = (ArrayList<SeriesDto>)cache.get(cacheKey);
    if (cacheValue != null) {
      return cacheValue;
    }
    
    Iterable<Key> keyList = keyGenerator.generate("SeriesInvertedIndex", words); 
    Map<Key, Entity> resultMap = datastoreService.get(keyList);

    if (resultMap.size() != words.size()){
      throw new SeriesNotFoundException();
    }

    List<Set<Long>> setList = Lists.newArrayListWithCapacity(words.size());
    for (Map.Entry<Key, Entity> entry : resultMap.entrySet()) {
      Map<String, Object> properties = entry.getValue().getProperties();
      setList.add(Sets.newHashSet((List<Long>)properties.get("seriesNumberList")));
    }
    Iterable<Long> series = setUtils.intersect(setList);

    if (Iterables.isEmpty(series)){
      throw new SeriesNotFoundException();
    }

    ArrayList<SeriesDto> seriesList = buildSeriesDto(series);
    rankingEngine.rankSeries(seriesList);
    cache.put(cacheKey, seriesList);
    return seriesList;
  }

  private ArrayList<SeriesDto> buildSeriesDto(Iterable<Long> seriesIdList) 
      throws SeriesNotFoundException {
    Iterable<Long> limitedSeries = Iterables.limit(seriesIdList, MAX_KEYS_IN_A_QUERY);
    Iterable<Key> keyList = keyGenerator.generate("Series", limitedSeries);       
    Map<Key, Entity> resultMap = datastoreService.get(keyList);

    ArrayList<SeriesDto> seriesDtoList = Lists.newArrayList();
    for (Map.Entry<Key, Entity> entry : resultMap.entrySet()) {
      Map<String, Object> properties = entry.getValue().getProperties();
      SeriesDto seriesDto = new SeriesDto();
      seriesDto.setId(Integer.parseInt(entry.getKey().getName()));
      seriesDto.setName((String)properties.get("name"));
      seriesDto.setYear(((Long)properties.get("year")).intValue());
      seriesDto.setIssues(((Long)properties.get("issues")).intValue());
      seriesDto.setPublisher((String)properties.get("publisher"));
      seriesDtoList.add(seriesDto);      
    }
    return seriesDtoList;
  }
}
