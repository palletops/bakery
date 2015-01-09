(defproject com.palletops/bakery-sente "0.3.0"
  :description "A leaven component for sente"
  :url "https://github.com/palletops/bakery"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-modules "0.3.10"]]
  :prep-tasks [["cljx" "once"]]
  :resource-paths ["src/cljs" "target/generated/src/cljs"]
  :source-paths ["src/clj" "target/generated/src/clj"]
  :dependencies [[com.taoensso/sente "1.0.0"
                  :exclusions [com.keminglabs/cljx]]
                 [compojure "1.1.8"]]
  :cljx {:builds [{:source-paths ["src/cljx"]
                   :output-path "target/generated/src/clj"
                   :rules :clj}
                  {:source-paths ["src/cljx"]
                   :output-path "target/generated/src/cljs"
                   :rules :cljs}]})
