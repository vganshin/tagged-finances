(defproject agile-backend "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://tagged-finances.herokuapp.com"
  :license {:name "MIT"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [compojure "1.6.1"]
                 [hiccup "1.0.5"]
                 [postgresql/postgresql "9.3-1102.jdbc41"]
                 [org.clojure/java.jdbc "0.7.0"]
                 [lib-noir "0.9.9"]
                 [clj-time "0.14.0"]
                 [migratus "0.9.8"]
                 [prone "1.1.4"]
                 [ring/ring-anti-forgery "1.1.0"]
                 [ring/ring-defaults "0.3.1"]
                 [ring/ring-json "0.1.2"]
                 [clj-json "0.2.0"]
                 [compojure "1.1.8"]
                 [ring/ring-jetty-adapter "1.2.2"]
                 [ring/ring-devel "1.2.2"]
                 [ring-basic-authentication "1.0.5"]
                 [environ "0.5.0"]
                 [com.cemerick/drawbridge "0.0.6"]]

  :plugins [[lein-ring "0.9.2"]
            [lein-ancient "0.6.10"]
            [migratus-lein "0.5.0"]]
  :migratus {:store :database
             :migration-dir "migrations"
             :db  {:subprotocol "postgresql"
                   :subname "//localhost:5433/agile_backend?charSet=UTF8"
                   :user "admin"
                   :password ""}}
  :ring {:handler agile_backend.handler/app
         :init agile_backend.handler/init}
  :uberjar-name "tagged-finances-standalone.jar"
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [kerodon "0.8.0"]
                        [ring/ring-mock "0.3.1"]]
         :ring {:stacktrace-middleware prone.middleware/wrap-exceptions}}})
