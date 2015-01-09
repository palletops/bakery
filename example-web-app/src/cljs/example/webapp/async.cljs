(ns example.webapp.async
  "Example routing function for sente's default router."
  (:require-macros
   [cljs.core.async.macros :as asyncm :refer (go go-loop)]
   [cljs.core.match.macros :refer (match)])
  (:require
   [cljs.core.async :as async  :refer (<! >! put! chan close!)]
   [cljs.core.match]                    ; avoid cannot read property backtrack
   [com.palletops.leaven.protocols :refer [Startable Stoppable]]
   [example.webapp.app :as app]
   [taoensso.sente :as sente]))

(defn handle-payload
  [state send-fn payload]
  (.log js/console "Message: %s" (pr-str payload))
  (match payload
         [:ui/greet n] (app/greet state n)
         :else (.log js/console "Unmatched payload: %s" (pr-str payload))))

(defn handle-recv
  [state {:keys [event]} send-fn ex-handler]
  (try
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
                 (handle-payload state send-fn payload))

             :else (.log js/console "Unmatched event: %s" ev)))
    (catch js/Error e
      (ex-handler e))))

(defn handle-events
  [state c send-fn ex-handler]
  {:pre [state c (fn? ex-handler)]}
  (go-loop []
    (when-let [ev (async/<! c)]
      (handle-recv state ev send-fn ex-handler)
      (recur))))

(defrecord WebappAsync [sente state loop-chan ex-handler]
  Startable
  (start [component]
    (if loop-chan
      component
      (assoc component
        :loop-chan (handle-events
                    state
                    (:ch-recv (:channel-socket sente))
                    (:send-fn (:channel-socket sente))
                    ex-handler))))
  Stoppable
  (stop [component]
    (if loop-chan
      (assoc component :loop-chan nil)
      component)))

(defn webapp-async
  "Return a leaven component to process async events."
  [{:keys [sente state ex-handler] :as options}]
  {:pre [sente state]}
  (map->WebappAsync
   (merge
    {:ex-handler #(.error js/console "Unexpected WebappAsync Exception" %)}
    options)))
