(ns combinator.core
                                        ;(:require [clojure.zip :as z])
                                        ;(:require [fast-zip.core :as z])
  (:require [combinator.zip :as z])
  (:use [clojure.core.match :only (match matchv)])
  (:require [ clj-tuple :as t]))

;; -agentpath:/Applications/YourKit_Java_Profiler_2013_build_13046.app/bin/mac/libyjpagent.jnilib
;;


(defn evaluate [init rulefn]
  (loop [node (z/vector-zip init) pass :down]
    ;(println [(z/node node) pass])
    (cond

     (= pass :down)
     (if-let [d (z/down node)]
       ;; if we have children, evaluate them first
       (recur d :down )
       ;; if no children, evaluate self
       (recur node :self))

     (= pass :self)
     (let [newval (rulefn (z/node node))]
       ;; if rule fires
       (if newval
         ;; replace node with new value
         (let [newnode (z/replace node newval)]

           ;; following branch is an optimization
           (if (number? newval)
             ;; if new value is primitive, we can skip its evaluation cycle and move to the next node
             ;; if there is a right sibling, move to the right and begin evaluation
             (if-let [r (z/right newnode)]
               (recur r :down)
               ;; if no right sibling, this level is done and we move to evaluate parent
               (if-let [u (z/up newnode)]
                 (recur (z/up newnode) :self)
                 ;; if no parent, we are done with evaluation and return the toplevel value
                 (z/node newnode)))

             ;; if replacement is not a primitive, then evaluate it
             (recur newnode  :down)))
         ;; match not fired, move to next node
         (if-let [r (z/right node)]
           ;; if has a right sibling, move there
           (recur r :down)
           ;; if no right sibling, move to parent if it exists
           (if-let [u (z/up node)]
             (recur (z/up node) :self)
             ;; otherwise we are done with evaluation, return toplevel form
             (z/node node))))

       ))))

(comment
  (time
   (dotimes [x 10000]
     (match [1 2] [1 _] 1)))
  "Elapsed time: 2.776 msecs"

  (time
   (dotimes [x 10000]
     (match [1 2] [([1 _] :seq)] 1))))


(comment


  (let [f
        (fn [x] (match x [1 1] 1 [1] 2 :else 3))]
    (time (dotimes [_ 100000] (f [1]))))
  "Elapsed time: 40.281 msecs"

    (let [f
        (fn [x] (match x [1 1] 1 [2] 2 :else 3))]
    (time (dotimes [_ 100000] (f [1]))))
    "Elapsed time: 40.281 msecs"

  (let [f
        (fn [x] (match x [1 1] 1 [1 _] 2 :else 3))]
    (time (dotimes [_ 100000] (f [1]))))

 (let [f
        (fn [x] (match x [1 1] 1 [2 _] 2 :else 3))]
    (time (dotimes [_ 100000] (f [2]))))


  (time
   (dotimes [xx 100000]
     (match [:fib 0 0]
                [:plus (a :guard number?) (b :guard number?)] (+ a b)
                [:fib 0 _] 1
                [:fib 1 _] 1
                [:fib x _] [:plus [:fib (- x 1) 1] [:fib (- x 2) 1]]
                :else nil)))

  (def rule4
    (fn [data]
      (match data
            [:plus (a :guard number?) (b :guard number?)] (+ a b)
            [:fib 0 _] 1
            [:fib 1 _] 1
            [:fib x _] [:plus [:fib (- x 1) 1] [:fib (- x 2) 1]]
            :else nil)))


   (def rule3
    (fn [data] (match [data]
                     [([:plus (a :guard number?) (b :guard number?)] :seq)] (+ a b)
                     [([:fib 0 ] :seq)] 1
                     [([:fib 1 ] :seq)] 1
                     [([:fib x ] :seq)] [:plus [:fib (- x 1) ] [:fib (- x 2) ]]
                     :else nil)))

  (def rule2
    (fn [data] (match [data]
                     [[:plus (a :guard number?) (b :guard number?)]] (+ a b)
                     [[:fib 0 ]] 1
                     [[:fib 1 ]] 1
                     [[:fib x ]] [:plus [:fib (- x 1) ] [:fib (- x 2) ]]
                     :else nil)))

  (def rule
    (fn [data] (match data
                     [:plus (a :guard number?) (b :guard number?)] (+ a b)
                     [:fib 0 ] 1
                     [:fib 1 ] 1
                     [:fib x ] [:plus [:fib (- x 1) ] [:fib (- x 2) ]]
                     :else nil)))

  (def rule5
    (fn [x]
      (if (vector? x)
        (if (identical? (x 0) :fib)
          (let [x0 (x 1)]
            (if (== 0 x0)
              1
              (if (== 1 x0)
                1
                [:plus [:fib (- x0 1)] [:fib (- x0 2)]])))
          (if (identical? (x 0) :plus)
            (if (and (number? (x 1)) (number? (x 2)))
              (+ (x 1) (x 2))
              nil)
            nil))
        nil)))

(comment

   (time (dotimes [ x 100000]
           (rule5 [:fib 1])))
   "Elapsed time: 5.011 msecs"


  (time (dotimes [ x 100000]
          (rule [:fib 1])))
  "Elapsed time: 25.212 msecs"


  (time (dotimes [ x 100000]
          (rule2 [:fib 1])))
  "Elapsed time: 25.212 msecs"


  (time (dotimes [ x 100000]
          (rule3 [:fib 1])))
  "Elapsed time: 79.276 msecs"


  (time (dotimes [ x 100000]
          (rule4 [:fib 1])))
  "Elapsed time: 8.128 msecs"

  (comment

   (let [v (fv/vector 1 2)]
     (time (dotimes [_ 1000000]
             (conj v 1)))))
  "Elapsed time: 50.078 msecs"

   (let [v (vector 1 2)]
    (time (dotimes [_ 1000000]
            (conj v 1))))
   "Elapsed time: 33.156 msecs"

 (let [v (transient (vector 1 2))]
    (time (dotimes [_ 1000000]
            (conj! v 1))))
 "Elapsed time: 13.674 msecs"

 (let [v  (vector 1 2)]
    (time (dotimes [_ 1000000]
            (persistent! (conj! (transient v) 1)))))
 "Elapsed time: 113.094 msecs"



   (let [v (into  (clojure.lang.PersistentQueue/EMPTY) [1 2]) ]
     (time (dotimes [_ 1000000]
             (conj v 1))))
   "Elapsed time: 44.629 msecs"

   (let [v (into  (clojure.lang.PersistentQueue/EMPTY)  (double-list 1 2) ) ]
     (time (dotimes [_ 1000000]
             (conj v 1))))
   "Elapsed time: 41.95 msecs"


   (let []
     (time (dotimes [x 1000000]
             (.add (java.util.ArrayList.) 1))))
   "Elapsed time: 12.27 msecs"

    (let []
     (time (dotimes [x 1000000]
             (let [e  (java.util.ArrayList.)]
               (.add e 1)
               (dorun (seq e))))))
    "Elapsed time: 117.079 msecs"




     (let [v (into  (clojure.lang.PersistentQueue/EMPTY) [1 2]) ]
       (time (dotimes [_ 1000000]
               (pop v)))))
    "Elapsed time: 19.056 msecs"

     (let [v (into  (clojure.lang.PersistentQueue/EMPTY) [1 2]) ]
      (time (dotimes [_ 1000000]
              (next v))))
     "Elapsed time: 64.453 msecs"




    (let [v (fv/vector 1 2)]
    (time (dotimes [_ 1000000]
            (dorun (conj v 1)))))
    "Elapsed time: 437.703 msecs"

    (let [v (vector 1 2)]
    (time (dotimes [_ 1000000]
            (dorun (conj v 1)))))
    "Elapsed time: 197.462 msecs"

     (let [v (into  (clojure.lang.PersistentQueue/EMPTY)  (double-list 1 2) ) ]
     (time (dotimes [_ 1000000]
             (dorun (conj v 1)))))
     "Elapsed time: 235.034 msecs"

      (let [v (into  (clojure.lang.PersistentQueue/EMPTY) [1 2]) ]
    (time (dotimes [_ 1000000]
            (dorun (conj v 1)))))
      "Elapsed time: 231.322 msecs"
      ;; can prob be sped up past vector speed


      (let [v (vec (range 100))]
        (time (dotimes [_ 1000000]
                (peek v))))
      "Elapsed time: 8.652 msecs"


       (let [v (into  (clojure.lang.PersistentQueue/EMPTY) [1 2]) ]
    (time (dotimes [_ 1000000]
            (peek v))))

       "Elapsed time: 12.79 msecs"

       (let [v (list 1 2 3)]
         (time (dotimes [_ 1000000]
                 (peek v))))
       "Elapsed time: 6.811 msecs"

        (let [v (list 1 2 3)]
         (time (dotimes [_ 1000000]
                 (first v))))
        "Elapsed time: 7.636 msecs"


    (let (clojure.lang.PersistentQueue/EMPTY))




  )

(comment
  (let [rule rule5]
   (time
    (evaluate
     [:fib 27]
     rule)))

  )


"Elapsed time: 1338.264 msecs"
;; don't eval primitive numbers when detected as return val
"Elapsed time: 1028.735 msecs"
;; with hand-rolled matchfn
"Elapsed time: 763.975 msecs"
;;; 34% slower than mathematica





(comment
  (let [rule
       (fn [m]
         (match m
                [:plus (a :guard number?) (b :guard number?)] (+ a b)
                [:fib 0] 1
                [:fib 1] 1
                [:fib x] (t/tuple :plus (t/tuple :fib (- x 1)) (t/tuple :fib (- x 2)))
                :else nil))]
   (time
    (evaluate
     [:fib 27]
     rule))))
"Elapsed time: 1407.591 msecs"



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

(defn rule-fn [lhs rhs]
  (eval `#(match % ~lhs ~rhs :else nil)))

(defn tuple-zip
  [root]
  (z/zipper
    vector?
    seq
    (fn [node children] (with-meta (apply t/tuple children) (meta node)))
    root))

(def the-rule
  (rule-fn
   '[['e x] y]
   '(t/tuple (t/tuple x (t/tuple 'e y)) x) ; '[[x ['e y]] x] ;
                       ))

(defn fast-rule
  [x]
  (if (vector? x)
    (let [l1 (x 0)]
      (if (vector? l1)
        (if (= 'e  (l1 0))
          (let [l1r (l1 1)] (t/tuple (t/tuple l1r (t/tuple 'e (x 1))) l1r))
          )))))

(defprotocol expr
  (head [x])
  (body [x]))


(defn fast-rule2
  [x]
  (if (identical? :vector (head x))
    (let [xb (body x)
          l1  (xb 0)]
      (if (identical? :vector (head l1))
        (let [l1b (body l1)] (if (= 'e (l1b 0))
           (let [l1r (l1b 1)] (t/tuple (t/tuple l1r (t/tuple 'e (xb 1))) l1r))
           ))))))


(extend-type
    clojure.lang.PersistentVector
  expr
  (head [x] :vector)
  (body [x] x))


(extend-type
    java.lang.Long
  expr
  (head [x] :long)
  (body [x] x))

(extend-type
    clj_tuple.Tuple2
  expr
  (head [x] :vector)
  (body [x] x))

(extend-type
    clojure.lang.Symbol
  expr
  (head [x] :symbol)
  (body [x] x))


(def the-count (atom 0))

(defn the-rule-counting [x]
  (swap! the-count + 1)
  (the-rule x))

(comment
  (def result (let []
               (iterate  #(replace-all % the-rule)
                          (t/tuple (t/tuple (t/tuple 'e (t/tuple (t/tuple 'e 'e) 'e)) 'e)  'e) ; '[ [ [e [ [e e] e] ] e]   e]
                          )))



  (time (do (nth result 300) 1))

  (def result2 (let []
               (iterate  #(replace-all % fast-rule)
                          (t/tuple (t/tuple (t/tuple 'e (t/tuple (t/tuple 'e 'e) 'e)) 'e)  'e) ; '[ [ [e [ [e e] e] ] e]   e]
                          )))

   (time (do (nth result2 300) 1))

     (def result3 (let []
               (iterate  #(replace-all % fast-rule2)
                          (t/tuple (t/tuple (t/tuple 'e (t/tuple (t/tuple 'e 'e) 'e)) 'e)  'e) ; '[ [ [e [ [e e] e] ] e]   e]
                          )))

   (time (do (nth result3 300) 1))


   (time (dotimes [x 1000000] (the-rule [1 2])))
   "Elapsed time: 164.016 msecs"

   ;;at least half time taken up in rule check


   (time (dotimes [_ 1000000]
           (vector? [1])))
   "Elapsed time: 4.17 msecs"

   (time (dotimes [x 1000000]
           (identical? x :1)))
   "Elapsed time: 3.687 msecs"


   (time (dotimes [_ 1000000]
           (vector? (first [[1]]))))
   "Elapsed time: 60.162 msecs"

   (time (dotimes [_ 1000000]
           (vector?  [[1]])))
   "Elapsed time: 4.182 msecs"

   (defn myvector? [x] (vector? x))
    (time (dotimes [_ 1000000]
           (myvector?  [[1]])))

     (time (dotimes [_ 1000000]
             (first [[1]])))
     "Elapsed time: 58.66 msecs"

     (time (dotimes [_ 1000000]
             (peek [[1]])))
     "Elapsed time: 8.245 msecs"


       (time (dotimes [_ 1000000]
             ([[1]] 0)))
    "Elapsed time: 7.269 msecs"


    (time (dotimes [x 1000000] (the-rule 1)))

   (time (dotimes [x 1000000] (the-rule [1 2])))
   "Elapsed time: 163.692 msecs"
   (time (dotimes [x 1000000] (the-rule [['e 1] 2])))
   "Elapsed time: 346.717 msecs"


   (time (dotimes [x 1000000] (fast-rule 1)))
   "Elapsed time: 27.386 msecs"
   ;; not inlining. should just be a vector? check

   (time (dotimes [x 1000000] (fast-rule2 1)))
   "Elapsed time: 8.815 msecs"



   (time (dotimes [x 1000000] (fast-rule [1 2])))
   (time (dotimes [x 1000000] (fast-rule [1 2])))
   "Elapsed time: 146.882 msecs"
   "Elapsed time: 55.964 msecs"
   "Elapsed time: 31.188 msecs"

   (time (dotimes [x 1000000] (fast-rule2 [1 2])))
   "Elapsed time: 35.061 msecs"

   (time (dotimes [x 1000000] (fast-rule [1 2])))



   (time (dotimes [x 1000000] (fast-rule [['e 1] 2])))
   "Elapsed time: 333.169 msecs"
   ;; with vector as fn
   "Elapsed time: 151.368 msecs"
   "Elapsed time: 75.217 msecs"


   (time (dotimes [x 1000000] (fast-rule2 [['e 1] 2])))
   "Elapsed time: 104.589 msecs"

(def bigresult (nth result 300))

(time
 (dotimes [x 1000] (match [[x 2] 3] [[a b] c] [a b c])))

(comment
  (defn balance-array [node]
   (matchv :m/objects [node]
           [(:or [:black [:red [:red a x b] y c] z d]
                 [:black [:red a x [:red b y c]] z d]
                 [:black a x [:red [:red b y c] z d]]
                 [:black a x [:red b y [:red c z d]]])] :balance
           :else :balanced)))

)




(comment
  ;; with core zip
 (time (do (nth result 300) 1))
 "Elapsed time: 4822.223 msecs"
 ;; core zip with hotspot
 "Elapsed time: 2405.303 msecs"


 ;; my zip with proper hotspot
 "Elapsed time: 521.092 msecs"
 ;; with tuple
 "Elapsed time: 374.325 msecs"
 ;; fastest time with fixed up
 "Elapsed time: 344.235 msecs"
 ;; fasted time with fast-rule2
 "Elapsed time: 319.347 msecs"

 ;; time in mathematica?



 ;; with fast-zip
 "Elapsed time: 1799.546 msecs"
 ;; eliminating unneccessary calls
 "Elapsed time: 1734.006 msecs"
 ;; create new records instead of assoc
 "Elapsed time: 1021.314 msecs"
 ;; with tuple
 "Elapsed time: 940.687 msecs"




  (time (dotimes [x 1000000] (first [1 2 3])))
"Elapsed time: 132.266 msecs"
  (time (dotimes [x 1000000] (next [1 2 3])))
  "Elapsed time: 145.485 msecs"

  (time (dotimes [x 1000000] ([1 2 3] 0)))
  "Elapsed time: 57.271 msecs"
  "Elapsed time: 7.654 msecs"


  (time (dotimes [x 1000000] (get [1 2 3] 0)))
 "Elapsed time: 66.911 msecs"




(time (dotimes [x 1000000] (first '(1 2 3) )))
"Elapsed time: 74.29 msecs"
(time (dotimes [x 1000000] (next '(1 2 3) )))
"Elapsed time: 74.71 msecs"
(time (dotimes [x 1000000] (rest '(1 2 3) )))
"Elapsed time: 76.898 msecs"




 (time (dotimes [x 1000000] (vec '(1 2 3))))

 (time (dotimes [x 100000] (concat [1] [2] [3])))
 "Elapsed time: 20.083 msecs"
  (time (dotimes [x 100000] (concat  '(1) '(2) '(3) )))
 "Elapsed time: 20.289 msecs"

  (time (dotimes [x 100000] (concat [1 1 1] [2 2 2] [3 3 3])))
 "Elapsed time: 20.083 msecs"
  (time (dotimes [x 100000] (concat  '(1 1 1) '(2 2 2) '(3 3 3) )))
 "Elapsed time: 20.289 msecs"

 (time (dotimes [x 1000000] (conj [] 1)))
 "Elapsed time: 99.248 msecs"

 (time (dotimes [x 1000000] (conj '() 1)))
 "Elapsed time: 80.166 msecs"

  (time (dotimes [x 1000000] (cons 1 '())))
 "Elapsed time: 78.224 msecs"


 (let [v '(1 2 3 4)] (time (dotimes [x 1000000] (apply list v))))
"Elapsed time: 514.018 msecs"


 (let [v '(1 2 3 4)] (time (dotimes [x 1000000] (apply vector v))))
 "Elapsed time: 281.087 msecs"

  (let [v '(1 2 3 4)] (time (dotimes [x 1000000] (into-array v))))
  "Elapsed time: 514.813 msecs"



1
;; match itself is about 3x faster than mma

;; speed of traversing tree
;; 1.4msec in mma with /. , 0.365msec with Cases
;; 35msec in clj-zip
;; 8msec with fast-zip
;; 4.175 msecs in improved-zip
;; 2.7msec in stack push
(time (dotimes [m 100]
   (let [init (z/vector-zip bigresult)]
     (loop [loc init]
       (if (z/end? loc)
         true
         (recur (z/next loc)))))))



(let [init (z/vector-zip bigresult)]
  (time
   (loop [loc init]
     (if (z/end? loc)
       true
       (recur (z/next loc))))))

;; using method lookups in records
"Elapsed time: 967.383 msecs"
;; assuming everything is a vec
"Elapsed time: 792.175 msecs"
;; current
"Elapsed time: 3.808 msecs"


(let [init (z/vector-zip bigresult)]
  (time
   (loop [loc init]
     (the-rule (.node loc))
     (if (z/end? loc)
       true
       (recur (z/next loc))))))
"Elapsed time: 34.092 msecs"


(let [init (z/vector-zip bigresult)]
  (time
   (loop [loc init]
     (fast-rule (.node loc))
     (if (z/end? loc)
       true
       (recur (z/next loc))))))
"Elapsed time: 30.996 msecs"


(let [init (z/vector-zip bigresult)]
  (time
   (loop [loc init]
     (fast-rule2 (.node loc))
     (if (z/end? loc)
       true
       (recur (z/next loc))))))


(defrecord Test [a])

(def t (Test. 1))
(let [t (Test. 1)] (time (dotimes [x 1000000] (assoc t :a 1 ))))
"Elapsed time: 155.004 msecs"


(let [t (Test. 1)] (time (dotimes [x 1000000] (Test. x))))
"Elapsed time: 69.557 msecs"
"Elapsed time: 4.54 msecs"


(time (dotimes [x 1000000]  (:a t)))
"Elapsed time: 79.846 msecs"
"Elapsed time: 6.808 msecs"


(time (dotimes [x 1000000]  (.a ^Test t)))
"Elapsed time: 42.379 msecs"
"Elapsed time: 3.604 msecs"

(let [v [1 2 3]] (time (dotimes [x 1000000]  (v 0))))
"Elapsed time: 86.333 msecs"
"Elapsed time: 56.494 msecs"
"Elapsed time: 9.256 msecs"


(let [v (double-array [1 2 3])] (time (dotimes [x 1000000 ]  (aget ^doubles v (int 0)))))
"Elapsed time: 60.915 msecs"





(let [v (into-array [1 2 3])] (time (dotimes [x 10000] (aget v 0))))

(defrecord Test2 [a b c d])
(let [t (Test2. 1 2 3 4)] (time (dotimes [x 1000000] (assoc t :a 1 ))))
"Elapsed time: 157.077 msecs"
(let [t (Test. 1)] (time (dotimes [x 1000000] (Test2. x 2 3 4))))
"Elapsed time: 93.308 msecs"

(let [t (Test2. 1 2 3 4)] (time (dotimes [x 1000000] (Test2. x (.b t) (.c t) (.d t)))))
"Elapsed time: 104.194 msecs"

(let [t {:a 1 :b 2 :c 3 :d 4}] (time (dotimes [x 1000000] (assoc t :a 1 ))))
"Elapsed time: 94.307 msecs"
(let [t {:a 1 :b 2 :c 3 :d 4}] (time (dotimes [x 1000000] (t :a))))
"Elapsed time: 53.56 msecs"

(defrecord Test3 [a b c d e])
(let [t (Test3. 1 2 3 4 5)] (time (dotimes [x 1000000] (Test3. x (.b t) (.c t) (.d t) (.e t)))))
"Elapsed time: 117.004 msecs"
(let [t (Test3. 1 2 3 4 5)] (time (dotimes [x 1000000] (assoc t :a 1 ))))
"Elapsed time: 157.014 msecs"







;; twice as fast

(time (dotimes [x 1000000]
   (conj (next [1 2] ) 1)))

(time (dotimes [x 1000000]
   (assoc {} :a 1)))

   ;; speed of clojure.walk?
   (time (clojure.walk/prewalk identity bigresult))
   ;; stack overflow

   (time (dorun (tree-seq vector? identity bigresult)))
   ;; 300 msec

   (time (count (flatten bigresult))))
   ;; 300 msec


(comment
  (time
   (loop [stack bigresult]
     (if-let [top (peek stack)]
       (if (vector? top)
         (recur (into (pop stack) top) )
         (recur (pop stack)))
       true)))
  "Elapsed time: 10.414 msecs"
  "Elapsed time: 3.924 msecs"

   (time
   (loop [stack bigresult]
     (if-let [top (first stack)]
       (if (vector? top)
         (recur (cons (top 1) (cons (top 0) (next stack))) )
         (recur (next stack)))
       true)))
   "Elapsed time: 5.573 msecs"
   "Elapsed time: 5.077 msecs"


   (let [s (java.util.Stack.)]
     (.push s bigresult)
     (time
      (loop [c 0]
        (if (.empty ^Stack s)
          c
          (let [top (.pop ^Stack s)]
            (if (vector? top)
              (dotimes [i (count top)]
                (.push ^Stack s (top i) )))
            (recur (+ c 1)))))))

   (import 'java.util.ArrayDeque)

   (defn push-vec [x ^ArrayDeque s]
     (loop [c (int (-  (count x) 1))]
       (if (== c  -1) nil (do (.push s (x c)) (recur (- c 1))))))

    (let [s (java.util.ArrayDeque. 100)]
     (.push s bigresult)
     (time
      (loop [c (int 0)]
        (if (.isEmpty  s)
          c
          (let [top (.pop  s)]
            (if (instance? clojure.lang.IPersistentVector top)
              (push-vec top s))
            (recur (+ c 1)))))))
    "Elapsed time: 2.725 msecs"


    (time (dotimes [x 10000] (instance? clojure.lang.IPersistentVector [1 2 3])))
    (time (dotimes [x 10000] (.isInstance clojure.lang.IPersistentVector  [1 2 3])))
        (time (dotimes [x 10000] (. clojure.lang.IPersistentVector (isInstance [1 2 3]))))


    "Elapsed time: 0.92 msecs"
    (time (dotimes [x 10000] ([1 2 3] 1)))
    "Elapsed time: 0.806 msecs"

    (let [s (java.util.ArrayDeque.)]
      (time (dotimes [x 10000] (.push s x))))
    "Elapsed time: 0.845 msecs"

    (time (dotimes [x 10000] x))
    "Elapsed time: 0.251 msecs"



    (time (dotimes [x  100000] 1))


     (time (for [x 10000] x))


     (loop [c 0 current-expr [1]  expr-stack '([ [1] [2 [3]]]) next-stack '(1) count-stack (2)]
       (if (instance? clojure.lang.IPersistentVector current-expr)
         (recur (+ c 1) (current-expr 0) )

         )

       )

     )

;; need println based on zippers?
;; combinator.core=> (nth (iterate vector 1) 5000)
;; StackOverflowError   java.nio.CharBuffer.<init> (CharBuffer.java:276)

(defn fib [n]
  (cond
   (= n 0) 1
   (= n 1) 1
   true (+ (fib (- n 1)) (fib (- n 2)))))
