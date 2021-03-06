(defproject mundi "0.1.0-SNAPSHOT"
  :description "A Clojure library for reading site maps (www.sitemaps.org)"

  :url "http://github.com/rorygibson/mundi"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.logging "0.2.6"]]

  :profiles {
             :dev {
                   :resource-paths ["test/resources"]
                   :dependencies [[midje "1.6.3"]
                                  [clj-http "0.9.1"]
                                  [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                                     javax.jms/jms
                                                                     com.sun.jdmk/jmxtools
                                                                     com.sun.jmx/jmxri]]]
                   :plugins      [[lein-ancient "0.5.4" :exclusions [org.clojure/clojure commons-codec org.clojure/data.xml]]
                                  [lein-midje "3.1.1"]]}
             })
