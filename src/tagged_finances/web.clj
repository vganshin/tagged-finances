 (ns tagged-finances.web
  (:require
    [migratus.core :as migratus]
    [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
    [compojure.handler :refer [site]]
    [compojure.core :refer :all]
    [compojure.handler :as handler]
    [compojure.route :as route]
    [compojure.coercions :refer [as-int]]
    [clojure.java.io :as io]
    [ring.middleware.stacktrace :as trace]
    [ring.middleware.session :as session]
    [ring.middleware.session.cookie :as cookie]
    [ring.adapter.jetty :as jetty]
    [ring.middleware.basic-authentication :as basic]
    [cemerick.drawbridge :as drawbridge]
    [ring.util.response :refer [response]]
    [ring.middleware.json :as middleware]
    [clojure.data.json :as json]
    [environ.core :refer [env]]
    [tagged-finances.model :as model]))

(defn my-value-writer [key value]
  (if (= key :creation_ts)
    (str (java.sql.Date. (.getTime value)))
    value))

(defn my-value-reader [key value]
  (if (= key "date")
    (java.sql.Date/valueOf value)
    value))

(defroutes deposits
  (GET "/" []
    {:body (json/write-str (model/select-deposit))
     :headers {"Content-Type" "application/json;charset=utf-8"}})
  (POST "/" {body :body}
    {:body (json/write-str (model/create-deposit (json/read-str (slurp body))))
     :headers {"Content-Type" "application/json;charset=utf-8"}})
  (PUT "/:id" [id :<< as-int :as {body :body}]
    {:body (json/write-str (model/update-deposit id (json/read-str (slurp body))))
     :headers {"Content-Type" "application/json;charset=utf-8"}})
  (DELETE "/:id" [id :<< as-int] (model/delete-deposit id) {:status 204}))

(defroutes transactions
  (GET "/" []
    {:body (json/write-str (model/select-transaction) :value-fn my-value-writer
                           :key-fn name)
     :headers {"Content-Type" "application/json;charset=utf-8"}})
  (POST "/" {body :body}
    {:body (json/write-str (model/create-transaction (json/read-str (slurp body))) :value-fn my-value-writer
                           :key-fn name)
     :headers {"Content-Type" "application/json;charset=utf-8"}})
  (PUT "/:id" [id :<< as-int :as {body :body}]
    {:body (json/write-str (model/update-transaction id (json/read-str (slurp body) :value-fn my-value-reader)) :value-fn my-value-writer)
     :headers {"Content-Type" "application/json;charset=utf-8"}})
  (DELETE "/:id" [id :<< as-int] (model/delete-transaction id) {:status 204}))

(defroutes api
  (context "/deposits" [] deposits)
  (context "/transactions" [] transactions))

(defroutes app
  (context "/api" [] api)
  (route/resources "/")
  (GET "*" [] (slurp (io/resource "public/index.html"))))

(defn wrap-error-page [handler]
  (fn [req]
    (try (handler req)
         (catch Exception e
           {:status 500
            :headers {"Content-Type" "text/html"}
            :body (slurp (io/resource "500.html"))}))))

(defn wrap-app [app]
    ;; TODO: heroku config:add SESSION_SECRET=$RANDOM_16_CHARS
  (let [store (cookie/cookie-store {:key (env :session-secret)})]
    (-> app
        ((if (env :production)
           wrap-error-page
           trace/wrap-stacktrace))
        (site {:session {:store store}}))))

(defonce server nil)

(defn start
  ([] (start {:port 5000}))
  ([{port :port}]
   (if (nil? server)
     (def server (jetty/run-jetty (wrap-app #'app) {:port port :join? false}))
     (throw (Throwable. "The server is running.")))))

(defn stop []
  (.stop server)
  (def server nil))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (start {:port port})))
