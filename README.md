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
user=> (require [clj-http.client :as client])
user=> (require [clj-sitemap.core :as sm]

user=> (def beeb-data (:body (client/get "http://www.bbc.co.uk/sport/sitemap.xml")))

user=> (def beeb-urls (sm/find-urls beeb-data))

user=> (count beeb-urls)
493

user=> (first beeb-urls)
{:loc "http://www.bbc.co.uk..." :change-freq "monthly" :priority 0.5 :last-modified #inst "2014-05-20T00:00:00.000+01:00"}

```


## TODO
+ parse for SiteMapIndex elements
+ respect and follow SiteMapIndex elements - need to provide a fetch function to do this
+ validate changefreq against always | hourly | daily | weekly | monthly | yearly | never - and default to always if it doesn't validate

## License

Copyright © 2014 Rory Gibson

Distributed under the Eclipse Public License either version 1.0.