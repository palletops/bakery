(ns com.palletops.bakery.sente
  "A sente client component.
  Requires [com.taoensso/sente \"0.15.1\"]"
  (:require-macros
   [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require
   [cljs.core.async :as async  :refer (<! >! put! chan close!)]
   [com.palletops.leaven.protocols :refer [ILifecycle]]
   [taoensso.sente :as sente]))

(defn start-router
  "A router for events that will be passed a receive channel and a
  send function."
  [event-handler {:keys [ch-recv send-fn] :as chan-sock}]
  (let [ctrl-ch (chan)]
    (go-loop []
      (let [[v p] (async/alts! [ch-recv ctrl-ch])]
        (if (identical? p ctrl-ch) ::stop
          (let [[id data :as event] v]
            ;; Provide ch to handler to allow event injection back into loop:
            (event-handler  ; Allow errors to throw
             {:event event
              :channel-socket chan-sock})
            (recur)))))
    (fn stop! [] (async/close! ctrl-ch))))

(defn start [{:keys [msg-handler path sente-options announce-fn] :as component}]
  {:pre [msg-handler]}
  (let [chan-sock (sente/make-channel-socket! path sente-options)
        router (start-router
                msg-handler
                (select-keys chan-sock [:ch-recv :send-fn]))]
    (when announce-fn
      (announce-fn chan-sock))
    (assoc component
      :router router
      :channel-socket chan-sock)))

(defn stop [{:keys [channel-socket router]}]
  {:pre [router]}
  (router)
  (close! (:ch-recv channel-socket)))

(defrecord Sente
    [channel-socket router msg-handler path sente-options async-send-fn]
  ILifecycle
  (start [component]
    (start component))
  (stop [component]
    (stop component)))

(defn sente
  "Create a sente component.
  `announce-fn` will be called on start with the channel socket as an
  argument.  The `msg-handler` will be passed a single map argument
  with `:event` and `:channel-socket` keys."
  [{:keys [path msg-handler sente-options announce-fn]}]
  (map->Sente {:path (or path "/chsk")
               :sente-options (merge {:type :auto} sente-options)
               :msg-handler msg-handler
               :announce-fn announce-fn}))
