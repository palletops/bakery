(ns com.palletops.bakery.weasel
  "A server side weasel component."
  (:require
   [cemerick.piggieback :as piggieback]
   [com.palletops.api-builder.api :refer [defn-api]]
   [schema.core :as schema]
   [weasel.repl.websocket :as weasel]))

(def WeaselOptions
  {(schema/optional-key :host) schema/Str
   (schema/optional-key :port) schema/Int})

(def Weasel {:config {:host schema/Str
                      :port schema/Int}
             :start-repl! (schema/make-fn-schema schema/Any [[]])})

(defn-api weasel
  "Return a weasel component for binding on the specified host and port.
  Default is localhost:9001. The component has a \":start-repl!\" key, which
  has a function to start the weasel repl on the server side."
  {:sig [[WeaselOptions :- Weasel]]}
  [{:keys [host port] :as config}]
  (let [config (merge {:host "localhost" :port 9001} config)]
    {:config config
     :start-repl! #(piggieback/cljs-repl
                    :repl-env (weasel/repl-env
                               :ip (:host config) :port (:port config)))}))
