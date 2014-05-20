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
  "Find all the URL nodes in a tree of XML, and return maps containing their child URIs (loc tag content), last-modified date etc."
  [loc]
  (loop [loc loc
         found '()]
    
    (if (zip/end? loc)
      found

      (if (= :url (:tag (zip/node loc)))
        (let [children (zip/children loc)
              loc-child (filter #(= (:tag %) :loc) children)
              last-mod-child (filter #(= (:tag %) :lastmodified) children)
              change-freq-child (filter #(= (:tag %) :changefreq) children)
              priority-child (filter #(= (:tag %) :priority) children)
              
              uri (first (:content (first loc-child)))
              last-mod (to-inst (first (:content (first last-mod-child))))
              change-freq (first (:content (first change-freq-child)))
              priority (to-number (first (:content (first priority-child))))
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
