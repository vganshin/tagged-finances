(ns agile_backend.models.model
  (:require [clojure.java.jdbc :as sql]
            [clojure.data.json :as json]
            [compojure.handler :as handler]
            [clojure.string :as str]
            [clj-time.coerce :as c]
            [clj-time.format :as f]
            [clj-time.core :as t]))

(def db {:connection-uri "jdbc:postgresql://localhost:5434/agile_backend?user=admin&password=pass"})

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
                                 :tags (let [tags  (get params "tags")] tags)}))
                                 
                                 
                                 (defn select-deposit []
                                  (sql/query db ["SELECT * FROM transactions "]))
