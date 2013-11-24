# combinator

This project is an experiment in fast term-rewriting in clojure. It aims to evolve a simple combinator-like system as quickly as possible, in clojure and clojurescript.

# the combinator system

We are using the combinator-like systems defined at https://www.wolframscience.com/nksonline/section-3.10

Currently only a single combinator is defined, pending an automated compiler. 

In mathematica notation, it is expressed as
 
 e[x_][y_]->x[e[y]][x]
 
where e is a symbol constant, and x and y are wildcards. 

The initial condition is

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
