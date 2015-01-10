{:provided {:dependencies [[org.clojure/clojurescript "0.0-2277"]]}
 :cljs-test {:dependencies [[com.cemerick/clojurescript.test "0.3.3"]]
             :plugins [[lein-cljsbuild "1.0.3"]]
             :cljsbuild
             {:test-commands
              {"tests" ["phantomjs" "runners/runner-none.js"
                        "target/unit-test" "target/unit-test.js"]}
              :builds
              [{:id "test"
                :source-paths ["target/generated/src/clj"
                               "target/generated/src/cljs"
                               "target/generated/test/cljs"]
                :compiler {:output-to "target/unit-test.js"
                           :output-dir "target/unit-test"
                           :source-map "target/unit-test.js.map"
                           :optimizations :none
                           :pretty-print true}}]}
             :aliases {"test" ["do" "test,"
                               "cljsbuild" "once" "test,"
                               "cljsbuild" "test"]}}
 :test-base {:cljx {:builds [{:source-paths ["test/cljx"]
                              :output-path "target/generated/test/clj"
                              :rules :clj}
                             {:source-paths ["test/cljx"]
                              :output-path "target/generated/test/cljs"
                              :rules :cljs}]}
             :test-paths ["target/generated/test/clj"]}
 :dev-base {:plugins [[com.keminglabs/cljx "0.5.0"]]}
 :dev [:dev-base :test-base :cljs-test]}
