(ns com.palletops.bakery.weasel
  "A server side weasel component.
  Requires [weasel \"0.3.0\"] and [com.cemerick/piggieback \"0.1.3\"].
  In project.clj,
    :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}"
  (:require
   [cemerick.piggieback :as piggieback]
   [weasel.repl.websocket :as weasel]))

(defn weasel
  "Return a weasel component for binding on the specified host and port.
  Default is localhost:9001. The component has a \":start-repl!\" key, which
  has a function to start the weasel repl on the server side."
  [{:keys [host port] :as config}]
  {:config (merge {:host "localhost" :port 9001} config)
   :start-repl! #(piggieback/cljs-repl
                  :repl-env (weasel/repl-env :ip host :port port))})
