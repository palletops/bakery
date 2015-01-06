(ns com.palletops.bakery.httpkit
  "Component for running http-kit server.
  Provides an idempotent start and stop."
  (:require
   [com.palletops.api-builder.api :refer [defn-api]]
   [com.palletops.leaven.protocols :refer [Startable Stoppable]]
   [org.httpkit.server :as httpkit]
   [schema.core :as schema]))

(defn- start
  [handler {:keys [port join?] :as config}]
  {:pre [handler port]
   :post [(fn? %)]}
  (httpkit/run-server handler config))

(defn- stop
  [server stop-timeout]
  (server :timeout stop-timeout))

(defrecord Httpkit
    [config server handler stop-timeout]
  Startable
  (start [component]
    (if server
      component
      (assoc component :server (start handler config))))
  Stoppable
  (stop [component]
    (if server
      (do
        (stop server stop-timeout)
        (assoc component :server nil))
      component)))

(def HttpkitOptions
  {:handler (schema/make-fn-schema schema/Any [[schema/Any]])
   (schema/optional-key :config) {schema/Keyword schema/Any}
   (schema/optional-key :stop-timeout) schema/Int})

(defn-api httpkit
  "Return an httpkit component, that will dispatch ring requests to
  the `handler` handler. An optional :stop-timeout can specify a
  timeout when waiting for the server to stop (defaults to 100ms).
  All other options are passed to httpkit.  The default port is 3000.
  The start and stop implementations are idempotent."
  {:sig [[HttpkitOptions :- Httpkit]]}
  [{:keys [config handler stop-timeout]
    :or {stop-timeout 100}
    :as options}]
  (map->Httpkit
   {:config (merge
             {:port 3000
              :join? false}
             config)
    :stop-timeout stop-timeout
    :handler handler}))
