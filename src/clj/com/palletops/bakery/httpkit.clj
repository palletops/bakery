(ns com.palletops.bakery.httpkit
  "Component for running http-kit server.
  Requires [http-kit \"2.1.16\"]."
  (:require
   [com.palletops.leaven.protocols :refer [ILifecycle]]
   [org.httpkit.server :as httpkit]))

(defn start
  [app {:keys [port join?] :as config}]
  {:pre [app port]}
  (httpkit/run-server app {:port port :join? join?}))

(defn stop
  [server stop-timeout]
  (server :timeout stop-timeout))

(defrecord Httpkit
    [config server app stop-timeout]
  ILifecycle
  (start [component]
    (if server
      component
      (assoc component :server (start app config))))
  (stop [component]
    (if server
      (do
        (stop server stop-timeout)
        (assoc component :server nil))
      component)))

(defn httpkit
  [{:keys [app port join? stop-timeout]
    :or {stop-timeout 100}
    :as options}]
  (map->Httpkit
   {:config (merge
             {:port 3000
              :join? false
              :app app}
             (dissoc options :stop-timeout :app))
    :stop-timeout stop-timeout
    :app app}))
