(ns clj-sitemap.t-core
  (:use [midje.sweet])
  (:require [clj-sitemap.core :refer :all]))


(fact "Loads a very simple single-URL sitemap"
  (first (find-locs "<urlset><url><loc>http://foo.com</loc></url></urlset>"))
  => "http://foo.com")
