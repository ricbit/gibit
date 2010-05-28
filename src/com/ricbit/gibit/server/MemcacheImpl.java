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

import net.sf.jsr107cache.Cache;

import com.google.inject.Inject;
import com.ricbit.gibit.shared.SeriesDto;

public class MemcacheImpl implements Memcache {
  private static final String CACHE_PREFIX = "2";
  private final Cache cache;
  
  @Inject
  public MemcacheImpl(Cache cache) {
    this.cache = cache;    
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<SeriesDto> getSeries(String normalizedQuery) {
    return (ArrayList<SeriesDto>) cache.get(buildCacheKey(normalizedQuery));
  }

  @Override
  public void setSeries(String normalizedQuery, List<SeriesDto> series) {
    cache.put(buildCacheKey(normalizedQuery), series);
  }

  private String buildCacheKey(String normalizedQuery) {
    return CACHE_PREFIX + normalizedQuery;
  }  
}
