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

import static com.ricbit.gibit.server.IterableEquals.eq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;

import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.easymock.classextension.IMocksControl;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableSet;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.ricbit.gibit.shared.SearchResponse;
import com.ricbit.gibit.shared.SeriesDto;
import com.ricbit.gibit.shared.SeriesNotFoundException;

public class SearchServiceImplTest extends TestCase {
  private IMocksControl control;
  private SetUtils setUtils;
  private RankingEngine rankingEngine;
  private Memcache memcache;
  private Datastore datastore;
  private SearchServiceImpl subject;
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    control = EasyMock.createStrictControl();
    setUtils = new SetUtils();
    rankingEngine = new RankingEngine();
    memcache = control.createMock(Memcache.class);
    datastore = control.createMock(Datastore.class);
    subject = new SearchServiceImpl(setUtils, rankingEngine, memcache, datastore);
  }
  
  public void testSearchServer_cacheHit() throws Exception {
    List<SeriesDto> seriesInfo = ImmutableList.of(createSeriesDto("new mutants", 1));
    expect(memcache.getSeries("mutants new")).andReturn(seriesInfo);
    control.replay();
    
    String input = "New mutants";
    SearchResponse response = subject.searchServer(input);
    assertSame(seriesInfo, response.getSeriesList());
    control.verify();
  }

  public void testSearchServer_fullPipeline() throws Exception {
    expect(memcache.getSeries("mutants new")).andReturn(null);
    
    Set<Long> firstSet = ImmutableSet.of(1L, 2L, 3L, 4L);
    Set<Long> secondSet = ImmutableSet.of(3L, 4L, 5L, 6L);
    List<Set<Long>> invertedIndex = ImmutableList.of(firstSet, secondSet);
    List<String> queryTerms = ImmutableList.of("mutants", "new");
    expect(datastore.getInvertedIndex(eq(queryTerms))).andReturn(invertedIndex);
        
    SeriesDto dto1 = createSeriesDto("new mutants", 101);
    SeriesDto dto2 = createSeriesDto("new mutants annual", 202);
    List<SeriesDto> seriesInfo = Lists.newArrayList(dto1, dto2);
    List<Long> intersection = ImmutableList.of(3L, 4L);
    expect(datastore.getSeries(eq(intersection))).andReturn(seriesInfo);
    
    List<SeriesDto> sortedSeries = ImmutableList.of(dto2, dto1);
    memcache.setSeries(eq("mutants new"), eq(sortedSeries));
    
    control.replay();
    
    SearchResponse response = subject.searchServer("New mutants");
    
    assertSame(seriesInfo, response.getSeriesList());
    control.verify();
  }
  
  public void testSearchServer_queryTermNotFound() throws Exception {
    expect(memcache.getSeries("mutants new")).andReturn(null);
    
    List<String> queryTerms = ImmutableList.of("mutants", "new");
    expect(datastore.getInvertedIndex(eq(queryTerms))).andThrow(new SeriesNotFoundException());
    
    control.replay();
    
    try {
      subject.searchServer("New mutants");
      fail();
    } catch (SeriesNotFoundException e) {
      // Expected result.
    }
    control.verify();
  }

  public void testSearchServer_emptyIntersection() throws Exception {
    expect(memcache.getSeries("mutants new")).andReturn(null);
    
    Set<Long> firstSet = ImmutableSet.of(1L, 2L);
    Set<Long> secondSet = ImmutableSet.of(5L, 6L);
    List<Set<Long>> invertedIndex = ImmutableList.of(firstSet, secondSet);
    List<String> queryTerms = ImmutableList.of("mutants", "new");
    expect(datastore.getInvertedIndex(eq(queryTerms))).andReturn(invertedIndex);
    
    control.replay();
    
    try {
      subject.searchServer("New mutants");
      fail();
    } catch (SeriesNotFoundException e) {
      // Expected result.
    }
    control.verify();
  }

  private SeriesDto createSeriesDto(String name, int issues) {
    SeriesDto dto = new SeriesDto();
    dto.setName(name);
    dto.setIssues(issues);
    return dto;
  }
}
