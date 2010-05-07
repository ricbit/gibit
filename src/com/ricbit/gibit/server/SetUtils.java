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
import java.util.Set;

import com.google.appengine.repackaged.com.google.common.base.Predicate;
import com.google.appengine.repackaged.com.google.common.collect.Iterables;
import com.google.appengine.repackaged.com.google.common.collect.Lists;

/**
 * Utilities for dealing with Sets.
 *  
 * @author Ricardo Bittencourt (bluepenguin@gmail.com)
 */
public class SetUtils {
  
  /**
   * Find the intersection between all sets.
   * 
   * @param setList a list with sets whose intersection will be found
   * @return the intersection of all sets
   */
  public <T> Iterable<T> intersect(final List<Set<T>> setList) {
    // Return early for simple cases.
    if (setList.isEmpty()) {
      return Lists.newArrayList();
    }
    if (setList.size() == 1) {
      return Lists.newArrayList(setList.get(0));
    }
    
    // Find the smallest set.
    Set<T> smallestSet = setList.get(0);
    for (Set<T> set : setList) {
      if (set.size() < smallestSet.size()) {
        smallestSet = set;
      }
    }
    
    // Find the intersection.
    Iterable<T> output = Iterables.filter(smallestSet, new Predicate<T>() {
      @Override
      public boolean apply(final T value) {
        return Iterables.all(setList, new Predicate<Set<T>>() {
          @Override
          public boolean apply(Set<T> set) {
            return set.contains(value);
          }        
        });
      }      
    });
    return output;    
  }
}
