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

import com.google.appengine.repackaged.com.google.common.base.CharMatcher;
import com.google.appengine.repackaged.com.google.common.base.Splitter;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableSet;
import com.google.appengine.repackaged.com.google.common.collect.Lists;

/**
 * Utility class holding methods to split strings into words.
 * 
 * @author Ricardo Bittencourt (bluepenguin@gmail.com)
 */
public class WordSplitter {
  private static final Set<String> STOP_WORDS = ImmutableSet.of("the", "of");  
  private static final Splitter SPLITTER = 
    Splitter.on(CharMatcher.JAVA_LETTER_OR_DIGIT.negate()).omitEmptyStrings().trimResults();

  /**
   * Splits an input string into words, taking into account unicode and stop words.
   * All words are converted to lower case.
   * 
   * @param input input string
   * @return a list of lowercase, unicode words with no stop words
   */
  public List<String> split(String input) {
    Iterable<String> words = SPLITTER.split(input);
    List<String> filtered = Lists.newArrayList();
    for (String word : words) {
      String lowercaseWord = word.toLowerCase();
      if (!STOP_WORDS.contains(lowercaseWord)) {
        filtered.add(lowercaseWord);
      }
    }
    return filtered;
  }
}
