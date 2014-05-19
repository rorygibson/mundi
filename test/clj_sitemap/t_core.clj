(ns clj-sitemap.t-core
  (:use [midje.sweet])
  (:require [clj-sitemap.core :refer :all]))


(def two-urls-xml "<urlset><url><loc>http://foo.com</loc></url><url><loc>http://bar.com</loc></url></urlset>")


(fact "Finds the (single) location in a very simple sitemap with one location"
  (first (find-locs "<urlset><url><loc>http://foo.com</loc></url></urlset>"))
  => "http://foo.com")


(fact "Finds all the locations in a simple sitemap with two locations"
  (count (find-locs two-urls-xml))
  => 2)


(fact "Finds all the URLs in a sitemap"
  (find-urls two-urls-xml)
  => [{:loc "http://foo.com"} {:loc "http://bar.com"}])



