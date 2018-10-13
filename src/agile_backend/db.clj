; src/agile-backend/db.clj
(ns agile_backend.db
  (:require [jdbc.pool.c3p0 :as pool]))

(def spec
  (sql.pool.c3p0/make-datasource-spec
   {:subprotocol "postgresql"
    :subname "//localhost:5433/agile_backend?charSet=UTF8"
    :user "admin"
    :password ""}))