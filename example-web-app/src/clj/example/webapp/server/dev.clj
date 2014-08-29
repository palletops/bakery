(ns example.webapp.server.dev
  "Dev functions"
  (:require
   [com.palletops.bakery.weasel :as weasel]
   [com.palletops.leaven :as leaven]
   [example.webapp.server :as server-app]
   [example.webapp.server.index :refer [js-script-dev]]))

(def weasel-config
  {:host "localhost"
   :port 9001})

(defmacro weasel-config-map
  "Define a macro so we can use the same config in clojurescript."
  []
  weasel-config)

(leaven/defsystem DevServer [:server :weasel])

(defn dev-server [options]
  (map->DevServer
   {:server (server-app/server (merge {:tags (js-script-dev)} options))
    :weasel (weasel/weasel weasel-config)}))

(defonce server nil)

(defn start
  "Run the ring server."
  [options]
  (if (nil? server)
    (do
      (alter-var-root #'server (fn [_] (dev-server options)))
      (alter-var-root #'server (fn [component]
                                 (try
                                   (leaven/start component)
                                   (catch Exception e
                                     (when-let [s (:system (ex-data e))]
                                       (leaven/stop s))
                                     (throw e))))))
    (println "Server already running")))

(defn stop
  "Stop the ring server."
  []
  (if server
    (alter-var-root #'server (fn [s] (leaven/stop s) nil))
    (println "No server is running")))

(defn start-cljs-repl!
  "Start the clojurescript repl."
  []
  (if-let [f (:start-repl! (:weasel server))]
    (f)
    (println "No server running")))
