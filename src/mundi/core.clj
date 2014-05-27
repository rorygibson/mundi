(ns mundi.core
  (:require [clojure.zip :as zip]
            [mundi.io :refer :all]
            [mundi.time :refer :all]))


(def change-freqs #{:always :hourly :daily :weekly :monthly :yearly :never})


(def default-change-freq :always)


(defn sitemap?
  "Returns true if the root element of the XML is a <urlset>"
  [^String xml]
  (if xml
    (let [t (parse xml)
          z (zip/xml-zip t)]
      (= :urlset (:tag (zip/node z))))))


(defn sitemap-index?
  "Returns true if the root element of the XML is a <sitemapindex>"
  [^String xml]
  (if xml
    (let [t (parse xml)
          z (zip/xml-zip t)]
      (= :sitemapindex (:tag (zip/node z))))))


(defn- find-urls-in-tree-zipper
  "Find all the URL nodes in a tree of XML, and return maps containing their child URIs (loc tag content), last-modified date, changefreq, priority"
  [loc]
  (loop [loc loc
         found '()]
    
    (if (zip/end? loc)
      found

      (if (= :url (:tag (zip/node loc)))
        (let [children (zip/children loc)              
              
              uri (content (filter-for-tag :loc children))
              last-mod (to-inst (content (filter-for-tag :lastmod children)))
              change-freq (to-validated-keyword (content (filter-for-tag :changefreq children)) change-freqs default-change-freq)
              priority (to-number (content (filter-for-tag :priority children)))
              
              url {:loc uri :last-modified last-mod :change-freq change-freq :priority priority}]
          (recur (zip/next loc) (cons url found)))
        (recur (zip/next loc) found)))))


(defn- find-sitemaps-in-tree-zipper
  "Find all the SiteMap nodes in a tree of XML, and return maps containing their child URIs (loc tag content) & last-modified date"
  [loc]
  (loop [loc loc
         found '()]
    (if (zip/end? loc)
      found

      (if (= :sitemap (:tag (zip/node loc)))
        (let [children (zip/children loc)           
              uri (content (filter-for-tag :loc children))
              last-mod (to-inst (content (filter-for-tag :lastmod children)))              
              smi {:loc uri :last-modified last-mod}]
          (recur (zip/next loc) (cons smi found)))
        (recur (zip/next loc) found)))))


(defn find-sitemaps
  "Find all the SiteMaps in a SiteMapIndex, return a seq of {:loc \"http://...\" :last-modified <date>} elements"
  [^String xml]
  (let [t (parse xml)
        z (zip/xml-zip t)]
    (find-sitemaps-in-tree-zipper z)))


(defn find-urls
  "Find all the URLs in the sitemap, return a seq of {:loc \"http://...\" :last-modified <date> elements."
  [^String xml]
  (let [t (parse xml)
        z (zip/xml-zip t)
        urls (find-urls-in-tree-zipper z)]
    urls))


