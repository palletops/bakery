(ns example.webapp.client
  (:require
   [cljs.core.async :as async :refer [chan put! <!]]
   [com.palletops.bakery.local-storage-atom :as local-storage-atom]
   [com.palletops.bakery.om-root :as om-root :refer [om-root]]
   [com.palletops.bakery.secretary :as secretary-comp :refer [secretary]]
   [com.palletops.bakery.sente :as sente]
   ;; [com.palletops.bakery.sente.router :as router]
   [com.palletops.leaven :as leaven :include-macros true]
   [com.palletops.leaven.protocols :refer [Startable]]
   [om.core :as om :include-macros true]
   [om-tools.dom :as dom :include-macros true]
   [example.webapp.async :as pasync :refer [webapp-async]]
   [example.webapp.nav :as nav]
   [example.webapp.view :as view]
   [figwheel.client :as figwheel]
   [secretary.core :as secretary :refer-macros [defroute]]))

(enable-console-print!)

(defrecord RootOptions [sente value]
  Startable
  (start [component]
    (assoc component
      :value {:shared {:send-fn (:send-fn (:channel-socket sente))}})))

(leaven/defsystem Ui
  [app-state sente sente-handler routing root-options root]
  {:depends
   {:sente-handler {:sente :sente
                    :app-state :state}
    :root-options [:sente]
    :root {:root-options :options-component}}})


(defn element-by-id
  [id]
  (. js/document (getElementById id)))

(def default-state
  (atom {;; :nav is used to control what is on the screen
         ;; at any time. It is a map, where the :view key specifes
         ;; the view and other keys specify data for that view.
         :nav {}}))

(defn make-ui
  []
  (let [app-state (local-storage-atom/local-storage-atom
                   default-state :example-webapp)
        set-nav! (fn [v] (nav/set-nav! app-state v))
        r (secretary-comp/secretary)
        navigate-to (:nav-fn r)
        sente (sente/sente {})
        sente-handler (webapp-async {:sente sente :state app-state})
        root-options (map->RootOptions {:sente sente})]

    ;; non-global routes would be good to have!
    (defroute "/" []
      (navigate-to "/who"))

    (defroute "/who" []
      (set-nav! {:view :who}))

    (defroute "/hi" []
      (set-nav! {:view :hi}))

    (map->Ui
     {:app-state app-state
      :sente sente
      :sente-handler sente-handler
      ;; :sente-router (router/router
      ;;                {:sente sente
      ;;                 :handler sente-handler ;; (router/handler (handle-recv app-state))
      ;;                 })
      :root-options root-options
      :root (om-root view/root app-state {:target (element-by-id "root")}
                     root-options)
      :routing r})))

(figwheel/start {:on-jsload (fn [] (println "Reloaded"))
                 :websocket-url "ws://localhost:3449/figwheel-ws"})
