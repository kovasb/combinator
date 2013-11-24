;   Copyright (c) Rich Hickey. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

;functional hierarchical zipper, with navigation, editing and enumeration
;see Huet

(ns
  combinator.zip
  (:refer-clojure :exclude (replace remove next)))

(declare Tuple1 Tuple2)

(deftype Tuple0 []
  ICollection
  (-conj [coll o]
    (Tuple1. o))
  ISeqable
  (-seq [_]
    ()))

(deftype Tuple1 [x]
  ICollection
  (-conj [coll o]
    (Tuple2. x o))
  ISeqable
  (-seq [_]
    (prim-seq (array x))))

(deftype Tuple2 [x y]
  ICollection
  (-conj [coll o]
    [x y o])
  ISeqable
  (-seq [_]
    (prim-seq (array x y))))

(deftype ZipperPath [l r ppath pnodes changed?])

(deftype ZipperLocation [node path])

(defn ^:export zipper
  "Creates a new zipper structure.

  branch? is a fn that, given a node, returns true if can have
  children, even if it currently doesn't.

  children is a fn that, given a branch node, returns a seq of its
  children.

  make-node is a fn that, given an existing node and a seq of
  children, returns a new branch node with the supplied children.
  root is the root node."
  {:added "1.0"}
  [root]
  (ZipperLocation. root nil))



(defn vector-zip
  "Returns a zipper for nested vectors, given a root vector"
  {:added "1.0"}
  [root]
  (zipper
    root))



(defn node
  "Returns the node at loc"
  [^ZipperLocation loc]
  (.-node loc))

(defn ^boolean branch?
  "Returns true if the node at loc is a branch"
  [^ZipperLocation loc]
  (seq? (.-node loc)))

(defn children
  "Returns a seq of the children of node at loc, which must be a branch"
  [^ZipperLocation loc]
  (.-node loc))

(defn make-node
  "Returns a new branch node, given an existing node and new children.
  The loc is only used to supply the constructor."
  [^ZipperLocation loc node children]
  children)

(defn path
  "Returns a seq of nodes leading to this loc"
  [^ZipperLocation loc]
  (.-pnodes ^ZipperPath (.-path loc)))

(defn lefts
  "Returns a seq of the left siblings of this loc"
  [^ZipperLocation loc]
  (seq (.-l ^ZipperPath (.-path loc))))

(defn rights
  "Returns a seq of the right siblings of this loc"
  [^ZipperLocation loc]
  (.-r ^ZipperPath (.-path loc)))

(defn down
  "Returns the loc of the leftmost child of the node at this loc,
  or nil if no children"
  [^ZipperLocation loc]
  (when (branch? loc)
    (when-let [cs (children loc)]
      (let [node (.-node loc), path ^ZipperPath (.-path loc)]
        (ZipperLocation.
          (first cs)
          (ZipperPath.
            (Tuple0.)
            (clojure.core/-next ^not-native cs)
            path
            (if path (-conj ^not-native (.-pnodes path) node) (list node)) nil))))))

(defn up
  "Returns the loc of the parent of the node at this loc, or nil if at the top"
  [^ZipperLocation loc]
  (let [node (.-node loc), path ^ZipperPath (.-path loc)]
    (when-let [pnodes (and path (.-pnodes path))]
      (let [pnode (peek pnodes)]
        (if (.-changed? path)
          (ZipperLocation.
            (make-node loc pnode (seq (into (.-l path) (cons node (.-r path)))))
            (if-let [ppath (.-ppath path)]
              (ZipperPath. (.-l ppath) (.-r ppath) (.-ppath ppath) (.-pnodes ppath) true)))
          (ZipperLocation.

            pnode
            (.-ppath path)))))))


; (deftype ZipperPath [l r ppath pnodes changed?])



(defn root
  "zips all the way up and returns the root node, reflecting any changes."
  [^ZipperLocation loc]
  (if (keyword-identical? :end (.-path loc))
    (.-node loc)
    (let [p (up loc)]
      (if p
        (recur p)
        (.-node loc)))))

(defn right
  "Returns the loc of the right sibling of the node at this loc, or nil"
  [^ZipperLocation loc]
  (let [path ^ZipperPath (.-path loc)]
    (when-let [r (and (not (nil? path)) (.-r path))]
      (let [^not-native r r]
        (ZipperLocation.
          (-first r)
          (ZipperPath. (-conj ^not-native (.-l path) (.-node loc))
            (clojure.core/-next r)
            (.-ppath path)
            (.-pnodes path)
            (.-changed? path)))))))

(defn rightmost
  "Returns the loc of the rightmost sibling of the node at this loc, or self"
  [^ZipperLocation loc]
  (let [path ^ZipperPath (.-path loc)]
    (if-let [r (and path (.-r path))]
      (ZipperLocation.

        (last r)
        (assoc path :l (apply conj (.-l path) (.-node loc) (butlast r)) :r nil))
      loc)))

(defn left
  "Returns the loc of the left sibling of the node at this loc, or nil"
  [^ZipperLocation loc]
  (let [path ^ZipperPath (.-path loc)]
    (when (and path (seq (.-l path)))
      (ZipperLocation.

        (peek (.-l path))
        (assoc path :l (pop (.-l path)) :r (cons (.-node loc) (.-r path)))))))

(defn leftmost
  "Returns the loc of the leftmost sibling of the node at this loc, or self"
  [^ZipperLocation loc]
  (let [path ^ZipperPath (.-path loc)]
    (if (and path (seq (.-l path)))
      (ZipperLocation.

        (first (.-l path))
        (assoc path :l [] :r (concat (rest (.-l path)) [(.-node loc)] (.-r path))))
      loc)))

(defn insert-left
  "Inserts the item as the left sibling of the node at this loc, without moving"
  [^ZipperLocation loc item]
  (if-let [path ^ZipperPath (.-path loc)]
    (ZipperLocation.

      (.-node loc)
      (assoc path :l (conj (.-l path) item) :changed? true))
   ))

(defn insert-right
  "Inserts the item as the right sibling of the node at this loc, without moving"
  [^ZipperLocation loc item]
  (if-let [path ^ZipperPath (.-path loc)]
    (ZipperLocation.

      (.-node loc)
      (assoc path :r (cons item (.-r path)) :changed? true))
    ))

(defn replace
  "Replaces the node at this loc, without moving"
  [^ZipperLocation loc node]
  (ZipperLocation.
    node
    (if-let [path (.-path loc)]
      (ZipperPath. (.-l path) (.-r path) (.-ppath path) (.-pnodes path) true))))

(defn insert-child
  "Inserts the item as the leftmost child of the node at this loc, without moving"
  [^ZipperLocation loc item]
  (replace loc (make-node loc (.-node loc) (cons item (children loc)))))

(defn append-child
  "Inserts the item as the rightmost child of the node at this loc, without moving"
  [^ZipperLocation loc item]
  (replace loc (make-node loc (.-node loc) (concat (children loc) [item]))))

(defn next
  "Moves to the next loc in the hierarchy, depth-first. When reaching
  the end, returns a distinguished loc detectable via end?. If already
  at the end, stays there."
  [^ZipperLocation loc]
  (let [path (.-path loc)]
    (if (keyword-identical? :end path)
      loc
      (if (branch? loc)
        (down loc)
        (let [right (combinator.zip/right loc)]
          (if-not (nil? right)
            right
            (loop [p loc]
              (let [u (up p)]
                (if-not (nil? u)
                  (let [right (combinator.zip/right u)]
                    (if-not (nil? right)
                      right
                      (recur u)))
                  (ZipperLocation. (.-node p) :end))))))))))

(defn prev
  "Moves to the previous loc in the hierarchy, depth-first. If already at the root, returns nil."
  [loc]
  (if-let [lloc (left loc)]
    (loop [loc lloc]
      (if-let [child (and (branch? loc) (down loc))]
        (recur (rightmost child))
        loc))
    (up loc)))

(defn end?
  "Returns true if loc represents the end of a depth-first walk"
  [^ZipperLocation loc]
  (keyword-identical? :end (.-path loc)))

(defn remove
  "Removes the node at loc, returning the loc that would have preceded it in a depth-first walk."
  [^ZipperLocation loc]
  (if-let [path ^ZipperPath (.-path loc)]
    (if (pos? (count (.-l path)))
      (loop [loc (ZipperLocation.
                   (peek (.-l path))
                   (assoc path :l (pop (.-l path)) :changed? true))]
        (if-let [child (and (branch? loc) (down loc))]
          (recur (rightmost child))
          loc))
      (ZipperLocation.

        (make-node loc (peek (.-pnodes path)) (.-r path))
        (if-let [ppath (.-ppath path)] (and ppath (assoc ppath :changed? true)))))
    ))

(defn edit
  "Replaces the node at this loc with the value of (f node args)"
  [^ZipperLocation loc f & args]
  (replace loc (apply f (.-node loc) args)))
