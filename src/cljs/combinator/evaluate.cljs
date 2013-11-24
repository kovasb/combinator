(ns combinator.evaluate
  (:require [combinator.zip :as z]))


(defn replace-all [init rulefn]
  (loop [node (z/vector-zip init)]
   (if (z/end? node)
     (z/node node)
     (if-let [m (rulefn  (z/node node))]
       (let [new (z/replace node m)]
         (if-let [r (z/right new)]
           (recur r)
           (z/root new)))
       (recur (z/next node))))))



(def sample-combinator
  (fn [x]
    (if (seq? x)
      (let [l1 (first x)]
        (if (seq? l1)
          (if (keyword-identical? :e (first l1))
            (let [xr (second l1) yr (second x)]
              (list (list xr (list :e yr) ) xr )   ;[[xr [:e yr]] xr]
              )
            false)
          false))
      false)))
