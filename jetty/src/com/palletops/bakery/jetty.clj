(ns com.palletops.bakery.jetty
  "Component for running jetty server.
  Provides an idempotent start and stop."
  (:require
   [com.palletops.api-builder.api :refer [defn-api]]
   [com.palletops.leaven.protocols :refer [ILifecycle]]
   [ring.adapter.jetty :as jetty]
   [schema.core :as schema]))

(defn- start
  [handler {:keys [port join?] :as config}]
  {:pre [handler port]}
  (jetty/run-jetty handler config))

(defn- stop
  [{:keys [^org.eclipse.jetty.server.Server server]}]
  (.stop server))

(defrecord Jetty [handler config server]
  ILifecycle
  (start [component]
    (if server
      component
      (assoc component :server (start handler config))))
  (stop [component]
    (if server
      (do
        (stop server)
        (assoc component :server nil))
      component)))

(def JettyOptions
  {:handler (schema/make-fn-schema schema/Any [[schema/Any]])
   (schema/optional-key :config) {schema/Keyword schema/Any}})

(defn-api jetty
  "Return a jetty component, that will dispatch to the `:handler` handler.
  The map passed to the `:config` key will be passed to the ring
  adapter's `run-jetty`.  The default port is 3000."
  {:sig [[JettyOptions :- Jetty]]}
  [{:keys [handler config]}]
  (map->Jetty {:config (merge
                        {:port 3000
                         :join? false}
                        config)
               :handler handler}))
