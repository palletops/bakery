(ns example.webapp.server
  "HTTP Server for the example.webapp"
  (:require
   [com.palletops.bakery.httpkit :as httpkit]
   [com.palletops.bakery.sente :as sente]
   [com.palletops.leaven :refer [start stop defsystem]]
   [compojure.core :refer [GET POST routes]]
   [compojure.route :refer [resources]]
   [example.webapp.server.async :refer [msg-handler]]
   [example.webapp.server.data-middleware :as dm :refer [wrap-data]]
   [example.webapp.server.index :refer [index-page js-script]]
   [ring.middleware.absolute-redirects :refer [wrap-absolute-redirects]]
   [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
   [ring.middleware.not-modified :refer [wrap-not-modified]]
   [ring.middleware.session :refer [wrap-session]]
   [ring.middleware.stacktrace :refer [wrap-stacktrace]]))

(defn default-state []
  {})

(defn site
  [injected-routes tags]
  (routes
   (resources "/")
   injected-routes
   ;; application routes
   (GET "/*" request (index-page tags))))

(defn wrap-random-uid
  [handler]
  (fn [req]
    (handler
     (update-in req [:session :uid]
                #(or % (str (java.util.UUID/randomUUID)))))))

(defn ui-app
  [state injected-routes tags]
  (->
   (site injected-routes tags)
   wrap-stacktrace
   wrap-absolute-redirects
   wrap-not-modified
   wrap-stacktrace
   (wrap-data {:state state})
   wrap-random-uid
   (wrap-anti-forgery
    {:read-token (fn [req] (-> req :params :csrf-token))})
   wrap-session))

(defsystem Server [:state :server :sente :channel-socket])

(defn server
  [options]
  (let [state (atom (default-state))
        sente (sente/sente {:handler #'msg-handler})
        script-tags (:tags options (js-script))]
    (map->Server
     {:state state
      :sente sente
      :server (httpkit/httpkit
               {:handler (ui-app state (:routes sente) script-tags)
                :config options})})))
