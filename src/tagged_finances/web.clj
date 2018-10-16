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
   [tagged-finances.model :as model]
   [environ.core :refer [env]]))

(def migratus-config
  {:store :database
   :migration-dir "migrations"
   :db  {:subprotocol "postgresql"
         :subname "//localhost:5432/agile_backend"
         :user "postgres"
         :password ""}})

(defn- authenticated? [user pass]
  ;; TODO: heroku config:add REPL_USER=[...] REPL_PASSWORD=[...]
  (= [user pass] [(env :repl-user false) (env :repl-password false)]))

(def ^:private drawbridge
  (-> (drawbridge/ring-handler)
      (session/wrap-session)
      (basic/wrap-basic-authentication authenticated?)))

(defn my-value-writer [key value]
  (if (= key :creation_ts)
    (str (java.sql.Date. (.getTime value)))
    value))

(defn my-value-reader [key value]
      (if (= key :date)
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
    {:body (model/update-deposit id (json/read-str (slurp body)))
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
    {:body (model/update-transaction id (json/read-str (slurp body) :value-fn my-value-reader :key-fn keyword))
     :headers {"Content-Type" "application/json;charset=utf-8"}})
  (DELETE "/:id" [id :<< as-int] (model/delete-transaction id) {:status 204}))

(defroutes api
  (context "/deposits" [] deposits)
  (context "/transactions" [] transactions))

(defroutes app
  (context "/api" [] api)
  (route/resources "/")
  (GET "*" [] (slurp (io/resource "public/index.html"))))

#_(defroutes app
    (context "/api/deposits" []
      (defroutes deposits-routes;get query fetch result of all deposits
        (GET "/" [] response (json/write-str (model/select-deposit)));post query creates new deposit based on name and balance written in json raw data.
              ; response is inserted data
              ;example of input raw json data to backend
              ; {
              ;   "deposit": {
              ;     "name": "Sber",
              ;     "balance": 5.22
              ;   }
              ; }
        (POST "/" {body :body}
          (model/create-deposit (json/read-str (slurp body))));  PUT and DELETE queries need id of deposit

        (context "/:id" [id]
          (defroutes document-routes; PUT query update deposit with selected id based on name and balance written in json raw data
            ; response is 1
            ;example of input raw json data to backend
              ; {
              ;   "deposit": {
              ;     "name": "Sber",
              ;     "balance": 5.22
              ;   }
              ; }


            (PUT "/" {body :body} (model/update-deposit (read-string id) (json/read-str (slurp body)))); delete query delete query with id. response is 1


            (DELETE "/" [] (model/delete-deposit (read-string id)))))))
    (context "/api/transactions" []
      (defroutes transactions-routes

           ;get query fetch result of all transactions
        (POST "/" {body :body} (model/create-trans (json/read-str (slurp body))))))

    (ANY "/repl" {:as req}
      (drawbridge req))
    (GET "/" [] (slurp (io/resource "public/index.html")))
    (route/resources "/")
    (GET "*" [] (slurp (io/resource "public/index.html")))
    (ANY "*" []
      (route/not-found (slurp (io/resource "404.html")))))

(defroutes app-routes)

(defn wrap-log-request [handler]
  (fn [req]
    (println req)
    (handler req)))

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

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (wrap-app #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
(comment
  (defn restart []
    (.stop server)
    (def server (-main)))

  (restart))