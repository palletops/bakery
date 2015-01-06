(ns com.palletops.bakery.sente
  "A sente client component."
  (:require-macros
   [com.palletops.api-builder.api :refer [defn-api]]
   [cljs.core.async.macros :refer [go-loop]]
   [schema.macros :refer [protocol]])
  (:require
   [cljs.core.async :refer [alts! chan close!]]
   [com.palletops.bakery.sente.protocols :as protocols]
   [com.palletops.leaven.protocols :refer [Startable Stoppable]]
   [schema.core :as schema :refer [optional-key]]
   [taoensso.sente :as sente]))

(defn- start
  [{:keys [path config] :as component}]
  {:pre [path]}
  (sente/make-channel-socket! path config))

(defn- stop
  [{:keys [channel-socket]}]
  {:pre [channel-socket]}
  (close! (:ch-recv channel-socket)))

(defrecord Sente [channel-socket path config]
  Startable
  (start [component]
    (if channel-socket
      component
      (assoc component :channel-socket (start component))))
  Stoppable
  (stop [component]
    (if channel-socket
      (do (stop component)
          (assoc component :channel-socket nil))
      component)))

(def SenteOptions
  {(optional-key :path) schema/Str
   (optional-key :config) {schema/Keyword schema/Any}})

(defn-api sente
  "Create a sente component.
  `announce-fn` will be called on start with the channel socket as an
  argument.  The `handler` component is used to obtain a handler
  function using the HandlerReturnable protocol.  The handler function
  will be passed a single map argument with `:event` and
  `:channel-socket` keys."
  {:sig [[SenteOptions :- Sente]]} [{:keys
  [path handler config announce-fn]}]
  (map->Sente {:path (or path "/chsk")
               :config (merge {:type :auto} config)}))
