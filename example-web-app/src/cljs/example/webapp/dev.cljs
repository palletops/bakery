(ns example.webapp.dev
  "Dev client"
  (:require-macros
   [example.webapp.server.dev :as dev-clj :refer [weasel-config-map]])
  (:require
   [com.palletops.bakery.weasel :as weasel]
   [com.palletops.leaven :as leaven :include-macros true]
   [example.webapp.client :as client]))

(enable-console-print!)

(leaven/defsystem DevUi [:ui :weasel])

(def weasel-config
  (dev-clj/weasel-config-map))

(def dev-ui (atom (map->DevUi {:ui (client/make-ui)
                               :weasel (weasel/weasel weasel-config)})))

(defn start []
  (try
    (swap! dev-ui leaven/start)
    ;; This is to try and provide useful information in the console if
    ;; an exception occurs on start.
    (catch ExceptionInfo e
      (.log js/console e)
      (if-let [c (ex-cause e)]
        (throw c)
        (throw e)))))

(defn stop []
  (swap! dev-ui leaven/stop))

(.log js/console "Starting dev client")
(start)
(.log js/console "Dev client started")
