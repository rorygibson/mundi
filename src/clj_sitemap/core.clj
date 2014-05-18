(ns clj-sitemap.core
  (:require [clojure.xml]
            [clojure.zip :as zip]
            [clj-sitemap.io :refer :all]
            [clj-sitemap.io :refer [stream-from]]))

(defn parse
  "Parse XML provided as a string"
  [xml]
  (clojure.xml/parse (stream-from xml)))


(defn find-nodes-in-tree-zipper
  "Given a tree zipper, return all the values of the nodes whose name matches Name"
  [z ^String name]
  (loop [loc z
         found #{}]
    (let [cur-name (:tag (zip/node loc))
          cur-val (zip/node (zip/next loc))
          match (= (keyword name) cur-name)]
      
      (if (zip/end? loc)
        found

        (if match
          (recur (zip/next loc) (clojure.set/union #{cur-val} found))
          (recur (zip/next loc) found))))))


(defn find-locs
  "Find all the LOC nodes in the sitemap XML."
  [xml]
  (let [t (parse xml)
        z (zip/xml-zip t)]
    (find-nodes-in-tree-zipper z "loc")))
