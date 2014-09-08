(ns example.webapp.app)

(defn set-user
  [state user]
  (assoc state :user user))

(defn start-user!
  [state]
  (.log js/console "start user %s %s" (pr-str @state) (pr-str (type (:send-fn @state))))
  (@(:send-fn @state) [:example/msg (:user @state)]))

(defn greet
  [state n]
  (swap! state #(-> % (assoc :n n))))
