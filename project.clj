(defproject combinator "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :jvm-opts ^:replace ["-server"  "-Xmx1G"]
  :dependencies [
                 [org.clojure/clojure "1.5.1"]
                 [fast-zip "0.3.0"]
                 [clj-tuple "0.1.3-SNAPSHOT"]
                 [criterium "0.4.2"]
                 [org.clojure/clojurescript "0.0-2030"]

                 ]
  :source-paths ["src/clj"]
   :cljsbuild
  {
   :builds [{
             :id "combinator"
             ;; The path to the top-level ClojureScript source directory:
             :source-paths ["src/cljs"]
             ;; The standard ClojureScript compiler options:
             ;; (See the ClojureScript compiler documentation for details.)
             :compiler {:output-dir "out"
                        :output-to "combinator.js"
                        :static-fns true
                        :optimizations :advanced}}]}

  :plugins [[lein-cljsbuild "1.0.0-alpha2"]])
