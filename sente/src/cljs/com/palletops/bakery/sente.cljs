(ns com.palletops.bakery.sente
  "A sente client component."
  (:require-macros
   [com.palletops.api-builder.api :refer [defn-api]]
   [cljs.core.async.macros :refer [go-loop]])
  (:require
   [cljs.core.async :refer [alts! chan close!]]
   [com.palletops.leaven.protocols :refer [ILifecycle]]
   [schema.core :as schema :refer [optional-key]]
   [taoensso.sente :as sente]))

(defn- start-router
  "A router for events that will be passed a receive channel and a
  send function."
  [event-handler {:keys [ch-recv send-fn] :as chan-sock}]
  (let [ctrl-ch (chan)]
    (go-loop []
      (let [[v p] (alts! [ch-recv ctrl-ch])]
        (if (identical? p ctrl-ch) ::stop
          (let [[id data :as event] v]
            ;; Provide ch to handler to allow event injection back into loop:
            (event-handler  ; Allow errors to throw
             {:event event
              :channel-socket chan-sock})
            (recur)))))
    (fn stop! [] (close! ctrl-ch))))

(defn- start
  [{:keys [handler path config announce-fn] :as component}]
  {:pre [handler]}
  (let [chan-sock (sente/make-channel-socket! path config)
        router (start-router
                handler
                (select-keys chan-sock [:ch-recv :send-fn]))]
    (when announce-fn
      (announce-fn chan-sock))
    (assoc component
      :router router
      :channel-socket chan-sock)))

(defn- stop
  [{:keys [channel-socket router]}]
  {:pre [router]}
  (router)
  (close! (:ch-recv channel-socket)))

(defrecord Sente
    [channel-socket router handler path config async-send-fn]
  ILifecycle
  (start [component]
    (start component))
  (stop [component]
    (stop component)))

(def SenteOptions
  {:handler (schema/make-fn-schema schema/Any [[schema/Any schema/Any]])
   (optional-key :path) schema/Str
   (optional-key :announce-fn) (schema/make-fn-schema schema/Any [[schema/Any]])
   (optional-key :config) {schema/Keyword schema/Any}})

(defn-api sente
  "Create a sente component.
  `announce-fn` will be called on start with the channel socket as an
  argument.  The `handler` will be passed a single map argument
  with `:event` and `:channel-socket` keys."
  {:sig [[SenteOptions :- Sente]]}
  [{:keys [path handler config announce-fn]}]
  (map->Sente {:path (or path "/chsk")
               :handler handler
               :announce-fn announce-fn
               :config (merge {:type :auto} config)}))
