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

import junit.framework.TestCase;

import com.ricbit.gibit.shared.MoreAsserts;

/**
 * Tests for WordSplitter.
 * 
 * @author Ricardo Bittencourt (bluepenguin@gmail.com)
 */
public class WordSplitterTest extends TestCase {
  private WordSplitter subject;

  protected void setUp() throws Exception {
    super.setUp();
    subject = new WordSplitter();
  }
  
  public void testSplit_emptyInput() throws Exception {
    MoreAsserts.assertEmpty(subject.split("   "));
    MoreAsserts.assertEmpty(subject.split(""));
    MoreAsserts.assertEmpty(subject.split("-!_"));
  }

  public void testSplit_simpleInput() throws Exception {
    MoreAsserts.assertContentsInOrder(subject.split("superman"), "superman");
    MoreAsserts.assertContentsInOrder(subject.split("   batman   "), "batman");
    MoreAsserts.assertContentsInOrder(subject.split("x-men"), "x", "men");
    MoreAsserts.assertContentsInOrder(subject.split("groo: cheese"), "groo", "cheese");
  }

  public void testSplit_lowercase() throws Exception {
    MoreAsserts.assertContentsInOrder(subject.split("Wolverine"), "wolverine");
    MoreAsserts.assertContentsInOrder(subject.split("Spider-Man"), "spider", "man");
    MoreAsserts.assertContentsInOrder(subject.split("SHIELD"), "shield");
  }

  public void testSplit_digits() throws Exception {
    MoreAsserts.assertContentsInOrder(subject.split("gen 13"), "gen", "13");
    MoreAsserts.assertContentsInOrder(subject.split("dc 2000"), "dc", "2000");
    MoreAsserts.assertContentsInOrder(subject.split("x-men 2099"), "x", "men", "2099");
  }

  public void testSplit_stopWords() throws Exception {
    MoreAsserts.assertContentsInOrder(subject.split("conan the destroyer"), "conan", "destroyer");
    MoreAsserts.assertContentsInOrder(subject.split("thor of asgard"), "thor", "asgard");
  }

  public void testSplit_unicode() throws Exception {
    MoreAsserts.assertContentsInOrder(subject.split("mônica"), "mônica");
    MoreAsserts.assertContentsInOrder(subject.split("saga da fênix"), "saga", "da", "fênix");
    MoreAsserts.assertContentsInOrder(subject.split("África"), "áfrica");
  }
}
