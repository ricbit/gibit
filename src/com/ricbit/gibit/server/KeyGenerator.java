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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.com.google.common.base.Function;
import com.google.appengine.repackaged.com.google.common.collect.Iterables;

/**
 * Utilities to generate Keys to use in the Datastore.
 * 
 * @author Ricardo Bittencourt (bluepenguin@gmail.com)
 */
public class KeyGenerator {
  /**
   * Convert a list of elements to Keys
   * 
   * @param kind kind of key
   * @param elements items to be converted to keys
   * @return a list of Keys 
   */
  public <T> Iterable<Key> generate(final String kind, Iterable<T> elements) {
    return Iterables.transform(elements, new Function<T, Key>() {
      @Override
      public Key apply(T element) {
        return KeyFactory.createKey(null, kind, element.toString());
      }
    });
  }
}
