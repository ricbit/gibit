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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.ricbit.gibit.shared.MoreAsserts;

import junit.framework.TestCase;

/**
 * Tests for KeyGenerator.
 *
 * @author Ricardo Bittencourt (bluepenguin@gmail.com)
 */
public class KeyGeneratorTest extends TestCase {
 
  private static final String SOME_KIND = "kind";
  private final LocalServiceTestHelper helper =
    new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private KeyGenerator subject;

  protected void setUp() throws Exception {
    super.setUp();
    helper.setUp();
    subject = new KeyGenerator();
  }

  protected void tearDown() throws Exception {
    helper.tearDown();
    super.tearDown();
  }
  
  public void testGenerate_empty() throws Exception {
    List<String> emptyList = Lists.newArrayList();
    MoreAsserts.assertEmpty(subject.generate(SOME_KIND, emptyList));
  }
  
  public void testGenerate_stringKeys() throws Exception {
    List<String> inputList = ImmutableList.of("batman", "superman");
    List<Key> keys = Lists.newArrayList(subject.generate(SOME_KIND, inputList));
    assertEquals(inputList.size(), keys.size());
    for (int i = 0; i < inputList.size(); i++) {
      assertEquals(SOME_KIND, keys.get(i).getKind());
      assertEquals(inputList.get(i), keys.get(i).getName());
    }
  }

  public void testGenerate_longKeys() throws Exception {
    List<String> expectedList = ImmutableList.of("123", "456");
    List<Long> inputList = ImmutableList.of(123L, 456L);
    List<Key> keys = Lists.newArrayList(subject.generate(SOME_KIND, inputList));
    assertEquals(expectedList.size(), keys.size());
    for (int i = 0; i < inputList.size(); i++) {
      assertEquals(SOME_KIND, keys.get(i).getKind());
      assertEquals(expectedList.get(i), keys.get(i).getName());
    }
  }
}
