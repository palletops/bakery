(ns example.webapp.main
  (:gen-class)
  (:require
   [clojure.tools.cli :refer [parse-opts]]
   [com.palletops.leaven :refer [start]]
   [example.webapp.server :as server-app]
   [taoensso.timbre :refer [debugf infof]]))

(def cli-options
  ;; An option with a required argument
  [["-p" "--port PORT" "Port number"
    :default 3000
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]])

(defn -main [& args]
  (let [{:keys [options errors]} (parse-opts args cli-options)
        server (server-app/server options)]
    (start server)))
