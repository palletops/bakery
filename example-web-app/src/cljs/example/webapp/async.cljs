(ns example.webapp.async
  (:require-macros
   [cljs.core.async.macros :as asyncm :refer (go go-loop)]
   [cljs.core.match.macros :refer (match)])
  (:require
   [cljs.core.async :as async  :refer (<! >! put! chan close!)]
   [cljs.core.match]                    ; avoid cannot read property backtrack
   [taoensso.sente :as sente]
   [example.webapp.app :as app]))

(defn handle-payload
  [state payload]
  (.log js/console "Message: %s" (pr-str payload))
  (match payload
    [:ui/greet n] (app/greet state n)
    :else (.log js/console "Unmatched payload: %s" (pr-str payload))))

(defn handle-recv
  [state]
  (fn
    [{:keys [event]} chan-sock]
    (let [[id data :as ev] event]
      (.log js/console "Event id:" id )
      (.log js/console "Event:" ev)
      (match [id data]
        [:chsk/state {:first-open? true}]
        (.log js/console "Channel socket successfully established!")

        [:chsk/state new-state]
        (.log js/console "Chsk state change: %s" new-state)

        [:chsk/recv payload]
        (do (.log js/console "Push event from server: %s" payload)
            (handle-payload state payload))

        :else (.log js/console "Unmatched event: %s" ev)))))
