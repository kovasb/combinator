(defproject combinator "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :jvm-opts ["-server"
             "-Xmx1G"]
  :dependencies [
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/core.match "0.2.0"]
                 [fast-zip "0.3.0"]
                 [clj-tuple "0.1.3-SNAPSHOT"] ;[clj-tuple "0.1.2"]
                 [org.clojure/data.finger-tree "0.0.1"]
                 ])
