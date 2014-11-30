(ns com.palletops.bakery.sente
  "A sente component."
  (:require
   [clojure.core.async :as async :refer [chan close! go-loop]]
   [com.palletops.api-builder.api :refer [defn-api]]
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
  [{:keys [handler config announce-fn]} chan-sock-atom]
  {:pre [handler chan-sock-atom]}
  (let [chan-sock (sente/make-channel-socket! config)
        router (sente/start-chsk-router! (:ch-recv chan-sock) handler)]
    (reset! chan-sock-atom chan-sock)
    (when announce-fn
      (announce-fn chan-sock))
    {:router router
     :channel-socket chan-sock}))

(defn- stop
  [channel-socket router]
  {:pre [router]}
  (router)
  (close! (:ch-recv channel-socket)))

(defrecord Sente
    [config channel-socket router routes chan-sock-atom]
  Startable
  (start [component]
    (if channel-socket
      component
      (merge component (start config chan-sock-atom))))
  Stoppable
  (stop [component]
    (if channel-socket
      (do
        (stop channel-socket router)
        (assoc component :channel-socket nil :router nil :routes nil))
      component)))

(def SenteOptions
  {:handler (schema/conditional
             var? schema/Any
             :else (schema/make-fn-schema schema/Any [[schema/Any schema/Any]]))
   (optional-key :path) schema/Str
   (optional-key :announce-fn) (schema/make-fn-schema schema/Any [[schema/Any]])
   (optional-key :config) {schema/Keyword schema/Any}})

(defn-api sente
  "Return a sente component, routing messages to `handler`.
  `config` is a map passed to the last argument of sente's
  `make-channel-socket!` function.  `announce-fn` will be called on
  start with the channel socket as an argument.  The `:routes` key
  will contain Ring routes for handling asynchronous requests, which
  need to be injected into your application's routes.  The `:path` key
  can be used to override the default path for these routes."
  {:sig [[SenteOptions :- Sente]]}
  [{:keys [path handler announce-fn config]}]
  (let [chan-sock-atom (atom nil)
        config {:handler handler
                :announce-fn announce-fn
                :config (merge {:type :auto} config)}]
    (map->Sente
     {:config config
      :chan-sock-atom chan-sock-atom
      :routes (sente-routes (or path "/chsk") chan-sock-atom)})))
