(ns skeleton.hello
  (:require
    [clojure.tools.logging :as log]
    [hiccup.page :as page]
    [optimus.link :as link]))

(defn render-view [request]
  (log/info "Rendering hello world view")
  (page/html5
    {:lang "en"}
    [:head
      [:meta{:http-equiv "Content-Type" :content "text/html; charset=UTF-8" :charset "utf-8"}]]
    [:body {}
      [:p "Hello World"]
      (map page/include-js (link/bundle-paths request ["application.js"]))]))
