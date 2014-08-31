(ns example.webapp.server.data-middleware
  "Add data into a request, under a private key.")

(defn- add-data
  "Add state into a request"
  [request state]
  (assoc request ::data state))

(defn wrap-data
  "Add state into the request."
  {:arglists '([handler] [handler options])}
  [handler data]
  (fn [request]
    (handler (add-data request data))))

(defn data
  "Return the data in a request."
  [request]
  (::data request))
