(ns example.webapp.server.script-inject
  "Inject tags into an HTML page"
  (:require
   [clojure.java.io :as io]
   [clojure.zip :as zip]
   [hickory.core :as hc]
   [hickory.render :as hr]
   [hickory.select :as hs]
   [hickory.zip :as hz]))

(defn page-from-resource
  "Load a page from resource-path."
  [resource-path]
  (-> resource-path
      io/resource
      slurp
      hc/parse
      hc/as-hickory))

(defn page
  "Return the page from resource-path, augmented with tag-string inserted
  at the end of the <body> content."
  [resource-path tag-string]
  (->> (page-from-resource resource-path)
       hz/hickory-zip
       (hs/select-next-loc (hs/tag :body))
       ;; (#(zip/append-child % (@injection)))
       (#(reduce (fn [x c] (zip/append-child x c)) %
                 (->> tag-string
                      hc/parse-fragment
                      (map hc/as-hickory))))
       zip/root
       hr/hickory-to-html))
