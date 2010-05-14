# Copyright (C) 2010 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Author: Ricardo Bittencourt (bluepenguin@gmail.com)

# Uploads the inverted index to datastore (entity "SeriesInvertedIndex").

# Use with appcfg:
# python appcfg.py upload_data --config_file=inverted_index_loader.py \
#     --filename=inverse.txt --kind=SeriesInvertedIndex app

from google.appengine.ext import db
from google.appengine.tools import bulkloader
import models

def numberListConverter(str):
  values = str.split(";")[1]
  return [long(i) for i in values.split(":")]

class SeriesInvertedIndexLoader(bulkloader.Loader):
  def __init__(self):
    bulkloader.Loader.__init__(
        self, 'SeriesInvertedIndex',
        [('seriesNumberList', numberListConverter)])
  def generate_key(self, number, values):
    return values[0].split(";")[0].decode("utf-8")
        
loaders = [SeriesInvertedIndexLoader]        