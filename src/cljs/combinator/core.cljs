(ns combinator.core
  (:require-macros [combinator.macros :as m])
  (:require [combinator.evaluate :as e]
            [combinator.zip :as z]))

(defn js-print [& args]
  (if (js* "typeof console != 'undefined'")
    (.log js/console (apply str args))
    (js/print (apply str args))))

(set! *print-fn* js-print)

(defn result [n]
  (loop [i 0 term '(((:e ((:e :e) :e)) :e) :e)]
    (if-not (> i n)
      (recur (inc i) (e/replace-all term e/sample-combinator))
      term)))

(println (result 300))

(dotimes [_ 20]
  (time (result 300)))

;; median timing is 240msec
