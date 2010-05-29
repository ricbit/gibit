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

import com.ricbit.gibit.shared.MoreAsserts;

import junit.framework.TestCase;

public class QueryTest extends TestCase {

  public void testGetQueryTerms_emptyInput() throws Exception {
    MoreAsserts.assertEmpty(new Query("   ").getQueryTerms());
    MoreAsserts.assertEmpty(new Query("").getQueryTerms());
    MoreAsserts.assertEmpty(new Query("-!_").getQueryTerms());
  }

  public void testGetNormalizedQuery_emptyInput() throws Exception {
    assertTrue(new Query("   ").getNormalizedQuery().isEmpty());
    assertTrue(new Query("").getNormalizedQuery().isEmpty());
    assertTrue(new Query("-!_").getNormalizedQuery().isEmpty());
  }

  public void testGetQueryTerms_simpleInput() throws Exception {
    MoreAsserts.assertContentsInOrder(new Query("superman").getQueryTerms(), "superman");
    MoreAsserts.assertContentsInOrder(new Query("   batman   ").getQueryTerms(), "batman");
    MoreAsserts.assertContentsInOrder(new Query("x-men").getQueryTerms(), "men", "x");
    MoreAsserts.assertContentsInOrder(new Query("groo: cheese").getQueryTerms(), "cheese", "groo");
  }

  public void testGetNormalizedQuery_simpleInput() throws Exception {
    assertEquals("superman", new Query("superman").getNormalizedQuery());
    assertEquals("batman", new Query("   batman   ").getNormalizedQuery());
    assertEquals("men x", new Query("x-men").getNormalizedQuery());
    assertEquals("cheese groo", new Query("groo: cheese").getNormalizedQuery());
  }

  public void testGetQueryTerms_lowercase() throws Exception {
    MoreAsserts.assertContentsInOrder(new Query("Wolverine").getQueryTerms(), "wolverine");
    MoreAsserts.assertContentsInOrder(new Query("Spider-Man").getQueryTerms(), "man", "spider");
    MoreAsserts.assertContentsInOrder(new Query("SHIELD").getQueryTerms(), "shield");
  }

  public void testGetNormalizedQuery_lowercase() throws Exception {
    assertEquals("wolverine", new Query("Wolverine").getNormalizedQuery());
    assertEquals("man spider", new Query("Spider-Man").getNormalizedQuery());
    assertEquals("shield", new Query("SHIELD").getNormalizedQuery());
  }

  public void testGetQueryTerms_digits() throws Exception {
    MoreAsserts.assertContentsInOrder(new Query("gen 13").getQueryTerms(), "13", "gen");
    MoreAsserts.assertContentsInOrder(new Query("dc 2000").getQueryTerms(), "2000", "dc");
    MoreAsserts.assertContentsInOrder(new Query("x-men 2099").getQueryTerms(), "2099", "men", "x");
  }

  public void testGetNormalizedQuery_digits() throws Exception {
    assertEquals("13 gen", new Query("gen 13").getNormalizedQuery());
    assertEquals("2000 dc", new Query("dc 2000").getNormalizedQuery());
    assertEquals("2099 men x", new Query("x-men 2099").getNormalizedQuery());
  }

  public void testGetQueryTerms_stopWords() throws Exception {
    MoreAsserts.assertContentsInOrder(
        new Query("conan the destroyer").getQueryTerms(), "conan", "destroyer");
    MoreAsserts.assertContentsInOrder(
        new Query("thor of asgard").getQueryTerms(), "asgard", "thor");
  }

  public void testGetNormalizedQuery_stopWords() throws Exception {
    assertEquals("conan destroyer", new Query("conan the destroyer").getNormalizedQuery());
    assertEquals("asgard thor", new Query("thor of asgard").getNormalizedQuery());
  }

  public void testGetQueryTerms_unicode() throws Exception {
    MoreAsserts.assertContentsInOrder(
        new Query("mônica").getQueryTerms(), "mônica");
    MoreAsserts.assertContentsInOrder(
        new Query("saga da fênix").getQueryTerms(), "da", "fênix", "saga");
    MoreAsserts.assertContentsInOrder(
        new Query("África").getQueryTerms(), "áfrica");
  }
  
  public void testGetNormalizedQuery_unicode() throws Exception {
    assertEquals("mônica", new Query("mônica").getNormalizedQuery());
    assertEquals("da fênix saga", new Query("saga da fênix").getNormalizedQuery());
    assertEquals("áfrica", new Query("África").getNormalizedQuery());
  }
  
  public void testIsDebug() throws Exception {
    assertTrue(new Query("debug:new mutants").isDebug());
    assertFalse(new Query("new mutants").isDebug());
  }
  
  public void testIsDebugPreservesQueryTerms() throws Exception {
    assertEquals("mutants new", new Query("debug:new mutants").getNormalizedQuery());
  }
}
