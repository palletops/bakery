(defproject com.palletops/bakery-weasel "0.2.1-SNAPSHOT"
  :description "A leaven component for weasel"
  :url "https://github.com/palletops/bakery"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-modules "0.3.10"]]
  :resource-paths ["src/cljs"]
  :source-paths ["src/clj"]
  :dependencies [[weasel "0.4.2"]
                 [com.cemerick/piggieback "0.1.3"]])
