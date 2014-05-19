(ns clj-sitemap.io
  (:import [java.io InputStream StringWriter ByteArrayInputStream]))


(defn string-from
  "Read the contents of an InputStream into a String"
  [^InputStream st]
  (with-open [rdr (clojure.java.io/reader st)]
    (clojure.string/join "\n" (line-seq rdr))))


(defn stringify
  "Return a lowercased version of a string (calls toString on anything that isn't already a String)"
  [s]
  (let [^String s (.toString s)]
    (.toLowerCase s)))


(defn stream-from
  "Given a string, return an InputStream on the content"
  [^String st]
  (ByteArrayInputStream. (.getBytes st "UTF-8")))


(defn to-number
  "Convert a string representing a number between 0.1 and 1.0, to a number"
  [s]
  (if (and s (re-find #"^\d+\.\d+$" s))
    (read-string s)
    0.5))
