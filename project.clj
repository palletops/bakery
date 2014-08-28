(defproject com.palletops/bakery "0.1.0-SNAPSHOT"
  :description "A library of leaven components"
  :url "https://github.com/palletops/bakery"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :modules {:inherited
            {:dependencies [[com.palletops/leaven "0.1.1"
                             :exclusions [com.keminglabs/cljx]]]}}
  :source-paths ["src/clj"]
  :resource-paths ["src/cljs"]
  :plugins [[lein-modules "0.3.8"]]
  :aliases {"install" ["modules" "install"]
            "deploy" ["modules" "deploy"]
            "clean" ["with-profiles" "+no-subprocess" "modules" "clean"]})
