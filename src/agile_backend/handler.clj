(ns agile_backend.handler
  (:require [migratus.core :as migratus]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.util.response :refer [response]]
            [ring.middleware.json :as middleware]
            [compojure.route :as route]
            [clojure.data.json :as json]
            [agile_backend.models.model :as model]))


    ; (def migratus-config
    ;           {:store :database
    ;            :migration-dir "migrations"
    ;            :db  {:subprotocol "postgresql" 
    ;                          :subname "//localhost:5434/agile_backend" 
    ;                          :user "admin" 
    ;                          :password "pass"}})

(defroutes app-routes
  (context "/api/deposits" [] (defroutes deposits-routes

           ;get query fetch result of all deposits
                                (GET "/" [] response (model/select-deposit))


          ;post query creates new deposit based on name and balance written in json raw data.
          ; response is inserted data
          ;example of input raw json data to backend
          ; {
          ;   "deposit": {
          ;     "name": "Sber",
          ;     "balance": 5.22
          ;   }
          ; }


                                (POST "/" {body :body} (model/create-deposit (json/read-str (slurp body))))


        ;  PUT and DELETE queries need id of deposit


                                (context "/:id" [id] (defroutes document-routes


        ; PUT query update deposit with selected id based on name and balance written in json raw data
        ; response is 1
        ;example of input raw json data to backend
          ; {
          ;   "deposit": {
          ;     "name": "Sber",
          ;     "balance": 5.22
          ;   }
          ; }


                                                       (PUT "/" {body :body} (model/update-deposit (read-string id) (json/read-str (slurp body))))


      ; delete query delete query with id. response is 1


                                                       (DELETE "/" [] (model/delete-deposit (read-string id)))))))
  (context "/api/transactions" [] (defroutes transactions-routes

       ;get query fetch result of all transactions
;       ; {
;   "date": "2018-10-10",
;   "tags": "tag1,tag2,tag3",
;   "deposit_id": 1,
;   "amount": 40
; }
                                    (POST "/" {body :body} (model/create-transaction (json/read-str (slurp body))))))
  (route/not-found "Not Found"))

        ; (GET "/api/deposits" [] (response (vec(model/select-deposits) )))

        ; ; (POST "/api/deposits" [name balance :as request] (model/create-deposit request))
        ; ; (PUT "/api/deposits/update/:id")[id name balance :as request] (model/update-deposit request )
        ; (DELETE "/api/transaction/delete-deposit/:id" [id]
        ;   (response (model/delete-deposit id)))
        ; (GET "/api/transactions/select-transactions-by-parent" [parent]  (response (model/select-transactions-by-parent parent)))
        ; (GET "/api/transaction/select-transactions-by-deposit"[deposit])
        ; (DELETE "/api/transaction/delete-transaction/:id" [id]
        ;   (response (model/delete-transaction id)))

        ; (route/resources "/")
        ; )

(defn wrap-log-request [handler]
  (fn [req]
    (println req)
    (handler req)))

; (def app
;   (-> (handler/api app-routes)
;     ;wrap-log-request
;     json/wrap-json-response
;     json/wrap-json-params
;     json/wrap-json-body {:keywords? true}))

(def app
  (-> (handler/api app-routes)))
      ;  middleware/wrap-json-body
      ; (middleware/wrap-json-body {:keywords? true})
      ; middleware/wrap-json-response))

(defn init []
  (do
      ; (migratus/migrate migratus-config)
   ))