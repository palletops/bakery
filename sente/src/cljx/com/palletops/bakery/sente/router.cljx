(ns com.palletops.bakery.sente.router
  "A leaven component for the standard sente router"
  #+cljs
  (:require-macros
   [schema.macros :refer [=> protocol]]
   #+cljs [com.palletops.api-builder.api :refer [defn-api]])
  (:require
   #+clj [com.palletops.api-builder.api :refer [defn-api]]
   [com.palletops.bakery.sente.protocols :as protocols]
   [com.palletops.leaven.protocols :refer [Startable Stoppable]]
   [schema.core :as schema]
   #+clj [schema.core :refer [=> protocol]]
   [taoensso.sente :as sente]))

(defn- start [sente handler]
  (sente/start-chsk-router!
   (:ch-recv (:channel-socket sente))
   (protocols/handler handler)))

(defrecord Router [sente handler router]
  Startable
  (start [component]
    (if router
      component
      (assoc component :router (start sente handler))))
  Stoppable
  (stop [component]
    (if router
      (do
        (router)
        (assoc component :router nil))
      component)))

(def RouterOptions
  {:sente {:channel-socket (schema/maybe
                            {:ch-recv schema/Any schema/Keyword schema/Any})
           schema/Keyword schema/Any}
   :handler (protocol protocols/HandlerReturnable)})

(defn-api router
  "Return a leaven component for the standard Sente router.
  A handler function is obtained from the `handler` component via the
  HandlerReturnable protocol."
  {:sig [[RouterOptions :- Router]]}
  [{:keys [sente handler] :as options}]
  (map->Router options))

(defn-api handler
  "Return a leaven component that can be passed to the sente router."
  {:sig [[(schema/either (=> schema/Any {schema/Any schema/Any})
                         #+clj (schema/pred var? "var?"))
          :- (protocol protocols/HandlerReturnable)]]}
  [handler-fn]
  (reify
    protocols/HandlerReturnable
    (handler [_]
      handler-fn)))
