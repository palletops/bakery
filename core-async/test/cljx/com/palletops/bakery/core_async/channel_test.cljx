(ns com.palletops.bakery.core-async.channel-test
  (:require
   #+cljs [cljs.core.async :as async]
   #+clj [clojure.core.async :as async]
   [com.palletops.leaven :as leaven]
   [com.palletops.bakery.core-async.channel :refer [channel]]
   #+clj [clojure.test :refer [deftest is testing]]
   #+cljs [cemerick.cljs.test :refer-macros [deftest is testing]]))

(deftest instantiate-test
  (testing "default channel"
    (is (channel {}))
    (is (:chan (leaven/start (channel {}))))
    (is (nil? (:chan (leaven/stop (leaven/start (channel {})))))))
  (testing "channel function"
    (is (channel {:chan-f #(async/chan 1)}))
    (is (:chan (leaven/start (channel {}))))))
