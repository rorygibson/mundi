(ns mundi.io
  (:require [clojure.xml])
  (:import [java.io InputStream StringWriter ByteArrayInputStream]))


(defn filter-for-tag
  "Filter for an XML node with a given tag name"
  [tag-name els]
  (filter #(= (:tag %) tag-name) els))


(defn content
  "Unwrap the content structure of the XML nodes"
  [n]
  (first (:content (first n))))


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


(defn parse
  "Parse XML provided as a string"
  [xml]
  (clojure.xml/parse (stream-from xml)))


(defn to-number
  "Convert a string representing a number between 0.1 and 1.0, to a number"
  [s]
  (if (and s (re-find #"^\d+\.\d+$" s))
    (read-string s)
    0.5))


(defn to-validated-keyword
  "Given a string value v, validate and convert it into one of a set of lowercased keywords ks. If validation fails, return the default."
  [^String v ks default]
    (let [v (if v (keyword (.toLowerCase v)) :INVALID)]
      (if (some #{v} ks)
        v
        default)))
