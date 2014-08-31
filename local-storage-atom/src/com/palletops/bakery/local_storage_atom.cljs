(ns com.palletops.bakery.local-storage-atom
  "A component for an atom backed by html5 local storage."
  (:require-macros
   [com.palletops.api-builder.api :refer [defn-api]]
   [schema.macros :refer [protocol]])
  (:require
   [alandipert.storage-atom :refer [local-storage]]
   [schema.core :as schema]))

(defn-api local-storage-atom
  "Return a component that is an atom backed by local storage (if supported).
  `default` is used to initialise the atom if it is not already in
  local storage.  The ILifecycle protocol is not implemented."
  {:sig [[schema/Any schema/Keyword :- (protocol IDeref)]]}
  [default key]
  (let [app-state-atom (atom default)]
    (if js/localStorage
      (local-storage app-state-atom key)
      app-state-atom)))

;; define a value reference type that is serialised as nil
(deftype Filtered [v]
  IDeref
  (-deref [_] v)
  tailrecursion.cljson/EncodeTagged
  (-encode [_] nil))

(defn transient-value
  "Return a value that when deref'd wil return v, and will not be
  stored in local storage."
  [v]
  (Filtered. v))
