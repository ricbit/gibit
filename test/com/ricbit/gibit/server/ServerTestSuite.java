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

import junit.framework.Test;
import junit.framework.TestSuite;

public class ServerTestSuite {
  private static final Class<?>[] tests = {
    InstrumentationTest.class,
    KeyGeneratorTest.class,
    MemcacheImplTest.class,
    QueryTest.class,
    RankingEngineTest.class,
    SearchServiceImplTest.class,
    SetUtilsTest.class
  };
  
  public static Test suite() {
    return new TestSuite(tests);
  }
  
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());  
  }
}