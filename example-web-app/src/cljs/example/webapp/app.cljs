(ns example.webapp.app)

(defn set-user
  [state user]
  (assoc state :user user))

(defn start-user!
  [state send-fn]
  {:pre [state send-fn]}
  (.log js/console "start user")
  (send-fn [:example/msg (:user state)]))

(defn greet
  [state n]
  (swap! state #(-> % (assoc :n n))))
