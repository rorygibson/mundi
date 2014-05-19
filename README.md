# clj-sitemap

A Clojure library for reading and interpreting [http://www.sitemaps.org](sitemaps).


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

(def beeb (:body (client/get "http://www.bbc.co.uk/sport/sitemap.xml")))
(count (sm/find-urls beeb))
=> 493
```


## TODO
+ parse for SiteMapIndex elements
+ respect and follow SiteMapIndex elements - need to provide a fetch function to do this
+ lastmod
+ changefreq
+ priority



## License

Copyright © 2014 Rory Gibson

Distributed under the Eclipse Public License either version 1.0.