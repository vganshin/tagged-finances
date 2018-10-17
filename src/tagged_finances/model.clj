(ns tagged-finances.model
  (:require [clojure.java.jdbc :as sql]
            [clojure.data.json :as json]
            [compojure.handler :as handler]
            [environ.core :refer [env]]
            [clojure.string :as str]
            [clj-time.coerce :as c]
            [clj-time.format :as f]
            [clj-time.core :as t]))

(def db {:connection-uri (or (env :jdbc-database-url) "jdbc:postgresql://localhost:5432/agile_backend?user=postgres&password=postgres")})

(defn select-deposit []
  (sql/query db ["select * from deposits"]))

(defn get-deposit [id]
  (let [result (sql/query db ["select * from deposits where id = ?" id])]
    (if (empty? (rest result))
      (first result))))

(defn create-deposit [params]
  (first (sql/insert! db :deposits
                      {:name (let [name  (get params "name")] name)
                       :balance (let [balance  (get params "balance")] balance)})))

(defn update-deposit [id params]
  (if (= 1 (first (sql/update! db :deposits
                               {:name (let [name  (get params "name")] name)
                                :balance (let [balance  (get params "balance")] balance)}
                               ["id = ?" id])))
    (get-deposit id)
    (throw (Exception. "The line didn't update!"))))

(defn delete-deposit [id]
  (sql/delete! db :deposits ["id = ?", id]))

(defn create-transaction [params]
  (first (sql/insert! db :transactions
                      {:deposit_id (let [deposit_id  (get params "deposit_id")] deposit_id)
                       :amount (let [amount  (get params "amount")] amount)
                       :creation_ts (let [creation_ts  (get params "date")]  (c/to-sql-date creation_ts))
                       :tags (let [tags  (get params "tags")] tags)})))

(defn select-transaction []
  (sql/query db ["select * from transactions"]))

(defn get-transaction [id]
  (let [result (sql/query db ["select * from transactions where id = ?" id])]
    (if (empty? (rest result))
      (first result))))

(defn update-transaction [id params]

  (if (= 1
         (first (sql/update! db :transactions
                             {:deposit_id (let [deposit_id  (get params "deposit_id")] deposit_id)
                              :amount (let [amount  (get params "amount")] amount)
                              :creation_ts (let [creation_ts  (get params "date")]  (c/to-sql-date creation_ts))
                              :tags (let [tags  (get params "tags")] tags)}
                             ["id = ?" id])))
    (get-transaction id)
    (throw (Exception. "The line didn't update!"))))

(defn delete-transaction [id]
  (sql/delete! db :transactions ["id = ?", id]))
