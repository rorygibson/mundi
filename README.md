# Mundi

![Mappa Mundi](/doc/212px-Hereford_Mappa_Mundi_1300.jpg)

A Clojure library for reading and interpreting [http://www.sitemaps.org](sitemaps).

Provides a minimal skin over the semantics of the sitemaps spec; aims to just gather the data and give you back nice, Clojure-ish maps and lists.


## Usage

Include it in your project.clj (you'll probably also want something like clj-http to actually retrieve the files from the remote server)

```clojure
[mundi "0.1.0-SNAPSHOT"]
[clj-http "0.9.1"]
```

Then from a REPL:

```clojure
user=> (require [clj-http.client :as client])
user=> (require [mundi.core :as sm]

user=> (def beeb-data (:body (client/get "http://www.bbc.co.uk/sport/sitemap.xml")))

user=> (def beeb-urls (sm/find-urls beeb-data))

user=> (count beeb-urls)
493

user=> (first beeb-urls)
{:loc "http://www.bbc.co.uk..." :change-freq :monthly :priority 0.5 :last-modified #inst "2014-05-20T00:00:00.000+01:00"}

```

## Features
+ Loads sitemap content from a string (requires the user to fetch the data from the source and convert to string - see examples for how to use e.g. clj-http)
+ Returns standard Clojure structures (maps, lists, keywords, date instants)
+ Provides sensible defaults for missing or invalid values in the sitemap data


## TODO
+ Tests for last-modified values with time portion

## License

Copyright © 2014 Rory Gibson

Distributed under the Eclipse Public License either version 1.0.