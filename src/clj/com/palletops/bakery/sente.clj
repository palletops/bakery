(ns com.palletops.bakery.sente
  "A sente component.
  Requires [com.taoensso/sente \"0.15.1\"] and [compojure \"1.1.8\"]."
  (:require
   [clojure.core.async :as async :refer [chan close! go-loop]]
   [com.palletops.leaven.protocols :refer [ILifecycle]]
   [compojure.core :refer [GET POST routes]]
   [compojure.route :refer [resources]]
   [taoensso.sente :as sente]
   [taoensso.timbre :as timbre]))

(defn random-id
  [req]
  (str (java.util.UUID/randomUUID)))

(defn sente-routes
  "Return the Ring routes required for Sente."
  [path chan-sock-atom]
  (let [ajax-get-or-ws-handshake-fn (delay
                                     (:ajax-get-or-ws-handshake-fn
                                      @chan-sock-atom))
        ajax-post-fn (delay (:ajax-post-fn @chan-sock-atom))]
    (routes
     (GET  path request (@ajax-get-or-ws-handshake-fn request))
     (POST path request (@ajax-post-fn request)))))

(defn start [{:keys [msg-handler path sente-config announce-fn]} chan-sock-atom]
  {:pre [msg-handler chan-sock-atom]}
  (let [chan-sock (sente/make-channel-socket! path sente-config)
        router (sente/start-chsk-router-loop! msg-handler (:ch-recv chan-sock))]
    (reset! chan-sock-atom chan-sock)
    (when announce-fn
      (announce-fn chan-sock))
    {:router router
     :channel-socket chan-sock}))

(defn stop [channel-socket router]
  {:pre [router]}
  (router)
  (close! (:ch-recv channel-socket)))


(defrecord Sente
    [config channel-socket router routes chan-sock-atom]
  ILifecycle
  (start [component]
    (if channel-socket
      component
      (merge component (start config chan-sock-atom))))
  (stop [component]
    (if channel-socket
      (do
        (stop channel-socket router)
        (assoc component :channel-socket nil :router nil :routes nil))
      component)))

(defn sente
  "Return a sente component, routing messages to `msg-handler`.
  `sente-config` is a map passed to the last argument of sente's
  `make-channel-socket!` function.  `announce-fn` will be called on
  start with the channel socket as an argument.  The `:routes` key
  will contain Ring routes for handling asynchronous requests, which
  need to be injected into your application's routes.  The `:path` key
  can be used to override the default path for these routes."
  [{:keys [path msg-handler announce-fn sente-config]}]
  (let [chan-sock-atom (atom nil)
        config {:path (or path "/chsk")
                :msg-handler msg-handler
                :announce-fn announce-fn
                :sente-config (merge {:type :auto
                                      :user-id-fn random-id}
                                     sente-config)}]
    (map->Sente
     {:config config
      :chan-sock-atom chan-sock-atom
      :routes (sente-routes (:path config) chan-sock-atom)})))
