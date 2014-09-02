(defproject com.palletops/bakery-sente "0.1.1"
  :description "A leaven component for sente"
  :url "https://github.com/palletops/bakery"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-modules "0.3.8"]]
  :resource-paths ["src/cljs"]
  :source-paths ["src/clj"]
  :dependencies [[com.taoensso/sente "1.0.0"
                  :exclusions [com.keminglabs/cljx]]
                 [compojure "1.1.8"]])
