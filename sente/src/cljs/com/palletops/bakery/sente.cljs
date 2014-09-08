(ns com.palletops.bakery.sente
  "A sente client component."
  (:require-macros
   [com.palletops.api-builder.api :refer [defn-api]]
   [cljs.core.async.macros :refer [go-loop]])
  (:require
   [cljs.core.async :refer [alts! chan close!]]
   [com.palletops.leaven.protocols :refer [Startable Stoppable]]
   [schema.core :as schema :refer [optional-key]]
   [taoensso.sente :as sente]))

(defn- start
  [{:keys [handler path config announce-fn] :as component}]
  {:pre [handler]}
  (let [chan-sock (sente/make-channel-socket! path config)
        router (sente/start-chsk-router!
                (:ch-recv chan-sock)
                handler)]
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
  Startable
  (start [component]
    (start component))
  Stoppable
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
