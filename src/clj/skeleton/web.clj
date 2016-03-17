(ns skeleton.web
  (:gen-class)
  (:use org.httpkit.server
    [hiccup.page :only (html5 include-css include-js)])
  (:require
    [ring.middleware.reload :as reload]
    [compojure.core :refer [defroutes GET PUT POST DELETE ANY context routes wrap-routes]]
    [compojure.route :as route]
    [clj-time.core :as t]
    [hiccup.middleware :as hiccup-middleware]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
    [ring.middleware.json :as json-middleware]
    [ring.middleware.anti-forgery :as anti-forgery]
    [ring.util.response :refer [response redirect]]
    [clojure.java.io :as io]
    [environ.core :refer [env]]
    [cheshire.core :refer [parse-string]]
    [org.httpkit.timer :refer [schedule-task]]
    (ring.middleware [multipart-params :as mp])
  )
)

(def database-url
  (env :database-url))

(defn in-dev? [] (env :dev))

(defroutes site-routes
  (GET "/" request
    (html5 [:p "Hello World"] (include-js "/js/template.js"))))

(defroutes api-routes
  (context "/api" []
    (context "/v1" []
      (GET "/" request (response {:type "success" :message "Hi world"})))))

(def handler
  (wrap-defaults
    (routes
      (-> api-routes
        (wrap-routes json-middleware/wrap-json-body)
        (wrap-routes json-middleware/wrap-json-response))
      (wrap-defaults
        (-> site-routes
          (wrap-routes hiccup-middleware/wrap-base-url)
          (wrap-routes anti-forgery/wrap-anti-forgery))
        (assoc-in site-defaults [:security :anti-forgery] false)))
    api-defaults))

(def app
  (if (in-dev?)
      (reload/wrap-reload handler) ;; only reload when dev
      handler))

(defn -main [& [port]] ;; entry point, lein run will pick up and start from here
  (let [p (Integer. (or port (env :PORT) 5000))]
    (run-server app {:port p :join? false})))
