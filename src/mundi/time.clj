(ns mundi.time)


(defn todays-date
  "Return a java.util.Date with the hours, minutes, secons and millis zeroed"
  []
  (doto (java.util.Calendar/getInstance)
    (.set java.util.Calendar/HOUR 0)
    (.set java.util.Calendar/MINUTE 0)
    (.set java.util.Calendar/SECOND 0)
    (.set java.util.Calendar/MILLISECOND 0)))


(defn to-inst
  "Given a string representing a timestamp (in W3C datetime format, http://www.w3.org/TR/NOTE-datetime), return an instant"
  [^String s]
  (try
    (clojure.instant/read-instant-date s)
    (catch Exception e
      (todays-date))))
