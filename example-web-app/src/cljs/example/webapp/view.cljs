(ns example.webapp.view
  "Om views"
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [cljs.core.async :refer [put! chan <!]]
   [clojure.data :as data]
   [clojure.string :as string]
   [example.webapp.app :as app]
   [reagent.core :as reagent :refer [atom]]))

(defn handle-change
  "Grab the input element via the `input` reference."
  [event state]
  (swap! state assoc :user (-> event .-target .-value)))

(defn who [nav state]
  [:span
   [:input
    {:feedback? true
     :type "text"
     :label "Who are you?"
     :placeholder "Enter your name"
     :help "I'ld like to say hello"
     :default-value (:user @state)
     ;; :bs-style (validation-state (:text state))
     :on-change #(handle-change % state)}]
   [:input {:type "button"
            :bs-style "primary"
            :on-click (fn [_]
                        (reset! nav {:view :hi})
                        (app/start-user! state))
            :value "Say Hi"}]])

(defn hi [nav state]
  [:div
   [:p (str "Hello " (:user @state) " " (:n @state))]
   [:a {:type "button" :href "#/"} "Bye"]])

(defn root [nav app-state]
  [:div
   [:h1 "Example Leaven based WebApp"]
   [:div
    (case (:view @nav)
      :who (who nav app-state)
      :hi (hi nav app-state))]])
