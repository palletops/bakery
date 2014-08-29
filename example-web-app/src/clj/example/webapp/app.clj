(ns example.webapp.app
  (:require
   [clojure.core.async :refer [<! go-loop timeout]]
   [taoensso.timbre :refer [debugf]]))

(defn example-message
  [state msg uid]
  (debugf "example-mesage for %s %s" msg uid)
  (debugf "example-mesage send-fn %s" (:send-fn @state))
  (go-loop [n 5]
    (when (pos? n)
      (<! (timeout 1000))
      ((:send-fn @state) uid [:ui/greet (str n)])
      (recur (dec n)))))
