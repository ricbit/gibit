package com.ricbit.gibit.server;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.appengine.repackaged.com.google.common.base.CharMatcher;
import com.google.appengine.repackaged.com.google.common.base.Joiner;
import com.google.appengine.repackaged.com.google.common.base.Splitter;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableSet;
import com.google.appengine.repackaged.com.google.common.collect.Lists;

public class Query {
  private static final Set<String> STOP_WORDS = ImmutableSet.of("the", "of");  
  private static final Splitter SPLITTER = 
      Splitter.on(CharMatcher.JAVA_LETTER_OR_DIGIT.negate()).omitEmptyStrings().trimResults();
  private static final Joiner JOINER = Joiner.on(" ");

  private List<String> queryTerms;
  private boolean debug = false;
  
  public Query(String query) {
    if (query.contains("debug:")) {
      query = query.replace("debug:", "");
      debug = true;
    }
    queryTerms = split(query);
    Collections.sort(queryTerms);
  }
  
  public List<String> getQueryTerms() {
    return queryTerms;
  }
  
  public String getNormalizedQuery() {
    return JOINER.join(queryTerms);
  }
  
  public boolean isDebug() {
    return debug;
  }  

  /**
   * Splits an input string into words, taking into account unicode and stop words.
   * All words are converted to lower case.
   * 
   * @param input input string
   * @return a list of lowercase, unicode words with no stop words
   */
  private List<String> split(String input) {
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
