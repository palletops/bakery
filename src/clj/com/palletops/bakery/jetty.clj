(ns com.palletops.bakery.jetty
  "Component for running jetty server.
  Requires [ring/ring-jetty-adapter \"1.3.0\"].
  Provides an idempotent start and stop."
  (:require
   [com.palletops.leaven.protocols :refer [ILifecycle]]
   [ring.adapter.jetty :as jetty]))

(defn start
  [{:keys [app port join?] :as config}]
  {:pre [app port]}
  (jetty/run-jetty app {:port port :join? join?}))

(defn stop
  [{:keys [^org.eclipse.jetty.server.Server server]}]
  (.stop server))

(defrecord Jetty [config server]
  ILifecycle
  (start [component]
    (if server
      component
      (assoc component :server (start config))))
  (stop [component]
    (if server
      (do
        (stop server)
        (assoc component :server nil))
      component)))

(defn jetty
  [{:keys [app port join?] :as config}]
  (map->Jetty {:config (merge
                        {:app app
                         :port 3000
                         :join? false}
                        config)}))
