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

import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.ricbit.gibit.shared.MoreAsserts;
import com.ricbit.gibit.shared.SeriesDto;

import junit.framework.TestCase;

/**
 * Tests for Ranking Engine.
 * 
 * @author Ricardo Bittencourt (bluepenguin@gmail.com)
 */
public class RankingEngineTest extends TestCase {
  private RankingEngine rankingEngine;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    rankingEngine = new RankingEngine();
  }
  
  public void testRankSeries() throws Exception {
    SeriesDto dto1 = createSeriesDto(1);
    SeriesDto dto2 = createSeriesDto(2);
    SeriesDto dto3 = createSeriesDto(3);
    List<SeriesDto> seriesList = Lists.newArrayList(dto2, dto1, dto3);
    rankingEngine.rankSeries(seriesList);
    MoreAsserts.assertContentsInOrder(seriesList, dto3, dto2, dto1);
  }

  private SeriesDto createSeriesDto(int issues) {
    SeriesDto dto = new SeriesDto();
    dto.setName("name");
    dto.setYear(1986);
    dto.setId(1);
    dto.setIssues(issues);
    return dto;
  }  
}
