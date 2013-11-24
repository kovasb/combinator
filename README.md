# combinator

This project is an experiment in fast term-rewriting in clojure. Its goal is to establish the upper bound on term-rewriting performance in clojure and clojurescript.


It aims to evolve a simple combinator-like system as quickly as possible, in clojure and clojurescript.

# the combinator system

For our benchmarks, we are using the simple combinator-like systems defined at https://www.wolframscience.com/nksonline/section-3.10 .

These combinators iteratively rewrite a binary tree, via a pre-order traversal that performs all non-overlapping rewrites.

The nodes in the binary tree consist of either the symbol constant e, or a node with two children that again satisfy the criteria.

In Mathematica notation, these trees are expressed as

```
;; node consistent of single atomic element
e
;; node consisting of two atomic elements
e[e]
;; two-level tree, with e[e] as the left branch, and e as the right branch
;; e[e][e] 
```

The combinators themselves are expressed as rules like

```
e[x_][y_]->x[e[y]][x]
```

Where x_ and y_ are wildcards that will match any subtree in those positions. 

Different right-hand-sides of the rules will generate different behavior, in terms of the final result, the pattern of recursion, and ultimately patterns of memory access and method invocation.

For the moment we are using only the sample combinator shown above, but a more rigorous test would enumerate all possible combinators up to a certain rhs tree size.

Starting from the initial condition 

```
e[e[e][e]][e][e]
```

the first tree of evolution are

```
e[e][e][e[e]][e[e][e]][e]
e[e[e]][e][e[e]][e[e[e]][e]][e]
e[e][e[e]][e[e]][e[e]][e[e][e[e]][e[e]]][e]
```

In general, and for this rule in particular, the evolution will not reach a fixed-point. Therefore we cannot implement the combinator as a naive recursive function because it will never return.   


This rule matches e[e][e], but does not match e[e][e][e]
 
 
where e is a symbol constant, and x and y are wildcards. 

The initial condition is

e[e[e][e]][e][e]

e[e[e][e]][e][e]


A single iteration consists of a depth-first search of the tree, attempting to match the LHS of the pattern. If a match is found, we substitute the RHS of the rule into the tree, and continue the traversal to perform all non-overlapping replacements. 

Note that this is a binary tree, where each node is either the symbol constant e, or a node with two children that again may be either e or another such node.

In clojure, we chose to represent these trees as lists and keywords:

e[e[e][e]][e][e]
->
(((:e ((:e :e) :e)) :e) :e)



# performances

# lessons learned





A Clojure library designed to ... well, that part is up to you.

## Usage

FIXME

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
