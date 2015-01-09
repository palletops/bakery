(ns com.palletops.bakery.sente
  "A sente component."
  (:require
   [clojure.core.async :as async :refer [chan close! go-loop]]
   [com.palletops.api-builder.api :refer [defn-api]]
   [com.palletops.bakery.sente.protocols :as protocols]
   [com.palletops.leaven.protocols :refer [Startable Stoppable]]
   [compojure.core :refer [GET POST routes]]
   [compojure.route :refer [resources]]
   [schema.core :as schema :refer [optional-key]]
   [taoensso.sente :as sente]
   [taoensso.timbre :as timbre]))

(defn- sente-routes
  "Return the Ring routes required for Sente."
  [path chan-sock-atom]
  (let [ajax-get-or-ws-handshake-fn (delay
                                     (:ajax-get-or-ws-handshake-fn
                                      @chan-sock-atom))
        ajax-post-fn (delay (:ajax-post-fn @chan-sock-atom))]
    (routes
     (GET  path request (@ajax-get-or-ws-handshake-fn request))
     (POST path request (@ajax-post-fn request)))))

(defn- start
  [{:keys [path config]} chan-sock-atom]
  {:pre [chan-sock-atom]}
  (let [chan-sock (sente/make-channel-socket! path config)]
    (reset! chan-sock-atom chan-sock)
    chan-sock))

(defn- stop
  [channel-socket]
  {:pre [channel-socket]}
  (close! (:ch-recv channel-socket)))

(defrecord Sente [config channel-socket routes chan-sock-atom]
  Startable
  (start [component]
    (if channel-socket
      component
      (assoc component :channel-socket (start config chan-sock-atom))))
  Stoppable
  (stop [component]
    (if channel-socket
      (assoc component :channel-socket nil :routes nil)
      component)))

(def SenteOptions
  {(optional-key :path) schema/Str
   (optional-key :config) {schema/Keyword schema/Any}})

(defn-api sente
  "Return a sente component.  `config` is a map passed to the last
  argument of sente's `make-channel-socket!` function.  The `:routes`
  key will contain Ring routes for handling asynchronous requests,
  which need to be injected into your application's routes.  The
  `:path` key can be used to override the default path for these
  routes."
  {:sig [[SenteOptions :- Sente]]}
  [{:keys [path config]}]
  (let [chan-sock-atom (atom nil)
        config {:path (or path "/chsk")
                :config (merge {:type :auto} config)}]
    (map->Sente
     {:config config
      :chan-sock-atom chan-sock-atom
      :routes (sente-routes (:path config) chan-sock-atom)})))
