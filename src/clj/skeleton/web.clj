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
    [ring.middleware.multipart-params :as mp]
    [ring.middleware.logger :as logger]
    [clojure.tools.logging :as log]
    [clansi.core :as clansi]
    [ring.util.response :refer [response redirect]]
    [clojure.java.io :as io]
    [environ.core :refer [env]]
    [cheshire.core :refer [parse-string]]
    [org.httpkit.timer :refer [schedule-task]]
    [optimus.prime :as optimus]
    [optimus.assets :as assets]
    [optimus.optimizations :as optimizations]
    [optimus.strategies :as strategies]
    [optimus.link :as link]
  )
)

(def database-url
  (env :database-url))

(defn in-dev? [] (= "true" (env :dev)))

(defroutes site-routes
  (GET "/" request
    (html5
      {:lang "en"}
      [:head
        [:meta{:http-equiv "Content-Type" :content "text/html; charset=UTF-8" :charset "utf-8"}]]
      [:body {}
        [:p "Hello World"]
        (map include-js (link/bundle-paths request ["application.js"]))])))

(defroutes api-routes
  (context "/api" []
    (context "/v1" []
      (GET "/" request (response {:type "success" :message "Hi world"})))))

(defn get-assets []
  (concat
    (assets/load-bundles "public"
      {"application.js" ["/js/template.js"]})))

(defn wrap-with-logger [handler]
  (let [logging-handler (logger/wrap-with-logger handler)]
    (if (in-dev?)
      logging-handler
      (fn [request]
        (let [response (clansi/without-ansi (logging-handler request))]
          response)))))

(def wrapped-handler
  (->
    (routes
      (-> api-routes
        (wrap-routes json-middleware/wrap-json-body)
        (wrap-routes json-middleware/wrap-json-response))
      (-> site-routes
          (wrap-routes hiccup-middleware/wrap-base-url)
          (wrap-routes anti-forgery/wrap-anti-forgery)
          (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))
    (wrap-defaults api-defaults)
    (optimus/wrap get-assets
      (if (in-dev?) optimizations/none optimizations/all)
      (if (in-dev?) strategies/serve-live-assets strategies/serve-frozen-assets))
    (wrap-with-logger)))

(def handler
  (if (in-dev?)
      (reload/wrap-reload wrapped-handler) ;; only reload when dev
      wrapped-handler))

(defn -main [& [port]] ;; entry point, lein run will pick up and start from here
  (let [p (Integer. (or port (env :PORT) 5000))]
    (run-server handler {:port p :join? false})))
