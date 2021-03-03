(ns gp-birthday-greeting.core
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def friend "Hannah, Ferguson, 1952/01/19, hannah.ferguson@fakemail.com")

(defn split-line
  "Parse a friend line"
  [friend]
  (str/split friend #"\s*,\s*"))


(let [{a :b} {:b 1}]
  [a])


(defn birthdee->reminder-date [birthdee]
  (let [{:keys [:month :day]} birthdee]
    {:month month :day (dec day)}))


(def birthday-email-template
  {:body    "Happy Birthday ${firstname} ${lastname}, have a good one!!"
   :subject "Happy Birthday!"})


(def reminder-email-template
  {:body "Remember to email: ${friends}"
   :subject "Upcoming birthdays"})


(defn birthday-email [template birthdee]
  {:email/body    (-> (:body template)
                      (str/replace "${firstname}" (:firstname birthdee))
                      (str/replace "${lastname}" (:lastname birthdee)))
   :email/to      (:email birthdee)
   :email/subject (:subject template)})


(defn full-name [{:keys [:firstname :lastname]}]
  (str firstname " " lastname))


(defn reminder-email [template birthdees recipient]
  (let [names (str/join ", " (map full-name birthdees))]
    {:email/body    (-> (:body template)
                        (str/replace "${friends}" names))
     :email/to      recipient
     :email/subject (:subject template)}))


(defn reminder? [birthdee date]
  (let [{birthdee-month :month
         birthdee-day   :day} birthdee
        {given-month :month
         given-day   :day}    date]
    (and (= birthdee-month given-month)
         (= birthdee-day given-day))))

(defn parse-dob [dob]
  (let [[year month day] (str/split dob #"/")]
    {:year  (Integer/parseInt year)
     :month (Integer/parseInt month)
     :day   (Integer/parseInt day)}))

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

(defn parse-file [file] (->>
                         file
                         (slurp)
                         (str/split-lines)
                         (map parse-line)))

(defn matching-birthdee? [friend date]
  ;; TODO Add logic to handle the next month date and 29 Feb date.
  (and (= (:month friend) (:month date))
       (= (:day friend) (:day date))))

(defn today []
  (let [date (java.util.Date.)]
    {:month (inc (.getMonth date))
     :day (.getDate date)}))


;; friends == path to the file
;; today == today's date
(defn send-emails!
  ([friends-file]
   (send-emails! friends-file (today) birthday-email-template))
  ([friends-file date]
   (send-emails! friends-file date birthday-email-template))
  ([friends-file date template]
   (->> friends-file
        parse-file
        (filter #(matching-birthdee? % date))
        (map #(birthday-email template %))
        (map prn)
        doall)))

(defn get-birthdees [friends-file date]
  (->> friends-file
        parse-file
        (filter #(matching-birthdee? % date))))

(defn send-reminder-email [friends-file date template recipient]
  (let [birthdees (get-birthdees friends-file date)]
    (reminder-email template birthdees recipient)))


(comment
  (println "Hello kata")
  (split-line friend)
  (parse-line friend)


  (send-reminder-email (io/resource "friends.txt")
                       {:month 2 :day 10}
                       reminder-email-template
                       "bob@example.com")


  (str/join " " ["a" "b"])

  (str/split "one, two, three" #"\s*,\s*")
  (parse-dob "2000/01/")
  (reminder? (parse-line friend) {:month 1 :day 19})
  (parse-file (io/resource "friends.txt"))

  (send-emails! (io/resource "friends.txt")
                {:month 2 :day 10}
                birthday-email-template)


  (filled-email-template template (parse-line friend))
   
  ,)
