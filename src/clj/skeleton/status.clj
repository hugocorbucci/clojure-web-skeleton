(ns skeleton.status
  (:require
    [clojure.tools.logging :as log]
    [hiccup.page :as page]
    [optimus.link :as link]
    [clj-http.client :as client]
    [monger.collection :as mc]
    [skeleton.db.config :as config]))

(defn- db-status []
  (try
    (config/with-db mc/find-maps "records" {})
    "OK"
    (catch Exception e (str "Unavailable. Error is: " (.getMessage e)))))

(def default-request-url
  "https://www.google.com")

(def default-timeout 1000)

(defn default-request [url timeout]
  {:method :get :url url :socket-timeout timeout :conn-timeout timeout :throw-exceptions false})

(defn timed [op & args]
  (let [writer (java.io.StringWriter.)]
    (merge
      (binding [*out* writer]
        (time (apply op args)))
      {
        :time
        (Integer. (re-find #"\d+" (.toString (.getBuffer writer))))
      })))

(defn- url-status [url]
  (let [response (timed client/request (default-request url default-timeout))]
    {
      :code (response :status)
      :time (response :time)
    }))

(defn render-view [request]
  (let [status (url-status default-request-url)]
    (page/html5
      {:lang "en"}
      [:head
        [:meta{:http-equiv "Content-Type" :content "text/html; charset=UTF-8" :charset "utf-8"}]]
      [:body {}
        [:p "Application is up"]
        [:p "DB connection is " (db-status)]
        [:p "Connection to " default-request-url " has response code " (status :code) " with response time " (status :time) "ms."]])))
