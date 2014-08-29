(ns example.webapp
  (:require
   [com.palletops.leaven :as leaven :include-macros true]
   [example.webapp.client :as client :refer [make-ui]]))

(def ui (atom (make-ui)))

(try
  (swap! ui leaven/start)
  ;; This is to try and provide useful information in the console if
  ;; an exception occurs on start.
  (catch ExceptionInfo e
    (.log js/console e)
    (if-let [c (ex-cause e)]
      (throw c)
      (throw e))))

;; (swap! ui leaven/stop)
