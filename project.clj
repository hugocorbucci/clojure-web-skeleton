(defproject skeleton (str "1.0.0_" (System/getenv "BUILD_NUMBER"))
  :description "A skeleton clojure server"
  :url "https://skeleton.herokuapps.com"
  :license {:name "MIT"}
  :min-lein-version "2.0.0"
  :source-paths ["src/clj", "war/WEB-INF/classes"] ;Making log4j available
  :test-paths ["test/clj"]
  :main skeleton.web
  :aot [skeleton.web]
  :dependencies [[org.clojure/clojure "1.8.0"]
    [org.clojure/data.zip "0.1.1"]
    [org.clojure/data.xml "0.0.8"]
    [org.clojure/data.json "0.2.6"]
    [org.clojure/tools.logging "0.3.1"]
    [javax.servlet/servlet-api "2.5"]
    [compojure "1.5.0"]
    [ring/ring-defaults "0.2.0"]
    [ring/ring-core "1.4.0"]
    [ring/ring-devel "1.4.0"]
    [ring/ring-json "0.4.0"]
    [ring.middleware.logger "0.5.0"]
    [http-kit "2.1.19"]
    [environ "1.0.2"]
    [clj-http "2.1.0"]
    [hiccup "1.0.5"]
    [com.novemberain/monger "3.0.2"]
    [clj-time "0.11.0"]]
  :dev-dependencies [[com.jakemccrary/lein-test-refresh "0.11.0"]
    [lein-cloverage "1.0.2"]
    [clj-livereload "0.2.0"]
    [org.clojure/tools.nrepl "0.2.12"]]
  :plugins [[lein-environ "1.0.2"]
    [lein-ring "0.9.7"]
    [com.jakemccrary/lein-test-refresh "0.11.0"]]
  :ring {:handler skeleton.web/handler}
  :uberjar {:aot :all}
  :uberjar-name "skeleton-standalone.jar"
  :profiles {
    :production
    {:env {:production "true" :clj-env "production"}}
    :test
    { :plugins [[lein-cloverage "1.0.2"]
        [lein-dotenv "RELEASE"]]
      :resource-paths ["resources" "test/resources/"]
      :dependencies [[pjstadig/humane-test-output "0.7.1"]]
      :env {:test "true" :clj-env "test"}
      :injections [(require 'pjstadig.humane-test-output)
        (pjstadig.humane-test-output/activate!)]}
    :dev
    { :plugins [[lein-dotenv "RELEASE"]]
      :env {:development "true" :clj-env "development"}
      :resource-paths ["resources"]
      :dependencies [[clj-livereload "0.2.0"]]
      :injections [(require 'clj-livereload.server)
        (clj-livereload.server/start! {:paths ["resources/public/" "src/clj/skeleton/"] :debug? true})]}
    :repl
    { :plugins [[lein-dotenv "RELEASE"]]
      :env {:development "true" :clj-env "development"}
      :dependencies [[org.clojure/tools.nrepl "0.2.11"]]
      :resource-paths ["resources"]}}
  :test-refresh {:notify-command ["terminal-notifier" "-title" "Skeleton Tests" "-message"]})
