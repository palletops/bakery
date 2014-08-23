(ns com.palletops.bakery.sente
  "A sente component.
  Requires [com.taoensso/sente \"0.15.1\"]"
  (:require
   [clojure.core.async :as async :refer [chan close! go-loop]]
   [com.palletops.leaven.protocols :refer [ILifecycle]]
   [taoensso.sente :as sente]
   [taoensso.timbre :as timbre]))

(defn random-id
  [req]
  (str (java.util.UUID/randomUUID)))

(defn start [{:keys [msg-handler path sente-config announce-fn]}]
  {:pre [msg-handler]}
  (let [chan-sock (sente/make-channel-socket! path sente-config)
        chan-sock (select-keys chan-sock
                               [:ch-recv :send-fn
                                :ajax-post-fn
                                :ajax-get-or-ws-handshake-fn])
        router (sente/start-chsk-router-loop!
                msg-handler (:ch-recv chan-sock))]
    (when announce-fn
      (announce-fn chan-sock))
    {:router router
     :channel-socket chan-sock}))

(defn stop [channel-socket router]
  {:pre [router]}
  (router)
  (close! (:ch-recv channel-socket)))


(defrecord Sente
    [config channel-socket router]
  ILifecycle
  (start [component]
    (if channel-socket
      component
      (merge component (start config))))
  (stop [component]
    (if channel-socket
      (do
        (stop channel-socket router)
        (assoc component :channel-socket nil :router nil))
      component)))

(defn sente
  "Return a sente component, routing messages to `msg-handler`.
  `announce-fn` will be called on start with the channel socket as an
  argument."
  [{:keys [path msg-handler announce-fn sente-config]}]
  (map->Sente
   {:config {:path (or path "/chsk")
             :msg-handler msg-handler
             :announce-fn announce-fn
             :sente-config (merge {:type :auto
                                   :user-id-fn random-id}
                                  sente-config)}}))
