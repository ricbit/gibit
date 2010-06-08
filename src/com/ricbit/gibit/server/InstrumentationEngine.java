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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ricbit.gibit.shared.TimeMeasurementsDto;

public class InstrumentationEngine implements MethodInterceptor {
  @Inject
  @Timestamp private Provider<Long> timestampProvider; 

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Measure measure = invocation.getMethod().getAnnotation(Measure.class);
    Object[] arguments = invocation.getArguments();
    
    long start = timestampProvider.get();
    Object result = invocation.proceed();
    long end = timestampProvider.get();
    
    TimeMeasurementsDto dto = (TimeMeasurementsDto) arguments[arguments.length - 1];
    dto.setMeasure(measure.value(), (int) (end - start));
    return result;
  }
}
