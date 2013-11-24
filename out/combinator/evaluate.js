// Compiled by ClojureScript 0.0-2030
goog.provide('combinator.evaluate');
goog.require('cljs.core');
goog.require('combinator.zip');
goog.require('combinator.zip');
combinator.evaluate.replace_all = (function replace_all(init,rulefn){var node = combinator.zip.vector_zip(init);while(true){
if(cljs.core.truth_(combinator.zip.end_QMARK_(node)))
{return combinator.zip.node(node);
} else
{var temp__4090__auto__ = (rulefn.cljs$core$IFn$_invoke$arity$1 ? rulefn.cljs$core$IFn$_invoke$arity$1(combinator.zip.node(node)) : rulefn.call(null,combinator.zip.node(node)));if(cljs.core.truth_(temp__4090__auto__))
{var m = temp__4090__auto__;var new$ = combinator.zip.replace(node,m);var temp__4090__auto____$1 = combinator.zip.right(new$);if(cljs.core.truth_(temp__4090__auto____$1))
{var r = temp__4090__auto____$1;{
var G__4877 = r;
node = G__4877;
continue;
}
} else
{return combinator.zip.root(new$);
}
} else
{{
var G__4878 = combinator.zip.next(node);
node = G__4878;
continue;
}
}
}
break;
}
});
combinator.evaluate.sample_combinator = (function sample_combinator(x){if(cljs.core.seq_QMARK_(x))
{var l1 = cljs.core.first(x);if(cljs.core.seq_QMARK_(l1))
{if(cljs.core.keyword_identical_QMARK_(cljs.core.constant$keyword$19,cljs.core.first(l1)))
{var xr = cljs.core.second(l1);var yr = cljs.core.second(x);return cljs.core._conj(cljs.core._conj(cljs.core.List.EMPTY,xr),cljs.core._conj(cljs.core._conj(cljs.core.List.EMPTY,cljs.core._conj(cljs.core._conj(cljs.core.List.EMPTY,yr),cljs.core.constant$keyword$19)),xr));
} else
{return false;
}
} else
{return false;
}
} else
{return false;
}
});
