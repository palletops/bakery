(ns example.webapp.nav
  "Navigate by setting values under the state :nav key")

(defn update!
  [state korks v]
  (swap! state assoc-in (if (sequential? korks) korks [korks]) v))

(defn set-nav!
  [state v]
  (update! state :nav v))
