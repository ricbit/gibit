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

# Crawls the series covers from the gcd site.

import gflags
import os
import random
import re
import sys
import threading
import urllib2

FLAGS = gflags.FLAGS

gflags.DEFINE_string("covers", "missing.txt", "File name from where the covers ids will be read")
gflags.DEFINE_integer("threads", 20, "Number of crawling threads to use")

class Crawler(threading.Thread):
  def __init__(self, id, files):
    threading.Thread.__init__(self)
    self.files = files
    self.id = id
    
  def run(self):
    for line in self.files:
      id = line.strip()
      #print "thread %d: %s" % (self.id, id)
      if os.path.exists("%s.jpg" % id):
        continue
      info_url = "http://www.comics.org/series/%s/" % id
      page = urllib2.urlopen(info_url).read()
      match = re.search('"(http://images\.comics\.org//img.gcd/covers_by_id[^"]+)"', page)
      if match is not None:
        print "thread %d: %s" % (self.id, id)
        image_url = match.group(1)
        image = urllib2.urlopen(image_url).read()
        image_file = open("%s.jpg" % id, "wb")
        image_file.write(image)
        image_file.close()    

FLAGS(sys.argv)
number_threads = FLAGS.threads
input = open(FLAGS.covers, "r").readlines()
random.shuffle(input)
slices = [input[i::number_threads] for i in range(number_threads)]
threads = [Crawler(i, slices[i]) for i in range(number_threads)]
for thread in threads:
  thread.start()
for thread in threads:
  thread.join()
