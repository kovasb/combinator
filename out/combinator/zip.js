// Compiled by ClojureScript 0.0-2030
goog.provide('combinator.zip');
goog.require('cljs.core');

/**
* @constructor
*/
combinator.zip.Tuple0 = (function (){
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 8388616;
})
combinator.zip.Tuple0.cljs$lang$type = true;
combinator.zip.Tuple0.cljs$lang$ctorStr = "combinator.zip/Tuple0";
combinator.zip.Tuple0.cljs$lang$ctorPrWriter = (function (this__3685__auto__,writer__3686__auto__,opt__3687__auto__){return cljs.core._write(writer__3686__auto__,"combinator.zip/Tuple0");
});
combinator.zip.Tuple0.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (_){var self__ = this;
var ___$1 = this;return cljs.core.List.EMPTY;
});
combinator.zip.Tuple0.prototype.cljs$core$ICollection$_conj$arity$2 = (function (coll,o){var self__ = this;
var coll__$1 = this;return (new combinator.zip.Tuple1(o));
});
combinator.zip.__GT_Tuple0 = (function __GT_Tuple0(){return (new combinator.zip.Tuple0());
});

/**
* @constructor
*/
combinator.zip.Tuple1 = (function (x){
this.x = x;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 8388616;
})
combinator.zip.Tuple1.cljs$lang$type = true;
combinator.zip.Tuple1.cljs$lang$ctorStr = "combinator.zip/Tuple1";
combinator.zip.Tuple1.cljs$lang$ctorPrWriter = (function (this__3685__auto__,writer__3686__auto__,opt__3687__auto__){return cljs.core._write(writer__3686__auto__,"combinator.zip/Tuple1");
});
combinator.zip.Tuple1.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (_){var self__ = this;
var ___$1 = this;return cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$1([self__.x]);
});
combinator.zip.Tuple1.prototype.cljs$core$ICollection$_conj$arity$2 = (function (coll,o){var self__ = this;
var coll__$1 = this;return (new combinator.zip.Tuple2(self__.x,o));
});
combinator.zip.__GT_Tuple1 = (function __GT_Tuple1(x){return (new combinator.zip.Tuple1(x));
});

/**
* @constructor
*/
combinator.zip.Tuple2 = (function (x,y){
this.x = x;
this.y = y;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 8388616;
})
combinator.zip.Tuple2.cljs$lang$type = true;
combinator.zip.Tuple2.cljs$lang$ctorStr = "combinator.zip/Tuple2";
combinator.zip.Tuple2.cljs$lang$ctorPrWriter = (function (this__3685__auto__,writer__3686__auto__,opt__3687__auto__){return cljs.core._write(writer__3686__auto__,"combinator.zip/Tuple2");
});
combinator.zip.Tuple2.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (_){var self__ = this;
var ___$1 = this;return cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$1([self__.x,self__.y]);
});
combinator.zip.Tuple2.prototype.cljs$core$ICollection$_conj$arity$2 = (function (coll,o){var self__ = this;
var coll__$1 = this;return cljs.core.PersistentVector.fromArray([self__.x,self__.y,o], true);
});
combinator.zip.__GT_Tuple2 = (function __GT_Tuple2(x,y){return (new combinator.zip.Tuple2(x,y));
});

/**
* @constructor
*/
combinator.zip.ZipperPath = (function (l,r,ppath,pnodes,changed_QMARK_){
this.l = l;
this.r = r;
this.ppath = ppath;
this.pnodes = pnodes;
this.changed_QMARK_ = changed_QMARK_;
})
combinator.zip.ZipperPath.cljs$lang$type = true;
combinator.zip.ZipperPath.cljs$lang$ctorStr = "combinator.zip/ZipperPath";
combinator.zip.ZipperPath.cljs$lang$ctorPrWriter = (function (this__3688__auto__,writer__3689__auto__,opts__3690__auto__){return cljs.core._write(writer__3689__auto__,"combinator.zip/ZipperPath");
});
combinator.zip.__GT_ZipperPath = (function __GT_ZipperPath(l,r,ppath,pnodes,changed_QMARK_){return (new combinator.zip.ZipperPath(l,r,ppath,pnodes,changed_QMARK_));
});

/**
* @constructor
*/
combinator.zip.ZipperLocation = (function (node,path){
this.node = node;
this.path = path;
})
combinator.zip.ZipperLocation.cljs$lang$type = true;
combinator.zip.ZipperLocation.cljs$lang$ctorStr = "combinator.zip/ZipperLocation";
combinator.zip.ZipperLocation.cljs$lang$ctorPrWriter = (function (this__3688__auto__,writer__3689__auto__,opts__3690__auto__){return cljs.core._write(writer__3689__auto__,"combinator.zip/ZipperLocation");
});
combinator.zip.__GT_ZipperLocation = (function __GT_ZipperLocation(node,path){return (new combinator.zip.ZipperLocation(node,path));
});
/**
* Creates a new zipper structure.
* 
* branch? is a fn that, given a node, returns true if can have
* children, even if it currently doesn't.
* 
* children is a fn that, given a branch node, returns a seq of its
* children.
* 
* make-node is a fn that, given an existing node and a seq of
* children, returns a new branch node with the supplied children.
* root is the root node.
*/
combinator.zip.zipper = (function zipper(root){return (new combinator.zip.ZipperLocation(root,null));
});
goog.exportSymbol('combinator.zip.zipper', combinator.zip.zipper);
/**
* Returns a zipper for nested vectors, given a root vector
*/
combinator.zip.vector_zip = (function vector_zip(root){return combinator.zip.zipper(root);
});
/**
* Returns the node at loc
*/
combinator.zip.node = (function node(loc){return loc.node;
});
/**
* Returns true if the node at loc is a branch
*/
combinator.zip.branch_QMARK_ = (function branch_QMARK_(loc){return cljs.core.seq_QMARK_(loc.node);
});
/**
* Returns a seq of the children of node at loc, which must be a branch
*/
combinator.zip.children = (function children(loc){return loc.node;
});
/**
* Returns a new branch node, given an existing node and new children.
* The loc is only used to supply the constructor.
*/
combinator.zip.make_node = (function make_node(loc,node,children){return children;
});
/**
* Returns a seq of nodes leading to this loc
*/
combinator.zip.path = (function path(loc){return loc.path.pnodes;
});
/**
* Returns a seq of the left siblings of this loc
*/
combinator.zip.lefts = (function lefts(loc){return cljs.core.seq(loc.path.l);
});
/**
* Returns a seq of the right siblings of this loc
*/
combinator.zip.rights = (function rights(loc){return loc.path.r;
});
/**
* Returns the loc of the leftmost child of the node at this loc,
* or nil if no children
*/
combinator.zip.down = (function down(loc){if(combinator.zip.branch_QMARK_(loc))
{var temp__4092__auto__ = combinator.zip.children(loc);if(cljs.core.truth_(temp__4092__auto__))
{var cs = temp__4092__auto__;var node = loc.node;var path = loc.path;return (new combinator.zip.ZipperLocation(cljs.core.first(cs),(new combinator.zip.ZipperPath((new combinator.zip.Tuple0()),cs.cljs$core$INext$_next$arity$1(null),path,(cljs.core.truth_(path)?path.pnodes.cljs$core$ICollection$_conj$arity$2(null,node):cljs.core._conj(cljs.core.List.EMPTY,node)),null))));
} else
{return null;
}
} else
{return null;
}
});
/**
* Returns the loc of the parent of the node at this loc, or nil if at the top
*/
combinator.zip.up = (function up(loc){var node = loc.node;var path = loc.path;var temp__4092__auto__ = (function (){var and__3139__auto__ = path;if(cljs.core.truth_(and__3139__auto__))
{return path.pnodes;
} else
{return and__3139__auto__;
}
})();if(cljs.core.truth_(temp__4092__auto__))
{var pnodes = temp__4092__auto__;var pnode = cljs.core.peek(pnodes);if(cljs.core.truth_(path.changed_QMARK_))
{return (new combinator.zip.ZipperLocation(combinator.zip.make_node(loc,pnode,cljs.core.seq(cljs.core.into(path.l,cljs.core.cons(node,path.r)))),(function (){var temp__4090__auto__ = path.ppath;if(cljs.core.truth_(temp__4090__auto__))
{var ppath = temp__4090__auto__;return (new combinator.zip.ZipperPath(ppath.l,ppath.r,ppath.ppath,ppath.pnodes,true));
} else
{return null;
}
})()));
} else
{return (new combinator.zip.ZipperLocation(pnode,path.ppath));
}
} else
{return null;
}
});
/**
* zips all the way up and returns the root node, reflecting any changes.
*/
combinator.zip.root = (function root(loc){while(true){
if(cljs.core.keyword_identical_QMARK_(cljs.core.constant$keyword$15,loc.path))
{return loc.node;
} else
{var p = combinator.zip.up(loc);if(cljs.core.truth_(p))
{{
var G__4879 = p;
loc = G__4879;
continue;
}
} else
{return loc.node;
}
}
break;
}
});
/**
* Returns the loc of the right sibling of the node at this loc, or nil
*/
combinator.zip.right = (function right(loc){var path = loc.path;var temp__4092__auto__ = (function (){var and__3139__auto__ = !((path == null));if(and__3139__auto__)
{return path.r;
} else
{return and__3139__auto__;
}
})();if(cljs.core.truth_(temp__4092__auto__))
{var r = temp__4092__auto__;var r__$1 = r;return (new combinator.zip.ZipperLocation(r__$1.cljs$core$ISeq$_first$arity$1(null),(new combinator.zip.ZipperPath(path.l.cljs$core$ICollection$_conj$arity$2(null,loc.node),r__$1.cljs$core$INext$_next$arity$1(null),path.ppath,path.pnodes,path.changed_QMARK_))));
} else
{return null;
}
});
/**
* Returns the loc of the rightmost sibling of the node at this loc, or self
*/
combinator.zip.rightmost = (function rightmost(loc){var path = loc.path;var temp__4090__auto__ = (function (){var and__3139__auto__ = path;if(cljs.core.truth_(and__3139__auto__))
{return path.r;
} else
{return and__3139__auto__;
}
})();if(cljs.core.truth_(temp__4090__auto__))
{var r = temp__4090__auto__;return (new combinator.zip.ZipperLocation(cljs.core.last(r),cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(path,cljs.core.constant$keyword$16,cljs.core.apply.cljs$core$IFn$_invoke$arity$4(cljs.core.conj,path.l,loc.node,cljs.core.butlast(r)),cljs.core.array_seq([cljs.core.constant$keyword$17,null], 0))));
} else
{return loc;
}
});
/**
* Returns the loc of the left sibling of the node at this loc, or nil
*/
combinator.zip.left = (function left(loc){var path = loc.path;if(cljs.core.truth_((function (){var and__3139__auto__ = path;if(cljs.core.truth_(and__3139__auto__))
{return cljs.core.seq(path.l);
} else
{return and__3139__auto__;
}
})()))
{return (new combinator.zip.ZipperLocation(cljs.core.peek(path.l),cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(path,cljs.core.constant$keyword$16,cljs.core.pop(path.l),cljs.core.array_seq([cljs.core.constant$keyword$17,cljs.core.cons(loc.node,path.r)], 0))));
} else
{return null;
}
});
/**
* Returns the loc of the leftmost sibling of the node at this loc, or self
*/
combinator.zip.leftmost = (function leftmost(loc){var path = loc.path;if(cljs.core.truth_((function (){var and__3139__auto__ = path;if(cljs.core.truth_(and__3139__auto__))
{return cljs.core.seq(path.l);
} else
{return and__3139__auto__;
}
})()))
{return (new combinator.zip.ZipperLocation(cljs.core.first(path.l),cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(path,cljs.core.constant$keyword$16,cljs.core.PersistentVector.EMPTY,cljs.core.array_seq([cljs.core.constant$keyword$17,cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic(cljs.core.rest(path.l),cljs.core.PersistentVector.fromArray([loc.node], true),cljs.core.array_seq([path.r], 0))], 0))));
} else
{return loc;
}
});
/**
* Inserts the item as the left sibling of the node at this loc, without moving
*/
combinator.zip.insert_left = (function insert_left(loc,item){var temp__4090__auto__ = loc.path;if(cljs.core.truth_(temp__4090__auto__))
{var path = temp__4090__auto__;return (new combinator.zip.ZipperLocation(loc.node,cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(path,cljs.core.constant$keyword$16,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(path.l,item),cljs.core.array_seq([cljs.core.constant$keyword$18,true], 0))));
} else
{return null;
}
});
/**
* Inserts the item as the right sibling of the node at this loc, without moving
*/
combinator.zip.insert_right = (function insert_right(loc,item){var temp__4090__auto__ = loc.path;if(cljs.core.truth_(temp__4090__auto__))
{var path = temp__4090__auto__;return (new combinator.zip.ZipperLocation(loc.node,cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(path,cljs.core.constant$keyword$17,cljs.core.cons(item,path.r),cljs.core.array_seq([cljs.core.constant$keyword$18,true], 0))));
} else
{return null;
}
});
/**
* Replaces the node at this loc, without moving
*/
combinator.zip.replace = (function replace(loc,node){return (new combinator.zip.ZipperLocation(node,(function (){var temp__4090__auto__ = loc.path;if(cljs.core.truth_(temp__4090__auto__))
{var path = temp__4090__auto__;return (new combinator.zip.ZipperPath(path.l,path.r,path.ppath,path.pnodes,true));
} else
{return null;
}
})()));
});
/**
* Inserts the item as the leftmost child of the node at this loc, without moving
*/
combinator.zip.insert_child = (function insert_child(loc,item){return combinator.zip.replace(loc,combinator.zip.make_node(loc,loc.node,cljs.core.cons(item,combinator.zip.children(loc))));
});
/**
* Inserts the item as the rightmost child of the node at this loc, without moving
*/
combinator.zip.append_child = (function append_child(loc,item){return combinator.zip.replace(loc,combinator.zip.make_node(loc,loc.node,cljs.core.concat.cljs$core$IFn$_invoke$arity$2(combinator.zip.children(loc),cljs.core.PersistentVector.fromArray([item], true))));
});
/**
* Moves to the next loc in the hierarchy, depth-first. When reaching
* the end, returns a distinguished loc detectable via end?. If already
* at the end, stays there.
*/
combinator.zip.next = (function next(loc){var path = loc.path;if(cljs.core.keyword_identical_QMARK_(cljs.core.constant$keyword$15,path))
{return loc;
} else
{if(combinator.zip.branch_QMARK_(loc))
{return combinator.zip.down(loc);
} else
{var right = combinator.zip.right(loc);if(!((right == null)))
{return right;
} else
{var p = loc;while(true){
var u = combinator.zip.up(p);if(!((u == null)))
{var right__$1 = combinator.zip.right(u);if(!((right__$1 == null)))
{return right__$1;
} else
{{
var G__4880 = u;
p = G__4880;
continue;
}
}
} else
{return (new combinator.zip.ZipperLocation(p.node,cljs.core.constant$keyword$15));
}
break;
}
}
}
}
});
/**
* Moves to the previous loc in the hierarchy, depth-first. If already at the root, returns nil.
*/
combinator.zip.prev = (function prev(loc){var temp__4090__auto__ = combinator.zip.left(loc);if(cljs.core.truth_(temp__4090__auto__))
{var lloc = temp__4090__auto__;var loc__$1 = lloc;while(true){
var temp__4090__auto____$1 = (function (){var and__3139__auto__ = combinator.zip.branch_QMARK_(loc__$1);if(and__3139__auto__)
{return combinator.zip.down(loc__$1);
} else
{return and__3139__auto__;
}
})();if(cljs.core.truth_(temp__4090__auto____$1))
{var child = temp__4090__auto____$1;{
var G__4881 = combinator.zip.rightmost(child);
loc__$1 = G__4881;
continue;
}
} else
{return loc__$1;
}
break;
}
} else
{return combinator.zip.up(loc);
}
});
/**
* Returns true if loc represents the end of a depth-first walk
*/
combinator.zip.end_QMARK_ = (function end_QMARK_(loc){return cljs.core.keyword_identical_QMARK_(cljs.core.constant$keyword$15,loc.path);
});
/**
* Removes the node at loc, returning the loc that would have preceded it in a depth-first walk.
*/
combinator.zip.remove = (function remove(loc){var temp__4090__auto__ = loc.path;if(cljs.core.truth_(temp__4090__auto__))
{var path = temp__4090__auto__;if((cljs.core.count(path.l) > 0))
{var loc__$1 = (new combinator.zip.ZipperLocation(cljs.core.peek(path.l),cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(path,cljs.core.constant$keyword$16,cljs.core.pop(path.l),cljs.core.array_seq([cljs.core.constant$keyword$18,true], 0))));while(true){
var temp__4090__auto____$1 = (function (){var and__3139__auto__ = combinator.zip.branch_QMARK_(loc__$1);if(and__3139__auto__)
{return combinator.zip.down(loc__$1);
} else
{return and__3139__auto__;
}
})();if(cljs.core.truth_(temp__4090__auto____$1))
{var child = temp__4090__auto____$1;{
var G__4882 = combinator.zip.rightmost(child);
loc__$1 = G__4882;
continue;
}
} else
{return loc__$1;
}
break;
}
} else
{return (new combinator.zip.ZipperLocation(combinator.zip.make_node(loc,cljs.core.peek(path.pnodes),path.r),(function (){var temp__4090__auto____$1 = path.ppath;if(cljs.core.truth_(temp__4090__auto____$1))
{var ppath = temp__4090__auto____$1;var and__3139__auto__ = ppath;if(cljs.core.truth_(and__3139__auto__))
{return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(ppath,cljs.core.constant$keyword$18,true);
} else
{return and__3139__auto__;
}
} else
{return null;
}
})()));
}
} else
{return null;
}
});
/**
* Replaces the node at this loc with the value of (f node args)
* @param {...*} var_args
*/
combinator.zip.edit = (function() { 
var edit__delegate = function (loc,f,args){return combinator.zip.replace(loc,cljs.core.apply.cljs$core$IFn$_invoke$arity$3(f,loc.node,args));
};
var edit = function (loc,f,var_args){
var args = null;if (arguments.length > 2) {
  args = cljs.core.array_seq(Array.prototype.slice.call(arguments, 2),0);} 
return edit__delegate.call(this,loc,f,args);};
edit.cljs$lang$maxFixedArity = 2;
edit.cljs$lang$applyTo = (function (arglist__4883){
var loc = cljs.core.first(arglist__4883);
arglist__4883 = cljs.core.next(arglist__4883);
var f = cljs.core.first(arglist__4883);
var args = cljs.core.rest(arglist__4883);
return edit__delegate(loc,f,args);
});
edit.cljs$core$IFn$_invoke$arity$variadic = edit__delegate;
return edit;
})()
;
