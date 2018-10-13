(ns agile_backend.models.model
  (:require [clojure.java.jdbc :as sql]
            [clojure.data.json :as json]
            [compojure.handler :as handler]
            [clojure.string :as str]
            [clj-time.coerce :as c]
            [clj-time.format :as f]
            [clj-time.core :as t]
            ))

(def db {:dbtype "postgresql"
         :dbname "agile_backend"
         :host "127.0.0.1"
         :port "5433"
         :user "admin"
         :password ""})

    ; getting the current date
(defn now [] (new java.util.Date))

(defn select-deposit []
  (sql/query db ["SELECT * FROM deposits "]))

(defn create-deposit [params]
  (sql/insert! db :deposits {:name (let [name  (get params "name")] name)
                             :balance (let [balance  (get params "balance")] balance)}))

(defn update-deposit [id params]
  (sql/update! db :deposits {:name (let [name  (get params "name")] name)
                             :balance (let [balance  (get params "balance")] balance)}
               ["id = ?" id]))

(defn delete-deposit [id]
  (sql/delete! db :deposits ["id = ?", id]))


  (defn create-transaction [params]
    (sql/insert! db :transactions {:deposit_id (let [deposit  (get params "deposit")] deposit)
                               :amount (let [amount  (get params "amount")] amount)
                               :creation_ts (let [creation_ts  (get params "date")] (c/to-sql-date creation_ts))
                               }))
   (defn check-tag[tag]
    (sql/execute! 
      db 
      ["insert into tags(name) select (?) from tags where not exists(select 1 from tags where name=(?));"  tag]  
      {:multi? false} ))
;  name уникальный ключ
;insert into tags(name) values('tag7') on confict do nothing; уникальный ключ
    


  (defn create-tag-transaction[id params]
    (def tags (apply list (get params "tags")))
    
    (for [item tags] (apply str(check-tag item)))
    )

   (defn create-trans[params]

    (def id (apply str(map :id (create-transaction params))))
    ; (str id) 
    (create-tag-transaction id params)
    
    ; (create-tag-transaction)
    
; ({:id 32, :parent_id nil, :amount 4.2M, :author nil, :creation_ts nil, :deposit_id 1})
)

; (defn select-transactions []
;     (sql/query db ["SELECT * FROM transactions "]))


; (defn create-tag-transaction [tag-transaction]
;   (sql/insert! db :tags-transactions tag-transaction))

; (defn select-transactions-by-parent [parent]
;   (sql/query db ["SELECT * FROM transactions WHERE parent = ?", parent]))







; (defn select-transactions-by-deposit [deposit]
;   (sql/query db ["SELECT * FROM transactions WHERE deposit_id = ?", deposit]))

; (defn select-transaction-recursive [id]
;   (sql/query db ["with recursive trans(id, parent_id)
;  as (
;    select id, parent_id from transactions where id = ?
;    union
;    select t.id, t.parent_id from transactions as t
;      join trans on trans.parent_id = t.id",id]))

; (defn select-tags-name []
;   (sql/query db ["select tags.name  from trans as t
;             join tags_transactions as tt on t.id = tt.transaction_id
;             join tags on tt.tag_id = tags.id"]))

; (defn update-transaction [id m])

; ;    (defn update-deposit[request]
; ;     (sql/update! db :transactions {:name (let[name  (get-in request[:params :name]) ] name) 
; ;         :balance (read-string(let[balance   (get-in request[:params :balance]) ]balance))} :transactions/where 
; ;         {:id (read-string(let[id(get-in request[:params :id]) ]id))})
; ;     )

; (defn update-tag [id m]
;   (sql/update! db :tags m :tags/where {:id id}))

; (defn delete-transaction [id]
;   (sql/delete! db :transactions ["id = ?", id]))

; (defn delete-tag [id]
;   (sql/delete! db :tags ["id = ?", id]))



; ; (defn create-transaction[])