(ns combinator.core
  (:require [clojure.zip])
  (:require [fast-zip.core :as fast-zip])
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

(defn replace-all-core-zip [init rulefn]
  (loop [node (clojure.zip/zipper seq? seq (fn [x y] y) init)]
   (if (clojure.zip/end? node)
     (clojure.zip/node node)
     (if-let [m (rulefn  (clojure.zip/node node))]
       (let [new (clojure.zip/replace node m)]
         (if-let [r (clojure.zip/right new)]
           (recur r)
           (clojure.zip/root new)))
       (recur (clojure.zip/next node))))))

(defn replace-all-fast-zip [init rulefn]
  (loop [node (fast-zip/zipper seq? seq (fn [x y] y) init)]
   (if (fast-zip/end? node)
     (fast-zip/node node)
     (if-let [m (rulefn  (fast-zip/node node))]
       (let [new (fast-zip/replace node m)]
         (if-let [r (fast-zip/right new)]
           (recur r)
           (fast-zip/root new)))
       (recur (fast-zip/next node))))))


(defn sample-combinator
  [x]
  (if (seq? x)
    (let [l1 (first x)]
      (if (seq? l1)
        (if (identical? :e  (first l1))
          (let [l1r (second l1)] (list (list l1r (list :e (second x))) l1r)))))))


(defn ops-counting-combinator [x test-count-atom test-success-count-atom]
  (do
    (swap! test-count-atom + 1)
   (if (seq? x)
     (let [l1 (first x)]
       (if (seq? l1)
         (if (identical? :e  (first l1))
           (let [l1r (second l1)]
              (swap!  test-success-count-atom + 1)
              (list (list l1r (list :e (second x))) l1r))))))))



(comment


  (use 'criterium.core)


  ;; make sure all zipper implementations give the same result

  (=
   (loop [i 0 term (list (list (list :e (list (list :e :e) :e)) :e)  :e)]
     (if (> i 300)
       term
       (recur (+ 1 i) (replace-all-core-zip term sample-combinator))))
   (loop [i 0 term (list (list (list :e (list (list :e :e) :e)) :e)  :e)]
     (if (> i 300)
       term
       (recur (+ 1 i) (replace-all-fast-zip term sample-combinator))))
   (loop [i 0 term (list (list (list :e (list (list :e :e) :e)) :e)  :e)]
     (if (> i 300)
       term
       (recur (+ 1 i) (replace-all term sample-combinator)))))


  ;; determine number of operations performed

  (def counts-data
    (let [test-count (atom 0) test-success-count (atom 0)
         test-combinator (fn [x] (ops-counting-combinator x test-count test-success-count))]

     (loop [i 0 term (list (list (list :e (list (list :e :e) :e)) :e)  :e)]
       (if (> i 300)
         {:term term :iterations @test-count :replacements @test-success-count }
         (recur (+ 1 i) (replace-all-core-zip term test-combinator))))))

  (dissoc counts-data :term)

  ;; => {:iterations 1275981, :replacements 601}


  ;; e[x_][y_]->x[e[y]][x]
  ;; median mathematica timing w 20 trials = 452 msec


  ;; bench with clojure.zip

   (bench
   (loop [i 0 term (list (list (list :e (list (list :e :e) :e)) :e)  :e)]
     (when-not (> i 300)
       (recur (+ 1 i) (replace-all-core-zip term sample-combinator)))))
   ;;          Execution time mean : 2.001708 sec
   ;; Execution time std-deviation : 19.954785 ms
   ;;Execution time lower quantile : 1.974356 sec ( 2.5%)
   ;;Execution time upper quantile : 2.038739 sec (97.5%)
   ;;                Overhead used : 2.001213 ns



   ;; bench with fast-zip

   (bench
    (loop [i 0 term (list (list (list :e (list (list :e :e) :e)) :e)  :e)]
      (when-not (> i 300)
        (recur (+ 1 i) (replace-all-fast-zip term sample-combinator)))))
   ;;          Execution time mean : 272.941419 ms
   ;; Execution time std-deviation : 2.047528 ms
   ;;Execution time lower quantile : 268.750873 ms ( 2.5%)
   ;;Execution time upper quantile : 276.424804 ms (97.5%)
   ;;                Overhead used : 2.001213 ns



   ;; bench with combinator.zip

  (bench
   (loop [i 0 term (list (list (list :e (list (list :e :e) :e)) :e)  :e)]
                 (when-not (> i 300)
                   (recur (+ 1 i) (replace-all term sample-combinator)))))
  ;;           Execution time mean : 177.763342 ms
  ;;  Execution time std-deviation : 1.402199 ms
  ;; Execution time lower quantile : 174.929998 ms ( 2.5%)
  ;; Execution time upper quantile : 180.029744 ms (97.5%)
  ;;                 Overhead used : 2.001213 ns


)
