(ns tagged-finances.model
  (:require [clojure.java.jdbc :as sql]
            [clojure.data.json :as json]
            [compojure.handler :as handler]
            [environ.core :refer [env]]
            [clojure.string :as str]
            [clj-time.coerce :as c]
            [clj-time.format :as f]
            [clj-time.core :as t]))

(def db {:connection-uri (or (env :jdbc-database-url) "jdbc:postgresql://localhost:5432/agile_backend?user=postgres")})

    ; getting the current date
(defn now [] (new java.util.Date))


(defn select-deposit []
  (sql/query db ["select * from deposits"]))

(defn get-deposit [id]
  (let [result (sql/query db ["select * from deposits where id = ?" id])]
    (if (empty? (rest result))
      (first result))))

(defn create-deposit [params]
  (first (sql/insert! db :deposits {:name (let [name  (get params "name")] name)
                                    :balance (let [balance  (get params "balance")] balance)})))

(defn update-deposit [id params]
  (sql/update! db :deposits {:name (let [name  (get params "name")] name)
                             :balance (let [balance  (get params "balance")] balance)}
               ["id = ?" id]))

(defn delete-deposit [id]
  (sql/delete! db :deposits ["id = ?", id]))


(defn create-transaction [params]
  (first (sql/insert! db :transactions {:deposit_id (let [deposit_id  (get params "deposit_id")] deposit_id)
                                        :amount (let [amount  (get params "amount")] amount)
                                        :creation_ts (let [creation_ts  (get params "date")]  (c/to-sql-date creation_ts))
                                        :tags (let [tags  (get params "tags")] tags)})))

(defn update-transaction [id params]
  (sql/update! db :transactions
               {:deposit_id (let [deposit_id  (get params "deposit_id")] deposit_id)
                :amount (let [amount  (get params "amount")] amount)
                :creation_ts (let [creation_ts  (get params "date")]  (c/to-sql-date creation_ts))
                :tags (let [tags  (get params "tags")] tags)}
               ["id = ?" id]))
(sql/update! db :transactions {:deposit_id 2} ["id=?" 14])
(update-transaction 14  {"date" "2018-10-12"
                         "tags" "tag1,tag2,tag3,tag4"
                         "deposit_id" 1
                         "amount" 40})

(defn select-transaction []
  (sql/query db ["select * from transactions"]))

(defn delete-transaction [id]
  (sql/delete! db :transactions ["id = ?", id]))