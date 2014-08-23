(ns com.palletops.bakery.httpkit
  "Component for running http-kit server.
  Requires [http-kit \"2.1.16\"]."
  (:require
   [com.palletops.leaven.protocols :refer [ILifecycle]]
   [org.httpkit.server :as httpkit]))

(defn- start
  [app {:keys [port join?] :as config}]
  {:pre [app port]}
  (httpkit/run-server app {:port port :join? join?}))

(defn- stop
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
  "Return an httpkit component, that will dispatch to the `app` handler.
  An optional :stop-timeout can specify a timeout when waiting for the
  server to stop (defaults to 100ms).  All other options are passed to
  httpkit.  The default port is 3000."
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
