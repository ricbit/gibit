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

import static org.easymock.EasyMock.expect;
import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.easymock.classextension.IMocksControl;

import com.google.inject.Provider;
import com.ricbit.gibit.server.Instrumentation.MeasurableCode;
import com.ricbit.gibit.shared.TimeMeasurementsDto;

public class InstrumentationTest extends TestCase {
  private static final int STAGE = 0;
  private static final int OUTPUT = 321;
  private static final int INPUT = 123;
  
  private IMocksControl control;
  private Provider<Long> timestamp;
  private Instrumentation subject;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    control = EasyMock.createStrictControl();
    timestamp = createLongProvider();
    subject = new Instrumentation(timestamp);
  }
  
  public void testRun() throws Exception {
    TimeMeasurementsDto dto = new TimeMeasurementsDto();
    subject.setMeasurementDto(dto);
    MeasurableCode<Integer, Integer> code = createMeasurableCode();
    expect(timestamp.get()).andReturn(300L);
    expect(code.run(INPUT)).andReturn(OUTPUT);
    expect(timestamp.get()).andReturn(400L);
    control.replay();
    
    int output = subject.measure(STAGE, 123, code);
    assertEquals(OUTPUT, output);
    assertEquals(100L, dto.getMeasure(STAGE));
    control.verify();
  }

  @SuppressWarnings("unchecked")
  private MeasurableCode<Integer, Integer> createMeasurableCode() {
    return control.createMock(MeasurableCode.class);
  }

  @SuppressWarnings("unchecked")
  private Provider<Long> createLongProvider() {
    return control.createMock(Provider.class);
  }
}
