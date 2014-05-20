(ns mundi.core
  (:require [clojure.xml]
            [clojure.zip :as zip]
            [mundi.io :refer :all]))

(defn- parse
  "Parse XML provided as a string"
  [xml]
  (clojure.xml/parse (stream-from xml)))


(defn- to-changefreq
  "Given a string representing a changefreq, convert it into a keyword representing a valid changefreq value; default to :always if the string did not represent a valid changefreq value."
  [^String s]
  (let [v (if s (keyword (.toLowerCase s)) :INVALID)]
    (if (some #{v} #{:always :hourly :daily :weekly :monthly :yearly :never})
      v
      :always)))


(defn to-number
  "Convert a string representing a number between 0.1 and 1.0, to a number"
  [s]
  (if (and s (re-find #"^\d+\.\d+$" s))
    (read-string s)
    0.5))


(defn- content
  "Unwrap the content structure of the XML nodes"
  [n]
  (first (:content (first n))))


(defn- filter-for
  "Filter for an XML node with a given tag name"
  [tag-name els]
  (filter #(= (:tag %) tag-name) els))


(defn- find-nodes-in-tree-zipper
  "Find all the URL nodes in a tree of XML, and return maps containing their child URIs (loc tag content), last-modified date, changefreq, priority"
  [loc]
  (loop [loc loc
         found '()]
    
    (if (zip/end? loc)
      found

      (if (= :url (:tag (zip/node loc)))
        (let [children (zip/children loc)              
              
              uri (content (filter-for :loc children))
              last-mod (to-inst (content (filter-for :lastmodified children)))
              change-freq (to-changefreq (content (filter-for :changefreq children)))
              priority (to-number (content (filter-for :priority children)))
              
              url {:loc uri :last-modified last-mod :change-freq change-freq :priority priority}]
          (recur (zip/next loc) (cons url found)))
        (recur (zip/next loc) found)))))


(defn find-urls
  "Find all the URLs in the sitemap, return a seq of {:loc \"http://...\" :last-modified <datestr> elements"
  [^String xml]
  (let [t (parse xml)
        z (zip/xml-zip t)]
    (find-nodes-in-tree-zipper z)))


(defn find-locs
  "Return a list of all URIs in a sitemap"
  [^String xml]
  (let [urls (find-urls xml)]
    (map :loc urls)))
