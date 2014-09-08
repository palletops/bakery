(ns example.webapp.client
  "Main client component."
  (:require
   [cljs.core.async :as async :refer [chan put! <!]]
   [com.palletops.bakery.local-storage-atom :as local-storage-atom]
   [com.palletops.bakery.secretary :as secretary-comp :refer [secretary]]
   [com.palletops.bakery.sente :as sente]
   [com.palletops.leaven :as leaven :include-macros true]
   [com.palletops.leaven.protocols :refer [ILifecycle]]
   [example.webapp.async :as pasync :refer [handle-recv]]
   [example.webapp.view :as view]
   [reagent.core :as reagent :refer [atom]]
   [secretary.core :as secretary :include-macros true :refer [defroute]]))

(enable-console-print!)

(leaven/defsystem Ui
  [:app-state
   :async
   :routing
   :root])

(defn element-by-id
  [id]
  (. js/document (getElementById id)))

(def default-state
  {})

(defrecord ReagentRoot
    [root node]
  ILifecycle
  (start [_] (reagent/render-component root node))
  (stop [_] (reagent/unmount-component-at-node node)))

(defn make-ui
  []
  (let [app-state (local-storage-atom/local-storage-atom
                   (atom default-state) :example-webapp)
        ;; :nav is used to control what is on the screen
        ;; at any time. It is a map, where the :view key specifes
        ;; the view and other keys specify data for that view.
        nav (atom {:view :who})
        set-nav! (fn [v] (reset! nav v))
        r (secretary-comp/secretary)
        navigate-to (:nav-fn r)]

    ;; non-global routes would be good to have!
    (defroute "/" []
      (navigate-to "/who"))

    (defroute "/who" []
      (set-nav! {:view :who}))

    (defroute "/hi" []
      (set-nav! {:view :hi}))

    (map->Ui
     {:app-state app-state
      :async (sente/sente
              {:handler (handle-recv app-state)
               :announce-fn #(swap! app-state assoc
                                    :send-fn (local-storage-atom/transient-value
                                              (:send-fn %)))})
      :root (reagent/render-component
             [view/root nav app-state]
             (element-by-id "root"))
      :routing r})))
