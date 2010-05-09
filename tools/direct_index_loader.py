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

# Uploads the direct index to datastore (entity "Series").

# Use with appcfg:
# python appcfg.py upload_data --config_file=direct_index_loader.py \
#     --filename=direct.txt --kind=Series app

from google.appengine.ext import db
from google.appengine.tools import bulkloader
import models

def numberListConverter(str):
  values = str.split(";")[1]
  return [long(i) for i in values.split(":")]

class SeriesLoader(bulkloader.Loader):
  def __init__(self):
    bulkloader.Loader.__init__(
        self, 'Series',
        [('issues', lambda x: int(x.split(":")[1])), ('year', lambda x: int(x)), 
         ('name', lambda x: x.decode("utf-8")), ('publisher', lambda x: x.decode("utf-8")),
         ('hascover', lambda x: bool(int(x)))])
  def generate_key(self, number, values):
    return values[0].split(":")[0]
        
loaders = [SeriesLoader]        