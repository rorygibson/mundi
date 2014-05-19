(ns clj-sitemap.t-core
  (:use [midje.sweet])
  (:require [clj-sitemap.core :refer :all]))


(def two-urls-xml
  "<urlset>
     <url>
       <loc>http://foo.com</loc>
       <lastmodified>2014-01-02</lastmodified>
     </url>

     <url>
       <loc>http://bar.com</loc>
       <changefreq>monthly</changefreq>
       <priority>0.7</priority>
     </url>
  </urlset>")

 
(fact "Finds the (single) location in a very simple sitemap with one location"
  (first (find-locs "<urlset><url><loc>http://foo.com</loc></url></urlset>"))
  => "http://foo.com")


(fact "Finds all the locations in a simple sitemap with two locations"
  (count (find-locs two-urls-xml))
  => 2)


(fact "Supports lastmodified"
  (:last-modified (first (find-urls two-urls-xml)))
  => nil

  (:last-modified (second (find-urls two-urls-xml)))
  => "2014-01-02")


(fact "Supports changefreq"
  (:change-freq (first (find-urls two-urls-xml)))
  => "monthly"

  (:change-freq (second (find-urls two-urls-xml)))
  => nil)


(fact "Supports priority"
  (:priority (first (find-urls two-urls-xml)))
  => "0.7"

  (:priority (second (find-urls two-urls-xml)))
  => nil)


(fact "Supports loc"
  (:loc (first (find-urls two-urls-xml)))
  => "http://bar.com"

  (:loc (second (find-urls two-urls-xml)))
  => "http://foo.com")

