(ns example.webapp.null-test
  (:require
   [cemerick.cljs.test :as t :refer-macros [use-fixtures]])
  (:require-macros
   [cemerick.cljs.test :refer [is deftest]]))

;;  something to keep cljsbuild's test hook happy
(deftest null-test
  (is true))
