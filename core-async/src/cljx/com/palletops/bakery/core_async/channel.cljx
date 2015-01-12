(ns com.palletops.bakery.core-async.channel
  "A core.async channel as a leaven component."
  #+cljs
  (:require-macros
   [schema.macros :refer [=>]]
   [com.palletops.api-builder.api :refer [defn-api]])
  (:require
   #+cljs [cljs.core.async :as async]
   #+clj [clojure.core.async :as async]
   #+clj [com.palletops.api-builder.api :refer [defn-api]]
   [com.palletops.leaven.protocols :refer [Startable Stoppable]]
   [schema.core :as schema]
   #+clj [schema.core :refer [=>]]))

(defrecord Channel [chan chan-f]
  Startable
  (start [component]
    (if chan
      component
      (assoc component :chan (chan-f))))
  Stoppable
  (stop [component]
    (if chan
      (do
        (async/close! chan)
        (assoc component :chan nil))
      component)))

(def ChannelOptions
  {(schema/optional-key :chan-f) (=> schema/Any)})

(defn-api channel
  "Return a leaven component wrapping a core.async channel.
  The core.async channel value is the :chan element of the component.
  The channel is created using chan-f, which defaults to
  clojure.core.async/chan."
  {:sig [[ChannelOptions :- Channel]]}
  [{:keys [chan-f] :or {chan-f async/chan} :as options}]
  (map->Channel {:chan-f chan-f}))
