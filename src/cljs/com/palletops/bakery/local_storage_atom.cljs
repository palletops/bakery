(ns com.palletops.bakery.local-storage-atom
  "A component for an atom backed by html5 local storage.
  Requires [alandipert/storage-atom \"1.2.3\"]."
  (:require
   [alandipert.storage-atom :refer [local-storage]]))

(defn local-storage-atom
  "Return a component that is an atom backed by local storage (if supported).
  `default` is used to initialise the atom if it is not already in
  local storage."
  [default key]
  (let [app-state-atom (atom default)]
    (if js/localStorage
      (local-storage app-state-atom :palletui1)
      app-state-atom)))
