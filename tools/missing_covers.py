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

# Dump the ids of all series whose cover has not been downloaded yet.

import gflags
import indextools
import os
import sys

FLAGS = gflags.FLAGS

gflags.DEFINE_string("output", "missing.txt", "File name where the ids will be stored")

FLAGS(sys.argv)
series = indextools.load_series(FLAGS.mysql_user, FLAGS.mysql_password)
missing = [x[0] for x in series if x[5] and not os.path.exists("%d.jpg" % x[0])]
f = open(FLAGS.output, "w")
for miss in missing:
  f.write("%d\n" % miss)
f.close()