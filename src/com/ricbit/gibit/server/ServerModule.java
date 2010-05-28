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

import java.util.Collections;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.ricbit.gibit.client.SearchService;

/**
 * Guice module for the server.
 * 
 * @author Ricardo Bittencourt (bluepenguin@gmail.com)
 */
public class ServerModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(SearchService.class).to(SearchServiceImpl.class);
    bind(Memcache.class).to(MemcacheImpl.class);
  }
  
  @Provides
  @Singleton
  public DatastoreService getDatastoreService() {
    return DatastoreServiceFactory.getDatastoreService();
  }
  
  @Provides
  @Singleton
  public Cache getCache() {
    Cache cache;
    try {
      CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
      cache = cacheFactory.createCache(Collections.emptyMap());
    } catch (CacheException e) {
      cache = null;
    }
    return cache;
  }
}
