(ns combinator.macros)



(defmacro timing [x]
  `(do
     (println ~x)
     ~x
     ~x
     ~x
     (time ~x)))
