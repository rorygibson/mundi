(ns clj-sitemap.t-core
  (:use [midje.sweet])
  (:require [clj-sitemap.core :refer :all]
            [clj-sitemap.io :refer [todays-date]]))


(def jan-2nd-2014-date (java.util.Date. 114 0 02))


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


(fact "Supports loc tag"
  (:loc (first (find-urls two-urls-xml)))
  => "http://bar.com"

  (:loc (second (find-urls two-urls-xml)))
  => "http://foo.com")


(fact "Supports lastmodified"
  (:last-modified (second (find-urls two-urls-xml)))
  => jan-2nd-2014-date)


(fact "If lastmodified is not supplied, it defaults to today"
  (:last-modified (first (find-urls two-urls-xml)))
  => (todays-date))


(fact "If lastmodified is supplied as an incorrect value, it defaults to today"
  (:last-modified (first (find-urls "<urlset><url><loc>http://x.com</loc><lastmodified>UH-OH</lastmodified></url></urlset>")))
  => (todays-date))


(fact "Supports changefreq"
  (:change-freq (first (find-urls two-urls-xml)))
  => :monthly)


(fact "If the changefreq is missing, default to :always"
  (:change-freq (second (find-urls two-urls-xml)))
  => :always)


(fact "The changefreq will always be lowercased"
  (:change-freq (first (find-urls "<urlset><url><loc>http://</loc><changefreq>MONTHLY</changefreq></url></urlset>")))
  => :monthly)


(fact "The changefreq is validated against  always | hourly | daily | weekly | monthly | yearly | never - and returns :always if invalid"
  (:change-freq (first (find-urls "<urlset><url><loc>http://</loc><changefreq>always</changefreq></url></urlset>"))) => :always
  (:change-freq (first (find-urls "<urlset><url><loc>http://</loc><changefreq>hourly</changefreq></url></urlset>"))) => :hourly
  (:change-freq (first (find-urls "<urlset><url><loc>http://</loc><changefreq>daily</changefreq></url></urlset>"))) => :daily
  (:change-freq (first (find-urls "<urlset><url><loc>http://</loc><changefreq>weekly</changefreq></url></urlset>"))) => :weekly
  (:change-freq (first (find-urls "<urlset><url><loc>http://</loc><changefreq>monthly</changefreq></url></urlset>"))) => :monthly
  (:change-freq (first (find-urls "<urlset><url><loc>http://</loc><changefreq>yearly</changefreq></url></urlset>"))) => :yearly
  (:change-freq (first (find-urls "<urlset><url><loc>http://</loc><changefreq>never</changefreq></url></urlset>"))) => :never
    (:change-freq (first (find-urls "<urlset><url><loc>http://</loc><changefreq>INVALID</changefreq></url></urlset>"))) => :always)


(fact "Supports priority"
  (:priority (first (find-urls two-urls-xml)))
  => 0.7)


(fact "If priority is not supplied, it defaults to 0.5"
  (:priority (second (find-urls two-urls-xml)))
  => 0.5)


(fact "If priority is supplied, but is not a decimal number between 0.0 and 1.0, then it defaults to 0.5"
  (:priority (first (find-urls "<urlset><url><loc>http://foo.com</loc><priority>x</priority></url></urlset>")))
  => 0.5)



