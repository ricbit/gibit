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

# Tools to dump the data from the GCD MySQL database and 
# build an index to be used in the datastore.

import codecs
import gflags
import MySQLdb
import sys
import unicodedata

FLAGS = gflags.FLAGS

gflags.DEFINE_string("index_type", "direct", "Type of index to be built, either 'direct' or 'inverse'")
gflags.DEFINE_string("mysql_user", None, "User to connect on the MySQL database")
gflags.DEFINE_string("mysql_password", None, "Password of the user to connect on the MySQL database")
gflags.DEFINE_string("index_output", "direct.txt", "File name where the index will be stored")

def load_series(db_user, db_password):
  database = load_database(db_user, db_password)
  cursor = database.cursor()
  cursor.execute("SELECT id,name,issue_count,year_began,publisher_id,has_gallery FROM gcd_series")
  return cursor.fetchall()
  
def load_database(db_user, db_password):    
  return MySQLdb.connect(db="gibi", user=db_user, passwd=db_password)

def load_publisher(db_user, db_password):    
  database = load_database(db_user, db_password)
  cursor = database.cursor()
  cursor.execute("SELECT id,name FROM gcd_publisher")
  return cursor.fetchall()
  
def word_split(original):
  decoded_word = unicode(original, "latin1")
  word_list = []
  current = []  
  for letter in decoded_word:
    if letter.isalnum():
      current.append(letter)
    else:
      if current:
        word_list.append(u"".join(current))  
      current = []
  if current:
    word_list.append(u"".join(current))  
  return [word.lower() for word in word_list]
  
def remove_accents(words):
  output = []
  for word in words:
    norm = unicodedata.normalize("NFKD", word)
    removed = u"".join(c for c in norm if not unicodedata.combining(c))
    if (removed != word):
      output.append(removed)
  return output

def build_inverted_index(data):
  inverted = {}
  for series in data:  
    words = word_split(series[1])    
    words += remove_accents(words)
    for word in words:
      if word in inverted:
        inverted[word].append(series[0])
      else:
        inverted[word] = [series[0]]
  return inverted
  
def build_direct_index(data):
  direct = {}
  for series in data:
    direct[series[0]] = series
  return direct
  
def search(series, direct, inverse):
  words = word_split(series)    
  choices = reduce(lambda x,y: x.intersection(y), (set(inverse[word]) for word in words))
  return [direct[choice] for choice in choices]
  
def dump_inverted_index(user, password, filename):
  data = load_series(user, password)
  inv = build_inverted_index(data)            
  f = codecs.open(filename, "w", "utf-8")
  for key,value in inv.iteritems():
    if len(value) < 5000:
      f.write(u"%s;%s\n" % (key, ":".join(str(i) for i in value)))
    else:
      print "ignored: " + key
  f.close()
  
def decode(name):
  clean_name = name.replace('"', "'")
  return unicode('"%s"' % clean_name, "latin1")
  
def dump_direct_index(user, password, filename):
  data = load_series(user, password)
  publisher_data = dict(load_publisher(user, password))
  f = codecs.open(filename, "w", "utf-8")
  for id,name,issues,year,publisher,has_gallery in data:
    f.write(u"%s:%s,%s,%s,%s\n" % (id,issues,year,decode(name),decode(publisher_data[publisher])))
  f.close()
  
if __name__ == "__main__":
  FLAGS(sys.argv)
  if FLAGS.index_type == "direct":
    dump_direct_index(FLAGS.mysql_user, FLAGS.mysql_password, FLAGS.index_output)
  elif FLAGS.index_type == "inverse":
    dump_inverted_index(FLAGS.mysql_user, FLAGS.mysql_password, FLAGS.index_output)