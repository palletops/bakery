(ns example.webapp.view
  "Om views"
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [cljs.core.async :refer [put! chan <!]]
   [clojure.data :as data]
   [clojure.string :as string]
   [example.webapp.app :as app]
   [example.webapp.nav :as nav]
   [om.core :as om :include-macros true]
   [om-tools.dom :as dom :include-macros true]
   [om-bootstrap.button :as b]
   [om-bootstrap.input :refer [input]]
   [om-bootstrap.panel :refer [panel]]
   [om-bootstrap.random :as r]))

(defn display
  "Return a style map for toggling display based on the boolean value
  of show."
  [show]
  (if show
    #js {}
    #js {:display "none"}))

(defn validation-state
  "Returns a Bootstrap :bs-style string based on the supplied string
  length."
  [s]
  (let [l (count s)]
    (cond (> l 2) "success"
          (> l 1) "warning"
          (pos? l) "error"
          :else nil)))

(defn handle-change
  "Grab the input element via the `input` reference."
  [state owner]
  (let [node (om/get-node owner "input")
        user (.-value node)]
    (om/transact! state #(app/set-user % user))))

(defn who [state owner]
  (reify
    om/IRender
    (render [this]
      (dom/span
       {:style (display (= :who (-> state :nav :view)))}
       (input
        {:feedback? true
         :type "text"
         :label "Who are you?"
         :placeholder "Enter your name"
         :help "I'ld like to say hello"
         :default-value (:user state)
         :bs-style (validation-state (:text state))
         :on-change #(handle-change state owner)})
       (b/button
        {:bs-style "primary"
         :on-click (fn [_]
                     (om/update! state :nav {:view :hi})
                     (app/start-user! @state))}
        "Say Hi")))))

(defn hi [state owner]
  (reify
    om/IRender
    (render [this]
      (dom/div
          {:style (display (= :hi (-> state :nav :view)))}
        (dom/p (str "Hello " (:user state) " " (:n state)))
        (b/button
         {:bs-style "primary" :href "#/"} "Bye")))))

(defn root [state owner]
  (reify
    om/IRender
    (render [this]
      (r/page-header {} "Example Leaven based WebApp")
      (panel {}
             (dom/div (om/build who state {}))
             (dom/div (om/build hi state {}))))))
