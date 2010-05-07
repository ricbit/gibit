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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ricbit.gibit.shared.SeriesDto;

/**
 * Rank search results.
 * 
 * @author Ricardo Bittencourt (bluepenguin@gmail.com)
 */
public class RankingEngine {
  /**
   * Rank a list of series.
   * 
   * @param seriesList the list of series to be ranked.
   */
  public void rankSeries(List<SeriesDto> seriesList) {
    // Currently we only sort by issues number. We suppose a series with more issues
    // is more likely to be important than a series with less issues.
    Collections.sort(seriesList, new Comparator<SeriesDto>() {
      @Override
      public int compare(SeriesDto dto1, SeriesDto dto2) {
        if (dto1.getIssues() > dto2.getIssues()) {
          return -1;
        } else if (dto1.getIssues() < dto2.getIssues()) {
          return 1;
        }
        return 0;
      }
    });
  }
}
