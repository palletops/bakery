(ns example.webapp.server.async
  (:require
   [clojure.core.match :refer [match]]
   [example.webapp.app :as app]
   [example.webapp.server.data-middleware :as dm :refer [wrap-data]]
   [taoensso.timbre :refer [debugf errorf tracef]]))

(defn wrap-update-send-fn
  "Middleware to update the state :send-fn."
  [handler]
  (fn update-send-fn [{:as ev-msg :keys [ring-req event ?reply-fn send-fn]}]
    (let [{:keys [state]} (dm/data ring-req)]
      ;; capture a send-fn
      (swap! state assoc :send-fn send-fn))
    (handler ev-msg)))

(defn msg-handler-fn
  "Application specific message handling."
  [{:as ev-msg :keys [ring-req event ?reply-fn]}]
  (let [session (:session ring-req)
        uid     (:uid session)
        [id data :as ev] event
        {:keys [state]} (dm/data ring-req)]

    (debugf "Event uid: %s" uid)
    (tracef "Event session: %s" session)
    (debugf "Dispatching %s" id)

    ;; The actual route dispatching
    (match [id data]
      [:example/msg data] (app/example-message state data uid)
      :else
      (do (debugf "Unmatched event: %s" ev)
          (when-not (:dummy-reply-fn? (meta ?reply-fn))
            (?reply-fn {:umatched-event-as-echoed-from-from-server ev}))))))

(def msg-handler*
  (-> msg-handler-fn
      wrap-update-send-fn))

(defn msg-handler
  [{:as ev-msg :keys [ring-req event ?reply-fn]}]
  (let [[id data :as ev] event]

    (debugf "Event id: %s" id)
    (tracef "Event: %s" ev)

    (match [id data]
      [:chsk/uidport-open _] (debugf "uidport-open")
      [:chsk/uidport-close _] (debugf "uidport-close")
      [:chsk/ws-ping _] (debugf "ws-ping")
      :else (try (msg-handler* ev-msg)
                 (catch Exception e
                   (errorf e "Unexpected exception"))))))
