(ns com.palletops.bakery.om-root
  "A component for an Om root element."
  (:require-macros
   [com.palletops.api-builder.api :refer [defn-api]])
  (:require
   [com.palletops.leaven.protocols :refer [ILifecycle]]
   [om.core :as om :include-macros true]
   [schema.core :as schema]))

(defrecord OmRoot [f value options]
  ILifecycle
  (start [_]
    (om/root f value options))
  (stop [_]
    (om/detach-root (:target options))))

(def OmRootFun (schema/make-fn-schema schema/Any [[schema/Any schema/Any]]))

(def OmRootOptions
  {:f OmRootFun
   :value schema/Any
   :options {schema/Keyword schema/Any}})

(defn-api om-root
  "Return an om-root component"
  {:sig [[OmRootOptions :- OmRoot]
         [OmRootFun schema/Any {schema/Keyword schema/Any} :- OmRoot]]}
  ([f value options]
     (->OmRoot f value options))
  ([{:keys [f value options] :as args}]
     (map->OmRoot args)))
