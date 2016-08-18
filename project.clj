(let [version (str "1.0.0_" (or (System/getenv "BUILD_NUMBER") "SNAPSHOT"))]
  (defproject skeleton version
    :description "A skeleton clojure server"
    :url "https://skeleton.herokuapps.com"
    :license {:name "MIT"}
    :min-lein-version "2.0.0"
    :source-paths ["src/clj", "war/WEB-INF/classes"] ;Making log4j available
    :test-paths ["test/clj"]
    :main skeleton.web
    :aot [skeleton.web]
    :dependencies [[org.clojure/clojure "1.8.0"]
      [org.clojure/data.zip "0.1.2"]
      [org.clojure/data.xml "0.0.8"]
      [org.clojure/data.json "0.2.6"]
      [org.clojure/tools.logging "0.3.1"]
      [javax.servlet/javax.servlet-api "3.1.0"]
      [compojure "1.5.1"]
      [ring/ring-defaults "0.2.1"]
      [ring/ring-core "1.5.0"]
      [ring/ring-devel "1.5.0"]
      [ring/ring-json "0.4.0"]
      [ring.middleware.logger "0.5.0"]
      [http-kit "2.2.0"]
      [environ "1.1.0"]
      [clj-http "3.1.0"]
      [hiccup "1.0.5"]
      [com.novemberain/monger "3.0.2"]
      [ragtime "0.6.3"]
      [org.postgresql/postgresql "9.4.1209"]
      [clojure.jdbc/clojure.jdbc-c3p0 "0.3.2"]
      [honeysql "0.8.0"]
      [clj-time "0.12.0"]
      [optimus "0.19.0"]]
    :plugins [[lein-environ "1.0.2"]]
    :ring
      { :handler skeleton.web/handler
        :uberwar-name ~(str "skeleton-with-dependencies_" version ".war")}
    :uberjar {:aot :all}
    :uberjar-name ~(str "skeleton-standalone_" version ".jar")
    :profiles {
      :production
      {:env {:production "true" :clj-env "production"}}
      :test
      { :plugins [[com.jakemccrary/lein-test-refresh "0.16.0"]
          [lein-cloverage "1.0.6"]
          [lein-dotenv "RELEASE"]]
        :resource-paths ["resources" "test/resources/"]
        :dependencies [[pjstadig/humane-test-output "0.8.1"]]
        :env {:test "true" :clj-env "test"}
        :injections [(require 'pjstadig.humane-test-output)
          (pjstadig.humane-test-output/activate!)]}
      :dev
      { :plugins [[lein-dotenv "RELEASE"] [lein-cooper "1.2.2"]]
        :env {:development "true" :clj-env "development"}
        :resource-paths ["resources"]
        :dependencies [[clj-livereload "0.2.0"]
          [org.clojure/tools.nrepl "0.2.12"]]
        :injections [(require 'clj-livereload.server)
          (clj-livereload.server/start! {:paths ["resources/public/" "src/clj/skeleton/"] :debug? true})]
        :cooper {"test"   ["lein" "with-profile" "base,test" "test-refresh"]
                 "mongo"  ["mongod" "--dbpath" "tmp/db/data"]
                 "server" ["lein" "run"]}}
      :repl
      { :plugins [[lein-dotenv "RELEASE"]]
        :env {:development "true" :clj-env "development"}
        :dependencies [[org.clojure/tools.nrepl "0.2.12"]]
        :resource-paths ["resources"]}}
    :test-refresh {:notify-command ["terminal-notifier" "-title" "Skeleton Tests" "-message"]}
    :aliases {"migrate"  ["with-profile" "base" "run" "-m" "skeleton.db.migration/migrate"]
              "rollback" ["with-profile" "base" "run" "-m" "skeleton.db.migration/rollback"]}))
