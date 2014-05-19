# clj-sitemap

A Clojure library for reading and interpreting [http://www.sitemaps.org](sitemaps).

Provides a minimal skin over the semantics of the sitemaps spec; effectively just gathers the data and gives you back nice, Clojure-ish maps and lists.


## Usage

Include it in your project.clj (you'll probably also want something like clj-http to actually retrieve the files from the remote server)

```clojure
[clj-sitemap "0.1.0-SNAPSHOT"]
[clj-http "0.9.1"]
```

Then from a REPL:

```clojure
(require [clj-http.client :as client])
(require [clj-sitemap.core :as sm]

(def beeb-data (:body (client/get "http://www.bbc.co.uk/sport/sitemap.xml")))

(def beeb-urls (sm/find-urls beeb-data))

(count beeb-urls)
=> 493

(first beeb-urls)
=> {:loc "http://www.bbc.co.uk..." :change-freq "monthly" :priority 0.5 :last-modified "2014-01-29"}

```


## TODO
+ parse for SiteMapIndex elements
+ respect and follow SiteMapIndex elements - need to provide a fetch function to do this
+ parse lastmod into some kind of non-string time structure (joda?)
+ convert priority into a number between 0.0 and 1.0, and default to 0.5 if it doesn't parse
+ validate changefreq against always | hourly | daily | weekly | monthly | yearly | never - and default to always if it doesn't validate

## License

Copyright © 2014 Rory Gibson

Distributed under the Eclipse Public License either version 1.0.