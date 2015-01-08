(defproject com.palletops/bakery-core-async "0.2.1-SNAPSHOT"
  :description "A leaven component for a core-async channel."
  :url "https://github.com/palletops/bakery"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]

  :resource-paths ["src/cljs" "target/generated/src/cljs"]
  :source-paths ["src/clj" "target/generated/src/clj"]
  :plugins [[lein-modules "0.3.10"]]
  :prep-tasks [["cljx" "once"]]
  :cljx {:builds [{:source-paths ["src/cljx"]
                   :output-path "target/generated/src/clj"
                   :rules :clj}
                  {:source-paths ["src/cljx"]
                   :output-path "target/generated/src/cljs"
                   :rules :cljs}]})
