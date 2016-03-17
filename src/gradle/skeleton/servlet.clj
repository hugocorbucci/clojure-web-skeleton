(clojure.core/ns skeleton.servlet
  (:gen-class :extends javax.servlet.http.HttpServlet))
(def service-method)
(clojure.core/defn -service [arg1 arg2 arg3]
  (service-method arg1 arg2 arg3))
