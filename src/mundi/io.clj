(ns mundi.io
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
  "Given a string, return an InputStream over the content of the string"
  [^String st]
  (ByteArrayInputStream. (.getBytes st "UTF-8")))


(defn todays-date
  "Return a java.util.Date with the hours, minutes, secons and millis zeroed"
  []
  (doto (java.util.Calendar/getInstance)
    (.set java.util.Calendar/HOUR 0)
    (.set java.util.Calendar/MINUTE 0)
    (.set java.util.Calendar/SECOND 0)
    (.set java.util.Calendar/MILLISECOND 0)))


(defn to-inst
  "Given a string representing a timestamp (in W3C datetime format, http://www.w3.org/TR/NOTE-datetime), return an instant"
  [^String s]
  (try
    (clojure.instant/read-instant-date s)
    (catch Exception e
      (todays-date))))
