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

import org.easymock.IArgumentMatcher;
import org.easymock.classextension.EasyMock;

import com.google.appengine.repackaged.com.google.common.collect.Iterables;

public class IterableEquals<T> implements IArgumentMatcher {
  private final Iterable<T> expected;

  public IterableEquals(Iterable<T> expected) {
    this.expected = expected;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean matches(Object actualObject) {
    if (!(actualObject instanceof Iterable<?>)) {
      return false;
    }
    Iterable<T> actual = (Iterable<T>) actualObject;
    return Iterables.elementsEqual(expected, actual);
  }

  @Override
  public void appendTo(StringBuffer buffer) {
    buffer.append(Iterables.toString(expected));
  }  
  
  public static <T, R extends Iterable<T>> R eq(R expected) {
    EasyMock.reportMatcher(new IterableEquals<T>(expected));
    return null;
  }
}
