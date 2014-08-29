(ns example.webapp.server.index
  "Injections for the index page"
  (:require
   [example.webapp.server.script-inject :refer [page]]))

(defn js-script
  "Return a script tag to connect to the cljs REPL."
  []
  "<script src=\"js/example.webapp.min.js\" type=\"text/javascript\"></script>")

(defn js-script-dev
  "Return a script tags for the dev page."
  []
  (str
   "<script src='http://fb.me/react-0.11.1.js' type='text/javascript'></script>"
   "<script src='js/goog/base.js' type='text/javascript'></script>"
   "<script src='js/example.webapp.js' type='text/javascript'></script>"
   "<script type='text/javascript'>goog.require('example.webapp.dev');</script>"))

(defn index-page [tags]
  (page "public/index.html" tags))
