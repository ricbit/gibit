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

import static org.easymock.EasyMock.expect;

import java.util.List;

import junit.framework.TestCase;
import net.sf.jsr107cache.Cache;

import org.easymock.classextension.EasyMock;
import org.easymock.classextension.IMocksControl;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;
import com.ricbit.gibit.shared.SeriesDto;

public class MemcacheImplTest extends TestCase {
  private IMocksControl control;
  private Cache cache;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    control = EasyMock.createControl();
    cache = control.createMock(Cache.class);    
  }
  
  public void testGetSeries() throws Exception {
    List<SeriesDto> expectedSeries = createSeries("batman the series");
    expect(cache.get("prefixbatman")).andReturn(expectedSeries);
    control.replay();
    
    MemcacheImpl subject = createMemcache("prefix");
    List<SeriesDto> actualSeries = subject.getSeries("batman");
    control.verify();
    assertSame(expectedSeries, actualSeries);
  }

  public void testGetSeriesAgain() throws Exception {
    List<SeriesDto> expectedSeries = createSeries("aquaman the series");
    expect(cache.get("differentaquaman")).andReturn(expectedSeries);
    control.replay();
    
    MemcacheImpl subject = createMemcache("different");
    List<SeriesDto> actualSeries = subject.getSeries("aquaman");
    control.verify();
    assertSame(expectedSeries, actualSeries);
  }
  
  public void testSetSeries() throws Exception {
    List<SeriesDto> series = createSeries("batman the series");
    expect(cache.put("prefixbatman", series)).andReturn(series);
    control.replay();
    
    MemcacheImpl subject = createMemcache("prefix");
    subject.setSeries("batman", series);
    control.verify();    
  }

  public void testSetSeriesAgain() throws Exception {
    List<SeriesDto> series = createSeries("aquaman the series");
    expect(cache.put("differentaquaman", series)).andReturn(series);
    control.replay();
    
    MemcacheImpl subject = createMemcache("different");
    subject.setSeries("aquaman", series);
    control.verify();    
  }

  private List<SeriesDto> createSeries(String name) {
    SeriesDto dto = new SeriesDto();
    dto.setName(name);
    return ImmutableList.of(dto);
  }

  private MemcacheImpl createMemcache(String cachePrefix) {
    return new MemcacheImpl(cache, cachePrefix);
  }
  

}
