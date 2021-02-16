(ns gp-birthday-greeting.core
  (:gen-class)
  (:require [clojure.string :as str]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def friend "Hannah, Ferguson, 1952/01/19, hannah.ferguson@fakemail.com")

(defn split-line
  "Parse a friend line"
  [friend]
  (str/split friend #"\s*,\s*"))


(defn parse-dob [dob]
  {:year  year
   :month month
   :day   day})

(defn parse-line [friend]
  (let [[firstname lastname dob email] (split-line friend)
        {:keys [:year :month :day]}    (parse-dob dob)]
    {:firstname firstname
     :lastname  lastname
     :dob       dob
     :year      year
     :month     month
     :day       day
     :email     email}))


(defn matching-birthdee? [friend day])


(comment
  (println "Hello kata")
  (split-line friend)
  (parse-line friend)

  (str/split "one, two, three" #"\s*,\s*")

  ,)
