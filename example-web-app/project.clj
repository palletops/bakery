(defproject com.palletops/bakery-web-app "0.1.0-SNAPSHOT"
  :description "A web app with bakery"
  :url "http://palletops.com/example.webapp"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}

  :min-lein-version "2.3.4"

  :source-paths ["src/clj" "src/cljs" "target/generated"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2277"]
                 [org.clojure/core.async "0.1.303.0-886421-alpha"
                  :exclusions [[org.clojure/clojure]]]

                 [com.palletops/bakery-httpkit "0.1.0-SNAPSHOT"]
                 [com.palletops/bakery-weasel "0.1.0-SNAPSHOT"]
                 [com.palletops/bakery-sente "0.1.0-SNAPSHOT"]
                 [com.palletops/bakery-secretary "0.1.0-SNAPSHOT"]
                 [com.palletops/bakery-local-storage-atom "0.1.0-SNAPSHOT"]
                 [com.palletops/bakery-om-root "0.1.0-SNAPSHOT"]

                 ;; client
                 [prismatic/om-tools "0.3.3"
                  :exclusions [org.clojure/clojure com.keminglabs/cljx]]
                 [racehub/om-bootstrap "0.2.6"
                  :exclusions [org.clojure/clojure prismatic/om-tools
                               com.keminglabs/cljx]]

                 ;; server
                 [org.clojure/tools.cli "0.3.1"]
                 [org.clojure/core.match "0.2.1"]
                 [ring "1.3.0"]
                 [ring/ring-headers "0.1.0"]
                 [ring/ring-anti-forgery "1.0.0"]
                 [hickory "0.5.3"]
                 [cheshire "5.3.1"]
                 [com.taoensso/timbre "3.2.1"]]

  :plugins [[lein-cljsbuild "1.0.3"]]
  :hooks [leiningen.cljsbuild]

  :jar-exclusions [#"\.cljx|\.swp|\.swo|\.DS_Store"]

  :profiles
  {:dev {:resource-paths ["resources"]
         :dependencies [[com.facebook/react "0.11.1"]
                        [com.cemerick/piggieback "0.1.3"]
                        [weasel "0.3.0"]]
         :cljsbuild {:builds [{:id "example.webapp"
                               :source-paths ["src/cljs"]
                               :compiler
                               {:output-to "target/classes/public/js/example.webapp.js"
                                :output-dir "target/classes/public/js"
                                :optimizations :none
                                :pretty-print true
                                :source-map true}}]}}
   :repl {:injections [(try
                         (require
                          '[example.webapp.server.dev
                            :refer [start stop start-cljs-repl!]])
                         (catch Exception e
                           (println "Exception on injections" e)))]}
   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

   :uberjar {:cljsbuild
             ^:replace {:builds [{:id "example.webapp"
                                  :source-paths ["src/cljs"]
                                  :compiler
                                  {:output-to "target/classes/public/js/example.webapp.min.js"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}]}
             :aot [example.webapp.main #"example.webapp.services\..*"]
             :main example.webapp.main
             :omit-source true}})
