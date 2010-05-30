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

package com.ricbit.gibit.client;

import static com.ricbit.gibit.shared.TimeMeasurementsDto.PipelineStage.TOTAL;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.ricbit.gibit.shared.TimeMeasurementsDto;

public class DebugWidget extends Composite {
  private static final int[] SCALE = {1, 2, 5};
  
  private Image image;

  public DebugWidget() {
    image = new Image();
    initWidget(image);
  }
  
  public void setTimeMeasurements(TimeMeasurementsDto dto, int totalTime) {
    int overhead = totalTime;
    StringBuffer measures = new StringBuffer();
    for (int i = 0; i < TOTAL; i++) {
      overhead -= dto.getMeasure(i);
      measures.append(Integer.toString(dto.getMeasure(i)) + "|");
    }
    int maxScale = getMaxScale(totalTime);
    String url = 
        // Start with a bar chart, size 700x85.
        "http://chart.apis.google.com/chart?cht=bhs&chs=700x85" +
        // Data values
        "&chd=t:" + measures + overhead +
        // Colors for each section
        "&chco=30c030,3030c0,ffc050,c00000,8000c0,b0b0ff,e0e030" +
        // Data scale
        "&chds=0," + maxScale + 
        // Show x axis
        "&chxt=x,x" +
        // Scale of x axis
        "&chxr=0,0," + maxScale +
        // Legend for x axis
        "&chxl=1:|Time%20spent%20(ms)&chxp=1,50|1,50" +
        // Legends for each section
        "&chdlp=t|l" +
        // Labels for the legend
        "&chdl=Memcache%20Read|Inverse%20Datastore|Intersection|" +
        "Direct%20Datastore|Ranking|Memcache Write|Overhead";
    image.setUrl(url);
    setVisible(true);
  }

  private int getMaxScale(int value) {
    int scalePos = 0;
    int scaleFactor = 1;
    while (SCALE[scalePos] * scaleFactor < value) {
      scalePos++;
      if (scalePos == 3) {
        scalePos = 0;
        scaleFactor *= 10;
      }
    }
    return SCALE[scalePos] * scaleFactor;
  }
}
