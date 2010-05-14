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

import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.ricbit.gibit.shared.MoreAsserts;

import junit.framework.TestCase;

/**
 * Tests for SetUtils.
 * 
 * @author Ricardo Bittencourt (bluepenguin@gmail.com)
 */
public class SetUtilsTest extends TestCase {

  private SetUtils subject;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    subject = new SetUtils();
  }
  
  public void testIntersect_empty() throws Exception {
    List<Set<Integer>> emptyList = Lists.newArrayList();
    MoreAsserts.assertEmpty(subject.intersect(emptyList));
  }
  
  public void testIntersect_oneSet() throws Exception {
    List<Set<Integer>> setList = Lists.newArrayList();
    setList.add(range(1, 4));
    MoreAsserts.assertContentsInAnyOrder(subject.intersect(setList), 1, 2, 3);
  }
  
  public void testIntersect_threeSets() throws Exception {
    List<Set<Integer>> setList = Lists.newArrayList();
    setList.add(range(1, 10));
    setList.add(range(5, 20));
    setList.add(range(3, 7));
    MoreAsserts.assertContentsInAnyOrder(subject.intersect(setList), 5, 6);
  }
  
  private Set<Integer> range(int start, int end) {
    Set<Integer> set = Sets.newHashSet();
    for (int i = start; i < end; i++) {
      set.add(i);
    }
    return set;
  }
}
