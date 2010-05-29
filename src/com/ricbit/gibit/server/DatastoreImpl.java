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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.common.collect.Iterables;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.ricbit.gibit.shared.SeriesDto;
import com.ricbit.gibit.shared.SeriesNotFoundException;

public class DatastoreImpl implements Datastore {
  private static final int MAX_KEYS_IN_A_QUERY = 1000;

  private final KeyGenerator keyGenerator;
  private final DatastoreService datastoreService;

  @Inject
  public DatastoreImpl(
      KeyGenerator keyGenerator,
      DatastoreService datastoreService) {
    this.keyGenerator = keyGenerator;
    this.datastoreService = datastoreService;
  }
  
  @Override
  public List<Set<Long>> getInvertedIndex(List<String> queryTerms) throws SeriesNotFoundException {
    Iterable<Key> keyList = keyGenerator.generate("SeriesInvertedIndex", queryTerms); 
    Map<Key, Entity> resultMap = datastoreService.get(keyList);

    if (resultMap.size() != queryTerms.size()){
      throw new SeriesNotFoundException();
    }
    List<Set<Long>> setList = Lists.newArrayListWithCapacity(queryTerms.size());
    for (Map.Entry<Key, Entity> entry : resultMap.entrySet()) {
      Map<String, Object> properties = entry.getValue().getProperties();
      setList.add(Sets.newHashSet(getSeriesIdList(properties)));
    }
    return setList;
  }

  @Override
  public List<SeriesDto> getSeries(Iterable<Long> seriesId) {
    Iterable<Long> limitedSeries = Iterables.limit(seriesId, MAX_KEYS_IN_A_QUERY);
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
      seriesDto.setCoverPresent(((Long)properties.get("hascover")) == 1);
      seriesDtoList.add(seriesDto);      
    }
    return seriesDtoList;
  }

  @SuppressWarnings("unchecked")
  private List<Long> getSeriesIdList(Map<String, Object> properties) {
    return (List<Long>) properties.get("seriesNumberList");
  }
}

