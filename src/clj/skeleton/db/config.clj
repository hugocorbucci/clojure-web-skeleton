(ns skeleton.db.config
  (:require [environ.core :refer [env]]
    [jdbc.pool.c3p0 :as pool]
    [monger.core :as mg]
    [clojure.tools.logging :as log]))

(def mongo-url
  (env :mongo-url))

(def postgres-url
  (env :postgres-url))


; If you use a relational DB, using JDBC
(def jdbc-config
  (if (nil? postgres-url)
    {}
    (let [uri-matches (re-matches #"^([^:]+):\/\/(?:([^:]*):([^@]*)@)?([^\s\n]*)" postgres-url)
      subprotocol (nth uri-matches 1)
      subname (str "//" (nth uri-matches 4))
      user (nth uri-matches 2)
      password (nth uri-matches 3)]
      (if (nil? user)
        {
          :subprotocol subprotocol
          :subname subname
        }
        {
          :subprotocol subprotocol
          :subname subname
          :user user
          :password password
        }))))

; Using a pool for JDBC connections
(def dbspec (pool/make-datasource-spec jdbc-config))

; For mongo
(defn- disconnect-and-return [conn result]
  (do
    (mg/disconnect conn)
    result))

(defn with-db [op & args]
  (let [{:keys [conn db]} (mg/connect-via-uri mongo-url)
    lazy-result (apply op db args)
    result (if (seq? lazy-result) (doall lazy-result) lazy-result)]
    (disconnect-and-return conn result)))

(defn log-error-and-return [message]
  (do
    (log/error message)
    {:error (str "MONGO: " message)}))
