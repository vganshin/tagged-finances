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

(defn my-value-writer [key value]
  (if (= key :creation_ts)
    (str (java.sql.Date. (.getTime value)))
    value))


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
  (try
    (if (= 1 (first (sql/update! db :deposits {:name (let [name  (get params "name")] name)
                                               :balance (let [balance  (get params "balance")] balance)}
                                 ["id = ?" id])))
      (json/write-str (get-deposit id)))
    (catch Exception e (str "caught exception: " (.getMessage e)))))


(defn delete-deposit [id]
  (sql/delete! db :deposits ["id = ?", id]))


(defn create-transaction [params]
  (first (sql/insert! db :transactions {:deposit_id (let [deposit_id  (get params "deposit_id")] deposit_id)
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
  (try
    (if (= 1 (first (sql/update! db :transactions
                                 {:deposit_id (let [deposit_id  (get params "deposit_id")] deposit_id)
                                  :amount (let [amount  (get params "amount")] amount)
                                  :creation_ts (let [creation_ts  (get params "date")]  (c/to-sql-date creation_ts))
                                  :tags (let [tags  (get params "tags")] tags)}
                                 ["id = ?" id])))
      (json/write-str (get-transaction id) :value-fn my-value-writer
                      :key-fn name))
    (catch Exception e (str "caught exception: " (.getMessage e)))))






(defn delete-transaction [id]
  (sql/delete! db :transactions ["id = ?", id]))
