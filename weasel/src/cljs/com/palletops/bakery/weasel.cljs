(ns com.palletops.bakery.weasel
  "A client side component for the Weasel REPL."
  (:require-macros
   [com.palletops.api-builder.api :refer [defn-api]])
  (:require
   [com.palletops.leaven.protocols :refer [Startable Stoppable]]
   [schema.core :as schema]
   [weasel.repl :as weasel]
   [weasel.impls.websocket :as ws]))

(defrecord Weasel [config]
  Startable
  (start [component]
    (when-not (weasel/alive?)
      (weasel/connect (str "ws://" (:host config) ":" (:port config))))
    component)
  Stoppable
  (stop [component]
    (when-let [connection @(weasel/ws-connection)]
      (ws/close connection)
      (swap! weasel/ws-connection (constantly nil)))
    component))

(def WeaselOptions
  {(schema/optional-key :host) schema/Str
   (schema/optional-key :port) schema/Int})

(defn-api weasel
  "Create a weasel component for connecting on the specified host and port.
  Default is localhost:9001."
  {:sig [[WeaselOptions :- Weasel]]}
  [{:keys [host port] :as config}]
  (map->Weasel
   {:config (merge {:host "localhost" :port 9001} config)}))
