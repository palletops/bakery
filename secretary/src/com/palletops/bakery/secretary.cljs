(ns com.palletops.bakery.secretary
  "Component for routing via Secretary."
  (:require-macros
   [com.palletops.api-builder.api :refer [defn-api]])
  (:require
   [com.palletops.leaven.protocols :refer [ILifecycle]]
   [goog.events :as events]
   [goog.history.EventType :as EventType]
   [secretary.core :as secretary :include-macros true])
  (:import
   goog.History))

(defn- on-navigate [event]
  (secretary/dispatch! (.-token event)))

(defn- start-routing!
  [history]
  (doto history
    (events/listen EventType/NAVIGATE on-navigate)
    (.setEnabled true)))

(defn- stop-routing!
  [history]
  (doto history
    (.setEnabled false)))

(defn- navigate-to
  [s history]
  (.setToken history s))

(defrecord Routing
    [history nav-fn]
  ILifecycle
  (start [_]
    (start-routing! history))
  (stop [_]
    (start-routing! history)))

(defn-api secretary
  "Create a routing component based on Secretary.
  Links Secretary with google closure History.  The `:nav-fn` of the
  component is a function of a single argument that can be used to
  navigate to an anchor."
  {:sig [[:- Routing]]}
  []
  (let [history (History.)]
    (map->Routing {:history history
                   :nav-fn #(navigate-to % history)})))
