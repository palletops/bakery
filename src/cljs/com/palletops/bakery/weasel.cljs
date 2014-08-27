(ns com.palletops.bakery.weasel
  "A client side component for the Weasel REPL."
  (:require
   [com.palletops.leaven.protocols :refer [ILifecycle]]
   [weasel.repl :as weasel]
   [weasel.impls.websocket :as ws]))

(defrecord Weasel [config]
  ILifecycle
  (start [component]
    (when-not (weasel/alive?)
      (weasel/connect (str "ws://" (:host config) ":" (:port config))))
    component)
  (stop [component]
    (when-let [connection @(weasel/ws-connection)]
      (ws/close connection)
      (swap! weasel/ws-connection (constantly nil)))
    component))

(defn weasel
  "Create a weasel component for connecting on the specified host and port.
  Default is localhost:9001."
  [{:keys [host port] :as config}]
  (map->Weasel
   {:config config}))
