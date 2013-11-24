// Compiled by ClojureScript 0.0-2030
goog.provide('combinator.core');
goog.require('cljs.core');
goog.require('combinator.zip');
goog.require('combinator.zip');
goog.require('combinator.evaluate');
goog.require('combinator.evaluate');
/**
* @param {...*} var_args
*/
combinator.core.js_print = (function() { 
var js_print__delegate = function (args){if(cljs.core.truth_(typeof console != 'undefined'))
{return console.log(cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.str,args));
} else
{return print(cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.str,args));
}
};
var js_print = function (var_args){
var args = null;if (arguments.length > 0) {
  args = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);} 
return js_print__delegate.call(this,args);};
js_print.cljs$lang$maxFixedArity = 0;
js_print.cljs$lang$applyTo = (function (arglist__4869){
var args = cljs.core.seq(arglist__4869);
return js_print__delegate(args);
});
js_print.cljs$core$IFn$_invoke$arity$variadic = js_print__delegate;
return js_print;
})()
;
cljs.core._STAR_print_fn_STAR_ = combinator.core.js_print;
combinator.core.result = (function result(n){var i = 0;var term = cljs.core.list(cljs.core.list(cljs.core.list(cljs.core.constant$keyword$19,cljs.core.list(cljs.core.list(cljs.core.constant$keyword$19,cljs.core.constant$keyword$19),cljs.core.constant$keyword$19)),cljs.core.constant$keyword$19),cljs.core.constant$keyword$19);while(true){
if(!((i > n)))
{{
var G__4870 = (i + 1);
var G__4871 = combinator.evaluate.replace_all(term,combinator.evaluate.sample_combinator);
i = G__4870;
term = G__4871;
continue;
}
} else
{return term;
}
break;
}
});
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([combinator.core.result(300)], 0));
var n__3937__auto___4872 = 20;var __4873 = 0;while(true){
if((__4873 < n__3937__auto___4872))
{var start__3962__auto___4874 = (new Date()).getTime();var ret__3963__auto___4875 = combinator.core.result(300);cljs.core.prn.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([[cljs.core.str("Elapsed time: "),cljs.core.str(((new Date()).getTime() - start__3962__auto___4874)),cljs.core.str(" msecs")].join('')], 0));
{
var G__4876 = (__4873 + 1);
__4873 = G__4876;
continue;
}
} else
{}
break;
}
