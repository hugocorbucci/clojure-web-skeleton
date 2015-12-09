(ns skeleton.web
  (:gen-class)
  (:use org.httpkit.server
    [hiccup.page :only (include-css include-js)])
  (:require
    [ring.middleware.reload :as reload]
    [compojure.core :refer [defroutes GET PUT POST DELETE ANY context routes]]
    [compojure.route :as route]
    [clj-time.core :as t]
    [ring.middleware.json :as middleware]
    [hiccup.middleware :as hiccup-middleware]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
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

(defn json-response [data & [status]]
  {:status  (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body    data})

(defroutes site-routes
  (GET "/" request
    (json-response "Hi"))
  (route/resources "/")
  (route/not-found (slurp (io/resource "404.html"))))

(defroutes api-routes
  (context "/api" []
    (context "/v1" []
      (route/not-found (slurp (io/resource "404.html"))))))

(def handler
  (routes
    (-> api-routes
      (middleware/wrap-json-body)
      (middleware/wrap-json-response))
    (hiccup-middleware/wrap-base-url site-routes)))

(def app
  (if (in-dev?)
      (reload/wrap-reload handler) ;; only reload when dev
      handler))

(defn -main [& [port]] ;; entry point, lein run will pick up and start from here
  (let [port (Integer. (or port (env :port) 5000))]
    (run-server app {:port port :join? false})))
