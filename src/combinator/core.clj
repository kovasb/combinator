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
     (match [1 2] [([1 _] :seq)] 1)))


  )

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

(comment
  (def result (let []
               (iterate  #(replace-all % the-rule)
                          (t/tuple (t/tuple (t/tuple 'e (t/tuple (t/tuple 'e 'e) 'e)) 'e)  'e) ; '[ [ [e [ [e e] e] ] e]   e]
                         )))

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
   (time
   (loop [stack bigresult]
     (if-let [top (first stack)]
       (if (vector? top)
         (recur (cons (top 1) (cons (top 0) (next stack))) )
         (recur (next stack)))
       true)))
   "Elapsed time: 5.573 msecs"

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
