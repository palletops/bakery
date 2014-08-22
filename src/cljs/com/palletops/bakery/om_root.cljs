(ns com.palletops.bakery.om-root
  "A component for an Om root element.
  Requires [om \"0.7.1\"]."
  (:require
   [com.palletops.leaven.protocols :refer [ILifecycle]]
   [om.core :as om :include-macros true]))

(defrecord OmRoot
    [f value options]
  ILifecycle
  (start [_]
    (om/root f value options))
  (stop [_]
    (om/detach-root (:target options))))

(defn om-root
  "Return an om-root component"
  ([f value options]
     (->OmRoot f value options))
  ([{:keys [f value options] :as args}]
     (map->OmRoot args)))
